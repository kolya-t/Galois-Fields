import java.util.*;

public class RPN {

//    class RPN {
//        public:
//        static double  calculate         (const QString&);
//        static QString removeBrackets    (const QString&);
//        public: // сделать private
//        static QString infixToPostfix    (const QString&);
//        static double  calculatePostfix  (const QString&);
//        static QString postfixToInfix    (const QString&);
//        static QString postfixToInfix2   (const QString&);
//        private:
//        static bool    isOperation       (const QString&);
//        static bool    isPlus            (const QString&);
//        static bool    isMinus           (const QString&);
//        static bool    isMultiply        (const QString&);
//        static bool    isDivision        (const QString&);
//        static bool    isPower           (const QString&);
//        static bool    isVariable        (const QString&);
//        static bool    isConstant        (const QString&);
//        static int     getPriority       (const QString&);
//        static double  inputVariableValue(const QString&);
//    };

    private GF gf;

    public RPN(GF gf) {
        this.gf = gf;
    }


    // вычисление выражения в инфиксной записи
    public int calculate(String expr) {
        return calculatePostfix(infixToPostfix(expr));
    }

    /**
     * Превращает строку "e1 +e2+ (e0 +e3)*e2" в
     * [e1, +, e2, +, (, e0, +, e3, ), *, e2]
     */
    public static String[] parse(String s) {
        s = s.replaceAll("[ ]+", "");

        /* Заменить позже на что то нормальное. например сделать список
        возможных операций и по нему проходиться (не посимвольно)*/
        for (char c : "()+*/".toCharArray()) {
            String op = String.format("\\%c", c);
            s = s.replaceAll(op, String.format(" %s ", op));
        }
        return s.split("[ ]+");
    }

    private int getPriority(String operation) {
        switch (operation) {
            case "(":
                return 0;
            case ")":
                return 1;
            case "+":
//            case "-":
                return 2;
            case "*":
            case "/":
                return 3;
            case "^":
                return 4;
            default:
                return 5;
        }
    }

    // преобразование инфиксной записи в обратную польскую (постфиксную)
    private String infixToPostfix(String expr) {
        String[] symbList = parse(expr);
        Stack<String> stack = new Stack<>();
        List<String> out = new LinkedList<>();

        for (String symbol : symbList) {
            // если текущий символ - ")"
            if (symbol.equals(")")) {
                // то выталкиваем из стека все операции до "("
                while (!stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop(); // удаляем из стека "("
            }

            // если текущий символ - операнд
            if (isEpsilon(symbol) || symbol.toLowerCase().equals("e") || symbol.toLowerCase().equals("1")) {
                // то переписываем в выходную строку
                out.add(symbol);
            }

            // если текущий символ - "("
            if (symbol.equals("(")) {
                // то заталкиваем ее в стек
                stack.push(symbol);
            }

            // если текущий символ - операция
            if (isOperation(symbol)) {
                // и если стек пуст
                if (stack.empty()) {
                    // то записываем операцию в стек
                    stack.push(symbol);
                }
                // и если приоритет текущей операции выше
                // приоритета операции на вершине стека
                else if (getPriority(symbol) > getPriority(stack.peek())) {
                    // тогда заталкиваем поступившую операцию в стек
                    stack.push(symbol);
                }
                // если приоритет операции меньше
                else {
                    // тогда переписываем в выходную строку все операции
                    // из стека с большим или равным приоритетом
                    while (!stack.empty() && getPriority(stack.peek()) >= getPriority(symbol)) {
                        out.add(stack.pop());
                    }
                    // записываем в стек поступившую операцию
                    stack.push(symbol);
                }
            }
        }

        // после рассмотрения всего выражения переписываем
        // операции из стека в выходную строку
        while (!stack.empty()) {
            out.add(stack.pop());
        }

        return String.join(" ", out);
    }

    // вычисление выражения в обратной польской записи (постфиксной)
    private int calculatePostfix(String expr) {
        String[] symbList = expr.split(" ");
        Stack<Integer> stack = new Stack<>();
        // операнды и результат промежуточной операции
        int op1, op2, res = 0;

        for (String symbol : symbList) {

            // если символ e1, e0, e10 ...
            if (isEpsilon(symbol)) {
                // степень у эпсилон
                int degree = Integer.parseInt(symbol.replaceAll("[^\\d]", ""));
                stack.push(gf.pow(degree));
            }
            // если символ - e (e1)
            else if (symbol.toLowerCase().equals("e")) {
                stack.push(gf.pow(1));
            }
            // если символ - 1 (e0)
            else if (symbol.equals("1")) {
                stack.push(gf.pow(0));
            }

            // если символ - операция
            else if (isOperation(symbol)) {
                op2 = stack.pop();
                op1 = stack.pop();
                if (isPlus(symbol)) {
                    res = gf.add(op1, op2);
                } else if (isMultiply(symbol)) {
                    res = gf.multiply(op1, op2);
                } else if (isDivision(symbol)) {
                    res = gf.divide(op1, op2);
                }
                stack.push(res);
            }
        }

        return stack.peek();
    }

    private boolean isEpsilon(String symbol) {
        return symbol.toLowerCase().matches("e[\\d]+");
    }

    // является ли символ операцией
    private boolean isOperation(String s) {
        return isPlus(s) || isMultiply(s) || isDivision(s);
    }

    // является ли символ операцией сложения
    private boolean isPlus(String op) {
        return op.equals("+");
    }

    // является ли символ операцией умножения
    private boolean isMultiply(String op) {
        return op.equals("*");
    }

    // является ли символ операцией деления
    private boolean isDivision(String op) {
        return op.equals("/");
    }
}
