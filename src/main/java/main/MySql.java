package main;

import templater.PageGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySql {
    private static final String url = "jdbc:mysql://localhost:3306/task_tracker";
    private static final String user = "root";
    private static final String password = "root";

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static String getFilters (String fieldName) {
        String fieldValues = new String();
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select DISTINCT " + fieldName + " from task;");

            while (resultSet.next()) {
            fieldValues += ("<option>" + resultSet.getString(1) + "</option>");

            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return fieldValues;
    }
}
