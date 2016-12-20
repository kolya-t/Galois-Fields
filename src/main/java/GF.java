public class GF {
    private int m;          // степень поля
    private int fieldSize;  // размер поля (2^m)
    private int p;          // образующий полином поля

    private int period;     // максимальное значение в поле
    private int[] powTable;
    private int[] logTable;
    private int[][] mulTable;
    private int[][] divTable;

    /**
     * Создание поля GF(2^m) на основе образующего полинома p.
     * Образующий полином p(x) задаётся как число, показывающие при каких
     * элементах стоят единицы начиная со старшего разряда.
     * То есть в порядке x^m+x^(m-1)+..+x^0
     *
     * @param m степень поля
     * @param p образующий полином поля
     */
    public GF(int m, String p) {
        this(m, Integer.parseInt(p, 2));
    }

    /**
     * Создание поля GF(2^m) на основе образующего полинома p.
     * Образующий полином p(x) задаётся как десятичное число
     *
     * @param m степень поля
     * @param p образующий полином поля
     */
    public GF(int m, int p) {
        assert m > 0;
        assert p > 0;

        this.m = m;
        fieldSize = pow(2, m);
        period = fieldSize - 1;
        this.p = p;

        logTable = new int[fieldSize];
        powTable = new int[fieldSize];
        mulTable = new int[fieldSize][fieldSize];
        divTable = new int[fieldSize][fieldSize];

        // Формирование powTable и logTable
        int value = 1;
        for (int pow = 0; pow < period; pow++) {
            powTable[pow] = value;
            logTable[value] = pow;
            value *= 2;
            if (value > period) {
                value = value ^ p;
            }
        }

        // Формирование mulTable
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (i == 0 || j == 0) {
                    mulTable[i][j] = 0;
                    continue;
                }
                int z = logTable[i] + logTable[j];
                if (z >= period) {
                    z -= period;
                }
                z = powTable[z];
                mulTable[i][j] = z;
            }
        }

        // Формирование divTable
        for (int i = 0; i < fieldSize; i++) {
            for (int j = 1; j < fieldSize; j++) {
                if (i == 0) {
                    divTable[i][j] = 0;
                    continue;
                }
                int z = logTable[i] - logTable[j];
                if (z < 0) {
                    z += period;
                }
                z = powTable[z];
                divTable[i][j] = z;
            }
        }
    }

    /**
     * Возвращает a^b
     *
     * @param a основание степени
     * @param b показатель степени
     * @return a^b
     */
    private static int pow(int a, int b) {
        return (int) Math.round(Math.exp(b * Math.log(a)));
    }

    /**
     * Функция сложения двух элементов поля, представленных как десятичные числа
     * @param x первое слагаемое
     * @param y второе слагаемое
     * @return результат сложения элементов поля
     */
    public int add(int x, int y) {
        assert (x >= 0 && x < fieldSize && y >= 0 && y < fieldSize);
        return x ^ y;
    }

    /**
     * Функция умножения двух элементов поля, представленных как десятичные числа
     * @param x первый множитель
     * @param y второй множитель
     * @return результат умножения элементов поля
     */
    public int multiply(int x, int y) {
        assert (x >= 0 && x < fieldSize && y >= 0 && y < fieldSize);
        return mulTable[x][y];
    }

    /**
     * Функция умножения двух элементов поля, представленных как десятичные числа
     * @param x делимое
     * @param y делитель
     * @return частное от деления x на y
     */
    public int divide(int x, int y) {
        assert (x >= 0 && x < fieldSize && y > 0 && y < fieldSize);
        return divTable[x][y];
    }

    public int getM() {
        return m;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getP() {
        return p;
    }

    public int getPeriod() {
        return period;
    }

    public int log(int pow) {
        return logTable[pow];
    }

    public int pow(int log) {
        return powTable[log];
    }

    public String toBinaryString(int n) {
        StringBuilder reverse = new StringBuilder(Integer.toBinaryString(n)).reverse();
        while (reverse.length() < m) {
            reverse.append("0");
        }
        return reverse.toString();
    }

    public int inverse(int n) {
        return Integer.parseInt(toBinaryString(n), 2);
    }
}
