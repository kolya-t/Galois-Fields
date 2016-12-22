import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;


public class Main {
    public static void main(String[] args) throws Exception {
        // обработчик статичных страниц
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase("public_html");

        // обработчик динамических запросов
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new CalculatorServlet()), "/calc");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] {resourceHandler, context});

        // сервер
        Server server = new Server(8080);
        server.setHandler(handlers);

        // запуск сервера
        server.start();
        Log.getRootLogger().info("Server started");
        server.join();
    }
}
