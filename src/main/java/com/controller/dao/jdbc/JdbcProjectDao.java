/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.constants.Attributes;
import com.wookie.devteam.dao.ProjectDao;
import com.wookie.devteam.entities.Project;
import com.wookie.devteam.entities.builder.ProjectBuilder;
import com.wookie.devteam.entities.builder.StatusBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;

public class JdbcProjectDao implements ProjectDao {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcProjectDao.class);
    
    public static final String TABLE_NAME = "Project";
    public static final String COLUMN_ID = TABLE_NAME + ".id";
    public static final String COLUMN_NAME = TABLE_NAME + ".name";
    public static final String COLUMN_DEADLINE = TABLE_NAME + ".deadline";
    public static final String COLUMN_STATUS = TABLE_NAME + ".status_id";
    public static final String COLUMN_COST = TABLE_NAME + ".cost";
    
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (name, deadline)"
            + " VALUES (?, ?);";
    public static final String CREATE_EMPTY_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (name, deadline)"
            + " VALUES (?, ?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET name=?, deadline=?, status_id=?, cost=?"
            + " WHERE id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from Project"
            + " left join Status on Project.status_id = Status.id"
            + " where Project.id = ?;";
    public static final String FIND_BY_NAME_STATEMENT =
            "SELECT * from Project"
            + " left join Status on Project.status_id = Status.id"
            + " where Project.name = ?;";
    public static final String FIND_TO_PROCESS_STATEMENT =
            "SELECT * from Project"
            + " left join Status on Project.status_id = Status.id"
            + " where Status.name = ?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from Project"
            + " left join Status on Project.status_id = Status.id;";
    public static final String FIND_BY_PERSON_ID_STATEMENT =
            "SELECT * from PersonProject"
            + " left join Person on PersonProject.person_id = Person.id"
            + " left join Project on PersonProject.project_id = Project.id"
            + " left join Status on Project.status_id = Status.id"
            + " where Person.id = ?"
            + " order by Project.name;";
    public static final String ROWS_COUNT_STATEMENT = 
    		"SELECT Count(*) FROM " + TABLE_NAME + ";";
    
    private Project getResult(ResultSet rs) throws SQLException {
        return new ProjectBuilder()
                    .setId(rs.getInt(COLUMN_ID))
                    .setName(rs.getString(COLUMN_NAME))
                    .setDeadline(rs.getDate(COLUMN_DEADLINE))
                    .setStatus(new StatusBuilder()
                            .setId(rs.getInt(COLUMN_STATUS))
                            .setName(rs.getString(JdbcStatusDao.COLUMN_NAME))
                            .build())
                    .setCost(rs.getBigDecimal(COLUMN_COST))
                    .build();
    }
    
    @Override
    public Project create(Connection connection, Project e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, e.getName());
            preparedStatement.setDate(2, new java.sql.Date(e.getDeadline().getTime()));
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
    public boolean update(Connection connection, Project e) {
        Project existed = findById(connection, e.getId());
        
        if(e.getCost() != null)
            existed.setCost(e.getCost());
        if(e.getStatus() != null)
            existed.setStatus(e.getStatus());
        
        try  (PreparedStatement preparedStatement = 
                    connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setString(1, existed.getName());
            preparedStatement.setDate(2, new java.sql.Date(existed.getDeadline().getTime()));
            preparedStatement.setInt(3, existed.getStatus().getId());
            preparedStatement.setBigDecimal(4, existed.getCost());
            preparedStatement.setInt(5, existed.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, Project e) {
        try (PreparedStatement preparedStatement =
                connection.prepareStatement(DELETE_STATEMENT))
        {
            preparedStatement.setInt(1, e.getId());
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
    public Project findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(FIND_BY_ID_STATEMENT)) 
        {
            preparedStatement.setInt(1, id[0]);
            ResultSet rs = preparedStatement.executeQuery();

            Project temp = null;
            if(rs.next()) {
                temp = getResult(rs);
            }
        
            return temp;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
       
    }

    @Override
    public Set<Project> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
            Set<Project> res = new LinkedHashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
            
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Project findByName(Connection connection, Project e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_NAME_STATEMENT))
        {
            preparedStatement.setString(1, e.getName());
            ResultSet rs = preparedStatement.executeQuery();

            Project temp = null;
            if(rs.next()) {
                temp = getResult(rs);
            }
        
            preparedStatement.close();
            return temp;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<Project> findProjectsToProcess(Connection connection) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(FIND_TO_PROCESS_STATEMENT))
        {
            preparedStatement.setString(1, Attributes.FREE_PROJECT);
            ResultSet rs = preparedStatement.executeQuery();

            Set<Project> res = new HashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
            
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<Project> findByPerson(Connection connection, int personId) {
        try (PreparedStatement preparedStatement = 
                connection.prepareStatement(FIND_BY_PERSON_ID_STATEMENT))
        {
            preparedStatement.setInt(1, personId);
            ResultSet rs = preparedStatement.executeQuery();

            Set<Project> res = new LinkedHashSet<>();
            while (rs.next()) {
                res.add(getResult(rs));
            }
            
            return res;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

//	@Override
//	public Set<Project> findByPersonPagination(Connection connection, int personId) {
//		try (Statement query = connection.createStatement())
//        {
//			ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
//			int count = rs.getInt(1);
//			System.out.println(count);
//        } catch (SQLException ex) {
//            logger.error("Error while database processing " + ex);
//            throw new RuntimeException(ex);
//        }
//        
//		return null;
//	}
}
