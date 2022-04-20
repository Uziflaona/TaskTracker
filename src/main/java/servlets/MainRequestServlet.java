package servlets;

import main.MySql;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainRequestServlet extends HttpServlet {

    MySql mySql = new MySql();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);
//        pageVariables.put("message", "");
        pageVariables.put("Assignee", mySql.getFilters("Assignee"));
        pageVariables.put("Status", mySql.getFilters("status"));
        pageVariables.put("Priority", mySql.getFilters("priority"));
        pageVariables.put("Project", mySql.getFilters("project"));

        pageVariables.put("Tasks", mySql.getTasks());

        response.setContentType("text/html;charset=utf-8");

        response.getWriter().println(PageGenerator.instance().getPage("tasks.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

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

//        response.getWriter().println(PageGenerator.instance().getPage("page.html", pageVariables));
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
//        pageVariables.put("method", request.getMethod());
//        pageVariables.put("URL", request.getRequestURL().toString());
//        pageVariables.put("pathInfo", request.getPathInfo());
//        pageVariables.put("sessionId", request.getSession().getId());
//        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }

}
