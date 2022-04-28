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

public class MainRequestServlet extends HttpServlet {

    MySql mySql = new MySql();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session=request.getSession(false);
        if (session!=null) {


            ArrayList <String> usersNamesList = mySql.getFilters("assignee");
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

            pageVariables.put("username", session.getAttribute("username"));
            pageVariables.put("Assignee", getOptionsHTML(usersNamesList));
            pageVariables.put("Project", getOptionsHTML(projectList));

            pageVariables.put("Tasks", mySql.getTasks("*", "*", "*", "*"));

            pageVariables.put("Status", getOptionsHTML(statusList));
            pageVariables.put("Priority", getOptionsHTML(priorityList));

            if (mySql.getUserClass(session.getAttribute("username").toString()).equals("admin")) {
                pageVariables.put("admin", "<a align=\"right\" href=\"\\editUsers\">Users</a>");
            } else {
                pageVariables.put("admin", "");
            }

            response.setContentType("text/html;charset=utf-8");

            response.getWriter().println(PageGenerator.instance().getPage("tasks.html", pageVariables));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session=request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }

        response.setContentType("text/html;charset=utf-8");

        String assignee = request.getParameter("Assignee");
        String status = request.getParameter("Status");
        String priority = request.getParameter("Priority");
        String project = request.getParameter("Project");

        ArrayList <String> usersNamesList = mySql.getFilters("assignee");
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


        usersNamesList = setFirst(assignee, usersNamesList);
        statusList = setFirst(status, statusList);
        projectList = setFirst(project, projectList);
        priorityList = setFirst(priority, priorityList);

        if (assignee == null || assignee.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
        }
//        pageVariables.put("message", assignee == null ? "" : assignee);
//        pageVariables.put("status", assignee == null ? "" : status);
//        pageVariables.put("priority", assignee == null ? "" : priority);
//        pageVariables.put("project", assignee == null ? "" : project);
        pageVariables.put("username", session.getAttribute("username"));
        pageVariables.put("Assignee", getOptionsHTML(usersNamesList));
        pageVariables.put("Project", getOptionsHTML(projectList));
        pageVariables.put("Tasks", mySql.getTasks(assignee, status, priority, project));
        pageVariables.put("Status", getOptionsHTML(statusList));
        pageVariables.put("Priority", getOptionsHTML(priorityList));

        if (mySql.getUserClass(session.getAttribute("username").toString()).equals("admin")) {
            pageVariables.put("admin", "<a align=\"right\" href=\"\\editUsers\">Users</a>");
        } else {
            pageVariables.put("admin", "");
        }

        response.getWriter().println(PageGenerator.instance().getPage("tasks.html", pageVariables));
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
