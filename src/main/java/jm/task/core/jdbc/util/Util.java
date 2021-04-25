package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/JDBCbase?autoReconnect=true";
    private static final String LOGIN = "root";
    private static final String PASSWORD = "psw_pa$$w0Rd";

    public static Connection openConnection() throws SQLException {
        registerDBDriver();
        return DriverManager.getConnection(URL, LOGIN, PASSWORD);
    }

    private static void registerDBDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
