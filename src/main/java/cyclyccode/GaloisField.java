package cyclyccode;

import java.util.HashMap;
import java.util.Map;

public class GaloisField {

    private final int[] logTable;
    private final int[] powTable;
    private final int[][] mulTable;
    private final int[][] divTable;
    private final int fieldSize;
    private final int primitivePeriod;
    private final int primitivePolynomial;

    private static final int DEFAULT_FIELD_SIZE = 256;
    private static final int DEFAULT_PRIMITIVE_POLYNOMIAL = 285;

    static private final Map<Integer, GaloisField> instances
            = new HashMap<>();

    public static GaloisField getInstance(int fieldSize,
                                          int primitivePolynomial) {
        int key = ((fieldSize << 16) & 0xFFFF0000) + (primitivePolynomial & 0x0000FFFF);
        GaloisField gf;
        synchronized (instances) {
            gf = instances.get(key);
            if (gf == null) {
                gf = new GaloisField(fieldSize, primitivePolynomial);
                instances.put(key, gf);
            }
        }
        return gf;
    }

    public double fact(double num) {
        return (num == 0) ? 1 : num * fact(num - 1);
    }

    public int kratn_oshibki(int dmin) {

        int t_kr = (int) Math.floor((dmin - 1) / 2);
        return t_kr;
    }

    public static GaloisField getInstance() {
        return getInstance(DEFAULT_FIELD_SIZE, DEFAULT_PRIMITIVE_POLYNOMIAL);
    }

    private GaloisField(int fieldSize, int primitivePolynomial) {
        assert fieldSize > 0;
        assert primitivePolynomial > 0;

        this.fieldSize = fieldSize;
        this.primitivePeriod = fieldSize - 1;
        this.primitivePolynomial = primitivePolynomial;
        logTable = new int[fieldSize];
        powTable = new int[fieldSize];
        mulTable = new int[fieldSize][fieldSize];
        divTable = new int[fieldSize][fieldSize];
        int value = 1;
        for (int pow = 0; pow < fieldSize - 1; pow++) {
            powTable[pow] = value;
            logTable[value] = pow;
            value = value * 2;
            if (value >= fieldSize) {
                value = value ^ primitivePolynomial;
            }
        }

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 0; j < fieldSize; j++) {
                if (i == 0 || j == 0) {
                    mulTable[i][j] = 0;
                    continue;
                }
                int z = logTable[i] + logTable[j];
                z = z >= primitivePeriod ? z - primitivePeriod : z;
                z = powTable[z];
                mulTable[i][j] = z;
            }
        }

        for (int i = 0; i < fieldSize; i++) {
            for (int j = 1; j < fieldSize; j++) {
                if (i == 0) {
                    divTable[i][j] = 0;
                    continue;
                }
                int z = logTable[i] - logTable[j];
                z = z < 0 ? z + primitivePeriod : z;
                z = powTable[z];
                divTable[i][j] = z;
            }
        }
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public int getPrimitivePolynomial() {
        return primitivePolynomial;
    }

    public int add(int x, int y) {
        assert (x >= 0 && x < getFieldSize() && y >= 0 && y < getFieldSize());
        return x ^ y;
    }

    public int multiply(int x, int y) {
        assert (x >= 0 && x < getFieldSize() && y >= 0 && y < getFieldSize());
        return mulTable[x][y];
    }

    public int divide(int x, int y) {
        assert (x >= 0 && x < getFieldSize() && y > 0 && y < getFieldSize());
        return divTable[x][y];
    }

    public int[] multiply(int[] p, int[] q) {
        int len = p.length + q.length - 1;
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = 0;
        }

        for (int i = 0; i < p.length; i++) {
            for (int j = 0; j < q.length; j++) {
                result[i + j] = add(result[i + j], multiply(p[i], q[j]));
            }
        }
        return result;
    }

    public int[] dev(int[] devided, int[] dev) {
        String result = "";//результат деления
        for (int i = 0; i < devided.length - dev.length + 1; i++)//чтобы в начале не быть не было нулей 001100
        {
            if (devided[i] != 0) {//теперь у нас имеется часть делителя, к примеру 1100
                int temp = devided[i];//первое значение - чтобы узнать, на что домножать надо - для частного
                for (int tt = 0; tt < dev.length; tt++)//сложили почисленно делимое с частным
                {
                    devided[i + tt] = add(devided[i + tt], dev[tt]);
                }
                result += divide(temp, dev[0]);//вместо единицы
            } else {
                result += "0";
            }
        }
        int result2[] = new int[result.length()];//создадим переменную для ретёрна
        for (int i = 0; i < result.length(); i++) {
            result2[i] = Character.getNumericValue(result.charAt(i));
        }
        //System.out.printf("<end>"+result+"</end>");
        return result2;
    }

    public int[] flip(int[] temperror) {
        int tempErr2[] = new int[temperror.length];
        for (int i = 0; i < temperror.length; i++) {
            tempErr2[i] = temperror[temperror.length - i - 1];
        }
        return tempErr2;
    }

    public void remainder(int[] dividend, int[] divisor) {
        for (int i = dividend.length - divisor.length; i >= 0; i--) {
            int ratio = divTable[dividend[i + divisor.length - 1]][divisor[divisor.length - 1]];
            for (int j = 0; j < divisor.length; j++) {
                int k = j + i;
                dividend[k] = dividend[k] ^ mulTable[ratio][divisor[j]];
            }
        }
    }

    public int[] add(int[] p, int[] q) {
        int len = Math.max(p.length, q.length);
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            if (i < p.length && i < q.length) {
                result[i] = add(p[i], q[i]);
            } else if (i < p.length) {
                result[i] = p[i];
            } else {
                result[i] = q[i];
            }
        }
        return result;
    }
}
