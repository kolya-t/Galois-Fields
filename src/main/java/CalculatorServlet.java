import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class CalculatorServlet extends HttpServlet {
    public static int MIN_POWER = 2;
    public static int MAX_POWER = 15;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (checkParameters(req.getParameterMap())) {
            sendError(resp);
        }

        Map<String, Object> map = new HashMap<>();
        map.putAll(req.getParameterMap());
        String power = req.getParameter("power");
        String polynomial = req.getParameter("polynomial");
        String formula = req.getParameter("formula");

        int pow = Integer.parseInt(power);
//        GF gf = new GF(pow, polynomial);
//        RPN rpn = new RPN(gf);

        resp.setStatus(HttpServletResponse.SC_OK);
        Gson gson = new Gson();
        resp.getWriter().println(gson.toJson(map));
    }

    /**
     * Проверка полученных параметров
     *
     * @param parameterMap словарь с параметрами полученный из request
     * @return {@code true} если проверка прошла успешно и ошибок не обнаружено,
     * в ином случае возвращает {@code false}
     */
    private boolean checkParameters(Map<String, String[]> parameterMap) {
        try {
            int power = Integer.parseInt(parameterMap.get("power")[0]);
            if (power < MIN_POWER || power > MAX_POWER) {
                throw new InvalidParameterException();
            }

            String polynomial = parameterMap.get("polynomial")[0];
            if (polynomial.matches("[^01]")) {
                throw new InvalidParameterException();
            }

            String formula = parameterMap.get("formula")[0];
            if (formula.matches("[^eE0-9+*/()]")) {
                throw new InvalidParameterException();
            }

            String[] tableS = parameterMap.get("table");
            if (tableS == null) {
                return true;
            }
            boolean table = tableS[0].equals("on");

            if (table) {
                String table_first = parameterMap.get("table_first")[0];
                if (table_first.matches("[^eE0-9]")) {
                    throw new InvalidParameterException();
                }

                int table_count = Integer.parseInt(parameterMap.get("table_count")[0]);
                if (table_count < 1) {
                    throw new InvalidParameterException();
                }
            }

            return true;
        } catch (NumberFormatException | InvalidParameterException | NullPointerException e) {
            return false;
        }
    }

    /**
     * Отправляет клиенту сообщение об ошибке при вводе исходных данных
     * @param resp ссылка на response клиента
     * @throws IOException
     */
    private void sendError(HttpServletResponse resp) throws IOException {
        resp.getWriter().println("Ошибка при вводе данных");
    }
}
