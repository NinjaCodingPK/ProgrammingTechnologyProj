/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.controller.dao.jdbc;

import com.wookie.devteam.constants.Roles;
import com.wookie.devteam.dao.PersonDao;
import com.wookie.devteam.entities.Person;
import com.wookie.devteam.entities.builder.PersonBuilder;
import com.wookie.devteam.entities.builder.QualificationBuilder;
import com.wookie.devteam.entities.builder.RoleBuilder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import org.apache.logging.log4j.LogManager;

public class JdbcPersonDao implements PersonDao {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(JdbcPersonDao.class);
    
    public static final String TABLE_NAME = "Person";
    public static final String COLUMN_ID = TABLE_NAME + ".id";
    public static final String COLUMN_NAME = TABLE_NAME + ".name";
    public static final String COLUMN_SURNAME = TABLE_NAME + ".surname";
    public static final String COLUMN_PHONE = TABLE_NAME + ".phone";
    public static final String COLUMN_DATE = TABLE_NAME + ".birthday";
    public static final String COLUMN_STATUS = TABLE_NAME + ".status";
    public static final String COLUMN_ROLE = TABLE_NAME + ".role_id";
    public static final String COLUMN_QUALIFICATION = TABLE_NAME + ".qualification_id";
    public static final String COLUMN_LOGIN = TABLE_NAME + ".login";
    public static final String COLUMN_PASSWORD = TABLE_NAME + ".password";
    public static final String COLUMN_EMAIL = TABLE_NAME + ".email";
    
    public static final String NOT_NULL_STATEMENT = "NOT NULL";
//    public static final String CREATE_STATEMENT =
//            "INSERT INTO " + TABLE_NAME + " (name, surname, phone, birthday, status,"
//            + " role_id, qualification_id, login, password, email)"
//            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String CREATE_STATEMENT =
            "INSERT INTO " + TABLE_NAME + " (name, surname, phone, birthday,"
            + " login, password, email)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String UPDATE_STATEMENT =
            "UPDATE " + TABLE_NAME + " SET name=?, surname=?, phone=?, birthday=?, "
            + "status=?, role_id=?, qualification_id=?, login=?, password=?, email=?"
            + " WHERE id=?;";
    public static final String DELETE_STATEMENT =
            "DELETE FROM " + TABLE_NAME + " WHERE id = ?;";
    public static final String FIND_BY_ID_STATEMENT =
            "SELECT * from Person" +
            " left join Role on Person.role_id = Role.id" +
            " left join Qualification on Person.qualification_id = Qualification.id" +
            " where Person.id = ?;";
    public static final String FIND_BY_LOGIN_STATEMENT =
            "SELECT * from Person" +
            " left join Role on Person.role_id = Role.id" +
            " left join Qualification on Person.qualification_id = Qualification.id" +
            " where Person.login = ?;";
    public static final String FIND_FREE_DEVS_STATEMENT =
            "SELECT * from Person" +
            " left join Role on Person.role_id = Role.id" +
            " left join Qualification on Person.qualification_id = Qualification.id" +
            " where Role.name = ? and Person.status = ?;";
    public static final String FIND_ALL_STATEMENT =
            "SELECT * from Person" +
            " left join Role on Person.role_id = Role.id" +
            " left join Qualification on Person.qualification_id = Qualification.id;";
    
//    public static final String FIND_BY_LOGIN_PATTERN =
//            "SELECT * from Person" +
//            " left join Role on Person.role_id = Role.id" +
//            " left join Qualification on Person.qualification_id = Qualification.id" +
//            " where";
    
    private Person getResult(ResultSet rs) throws SQLException {
        return new PersonBuilder()
                    .setId(rs.getInt(COLUMN_ID))
                    .setName(rs.getString(COLUMN_NAME))
                    .setSurname(rs.getString(COLUMN_SURNAME))
                    .setPhone(rs.getString(COLUMN_PHONE))
                    .setBirthday(rs.getDate(COLUMN_DATE))
                    .setStatus(rs.getBoolean(COLUMN_STATUS))
                    .setRole(new RoleBuilder()
                            .setId(rs.getInt(COLUMN_ROLE))
                            .setName(rs.getString(JdbcRoleDao.COLUMN_NAME))
                            .build())
                    .setQualification(new QualificationBuilder()
                            .setId(rs.getInt(COLUMN_QUALIFICATION))
                            .setName(rs.getString(JdbcQualificationDao.COLUMN_NAME))
                            .build())
                    .setLogin(rs.getString(COLUMN_LOGIN))
                    .setPassword(rs.getString(COLUMN_PASSWORD))
                    .setEmail(rs.getString(COLUMN_EMAIL))
                    .build();
    }
    
    @Override
    public Person create(Connection connection, Person e) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(CREATE_STATEMENT, Statement.RETURN_GENERATED_KEYS))
        {
            preparedStatement.setString(1, e.getName());
            preparedStatement.setString(2, e.getSurname());
            preparedStatement.setString(3, e.getPhone());
            preparedStatement.setDate(4, e.getBirthday());
//            preparedStatement.setBoolean(5, e.getStatus());
//            preparedStatement.setInt(6, e.getRole().getId());
//            preparedStatement.setInt(7, e.getQualification().getId());
            preparedStatement.setString(5, e.getLogin());
            preparedStatement.setString(6, e.getPassword());
            preparedStatement.setString(7, e.getEmail());
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
    public boolean update(Connection connection, Person e) {
        Person existed = findById(connection, e.getId());
        existed.setStatus(e.getStatus()); // TODO: maybe should change this... 
        
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(UPDATE_STATEMENT))
        {
            preparedStatement.setString(1, existed.getName());
            preparedStatement.setString(2, existed.getSurname());
            preparedStatement.setString(3, existed.getPhone());
            preparedStatement.setDate(4, existed.getBirthday());
            preparedStatement.setBoolean(5, existed.getStatus());
            preparedStatement.setInt(6, existed.getRole().getId());
            preparedStatement.setInt(7, existed.getQualification().getId());
            preparedStatement.setString(8, existed.getLogin());
            preparedStatement.setString(9, existed.getPassword());
            preparedStatement.setString(10, existed.getEmail());
            preparedStatement.setInt(11, existed.getId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
            
            return true;
        } catch (SQLException ex) {
            logger.error("Error while database processing " + ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean delete(Connection connection, Person e) {
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
    public Person findById(Connection connection, int... id) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_ID_STATEMENT))
        {
            preparedStatement.setInt(1, id[0]);
        
            ResultSet rs = preparedStatement.executeQuery();

            Person temp = null;
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
    public Set<Person> findAll(Connection connection) {
        try (Statement query = connection.createStatement())
        {
            ResultSet rs = query.executeQuery(FIND_ALL_STATEMENT);
            
            Set<Person> res = new HashSet<>();
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
    public Person findByLogin(Connection connection, String login) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_BY_LOGIN_STATEMENT))
        {
            preparedStatement.setString(1, login);
        
            ResultSet rs = preparedStatement.executeQuery();

            Person temp = null;
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
    public Set<Person> findFreeDevs(Connection connection) {
        try (PreparedStatement preparedStatement = 
                    connection.prepareStatement(FIND_FREE_DEVS_STATEMENT))
        {
            preparedStatement.setString(1, Roles.DEV_ROLE);
            preparedStatement.setBoolean(2, true);
        
            ResultSet rs = preparedStatement.executeQuery();

            Set<Person> res = new HashSet<>();
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
