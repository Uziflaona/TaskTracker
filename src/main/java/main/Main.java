package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;

public class Main {
    public static void main (String[] args) throws Exception { // Инициализация сервера
        MainRequestServlet mainRequestServlet = new MainRequestServlet();
        LoginRequestServlet loginRequestServlet = new LoginRequestServlet();
        EditTaskServlet editTaskServlet = new EditTaskServlet();
        EditNewTaskServlet editNewTaskServlet = new EditNewTaskServlet();
        EditUsersServlet editUsersServlet = new EditUsersServlet();
        LogoutServlet logoutServlet = new LogoutServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(loginRequestServlet), "/*");
        context.addServlet(new ServletHolder(mainRequestServlet), "/tasks");
        context.addServlet(new ServletHolder(editTaskServlet), "/task");
        context.addServlet(new ServletHolder(editNewTaskServlet), "/newTask");
        context.addServlet(new ServletHolder(editUsersServlet), "/editUsers");
        context.addServlet(new ServletHolder(logoutServlet), "/logout");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
