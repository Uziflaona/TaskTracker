package servlets;

import main.MySql;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginRequestServlet extends HttpServlet {

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws
            ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("status", "");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("login.html", pageVariables));
        response.setStatus(HttpServletResponse.SC_OK);

    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws
            ServletException, IOException {
        Map<String, Object> pageVariables = new HashMap<>();

        String username = request.getParameter("username");
        String password = request.getParameter("password");


        response.setContentType("text/html;charset=utf-8");

        MySql mySql = new MySql();

        if(mySql.getAuthorization(username, password).equals("Allright")) {
            HttpSession session=request.getSession();
            session.setMaxInactiveInterval(600);
            session.setAttribute("username", username);
            response.sendRedirect("tasks");
        } else {
            pageVariables.put("status", mySql.getAuthorization(username, password));
            response.getWriter().println(PageGenerator.instance().getPage("login.html", pageVariables));
        }
        response.setStatus(HttpServletResponse.SC_OK);

    }


}
