package com.service;

import com.constants.Attributes;
import com.constants.ErrorMessages;
import com.dao.DaoFactory;
import com.entities.Project;
import com.entities.builder.PersonBuilder;
import com.entities.builder.PersonProjectBuilder;
import com.entities.builder.ProjectBuilder;
import com.service.transactions.TransactionManager;

import java.math.BigDecimal;
import java.util.Date;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author SK
 */
public class ProjectService {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ProjectService.class);

    private DaoFactory factory;
    private TransactionManager transactionManager;
    
    public ProjectService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }
    
    /**
     * Method creates new Project in database or update the existed one.
     * @param name name of a project.
     * @param deadline deadline of a project.
     * @param clientId ID of a client.
     * @return Project entity.
     * @throws RuntimeException if such project is already existed.
     */
    public Project create(String name, Date deadline, int clientId) {       
        return transactionManager.performTransaction( connection -> {
            Project p = new ProjectBuilder()
                        .setName(name)
                        .setDeadline(deadline)
                        .build();
                
            Project checkP = factory.createProjectDao().findByName(connection, p);            
            if(checkP == null) {
                p = factory.createProjectDao().create(connection, p);
                factory.createPersonProjectDao().create(connection, new PersonProjectBuilder()
                    .setProject(p)
                    .setPerson(new PersonBuilder().setId(clientId).build())
                    .build());
            }
            else {
                logger.error("Project is already exists");
                throw new RuntimeException(ErrorMessages.EXISTING_PROJECT);
            }
        
            return p;
        });
    }
    
    /**
     * Method updates status of a project.
     * @param projectId ID of a project.
     * @param cost
     * @param statusName status name.
     */
    public void update(int projectId, String statusName) {
        transactionManager.performTransaction( 
            connection -> {
                return factory.createProjectDao().update(
                    connection, 
                    new ProjectBuilder()
                        .setId(projectId)
                        .setStatus(factory.createStatusDao().findByName(connection, statusName))
                        .build()
                );
            }
        );
    }
    
    /**
     * Method checks if project is available for update.
     * @param projectId ID of a project
     * @throws RuntimeException if project is not available for update.
     */
    public void checkAvailableForUpdate(int projectId) throws RuntimeException {
        transactionManager.performTransaction( 
            connection -> {
                Project project = factory.createProjectDao().findById(connection, projectId);
                
                if(!project.getStatus().getName().equals(Attributes.EMPTY_PROJECT))
                    throw new RuntimeException(ErrorMessages.UNAVAILABLE_FOR_UPDATE);
                
                return null;
            }
        );
    }
    
}
