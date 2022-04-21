package main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.*;
import templater.*;

public class Main {
    public static void main (String[] args) throws Exception {
        MainRequestServlet mainRequestServlet = new MainRequestServlet();
        LoginRequestServlet loginRequestServlet = new LoginRequestServlet();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(loginRequestServlet), "/*");
        context.addServlet(new ServletHolder(mainRequestServlet), "/task");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
