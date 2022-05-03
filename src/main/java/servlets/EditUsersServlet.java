package servlets;

import main.MySql;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditUsersServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws
            ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();

        MySql mySql = new MySql();

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }


        String username = session != null ? session.getAttribute("username").toString() : null;

        if (!mySql.getUserClass(username).equals("admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        pageVariables.put("username", username);

        response.setContentType("text/html;charset=utf-8");

        ArrayList<String> usersNamesList = mySql.getUsersNames();
        ArrayList <String> statusList = new ArrayList<>();
        ArrayList <String> projectList = mySql.getFilters("project");
        ArrayList <String> priorityList = new ArrayList<>();

        usersNamesList.add(0, "*");

        projectList.add(0, "*");

        priorityList.add("*");
        priorityList.add("low");
        priorityList.add("mid");
        priorityList.add("high");

        statusList.add("*");
        statusList.add("Backlog");
        statusList.add("Develop");
        statusList.add("Done");
        statusList.add("Obsolete");

        pageVariables.put("Assignee", getOptionsHTML(usersNamesList));
        pageVariables.put("Project", getOptionsHTML(projectList));
        pageVariables.put("Status", getOptionsHTML(statusList));
        pageVariables.put("Priority", getOptionsHTML(priorityList));

        pageVariables.put("admin", "<a align=\"right\" href=\"\\editUsers\">Users</a>");


        pageVariables.put("newTask", "<form action=\"/newTask\" method=\"GET\"> " +
                "<input type=\"submit\" value=\"new Task\"> </form>");

        pageVariables.put("users", mySql.getUsers());

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(PageGenerator.instance().getPage("editUsers.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        MySql mySql = new MySql();

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }

        String username = session != null ? session.getAttribute("username").toString() : null;

        if (!mySql.getUserClass(username).equals("admin")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (request.getParameter("action").equals("edit")) {
            mySql.setUser(request.getParameter("user_id"), request.getParameter("login"),
                    request.getParameter("password"), request.getParameter("name"),
                    request.getParameter("class"));
        }

        if (request.getParameter("action").equals("add")) {
            mySql.addUser(request.getParameter("login"), request.getParameter("password"),
                    request.getParameter("name"), request.getParameter("class"));
        }

        pageVariables.put("username", username);

        response.setContentType("text/html;charset=utf-8");

        ArrayList<String> usersNamesList = mySql.getUsersNames();
        ArrayList <String> statusList = new ArrayList<>();
        ArrayList <String> projectList = mySql.getFilters("project");
        ArrayList <String> priorityList = new ArrayList<>();

        usersNamesList.add(0, "*");

        projectList.add(0, "*");

        priorityList.add("*");
        priorityList.add("low");
        priorityList.add("mid");
        priorityList.add("high");

        statusList.add("*");
        statusList.add("Backlog");
        statusList.add("Develop");
        statusList.add("Done");
        statusList.add("Obsolete");

        pageVariables.put("Assignee", getOptionsHTML(usersNamesList));
        pageVariables.put("Project", getOptionsHTML(projectList));
        pageVariables.put("Status", getOptionsHTML(statusList));
        pageVariables.put("Priority", getOptionsHTML(priorityList));

        pageVariables.put("admin", "<a align=\"right\" href=\"\\editUsers\">Users</a>");
        pageVariables.put("newTask", "<form action=\"/newTask\" method=\"GET\"> " +
                    "<input type=\"submit\" value=\"new Task\"> </form>");

        pageVariables.put("users", mySql.getUsers());

        ArrayList<String> user = new ArrayList<>();
        user.add(request.getParameter("user_id"));
        user.add(request.getParameter("login"));
        user.add(request.getParameter("password"));
        user.add(request.getParameter("name"));
        user.add(request.getParameter("class"));

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(PageGenerator.instance().getPage("editUsers.html", pageVariables));
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
