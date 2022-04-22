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
        }

        pageVariables.put("Assignee", mySql.getFilters("Assignee"));
        pageVariables.put("Project", mySql.getFilters("project"));

        pageVariables.put("username", session.getAttribute("username"));

        response.setContentType("text/html;charset=utf-8");

        ArrayList<String> usersNamesList = mySql.getUsers();
        ArrayList<String> statusList = new ArrayList<>();
        ArrayList<String> priorityList = new ArrayList<>();

        priorityList.add("low");
        priorityList.add("mid");
        priorityList.add("high");

        statusList.add("Backlog");
        statusList.add("Develop");
        statusList.add("Done");
        statusList.add("Obsolete");

        if (request.getParameter("taskID") == null || request.getParameter("taskID") == "") {

            pageVariables.put("status", getOptionsHTML(statusList));
            pageVariables.put("assignee", getOptionsHTML(usersNamesList));
            pageVariables.put("priority", getOptionsHTML(priorityList));

            response.getWriter().println(PageGenerator.instance().getPage("newTask.html", pageVariables));
        } else {

            String taskID = request.getParameter("taskID");
            usersNamesList = setFirst(mySql.getUserName(pageVariables.get("username").toString()), usersNamesList);
            Map<String, Object> task = mySql.getTask(taskID);

            usersNamesList = setFirst(task.get("assignee").toString(), usersNamesList);
            priorityList = setFirst(task.get("priority").toString(), priorityList);
            statusList = setFirst(task.get("status").toString(), statusList);

            pageVariables.put("assignee", getOptionsHTML(usersNamesList));
            pageVariables.put("priority", getOptionsHTML(priorityList));
            pageVariables.put("status", getOptionsHTML(statusList));

            pageVariables.put("taskID", request.getParameter("taskID"));

            pageVariables.put("name", task.get("name"));
            pageVariables.put("project", task.get("project"));
            pageVariables.put("contact_person", (task.get("contact_person") == null) ? "" : task.get("contact_person"));
            pageVariables.put("contact", (task.get("contact") == null) ? "" : task.get("contact"));
            pageVariables.put("create_time", task.get("create_date"));
            pageVariables.put("creator", task.get("creator"));
            pageVariables.put("description", task.get("description"));
            response.getWriter().println(PageGenerator.instance().getPage("task.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        HttpSession session=request.getSession(false);
        if (session == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
        }
    }

    private static ArrayList<String> setFirst (String element, ArrayList<String> arrayList) {
        int indexOfElement = arrayList.indexOf(element);

        if (indexOfElement > 0) {
            System.out.println("ok");
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
