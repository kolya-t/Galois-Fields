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
        resp.setContentType("text/html;charset=utf-8");
        // проверка параметров
        if (!checkParameters(req.getParameterMap())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            sendError(resp);
            return;
        }

        Map<String, Object> calculatedMap = calculate(req);
        PageGenerator templater = PageGenerator.instance();
        setSharedVariables(req, templater);

        String json = new Gson().toJson(req.getParameterMap());
        System.out.println(json);
        resp.getWriter().println(json);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.instance().getPage("page.html", calculatedMap));
    }

    /**
     * Выполнение расчета
     * @param req
     */
    private Map<String, Object> calculate(HttpServletRequest req) {
        Map<String, Object> map = new HashMap<>();

        int power = Integer.parseInt(req.getParameter("power"));
        String polynom = req.getParameter("polynom");
        String formula = req.getParameter("formula");
        boolean table = false;
        String table_first;
        int table_count;
        if (req.getParameter("table") != null) {
            table = true;
            table_first = req.getParameter("table_first");
            table_count = Integer.parseInt(req.getParameter("table_count"));
        }

        GF gf = new GF(power, polynom);
        RPN rpn = new RPN(gf);

        return map;
    }

    private void setSharedVariables(HttpServletRequest req, PageGenerator templater) {
        String table = req.getParameter("table");
        if (table != null) {
            templater.setSharedVariable("table", true);
        } else {
            return;
        }
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

            ;
            if (parameterMap.get("table") == null) {
                return true;
            }
            boolean table = true;

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
