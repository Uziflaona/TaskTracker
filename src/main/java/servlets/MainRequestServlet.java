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

            pageVariables.put("username", session.getAttribute("username"));
            pageVariables.put("Assignee", mySql.getFilters("Assignee"));
            pageVariables.put("Project", mySql.getFilters("project"));

            pageVariables.put("Tasks", mySql.getAllTasks());



            response.setContentType("text/html;charset=utf-8");

            response.getWriter().println(PageGenerator.instance().getPage("tasks.html", pageVariables));
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect("/");
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
        }

        response.setContentType("text/html;charset=utf-8");

        String assignee = request.getParameter("Assignee");
        String status = request.getParameter("Status");
        String priority = request.getParameter("Priority");
        String project = request.getParameter("Project");

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
        pageVariables.put("Assignee", mySql.getFilters("Assignee"));
        pageVariables.put("Project", mySql.getFilters("project"));
        pageVariables.put("Tasks", mySql.getTasks(assignee, status, priority, project));

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

}
