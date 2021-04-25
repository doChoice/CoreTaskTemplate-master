package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS users (" +
            "id BIGINT NOT NULL AUTO_INCREMENT, " +
            "name CHAR(50), " +
            "lastname CHAR(50), " +
            "age TINYINT, " +
            "PRIMARY KEY (id))";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS users";
    private static final String SAVE_USER = "INSERT INTO users (name, lastname, age) VALUES (?, ?, ?)";
    private static final String REMOVE_USER_BY_ID = "DELETE FROM users WHERE id = ?";
    private static final String GET_ALL_USERS = "SELECT * FROM users";
    private static final String CLEAN_TABLE = "DELETE FROM users";
    private static Connection connection;

    {
        try {
            connection = Util.openConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROP_TABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.execute();
            connection.commit();
            System.out.println(" User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_USER_BY_ID)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        User user;
        List<User> users = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS);
            while(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastname");
                Byte age = resultSet.getByte("age");
                user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            statement.execute(CLEAN_TABLE);
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
