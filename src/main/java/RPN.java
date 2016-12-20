import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

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


    /**
     * Превращает строку "e1 +e2+ (e0 +e3)*e2" в
     * [e1, +, e2, +, (, e0, +, e3, ), *, e2]
     */
    private static String[] parse(String s) {
        s = s.replaceAll("[ ]+", "");

        /* Заменить позже на что то нормальное. например сделать список
        возможных операций и по нему проходиться (не посимвольно)*/
        for (char c : "()+*/".toCharArray()) {
            String op = String.format("\\%c", c);
            System.out.println(op);
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
            if (isVariable(symbol) || isConstant(symbol)) {
                // то переписываем в выходную строку
                out.append(symbol);
            }

            // если текущий символ - "("
            if (symbol == "(") {
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

        String.joi

        return out.join(" ");
    }

    // вычисление выражения в обратной польской записи (постфиксной)
    double RPN::

    calculatePostfix(String expr) {
        QStringList symbList = expr.split(" ");
        QStack<double> stack;
        // операнды и результат промежуточной операции
        double op1, op2, res;
        // словарь, хранящий названия и значения переменных
        QMap<QString, double> variables;

        foreach(QString symbol, symbList) {
            // если символ - переменная
            if (isVariable(symbol)) {
                double value;
                // искать значение переменной в словаре
                if (variables.find(symbol) != variables.end()) {
                    value = variables.value(symbol);
                }
                // если не найдено - ввести
                else {
                    value = inputVariableValue(symbol);
                    variables.insert(symbol, value);
                }
                // запушим значение переменной в стек
                stack.push(value);
            }

            // если символ - число
            else if (isConstant(symbol)) {
                // запушить в стек
                stack.push(symbol.toDouble());
            }

            // если символ - операция
            else if (isOperation(symbol)) {
                op2 = stack.pop();
                op1 = stack.pop();
                if (isPlus(symbol)) {
                    res = op1 + op2;
                } else if (isMinus(symbol)) {
                    res = op1 - op2;
                } else if (isMultiply(symbol)) {
                    res = op1 * op2;
                } else if (isDivision(symbol)) {
                    res = op1 / op2;
                } else if (isPower(symbol)) {
                    res = pow(op1, op2);
                }
                stack.push(res);
            }
        }

        return stack.top();
    }

    // восстановление выражения из обратной польской записи в инфиксную
    QString RPN::

    postfixToInfix(String expr) {
        QStringList symbList = expr.split(" ");
        QStack<QString> stack;
        QMultiMap<QString, QString> operands; // все операнды
        QString op1, op2; // текущие два операнда
        QString temp;

        foreach(QString symbol, symbList) {
            // если текущий символ - переменная или число
            if (isVariable(symbol) || isConstant(symbol)) {
                stack.push(symbol);
            }
            // если это операция
            else {
                op2 = stack.pop();
                op1 = stack.pop();

                //
                int op1Priority = getPriority(operands.key(op1));
                if (op1Priority < getPriority(symbol)) {
                    op1 = "( " + op1 + " )";
                }

                int op2Priority = getPriority(operands.key(op2));
                if (op2Priority < getPriority(symbol)) {
                    op2 = "( " + op2 + " )";
                }

                temp = op1 + " " + symbol + " " + op2;
                operands.insert(symbol, temp);

                stack.push(temp);
            }
        }

        return stack.top();
    }

    String RPN::postfixToInfix2(String expr) {
//    QStringList symbList = expr.split(" ");
        QQueue<QString> queue;
        queue.fromStdList(expr.split(" ").toStdList());
        QStack<QString> stack;
        QMultiMap<QString, QString> operands; // все операнды
        QString op1, op2; // текущие два операнда
        QString temp;

        foreach(QString symbol, queue) {
            // если текущий символ - переменная или число
            if (isVariable(symbol) || isConstant(symbol)) {
                stack.push(symbol);
            }
            // если это операция
            else {
                op2 = stack.pop();
                op1 = stack.pop();

                //
                int op1Priority = getPriority(operands.key(op1));
                if (op1Priority < getPriority(symbol)) {
                    op1 = "( " + op1 + " )";
                }

                int op2Priority = getPriority(operands.key(op2));
                if (op2Priority < getPriority(symbol)) {
                    op2 = "( " + op2 + " )";
                }

                temp = op1 + " " + symbol + " " + op2;
                operands.insert(symbol, temp);

                stack.push(temp);
            }
        }

        return stack.top();
    }

    // удаление лишних скобок из выражения
    public String removeBrackets(String expr) {
        return postfixToInfix2(infixToPostfix(expr));
    }

    // ввод значения переменной
    double inputVariableValue(String varName) {
        double varValue;
        cout << varName.toStdString() << " = ";
        cin >> varValue;
        return varValue;
    }

    // вычисление выражения в инфиксной записи
    public double calculate(String expr) {
        return calculatePostfix(infixToPostfix(expr));
    }

    // является ли символ операцией
    private boolean isOperation(String s) {
        return isPlus(s) || isMinus(s) || isMultiply(s) || isDivision(s) || isPower(s);
    }

    // является ли символ операцией сложения
    private boolean isPlus(String op) {
        return op.equals("+");
    }

    // является ли символ операцией вычитания
    private boolean isMinus(String op) {
        return op.equals("-");
    }

    // является ли символ операцией умножения
    private boolean isMultiply(String op) {
        return op.equals("*");
    }

    // является ли символ операцией деления
    private boolean isDivision(String op) {
        return op.equals("/");
    }

    // является ли символ операцией возведения в степень
    private boolean isPower(String op) {
        return op.equals("**");
    }

    // является ли символ переменной
    private boolean isVariable(String s) {
        return String.valueOf(s.charAt(0)).matches("[^\\d/*\\-+^]");
    }

    // является ли символ числом
    private boolean isConstant(String s) {
        return String.valueOf(s.charAt(0)).matches("[\\d]");
    }
}
