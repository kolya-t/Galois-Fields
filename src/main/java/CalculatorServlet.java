import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CalculatorServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int pow = Integer.parseInt(req.getParameter("power"));
            String p = req.getParameter("polinom");
            String formula = req.getParameter("formula");


        } catch (NumberFormatException e) {
            resp.getWriter().println("Ошибка при переводе строки в число");
        }
    }
}
