package com.service;

import com.constants.Attributes;
import com.constants.ErrorMessages;
import com.constants.Roles;
import com.constants.WebPages;
import com.dao.DaoFactory;
import com.entities.Person;
import com.entities.Project;
import com.entities.Task;
import com.entities.builder.PersonBuilder;
import com.service.transactions.TransactionManager;
import java.sql.Connection;
import java.util.Date;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author wookie
 */
public class PersonService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(PersonService.class);
    
    private DaoFactory factory;
    private TransactionManager transactionManager;

    public PersonService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }
    
    /**
     * Method finds user by his login.
     * @param login user's login.
     * @return Person instance.
     * @throws RuntimeException 
     */
    public Person getPerson(String login) throws RuntimeException {
        return transactionManager.performTransaction( 
            connection -> {
                Person person = factory.createPersonDao().findByLogin(connection, login);
                
                if(person == null)
                	throw new RuntimeException(ErrorMessages.WRONG_CREDENTIALS);
                	
                return person;
            }
        );
    }
    
    /**
     * Method finds user by his ID.
     * @param id person's ID.
     * @return User instance.
     * @throws RuntimeException 
     */
    public Person getPerson(int id) {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createPersonDao().findById(connection, id);
            }
        );
    }
    
    public Person createPerson(String name, String surname, String login, String email, 
    		String phoneNumber, Date birtday, String password) {
    	
    	return transactionManager.performTransaction(
    			connection -> {
    			try {
    				return factory.createPersonDao().create(connection, 
    					new PersonBuilder()
    						.setName(name)
    						.setSurname(surname)
    						.setLogin(login)
    						.setEmail(email)
    						.setPassword(password)
    						.setPhone(phoneNumber)
    						.setBirthday(new java.sql.Date(birtday.getTime()))
    						.build());
    			} catch (RuntimeException ex) {
    				throw new RuntimeException(ErrorMessages.EXISTING_PERSON);
    			}
    		}
    	);
    }
    
    /**
     * Method checks user's password
     * @param user Person instance.
     * @param password inputed password.
     * @throws RuntimeException if password is wrong.
     */
    public void checkPassword(Person user, String password) {
        if (!user.getPassword().equals(password)) {
            logger.error("Wrong password error");
            throw new RuntimeException(ErrorMessages.WRONG_PASSWORD);
        }
    }

    /**
     * Method finds all developers with "free"(true) status in database.
     * @return Collection of Person entities.
     */
    public Set<Person> fetchFreeDevs() {
        return transactionManager.performTransaction(
            connection -> {
                return factory.createPersonDao().findFreeDevs(connection);
            }
        );
    }

    /**
     * Method returns collection of Project entities using ID of a person.
     * @param personId ID of a person.
     * @return Set of projects.
     */
    public Set<Project> getProjectsByPerson(int personId) {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createProjectDao().findByPerson(connection, personId);
            }
        );
    }
    
    /**
     * Method checks user's rights and decides in which page current user should
     * be redirected.
     *
     * @param request Http request.
     * @param userId
     * @param userRole
     * @return name of page in which user should be redirected.
     * @throws RuntimeException if DAO classes throws exception or error while logging in appears.
     */
    public String processUser(HttpServletRequest request, int userId, String userRole) throws RuntimeException {
        return transactionManager.performTransaction( connection -> {
            switch (userRole) {
                case Roles.CONSUMER_ROLE:
                    request.setAttribute(Attributes.PROJECT_LIST, 
                            factory.createProjectDao().findByPerson(connection, userId));
                    return WebPages.CONSUMER_PAGE;
                case Roles.MANAGER_ROLE:
                    request.setAttribute(Attributes.PROJECT_LIST, 
                            factory.createProjectDao().findProjectsToProcess(connection));
                    return WebPages.MANAGER_PAGE;
                case Roles.DEV_ROLE:
                    return processDeveloper(connection, request, userId);
                default:
                    throw new RuntimeException(ErrorMessages.WRONG_CREDENTIALS);
            }
        });
    }
    
    /**
     * Method for developer's login command processing.
     * @param connection used connection. 
     * @param request HTTP request.
     * @param userId ID of a user.
     * @return developer's web page.
     */
    private String processDeveloper(Connection connection, HttpServletRequest request, int userId) {
    	try {
    		Project devsProject = null;
    		Set<Project> projects = factory.createProjectDao().findByPerson(connection, userId);
    		if(projects.iterator().hasNext())
    			devsProject = projects.iterator().next();
        
    		Set<Task> tasks = null;
    		if(devsProject != null) {
    			tasks = factory.createTaskDao().findByProject(connection, devsProject.getId());
    			request.setAttribute(Attributes.PROJECT_ID, devsProject.getId());
    		}
    		request.setAttribute(Attributes.TASK_LIST, tasks);
    		request.setAttribute(Attributes.STATUS_LIST, factory.createStatusDao().findAll(connection));
    		request.setAttribute(Attributes.TIME, 
        		factory.createPersonProjectDao().findById(connection, userId, devsProject.getId()).getTime());
    		return WebPages.DEVELOPER_PAGE;
    	} catch(Exception e) {
    		throw new RuntimeException(ErrorMessages.DEV_HAS_NO_PROJECT);
    	}
    }
}
