/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.TaskDao;
import static com.wookie.devteam.dao.jdbc.JdbcProjectDao.CREATE_STATEMENT;
import com.wookie.devteam.entities.Task;
import com.wookie.devteam.entities.builder.ProjectBuilder;
import com.wookie.devteam.entities.builder.StatusBuilder;
import com.wookie.devteam.entities.builder.TaskBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JdbcTaskDao implements TaskDao {
    private static final Logger logger = LogManager.getLogger(JdbcStatusDao.class);
    
    public static final String TABLE_NAME = "Task";
    public static final String COLUMN_ID = TABLE_NAME + ".id";
    public static final String COLUMN_TEXT = TABLE_NAME + ".text";
    public static final String COLUMN_PROJECT = TABLE_NAME + ".project_id";
    public static final String COLUMN_STATUS = TABLE_NAME + ".status_id";
    
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (text, project_id)"
            + " VALUES (?, ?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET text=?, project_id=?, status_id=?"
            + " WHERE id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from Task" +
            " left join Status ON Task.status_id = Status.id" +
            " where Task.id = ?;";
    public static final String FIND_BY_PROJECT_STATEMENT =
            "SELECT * from Task" +
            " left join Status ON Task.status_id = Status.id" +
            " where Task.project_id = ?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from Task" +
            " left join Status ON Task.status_id = Status.id;";
    
    
    private Task getResult(ResultSet rs) throws SQLException {
        return new TaskBuilder()
                    .setId(rs.getInt(COLUMN_ID))
                    .setText(rs.getString(COLUMN_TEXT))
                    .setProject(new ProjectBuilder().setId(rs.getInt(COLUMN_PROJECT)).build())
                    .setStatus(new StatusBuilder()
                            .setId(rs.getInt(COLUMN_STATUS))
                            .setName(rs.getString(JdbcStatusDao.COLUMN_NAME))
                            .build())
                    .build();
    }
    
    @Override
    public Task create(Connection connection, Task e) {
        try (PreparedStatement preparedStatement =
                    connection.prepareStatement(CREATE_STATEMENT,  Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, e.getText());
            preparedStatement.setInt(2, e.getProject().getId());
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
    public boolean update(Connection connection, Task e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setString(1, e.getText());
            preparedStatement.setInt(2, e.getProject().getId());
            preparedStatement.setInt(3, e.getStatus().getId());
            preparedStatement.setInt(4, e.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

     @Override
    public boolean delete(Connection connection, Task e) {
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
    public Task findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
            ResultSet rs = preparedStatement.executeQuery();

            Task temp = null;
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
    public Set<Task> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
        
            Set<Task> res = new HashSet<>();
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
    public Set<Task> findByProject(Connection connection, int projectId) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(FIND_BY_PROJECT_STATEMENT))
        {
            preparedStatement.setInt(1, projectId);
            ResultSet rs = preparedStatement.executeQuery();

            Set<Task> res = new LinkedHashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
        
            preparedStatement.close();
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

}
