package com.service;

import com.dao.DaoFactory;
import com.entities.ProjectQualification;
import com.entities.builder.ProjectBuilder;
import com.entities.builder.ProjectQualificationBuilder;
import com.entities.builder.QualificationBuilder;
import com.service.transactions.TransactionManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author SK
 */
public class ProjectQualificationService {
    private DaoFactory factory;
    private TransactionManager transactionManager;
    
    public ProjectQualificationService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }
    
    /**
     * Method finds all ProjectQualifications from database using project ID.
     * @param projectId ID of a project.
     * @return Collection of ProjectQualification entities.
     */
    public Set<ProjectQualification> getProjectQualification(int projectId) {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createProjectQualificationDao().findByProject(connection, projectId);
            }
        );
    }
    
    
    /**
     * Method returns all ProjectQuadlififcation entities from database using project ID.
     * Each ProjectQualification entry contains column 'count' which  displays 
     * count of developer's qualifications needed in current project. Method parse count of 
     * developer's qualifications and form an unique entry for each qualification
     * with it's unique ID.
     * @param projectId ID of a project.
     * @return java.util.Map in which key is a unique ID. 
     * Value is a ProjectQualification entity.
     */
    public Map<Integer, ProjectQualification> getProjectQualificationFullList(int projectId) {
        Set<ProjectQualification> list = transactionManager.performTransaction(
            connection -> {
                return factory.createProjectQualificationDao().findByProject(connection, projectId);
            }
        );
        
        Map<Integer, ProjectQualification> fullList = new HashMap<>();
        int id = 1;
        
        for(ProjectQualification pp : list) {
            for(int i = 0; i < pp.getCount(); i++) {
                fullList.put(id, pp);
                id++;
            }
        }
        
        return fullList;
    }
    
    /**
     * Method creates ProjectQualification entry in database using project ID,
     * qualification ID and count value. 
     * If an entry with similar IDs is already exist method only updates an
     * existing value with count.
     * @param projectId ID of a project.
     * @param qualificationId ID of qualification.
     * @param count count value.
     * @return added ProjectQualification entity.
     */
    public ProjectQualification addProjectQualification(int projectId, int qualificationId, int count) {
        return transactionManager.performTransaction( connection ->  {
            ProjectQualification pq = new ProjectQualificationBuilder()
                .setProject(new ProjectBuilder().setId(projectId).build())
                .setQualification(new QualificationBuilder().setId(qualificationId).build())
                .setCount(count)
                .build();
            ProjectQualification existed = factory.createProjectQualificationDao()
                    .findById(connection, qualificationId, projectId);
            
            if(existed == null) {
                pq = factory.createProjectQualificationDao().create(connection, pq);
                return pq;
            }
            else {
                existed.setCount(pq.getCount() + existed.getCount());
                factory.createProjectQualificationDao().update(connection, existed);
                return existed;
            }
        });
    }
}
