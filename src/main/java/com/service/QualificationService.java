package com.service;

import com.dao.DaoFactory;
import com.entities.Qualification;
import com.service.transactions.TransactionManager;
import java.util.Set;

/**
 *
 * @author SK
 */
public class QualificationService {
    private DaoFactory factory;
    private TransactionManager transactionManager;
    
    public QualificationService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }

    
    /**
     * Method returns all qualifications from database;
     * @return Collection of Qualification entities.
     */
    public Set<Qualification> getAll() {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createQualificationDao().findAll(connection);
            }
        );
    }
}
