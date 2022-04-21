package main;

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

    public static String getTasks () {
        String fieldValues = new String();
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select DISTINCT * from task;");

            while (resultSet.next()) {
                fieldValues += ("<li>\n" +
                        "      <table border=\"1\">\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"4\"> <a href=\"task?taskname=" + resultSet.getString(2) +
                        "\">" + resultSet.getString(2) + "</a></td>\n" +
                        "          <td>" + resultSet.getString(4) + "</td>\n" +
                        "        </tr>\n" +
                        "        <tr>\n" +
                        "          <td>Priority: " + resultSet.getString(5) + "</td>\n" +
                        "          <td>Project: " + resultSet.getString(6) + "</td>\n" +
                        "          <td colspan=\"3\">" + resultSet.getString(9) + "</td>\n" +
                        "        </tr>\n" +
                        "      </table>\n" +
                        "    </li>");
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

    public static String getAuthorization(String username, String pwd) {

        String status = new String("");

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select login, password from users where login = '" + username + "';");

            if(resultSet.next()) {
                if(pwd.equals(resultSet.getString(2))) {
                    status = "Allright";
                } else {
                    status = "Wrong password!";
                }
            } else {
                status = "Wrong username!";
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return status;
    }
}
