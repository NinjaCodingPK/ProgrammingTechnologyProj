package com.service;

import com.constants.Attributes;
import com.constants.ErrorMessages;
import com.constants.Roles;
import com.dao.DaoFactory;
import com.entities.Person;
import com.entities.PersonProject;
import com.entities.builder.PersonBuilder;
import com.entities.builder.PersonProjectBuilder;
import com.entities.builder.ProjectBuilder;
import com.service.transactions.TransactionManager;
import com.service.transactions.impl.JdbcTransactionManager;
import com.dao.PersonProjectDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author SK
 */
public class PersonProjectService {
	private static final Logger logger = LogManager.getLogger(PersonProjectService.class);
    private DaoFactory daoFactory;
    private TransactionManager transactionManager;
    
    public PersonProjectService(DaoFactory daoFactory, TransactionManager transactionManager) {
        this.daoFactory = daoFactory;
        this.transactionManager = transactionManager;
    }
    
    /**
     * Method fill collection of PersoProjects entities with values from Person
     * and Project DAO.
     * @param unfilled collection of PersonProject entities.
     * @return {@link Map} where key is a filled PersonProject entity. Key is a total cost
     * of a project.
     */
    private Set<PersonProject> fillValues(Set<PersonProject> unfilled) {
        Set<PersonProject> filled = new HashSet<>();
        
        transactionManager.performTransaction( connection -> {
            unfilled.stream().map((p) -> {
                p.setProject(daoFactory.createProjectDao().findById(connection, p.getProject().getId()));
                return p;
            }).map((p) -> {
                p.setPerson(daoFactory.createPersonDao().findById(connection, p.getPerson().getId()));
                return p;
            }).forEach((p) -> {
                filled.add(p);
            });
            
            return null;
        });
        
        return filled;
    }
    
    /**
     * Method finds all PersonProject entities in database by project ID.
     * @param projectId ID of a project.
     * @return {@link Map} where key is a filled PersonProject entity. Key is a total cost
     * of a project.
     */
    public Set<PersonProject> getByProject(int projectId) {
        Set<PersonProject> personProjects = transactionManager.performTransaction(
            connection -> {
                return daoFactory.createPersonProjectDao().findByProject(connection, projectId);
            }
        );
          
        return fillValues(personProjects);
    }
    
    /**
     * Method finds all PersonProject entities in database by person ID. 
     * @param personId ID of a person.
     * @return {@link Map} where key is a filled PersonProject entity. Key is a total cost
     * of a project.
     */
    public Set<PersonProject> getByPerson(int personId) {
        Set<PersonProject> personProjects = transactionManager.performTransaction(
            connection -> {
                return daoFactory.createPersonProjectDao().findByPerson(connection, personId);
            }
        );
           
        return fillValues(personProjects);
    }
    
    /**
     * Method finds all Developers in PersonProject table.
     * @param projectId ID of a project.
     * @return java.util.Map where key is a PersonProject entity. Key is developer's age.
     */
    public Map<PersonProject, Long> fetchDevs(int projectId) {
        Map<PersonProject, Long> devs = new HashMap<>();
        
        this.getByProject(projectId).stream().filter(
        		(personProject) -> (personProject.getPerson().getRole().getName().equals(Roles.DEV_ROLE))
            ).forEach((personProject) -> {
                  	devs.put(personProject, personProject.getPerson().getAge());
            	});
        
        return devs;
    }
    
    /**
     * Method updates developer's time of working with project. The new time
     * should be added to the previous value from database.
     * @param personId ID of developer.
     * @param projectId ID of a project.
     * @param time added time.
     */
    public void updateTime(int personId, int projectId, int time) {
        transactionManager.performTransaction( 
            connection -> {
            	logger.info("Start time updating. UserID = " + personId);
                PersonProject pp = daoFactory.createPersonProjectDao()
                    .findById(connection, personId, projectId);
                
                
                if(pp == null) {
                	throw new RuntimeException(ErrorMessages.DEV_HAS_NO_PROJECT);
                } else {
                	pp.setTime(pp.getTime() + time);
                }
        
                return daoFactory.createPersonProjectDao().update(connection, pp);
            }
        );
    }
    
    /**
     * Method checks if current project is available for processing.
     * @param connection
     * @param projectId ID of a project.
     * @throws RuntimeException if project is not available.
     */
    private void checkFreeProject(Connection connection, int projectId) throws RuntimeException {
        if(!Attributes.FREE_PROJECT.equals(
                daoFactory.createProjectDao().findById(connection, projectId).getStatus().getName())) {
                    	
            throw new RuntimeException(ErrorMessages.UNAVAILABLE_FOR_UPDATE);
        }
            
    }
    
    /**
     * Method creates entries in database using information from request.
     * In practice request object should contain ID of a project and list of 
     * persons who should be connected with current project.
     * @param developers {@link Set} of developers ID.
     * @param projectId ID of a project.
     * @param projectCost project's cost.
     * @param managerId ID of a manager.
     */
    public void createPersonProjectFromRequest(Set<Integer> developers, int managerId, 
                                        int projectId, BigDecimal projectCost) {
        
        transactionManager.performTransaction( connection -> {
            Person person;
            PersonProjectDao personProjectDao = daoFactory.createPersonProjectDao();
            
            personProjectDao.create(connection, new PersonProjectBuilder()
                .setPerson(new PersonBuilder().setId(managerId).build())
                .setProject(new ProjectBuilder().setId(projectId).build())
                .build());
            for(int devId : developers)  {
                person = new PersonBuilder().setId(devId).build();
                personProjectDao.create(connection, new PersonProjectBuilder()
                                .setPerson(new PersonBuilder().setId(person.getId()).build())
                                .setProject(new ProjectBuilder().setId(projectId).build())
                                .build());
                person.setStatus(false);
                daoFactory.createPersonDao().update(connection, person);
            }
            checkFreeProject(connection, projectId);
            daoFactory.createProjectDao().update(connection, new ProjectBuilder()
                .setId(projectId)
                .setCost(projectCost)
                .setStatus(daoFactory.createStatusDao().findByName(connection, Attributes.IN_PROGRESS_STATUS))
                .build());
            
            return null;
        });
    }
    
    /**
     * Method deletes all developers form project in database.
     * @param project ID of a project. 
     */
    public void deleteProjectsDevs(int project) {
        Map<PersonProject, Long> personProjects = fetchDevs(project);
        
        transactionManager.performTransaction( 
            connection -> {
                personProjects.entrySet().stream().forEach((entry) -> {
                    delete(connection, entry.getKey());
            });

                return null;
            }
        );
    }
    
    /**
     * Method deletes entry in PersonProject table in database by Person.
     * Also while deleting takes place an update of person's status in table
     * Person in database. Status should be changed on "free"(true).
     * @param personProject PersonProject entity.
     */
    private void delete(Connection connection, PersonProject personProject) {
        Person person = personProject.getPerson();
        person.setStatus(true);
                    
        daoFactory.createPersonDao().update(connection, person);
        daoFactory.createPersonProjectDao().delete(connection, personProject);
        daoFactory.createProjectDao().update(connection, new ProjectBuilder()
            .setId(personProject.getProject().getId())
            .setStatus(daoFactory.createStatusDao().findByName(connection, Attributes.DONE_PROJECT))
            .build());
    }
}
