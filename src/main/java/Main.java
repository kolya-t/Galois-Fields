import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class Main {
    public static void main(String[] args) throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new CalculatorServlet()), "/calc");
        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();

        String src = "e1 +e2+ (e0 +e3)*e2";
        GF gf = new GF(4, "10011");
        RPN rpn = new RPN(gf);
        System.out.println(String.join(" ", RPN.parse(src)) + " = e" + gf.log(rpn.calculate(src)));
    }
}
