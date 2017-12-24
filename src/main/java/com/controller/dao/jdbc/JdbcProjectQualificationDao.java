/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.dao.ProjectQualificationDao;
import com.wookie.devteam.entities.ProjectQualification;
import com.wookie.devteam.entities.builder.ProjectBuilder;
import com.wookie.devteam.entities.builder.ProjectQualificationBuilder;
import com.wookie.devteam.entities.builder.QualificationBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;

public class JdbcProjectQualificationDao implements ProjectQualificationDao {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcProjectQualificationDao.class);
    
    public static final String TABLE_NAME = "ProjectQualification";
    public static final String COLUMN_QUALIFICATION = TABLE_NAME + ".qualification_id";
    public static final String COLUMN_PROJECT = TABLE_NAME + ".project_id";
    public static final String COLUMN_COUNT = TABLE_NAME + ".count";
    
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (qualification_id, project_id, count)"
            + " VALUES (?, ?, ?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET count=? WHERE qualification_id=? AND project_id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE qualification_id = ? AND project_id=?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from " + TABLE_NAME +
            " left join Qualification on ProjectQualification.qualification_id = Qualification.id" +
            " WHERE qualification_id=? AND project_id=?;";
    public static final String FIND_BY_PROJECT_STATEMENT =
            "SELECT * from " + TABLE_NAME +
            " left join Qualification on ProjectQualification.qualification_id = Qualification.id" +
            " WHERE project_id=?;";
    public static final String FIND_BY_QUALIFICATION_STATEMENT =
            "SELECT * from " + TABLE_NAME +
            " left join Qualification on ProjectQualification.qualification_id = Qualification.id" +
            " WHERE qualification_id=?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from " + TABLE_NAME +
            " left join Qualification on ProjectQualification.qualification_id = Qualification.id";
    
    
    private ProjectQualification getResult(ResultSet rs) throws SQLException {
        return new ProjectQualificationBuilder()
                    .setQualification(new QualificationBuilder()
                                        .setId(rs.getInt(COLUMN_QUALIFICATION))
                                        .setName(rs.getString(JdbcQualificationDao.COLUMN_NAME))
                                        .build())
                    .setProject(new ProjectBuilder().setId(rs.getInt(COLUMN_PROJECT)).build())
                    .setCount(rs.getInt(COLUMN_COUNT))
                    .build();
    }
    
    @Override
    public ProjectQualification create(Connection connection, ProjectQualification e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(CREATE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getQualification().getId());
            preparedStatement.setInt(2, e.getProject().getId());
            preparedStatement.setInt(3, e.getCount());
            preparedStatement.executeUpdate();
            
            preparedStatement.close();
            return e;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean update(Connection connection, ProjectQualification e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getCount());
            preparedStatement.setInt(2, e.getQualification().getId());
            preparedStatement.setInt(3, e.getProject().getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, ProjectQualification e) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(DELETE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getQualification().getId());
            preparedStatement.setInt(2, e.getProject().getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public ProjectQualification findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
            preparedStatement.setInt(2, id[1]);
            ResultSet rs = preparedStatement.executeQuery();

            ProjectQualification temp = null;
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
    public Set<ProjectQualification> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
        
            Set<ProjectQualification> res = new HashSet<>();
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
    public Set<ProjectQualification> findByProject(Connection connection, int projectId) {
        try (PreparedStatement preparedStatement =
                connection.prepareStatement(FIND_BY_PROJECT_STATEMENT))
        {
            preparedStatement.setInt(1, projectId);
            ResultSet rs = preparedStatement.executeQuery();

            Set<ProjectQualification> res = new HashSet<>();
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

    @Override
    public Set<ProjectQualification> findByQualification(Connection connection, int qualificationId) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_QUALIFICATION_STATEMENT))
        {
            preparedStatement.setInt(1, qualificationId);
            ResultSet rs = preparedStatement.executeQuery();

            Set<ProjectQualification> res = new HashSet<>();
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
