import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = new HashMap<>();
        String power = req.getParameter("power");
        String p = req.getParameter("polinom");
        String formula = req.getParameter("formula");

            /* TODO: Здесь короче всякие проверки */

//        int pow = Integer.parseInt(power);
//        GF gf = new GF(pow, p);
//        RPN rpn = new RPN(gf);
        map.put("formula", "123");

        resp.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        resp.getWriter().println(gson.toJson(map));
    }
}
