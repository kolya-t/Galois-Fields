import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedHashMap;
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

        PageGenerator templater = PageGenerator.instance();
        Map<String, Object> calculatedMap = calculate(req, templater);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(PageGenerator.instance().getPage("results.html", calculatedMap));
    }

    /**
     * Выполнение расчета
     * @param req
     */
    private Map<String, Object> calculate(HttpServletRequest req, PageGenerator templater) {
        Map<String, Object> map = new HashMap<>();

        int power = Integer.parseInt(req.getParameter("power"));
        String polynomial = req.getParameter("polynomial");
        String formula = req.getParameter("formula");

        /* Добавление в словарь */
        map.put("power", power);
        map.put("bi_polynomial", polynomial);
        map.put("formula", formula);


        GF gf = new GF(power, polynomial);
        map.put("polynomial", gf.toPolynomial(polynomial));
        RPN rpn = new RPN(gf);
        int res = rpn.calculate(formula);
        String result = "e<sup>" + gf.log(res) + "</sup>";
        String bi_result = gf.toBinaryString(res);

        map.put("bi_result", bi_result);
        map.put("result", result);

        int table_first;
        int table_count;
        if (req.getParameter("table") != null) {
            table_first = Integer.parseInt(req.getParameter("table_first").replaceAll("[^0-9]", ""));
            table_count = Integer.parseInt(req.getParameter("table_count"));

            /* Добавление в словарь */
//            map.put("tbl", true); // если не будет работать то убрать

            Map<String, Integer> elements = new LinkedHashMap<>();
            for (int i = table_first; i < gf.getFieldSize(); i++) {
                String binary = gf.toBinaryString(gf.pow(i));
                elements.put(binary, i);
                if (i == table_first + table_count - 1) {
                    break;
                }
            }
            templater.setSharedVariable("elements", elements);
            templater.setSharedVariable("table", true);
        } else {
            templater.setSharedVariable("table", false);
        }

        return map;
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
