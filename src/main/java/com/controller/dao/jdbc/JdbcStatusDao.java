/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.StatusDao;
import com.wookie.devteam.entities.Status;
import com.wookie.devteam.entities.builder.StatusBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcStatusDao implements StatusDao{
    private static final Logger logger = LogManager.getLogger(JdbcStatusDao.class);
    
    public static final String TABLE_NAME = "Status";
    public static final String COLUMN_ID = TABLE_NAME + ".id";
    public static final String COLUMN_NAME = TABLE_NAME + ".name";
    
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (name) VALUES (?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET name=? WHERE id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from " + TABLE_NAME + " WHERE id = ?;";
    public static final String FIND_BY_NAME_STATEMENT =
            "SELECT * from " + TABLE_NAME + " WHERE name = ?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from " + TABLE_NAME + ";";
   
    
    private Status getResult(ResultSet rs) throws SQLException {
        return new StatusBuilder()
                    .setId(rs.getInt(COLUMN_ID))
                    .setName(rs.getString(COLUMN_NAME))
                    .build();
    }
    
    @Override
    public Status create(Connection connection, Status e) {
        try (PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_STATEMENT,  Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, e.getName());
            preparedStatement.executeUpdate();
            
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys(); 
            if (generatedKeys.next()) 
                e.setId(generatedKeys.getInt(1));
            
            preparedStatement.close();
            return e;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean update(Connection connection, Status e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setString(1, e.getName());
            preparedStatement.setInt(2, e.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            connection.commit();
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, Status e){
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(DELETE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Status findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement =
                    connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
            ResultSet rs = preparedStatement.executeQuery();

            Status temp = null;
            if(rs.next()) {
                temp = getResult(rs);
            }
        
            preparedStatement.close();
            return temp;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<Status> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
            Set<Status> res = new HashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
            
            query.close();
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Status findByName(Connection connection, String name) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_NAME_STATEMENT))
        {
            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();

            Status temp = null;
            if(rs.next()) {
                temp = getResult(rs);
            }
        
            preparedStatement.close();
            return temp;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

}
