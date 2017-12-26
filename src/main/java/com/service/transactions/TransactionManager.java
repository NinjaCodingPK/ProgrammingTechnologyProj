package com.service.transactions;

/**
 * Interface for performing transactions on service level. 
 * @author wookie
 */
public interface TransactionManager {
    
    /**
     * The method should call action method with prepared data.
     * @param <T>
     * @param action Operator instance.
     * @return type T.
     */
    <T> T performTransaction(Operation<T> action);
}
