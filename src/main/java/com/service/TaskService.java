
package com.wookie.devteam.service;

import com.dao.DaoFactory;
import com.entities.Task;
import com.entities.builder.ProjectBuilder;
import com.entities.builder.StatusBuilder;
import com.entities.builder.TaskBuilder;
import com.service.transactions.TransactionManager;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author wookie
 */
public class TaskService {
    private static final Logger logger = LogManager.getLogger(TaskService.class);
    
    private DaoFactory factory;
    private TransactionManager transactionManager;

    public TaskService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }
    
    /**
     * Method finds tasks by project ID.
     * @param id ID of a project.
     * @return Collection of tasks.
     */
    public Set<Task> getByProject(int id) {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createTaskDao().findByProject(connection, id);
            }
        );
    }
    
    /**
     * Create new Task entry in database.
     * @param projectId ID of a project.
     * @param taskText task text.
     */
    public void createTask(int projectId, String taskText) {
        logger.info("New task text " + taskText);
        
        Task task = new TaskBuilder()
                    .setProject(new ProjectBuilder().setId(projectId).build())
                    .setText(taskText)
                    .build();
        
        transactionManager.performTransaction( 
            connection -> { 
                return factory.createTaskDao().create(connection, task);
            }
        );
    }
 
    /**
     * Method updatesTask entry in database.
     * @param taskId ID of a task.
     * @param statusId ID if a status.
     */
    public void updateTask(int taskId, int statusId, int personId) {
        transactionManager.performTransaction( 
            connection -> {
                if(!factory.createPersonProjectDao().findByPerson(connection, personId).isEmpty()) {
                    Task existed = factory.createTaskDao().findById(connection, taskId);
                    existed.setStatus(new StatusBuilder().setId(statusId).build());
            
                    return factory.createTaskDao().update(connection, existed);
                } else {
                   throw new RuntimeException();
                }
            }
        );
    }
}
