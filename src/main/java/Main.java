import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
//        GF gf = new GF(4, "10011");
//        int[] powTable = gf.getPowTable();
//        int[] logTable = gf.getLogTable();
//        int[][] mulTable = gf.getMulTable();
//
//        System.out.println(mulTable[powTable[3]][powTable[5]]);


//        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        context.addServlet(new ServletHolder(new CalculatorServlet()), "/calc");
//        Server server = new Server(8080);
//        server.setHandler(context);
//
//        server.start();
//        server.join();

        String src = "e1 +e2+ (e0 +e3)*e2";
//        System.out.println(Arrays.toString(Parser.parse(s)));
        GF gf = new GF(4, "10011");
        RPN rpn = new RPN(gf);
        System.out.println(String.join(" ", RPN.parse(src)) + " = e" + gf.log(rpn.calculate(src)));
    }

    public static void buildField(GF gf) {
        for (int i = 0; i < gf.getPeriod(); i++) {
            System.out.printf("e^%-2d | %4s | %2d | %2d\n",
                    i,
                    gf.toBinaryString(gf.pow(i)),
                    gf.inverse(gf.pow(i)),
                    gf.pow(i));
        }
    }
}
