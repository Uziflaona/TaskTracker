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

public class EditNewTaskServlet extends HttpServlet {

    MySql mySql = new MySql();

    public void doGet(HttpServletRequest request,
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



        ArrayList<String> usersNamesList = mySql.getFilters("assignee");
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

        pageVariables.put("taskID", "");
        pageVariables.put("username", session.getAttribute("username"));
        pageVariables.put("Assignee", getOptionsHTML(usersNamesList));
        pageVariables.put("Project", getOptionsHTML(projectList));
        pageVariables.put("Status", getOptionsHTML(statusList));
        pageVariables.put("Priority", getOptionsHTML(priorityList));

        pageVariables.put("assignee", getOptionsHTML(usersNamesList));
        pageVariables.put("priority", getOptionsHTML(priorityList));
        pageVariables.put("status", getOptionsHTML(statusList));

        pageVariables.put("name", "");
        pageVariables.put("project", "");
        pageVariables.put("contact_person", "");
        pageVariables.put("contact", "");
        pageVariables.put("creator", "");
        pageVariables.put("create_time", "");
        pageVariables.put("description", "");

        if (mySql.getUserClass(session.getAttribute("username").toString()).equals("admin")) {
            pageVariables.put("admin", "<a align=\"right\" href=\"\\editUsers\">Users</a>");
        } else {
            pageVariables.put("admin", "");
        }

        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().println(PageGenerator.instance().getPage("newTask.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        HttpSession session=request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }

        Map<String, Object> task = new HashMap<>();



        task.put("task_id", request.getParameter("task_id"));
        task.put("name", request.getParameter("name"));
        task.put("status", request.getParameter("status"));
        task.put("assignee", request.getParameter("assignee"));
        task.put("priority", request.getParameter("priority"));
        task.put("project", request.getParameter("project"));
        task.put("contact_person", request.getParameter("contact_person"));
        task.put("contact", request.getParameter("contact"));
        task.put("description", request.getParameter("description"));
        task.put("creator", mySql.getUserName(session.getAttribute("username").toString()));

        MySql mySql = new MySql();

        mySql.createNewTask(task);

        response.sendRedirect("/task?taskID=" + mySql.getLastTaskId());
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
