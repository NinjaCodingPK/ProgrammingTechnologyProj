
package com.service.transactions.impl;

import com.dao.jdbc.JdbcDaoFactory;
import com.service.transactions.Operation;
import com.service.transactions.TransactionManager;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implementation of TransactionManager.
 * @author SK
 */
public class JdbcTransactionManager implements TransactionManager {
    private static final Logger logger = LogManager.getLogger(JdbcTransactionManager.class);
    private DataSource ds;
    
    public JdbcTransactionManager(DataSource ds) {
        this.ds = ds;
    }
    
    @Override
    public <T> T performTransaction(Operation<T> action) {
        Connection connection = null;
        T result = null;
        
        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);
            
            logger.info("Start transaction");
            result = action.execute(connection);
            
            connection.commit();
            logger.info("Transaction isolation level: " + connection.getTransactionIsolation());
        } catch (Exception ex) {
            logger.error("Exception during transaction " + ex);
            if (connection != null) {
                try {
                    logger.info("Rollback");
                    connection.rollback();
                } catch (SQLException ex1) {
                    throw new RuntimeException();
                }
            }
            
            throw new RuntimeException(ex.getMessage());
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException();
                }
            }
        }
        
    return result;
    }
}
