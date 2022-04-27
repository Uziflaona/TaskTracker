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

public class EditRequestServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws
            ServletException, IOException {

        MySql mySql = new MySql();

        Map<String, Object> pageVariables = new HashMap<>();

        HttpSession session=request.getSession(false);
        if (session == null) {
            response.sendRedirect("/");
            return;
        }

        String username = session != null ? session.getAttribute("username").toString() : null;

        pageVariables.put("username", username);

        response.setContentType("text/html;charset=utf-8");

        ArrayList <String> usersNamesList = mySql.getUsers();
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

        if (request.getParameter("taskID") == null || request.getParameter("taskID") == "") {
//            response.getWriter().println(PageGenerator.instance().getPage("newTask.html", pageVariables));
        } else {
            String taskID = request.getParameter("taskID");

            Map<String, Object> task = mySql.getTask(taskID);

            usersNamesList = setFirst(mySql.getUserName(username), usersNamesList);

            usersNamesList = setFirst(task.get("assignee").toString(), usersNamesList);
            priorityList = setFirst(task.get("priority").toString(), priorityList);
            statusList = setFirst(task.get("status").toString(), statusList);

            pageVariables.put("assignee", getOptionsHTML(usersNamesList));
            pageVariables.put("priority", getOptionsHTML(priorityList));
            pageVariables.put("status", getOptionsHTML(statusList));

            pageVariables.put("name", task.get("name"));
            pageVariables.put("project", task.get("project"));
            pageVariables.put("contact_person", task.get("contact_person") == null ? "" : task.get("contact_person"));
            pageVariables.put("contact", task.get("contact") == null ? "" : task.get("contact"));
            pageVariables.put("creator", task.get("creator"));
            pageVariables.put("create_time", task.get("create_date"));
            pageVariables.put("description", task.get("description"));


            pageVariables.put("taskID", request.getParameter("taskID"));

            response.getWriter().println(PageGenerator.instance().getPage("task.html", pageVariables));

        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> task = new HashMap<>();

        HttpSession session=request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
            return;
        }

        task.put("task_id", request.getParameter("task_id"));
        task.put("name", request.getParameter("name"));
        task.put("status", request.getParameter("status"));
        task.put("assignee", request.getParameter("assignee"));
        task.put("priority", request.getParameter("priority"));
        task.put("project", request.getParameter("project"));
        task.put("contact_person", request.getParameter("contact_person"));
        task.put("contact", request.getParameter("contact"));
        task.put("description", request.getParameter("description"));

        MySql mySql = new MySql();

        mySql.updateTask(task);

        response.sendRedirect("/task?taskID=" + task.get("task_id"));
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
