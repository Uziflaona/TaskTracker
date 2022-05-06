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

    public static ArrayList<String> getFilters (String fieldName) { // получение списка возможных опций из таблицы задач
        ArrayList<String> fieldValues = new ArrayList<>();
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("select DISTINCT " + fieldName + " from task;");

            while (resultSet.next()) {
            fieldValues.add(resultSet.getString(1));

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

    public static String getTasks(String assignee, String status, String priority, String project) { // задачи по
                                                                                                    // фильтрам
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
                            project = "";
                            where = "";
                        } else {
                            project = "project = '" + project + "'";
                        }
                    } else {
                        priority = "priority = '" + priority + "'";
                        if (project.equals("*")) {
                            project = "";
                        } else {
                            project = " and project = '" + project + "'";
                        }
                    }
                } else {
                    status = "status = '" + status + "'";
                    if (priority.equals("*")) {
                        priority = "";
                    } else {
                        priority = "and priority = '" + priority + "'";
                    }
                    if (project.equals("*")) {
                        project = "";
                    } else {
                        project = "and project = '" + project + "'";
                    }
                }
            } else {
                assignee = "assignee = '" + assignee + "'";
                if (status.equals("*")) {
                    status = "";
                } else {
                    status = "and status = '" + status + "'";
                }
                if (priority.equals("*")) {
                    priority = "";
                } else {
                    priority = "and priority = '" + priority + "'";
                }
                if (project.equals("*")) {
                    project = "";
                } else {
                    project = "and project = '" + project + "'";
                }
            }


            resultSet = statement.executeQuery("select * from task " + where + " " + assignee + " " + status + " "
                    + priority + " " + project + "order by task_id desc;");

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
                            "           <td>Status: " + resultSet.getString(3) + "</td>\n" +
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

            resultSet = statement.executeQuery("select login, password from users where login = '" + username +
                    "';");

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

    public static ArrayList<String> getUsersNames() {

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

            resultSet = statement.executeQuery("select name from users where login = \"" + username + "\";");

            if(resultSet.next()) {

                name = resultSet.getString(1);
            }
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

    public static void updateTask (Map<String, Object> task) {
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            statement.executeUpdate("update task " +
                    "set name = '" + task.get("name") + "', " +
                    "status = '" + task.get("status") + "', " +
                    "assignee = '" + task.get("assignee") + "', " +
                    "priority = '" + task.get("priority") + "', " +
                    "project = '" + task.get("project") + "', " +
                    "contact_person = '" + task.get("contact_person") + "', " +
                    "contact = '" + task.get("contact") + "', " +
                    "description = '" + task.get("description") + "'" +
                    " where task_id = '" + task.get("task_id") + "';");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
    }

    public void createNewTask(Map<String, Object> task) {
        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            statement.executeUpdate("insert into task (name, status, assignee, priority, project, contact_person, " +
                    "contact, create_date, creator, description) values (" +
                    "'" + task.get("name") + "', " +
                    "'" + task.get("status") + "', " +
                    "'" + task.get("assignee") + "', " +
                    "'" + task.get("priority") + "', " +
                    "'" + task.get("project") + "', " +
                    "'" + task.get("contact_person") + "', " +
                    "'" + task.get("contact") + "', " +
                    "NOW(), " +
                    "'" + task.get("creator") + "', " +
                    "'" + task.get("description") + "');");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
    }

    public String getLastTaskId() {

        String taskId = "";

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("Select task_id from task " +
                                                    "order by task_id desc " +
                                                    "limit 1;");



            if (resultSet.next()) {
                taskId = resultSet.getString(1);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return taskId;
    }

    public String getUserClass(String login) {

        String userClass = new String("");

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("Select class from users " + "where login = '" + login + "';");

            if (resultSet.next()) {
                userClass = resultSet.getString(1);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return userClass;
    }

    public String getUsers() {

        String users = new String();

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("Select * from users");

            while (resultSet.next()) {
                ArrayList<String> classes = new ArrayList<>();
                classes.add("admin");
                classes.add("user");
                classes.add("limited");

                classes = setFirst(resultSet.getString(5), classes);

                users += "<li><form action=\"/editUsers\" method=\"POST\"><table border=\"1\"><tr>" +
                        "<td><input name=\"user_id\" readonly value=\"" + resultSet.getString(1) + "\"></td>" +
                        "<td><input name=\"login\" value=\"" + resultSet.getString(2) + "\"></td>" +
                        "<td><input name=\"password\" value=\"" + resultSet.getString(3) + "\"></td>" +
                        "<td><input name=\"name\" value=\"" + resultSet.getString(4) + "\"></td>" +
                        "<td><select name=\"class\">" + getOptionsHTML(classes) + "<select></td></tr></table>" +
                        "<input name=\"action\" type=\"submit\" value=\"edit\"></form></li>";
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return users;
    }

    public void setUser(String user_id, String login, String pwd, String name, String role) {

        String userClass = new String("");

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            statement.executeUpdate("Update users set login = '" + login + "', " +
                    "password = '" + pwd + "', name = '" + name + "', class = '" + role + "'" +
                    "where user_id = '" + user_id + "';");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return;
    }

    public void addUser(String login, String pwd, String name, String role) {

        String userClass = new String("");

        try {
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.createStatement();

            statement.executeUpdate("insert into users (login, password, name, class) values ('" + login + "', '" +
                    pwd + "', '" + name + "', '" + role + "');");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }finally {
            //close connection ,stmt and resultset here
            try { connection.close(); } catch(SQLException se) { }
            try { statement.close(); } catch(SQLException se) { }
            try { resultSet.close(); } catch(SQLException se) { }
        }
        return;
    }

    private static ArrayList<String> setFirst (String element, ArrayList<String> arrayList) {
        int indexOfElement = arrayList.indexOf(element);

        if (indexOfElement > 0) {
            arrayList.set(indexOfElement, arrayList.get(0));
            arrayList.set(0, element);
        }

        return arrayList;
    }

    private static String getOptionsHTML (ArrayList<String> arrayList) {
        String options = new String();

        for (int i = 0; i < arrayList.size(); i++) {
            options += "<option>" + arrayList.get(i) + "</option>";
        }

        return options;
    }
}
