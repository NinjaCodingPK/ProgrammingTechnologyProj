package com.service.transactions;

import java.sql.Connection;

/**
 * Functional interface for performing transactions.
 * @author wookie
 * @param <T>
 */
@FunctionalInterface
public interface Operation<T> {
    
    /**
     * The source code of method would be handled with database transactions support.
     * @param connection connection which will be used during transaction.
     * @return any type.
     */
    public T execute(Connection connection);
}
