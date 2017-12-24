/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.QualificationDao;
import com.wookie.devteam.entities.Qualification;
import com.wookie.devteam.entities.builder.QualificationBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcQualificationDao implements QualificationDao {
    private static final Logger logger = LogManager.getLogger(JdbcQualificationDao.class);

    public static final String TABLE_NAME = "Qualification";
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
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from " + TABLE_NAME + ";";
    
    
    private Qualification getResult(ResultSet rs) throws SQLException {
        return new QualificationBuilder()
                    .setId(rs.getInt(COLUMN_ID))
                    .setName(rs.getString(COLUMN_NAME))
                    .build();
    }
    
    @Override
    public Qualification create(Connection connection, Qualification e) {
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
    public boolean update(Connection connection, Qualification e) {
        try  (PreparedStatement preparedStatement = 
                connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setString(1, e.getName());
            preparedStatement.setInt(2, e.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, Qualification e) {
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
    public Qualification findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
            ResultSet rs = preparedStatement.executeQuery();

            Qualification temp = null;
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
    public Set<Qualification> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
            Set<Qualification> res = new HashSet<>();
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
    
}
