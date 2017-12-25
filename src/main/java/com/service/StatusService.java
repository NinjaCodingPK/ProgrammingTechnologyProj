package com.service;

import com.dao.DaoFactory;
import com.entities.Status;
import com.service.transactions.TransactionManager;
import java.util.Set;

/**
 *
 * @author wookie
 */
public class StatusService {
    private DaoFactory factory;
    private TransactionManager transactionManager;

    public StatusService(DaoFactory factory, TransactionManager transactionManager) {
        this.factory = factory;
        this.transactionManager = transactionManager;
    }

    /**
     * Method finds all Status entries in database.
     * @return Collection of Status entities.
     */
    public Set<Status> getAll() {
        return transactionManager.performTransaction( 
            connection -> {
                return factory.createStatusDao().findAll(connection);
            }
        );
    }
}
