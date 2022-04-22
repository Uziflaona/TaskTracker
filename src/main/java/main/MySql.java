package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static String getAllTasks() {
        String fieldValues = new String();
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from task;");

            while (resultSet.next()) {
                fieldValues += ("<li>\n" +
                        "      <table border=\"1\">\n" +
                        "        <tr>\n" +
                        "          <td colspan=\"4\"> <a href=\"task?taskID=" + resultSet.getString(1) +
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

    public static String getTasks(String assignee, String status, String priority, String project) {
        String fieldValues = new String("");
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            String where = new String("where");

            if (assignee.equals("*")) {
                assignee = "";
                if (status.equals("*")) {
                    status = "";
                    if (priority.equals("*")) {
                        priority = "";
                        if (project.equals("*")) {
                            project = ";";
                            where = "";
                        } else {
                            project = "project = '" + project + "';";
                        }
                    } else {
                        priority = "priority = '" + priority + "'";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = " and project = '" + project + "';";
                        }
                    }
                } else {
                    status = "status = '" + status + "'";
                    if (priority.equals("*")) {
                        priority = "";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    } else {
                        priority = "and priority = '" + priority + "'";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    }
                }
            } else {
                assignee = "assignee = '" + assignee + "'";
                if (status.equals("*")) {
                    status = "*";
                    if (priority.equals("*")) {
                        priority = "";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    } else {
                        priority = "and priority = '" + priority + "'";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    }
                } else {
                    status = "and status = '" + status + "'";
                    if (priority.equals("*")) {
                        priority = "";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    } else {
                        priority = "and priority = '" + priority + "'";
                        if (project.equals("*")) {
                            project = ";";
                        } else {
                            project = "and project = '" + project + "';";
                        }
                    }
                }
            }


            resultSet = statement.executeQuery("select * from task " + where + " " + assignee + " " + status + " " + priority + " " + project);

            while (resultSet.next()) {
                if (resultSet.getString(1) != null) {
                    fieldValues += ("<li>\n" +
                            "      <table border=\"1\">\n" +
                            "        <tr>\n" +
                            "          <td colspan=\"4\"> <a href=\"task?taskID=" + resultSet.getString(1) +
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

//    public static

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

    public static ArrayList<String> getUsers() {

        ArrayList<String> namesArrayList = new ArrayList<>();

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select name from users;");

            while (resultSet.next()) {
                namesArrayList.add(resultSet.getString(1));
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return namesArrayList;
    }

    public static Map<String, Object> getTask(String taskID) {

        Map<String, Object> taskVariables = new HashMap<>();

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select * from task where task_id = " + taskID + ";");
            if(resultSet.next()) {
                taskVariables.put("name", resultSet.getString(2));
                taskVariables.put("status", resultSet.getString(3));
                taskVariables.put("assignee", resultSet.getString(4));
                taskVariables.put("priority", resultSet.getString(5));
                taskVariables.put("project", resultSet.getString(6));
                taskVariables.put("contact_person", resultSet.getString(7));
                taskVariables.put("contact", resultSet.getString(8));
                taskVariables.put("create_date", resultSet.getString(9));
                taskVariables.put("creator", resultSet.getString(10));
                taskVariables.put("description", resultSet.getString(11));
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return taskVariables;
    }

    public static String getUserName(String username) {

        String name = new String("");

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select name from users where login = " + username + ";");
            resultSet.next();

            name = resultSet.getString(1);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return name;
    }
}
