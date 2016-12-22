import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        map.putAll(req.getParameterMap());
        String power = req.getParameter("power");
        System.out.println("power = " + power);
        String p = req.getParameter("polynomial");
        System.out.println("p = " + p);
        String formula = req.getParameter("formula");
        System.out.println("formula = " + formula);
            /* TODO: Здесь короче всякие проверки */
        int pow = Integer.parseInt(power);
//        GF gf = new GF(pow, p);
//        RPN rpn = new RPN(gf);

        resp.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        resp.getWriter().println(gson.toJson(map));
    }
}
