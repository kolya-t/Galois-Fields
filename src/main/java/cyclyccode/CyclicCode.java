package cyclyccode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CyclicCode {

    GaloisField gf = GaloisField.getInstance(); //Создаем поле Галуа по умолчанию
    int[] g;//полином
    int n = 15, k = 9, dmin = 0;
    String primitive_poly_string = ""; //Порождающий полином в виде строки
    int primitive_poly_int; //Порождающий полином в десятичном виде

    int[] notsystcode_arrayint; //Несистематический код в виде массива целочисленных значений
    String notsystcode_string = "";

    int[] systcode_arrayint; //Систематический код в виде массива целочисленных значений
    String systcode_string = "";

    JTextField field_polinom; //блок для ввода выбора полинома
    JTextField field_n, field_k, field_dmin;
    JTextField field_infvector; //блок для ввода информационного вектора
    JTextField field_true_coded_vector;//блок с выводом декодированного информ. вектора без ошибок
    JTextField field_edited_coded_vector;//блок для редактирования информационного вектора
    JTextField field_corrected_coded_vector; //блок с выводом отредактированного информационного вектора после ошибок
    JTextField field_decoded_vector; //блок с выводом декодированного вектора

    JLabel type_of_cod; //строка с выводом типа кодирования
    JLabel field_inform_program; //вывод информации о декодировании

    String inform_vector_temp; //Вывод информации
    int flag_of_cod_type = 0; //тип кода для декодирования(выбирается автоматически при нажатии на кнопки кодирования)

    JButton button_decoding;//когда нет кода - декодировать не надо

    CyclicCode() {
        g = new int[]{1, 1, 1, 1, 0, 0, 1};
        for (int i = 0; i < g.length; i++) {
            primitive_poly_string = primitive_poly_string + Integer.toString(g[i]);
        }
        primitive_poly_int = Integer.parseInt(primitive_poly_string, 2);
        gf.getInstance(2, primitive_poly_int);

        JFrame frame = new JFrame("Кодирование и декодирование циклических кодов");
        frame.setMinimumSize(new Dimension(750, 350));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        JLabel label = new JLabel();
        frame.add(label);

        label.setLayout(new BoxLayout(label, BoxLayout.PAGE_AXIS));

        //------Строка 1. Выбор NK кода------------—
        JPanel panel_1_code_NK = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_1_code_NK.setPreferredSize(new Dimension(700, 35));
        panel_1_code_NK.setMaximumSize(new Dimension(700, 35));

        JLabel nk1_n = new JLabel("n : ");//надпись
        panel_1_code_NK.add(nk1_n);

        field_n = new JTextField(3);//поле для ввода n
        field_n.setText("15");
        field_n.setForeground(Color.black); //задаем цет и фон
        field_n.setBackground(Color.white);
        panel_1_code_NK.add(field_n);

        JLabel nk1_k = new JLabel("k : ");//надпись
        panel_1_code_NK.add(nk1_k);

        field_k = new JTextField(2);//поле для ввода k
        field_k.setText("9");
        field_k.setForeground(Color.black); //задаем цет и фон
        field_k.setBackground(Color.white);
        panel_1_code_NK.add(field_k);
        label.add(panel_1_code_NK);//добавление строки

        JLabel nk1_1 = new JLabel("<html>Образующий полином <font size='3'>x<sup>0</sup>+...+x<sup>k-1</sup>+x<sup>k</sup></font> : </html>");//надпись
        nk1_1.setPreferredSize(new Dimension(220, 30));

        panel_1_code_NK.add(nk1_1);

        field_polinom = new JTextField(10);//поле для ввода информационного вектора
        field_polinom.setText("1111001");
        field_polinom.setForeground(Color.black); //задаем цет и фон
        field_polinom.setBackground(Color.white);
        panel_1_code_NK.add(field_polinom);

        JLabel nk1_dmin = new JLabel("dmin : ");//надпись
        panel_1_code_NK.add(nk1_dmin);
        field_dmin = new JTextField(4);//поле для ввода dmin
        field_dmin.setText("4");
        field_dmin.setForeground(Color.black); //задаем цет и фон
        field_dmin.setBackground(Color.white);
        panel_1_code_NK.add(field_dmin);
        label.add(panel_1_code_NK);//добавление строки

        //------Строка 2. Ввод информационного вектора------------—
        JPanel panel_2_inf_vect = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_2_inf_vect.setPreferredSize(new Dimension(700, 30));
        panel_2_inf_vect.setMaximumSize(new Dimension(700, 30));

        JLabel infvector = new JLabel("Введите информационный вектор: ");//надпись
        infvector.setPreferredSize(new Dimension(250, 30));
        panel_2_inf_vect.add(infvector);

        field_infvector = new JTextField(30);//поле для ввода информационного вектора
        panel_2_inf_vect.add(field_infvector);

        label.add(panel_2_inf_vect);//добавление строки

        //------Строка 3. Вывод типа закодированного вектора------------—
        JPanel panel_3_type_of_cod = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_3_type_of_cod.setPreferredSize(new Dimension(700, 45));
        panel_3_type_of_cod.setMaximumSize(new Dimension(700, 45));

        JLabel type_coding = new JLabel("Тип кодирования:");//надпись
        type_coding.setPreferredSize(new Dimension(250, 40));
        panel_3_type_of_cod.add(type_coding);

        type_of_cod = new JLabel("<html>Вектор не закодирован. Полином 1+x+x<sup>2</sup>+x<sup>3</sup>+x<sup>6</sup></html>");
        type_of_cod.setPreferredSize(new Dimension(250, 40));
        panel_3_type_of_cod.add(type_of_cod);

        label.add(panel_3_type_of_cod); //добавление строки

        //------Строка 4. Вывод закодированного вектора------------—
        JPanel panel_4_coding_vector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_4_coding_vector.setMaximumSize(new Dimension(700, 30));
        panel_4_coding_vector.setPreferredSize(new Dimension(700, 30));

        JLabel string_zakod_vect = new JLabel("Закодированный вектор:");//надпись
        string_zakod_vect.setPreferredSize(new Dimension(250, 30));
        panel_4_coding_vector.add(string_zakod_vect);

        field_true_coded_vector = new JTextField(30);
        field_true_coded_vector.setEditable(false); //поле недоступно для редактирования
        field_true_coded_vector.setForeground(Color.black); //задаем цет и фон
        field_true_coded_vector.setBackground(Color.white);
        panel_4_coding_vector.add(field_true_coded_vector);

        label.add(panel_4_coding_vector);//добавление строки

        //------Строка 5. Закодированный вектор для редактирования------------—
        JPanel panel_5_edited_coding_vector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_5_edited_coding_vector.setPreferredSize(new Dimension(700, 30));
        panel_5_edited_coding_vector.setMaximumSize(new Dimension(700, 30));

        JLabel edit_leb = new JLabel("Вектор для редактирования");//надпись
        edit_leb.setPreferredSize(new Dimension(250, 30));
        panel_5_edited_coding_vector.add(edit_leb);

        field_edited_coded_vector = new JTextField(30); //поле для редактирования
        field_edited_coded_vector.setEnabled(false);
        panel_5_edited_coding_vector.add(field_edited_coded_vector);

        label.add(panel_5_edited_coding_vector);//добавление строки

        //------Строка 6. Исправленный кодовый вектор------------—
        JPanel panel_6_corrected_coding_vector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_6_corrected_coding_vector.setPreferredSize(new Dimension(700, 30));
        panel_6_corrected_coding_vector.setMaximumSize(new Dimension(700, 30));

        JLabel text_ispr_vect = new JLabel("Испр. вект. для декодирования");//надпись
        text_ispr_vect.setPreferredSize(new Dimension(250, 30));
        panel_6_corrected_coding_vector.add(text_ispr_vect);

        field_corrected_coded_vector = new JTextField(30);
        field_corrected_coded_vector.setEditable(false);
        field_corrected_coded_vector.setForeground(Color.black);
        field_corrected_coded_vector.setBackground(Color.white);
        panel_6_corrected_coding_vector.add(field_corrected_coded_vector);

        label.add(panel_6_corrected_coding_vector);//добавление строки

        //------Строка 7. Раскодированный кодовый вектор------------—
        JPanel panel_7_decoding_vector = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_7_decoding_vector.setPreferredSize(new
                Dimension(700, 30));
        panel_7_decoding_vector.setMaximumSize(new Dimension(700, 30));

        JLabel decodecode = new JLabel("Раскодированный вектор: ");//надпись
        decodecode.setPreferredSize(new Dimension(250, 30));
        panel_7_decoding_vector.add(decodecode);

        field_decoded_vector = new JTextField(30);//поле для вывода раскодированного вектора
        field_decoded_vector.setEnabled(false);
        field_decoded_vector.setDisabledTextColor(Color.black);
        field_decoded_vector.setBackground(Color.white);
        panel_7_decoding_vector.add(field_decoded_vector);

        label.add(panel_7_decoding_vector);//добавление строки

        //------Строка 7. Вывод информации------------—
        JPanel panel_7_inform = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_7_inform.setPreferredSize(new Dimension(700, 30));
        panel_7_inform.setMaximumSize(new Dimension(700, 30));

        JLabel informstion_slovo = new JLabel("Информация: ");//надпись
        informstion_slovo.setPreferredSize(new Dimension(250, 30));
        panel_7_inform.add(informstion_slovo);

        field_inform_program = new JLabel("Начните кодирование");//поле с информацией
        panel_7_inform.add(field_inform_program);

        label.add(panel_7_inform);//добавление строки

        //------Строка 8. Кнопки------------—
        JPanel panel_8_buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel_8_buttons.setPreferredSize(new Dimension(700, 30));
        panel_8_buttons.setMaximumSize(new Dimension(700, 30));

        JButton button_systcoding = new JButton("Сист. кодирование"); //кнопка систематическое кодирование
        button_systcoding.setPreferredSize(new Dimension(160, 20));
        panel_8_buttons.add(button_systcoding);

        JButton button_not_systcoding = new JButton("Несист. кодирование"); //кнопка несистематическое кодирование
        button_not_systcoding.setPreferredSize(new Dimension(160, 20));
        panel_8_buttons.add(button_not_systcoding);

        button_decoding = new JButton("Декодировать"); //кнопка с декодированием
        button_decoding.setPreferredSize(new Dimension(160, 20));
        button_decoding.setEnabled(false);
        panel_8_buttons.add(button_decoding);
        //добавим к кнопкам обработчики
        ActionListener systCod; //обработкик для кнопки с систематическим кодированием
        systCod = new sListener();
        button_systcoding.addActionListener(systCod);

        ActionListener notSystCod;//обработкик для кнопки с несистематическим кодированием
        notSystCod = new nsListener();
        button_not_systcoding.addActionListener(notSystCod);

        ActionListener decodingList;
        decodingList = new decoding();
        button_decoding.addActionListener(decodingList);

        label.add(panel_8_buttons);//добавление строки
    }

    public class decoding implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String error_temp = field_edited_coded_vector.getText();
                field_inform_program.setText("");
                field_edited_coded_vector.setEnabled(false);//нам не за чем теперь редактировать поле для изменений
                field_edited_coded_vector.setDisabledTextColor(Color.black);
                field_edited_coded_vector.setBackground(Color.white);

                button_decoding.setEnabled(false);//Запретим повторно декодировать
                field_infvector.setEnabled(true);//можно вводить новый код
                inform_vector_temp = "";
                String[] stringArray = error_temp.split("");
                int[] inarray = new int[stringArray.length];
                for (int i = 0; i < stringArray.length; i++) {
                    String numberAsString = stringArray[i];
                    inarray[i] = Integer.parseInt(numberAsString);
                }

                int[] temperror = new int[inarray.length];
                for (int i = 0; i < temperror.length; i++) {
                    temperror[i] = inarray[inarray.length - i - 1];
                }
                int[] gtemp = new int[g.length];
                for (int i = 0; i < gtemp.length; i++) {
                    gtemp[i] = g[g.length - i - 1];
                }
                gf.remainder(temperror, gtemp);//в темперрор остаток от деления
                int sind = 0;
                for (int i = 0; i < temperror.length; i++) {
                    sind += temperror[i];
                }
                if (sind == 0) {
                    inform_vector_temp = "Ошибок не обнаружено.";
                    String ispr_vect = "";
                    for (int tt =
                         0; tt < inarray.length; tt++) {
                        ispr_vect += inarray[tt];
                    }
                    field_corrected_coded_vector.setText(ispr_vect);
                    field_corrected_coded_vector.setForeground(Color.black);
                    if (!field_true_coded_vector.getText().equals(ispr_vect)) {
                        field_corrected_coded_vector.setForeground(Color.red);
                    } else {
                        field_corrected_coded_vector.setForeground(Color.black);
                    }
                    end_decod(inarray, g);
                } else {
                    inform_vector_temp = "Ошибки обнаружены в ";
                    int[] ostatok; //создаем переменную для остатка
                    int t_ispr = 1; //количество исправляемых ошибок кода.! надо варьировать
                    int w = 0; //вес исправляемой ошибки
                    int col_sdvigov = 0; //проделанное количество сдвигов

                    temperror = gf.flip(inarray); //в temperror запишем наш отраженный массив inarray(1100->0011 к примеру)
                    do //заходим в цикл с требуемым количеством сдвигов
                    {
                        col_sdvigov++; //увеличиваем количество проделанных сдвигов
                        int temp_er0 = temperror[0];
                        //произведем сдвиг отраженного массива
                        for (int i = 0; i < temperror.length - 1; i++) {
                            temperror[i] = temperror[i + 1];
                        }
                        temperror[temperror.length - 1] = temp_er0;
                        //скопируем значения массива в остаток, поскльку функция gf.remainder записывает значения в параметр функции
                        ostatok = temperror.clone();
                        gf.remainder(ostatok, gtemp);//получем остаток
                        w = 0; //теперь узнаем вес сдвига
                        for (int d = 0; d < ostatok.length; d++) {
                            if (ostatok[d] == 1) {
                                w++;
                            }
                        }
                    }
                    while ((w > t_ispr) & (col_sdvigov <= inarray.length)); //условием выхода будет то, когда вес ошибки будет меньше или равен исправляемой ошибки
                    temperror = gf.add(temperror, ostatok); //сложим наш вектор ошибки с остатком
                    //все готово, осталось проделать сдвиг обратно нашего нового массива на количество итераций в цикле
                    for (int i = 0; i < col_sdvigov; i++) {
                        int temp_er0 = temperror[temperror.length - 1];
                        for (int d = temperror.length - 1; d > 0; d--) {
                            temperror[d] = temperror[d - 1];
                        }
                        temperror[0] = temp_er0;
                    }
                    //теперь надо отразить массив обратно для декодирования. Создадим новый массив, куда запишем обратное отображение
                    int tempErr2[] = gf.flip(temperror);//исправленный вектор
                    String ispr_vect = "";
                    for (int tt = 0; tt < tempErr2.length; tt++) {
                        ispr_vect += tempErr2[tt];
                    }
                    field_corrected_coded_vector.setText(ispr_vect);
                    if (!field_true_coded_vector.getText().equals(ispr_vect)) {
                        field_corrected_coded_vector.setForeground(Color.red);
                    } else {
                        field_corrected_coded_vector.setForeground(Color.black);
                    }

                    int[] differenceArr = gf.add(tempErr2, inarray);
                    for (int tt = 0; tt < differenceArr.length; tt++)//выведем номер позиции ошибок
                    {
                        if (differenceArr[tt] != 0) {
                            inform_vector_temp += " " + Integer.toString(tt + 1);
                        }
                    }
                    inform_vector_temp += " разряде.";
//Ошибки в векторе исправлены. Осталось декодировать его 
                    end_decod(tempErr2, g);
                }
            } catch (Exception q) {
                JOptionPane.showMessageDialog(null, "Некорректно заданы данные");
            }
        }
    }

    public void end_decod(int[] vect, int[] g) {
        field_inform_program.setText(inform_vector_temp + " Кратность испр. ош: " + Integer.toString(gf.kratn_oshibki(dmin)));
        inform_vector_temp = "";
        String decoding_string = "";
        if (flag_of_cod_type == 2)//выбрано несистематическое кодирование
        {
            vect = gf.dev(vect, g);
        } else if (flag_of_cod_type == 1)//выбрано систематическое кодирование
        {
            int tempvect[] = vect;
            int kk = field_infvector.getText().length();
            int nn = field_true_coded_vector.getText().length();
            vect = new int[kk];
            for (int i = 0; i < kk; i++) {
                vect[i] = tempvect[(nn - kk) + i];
            }
        }
        for (int i = 0; i < vect.length; i++) {
            decoding_string += Integer.toString(vect[i]);
        }
        field_decoded_vector.setText(decoding_string);
        if (!decoding_string.equals(field_infvector.getText())) {
            field_decoded_vector.setForeground(Color.red);
            field_decoded_vector.setDisabledTextColor(Color.red);
        } else {
            field_decoded_vector.setForeground(Color.black);
            field_decoded_vector.setDisabledTextColor(Color.black);
        }
        field_polinom.setEditable(true);
        field_n.setEditable(true);
        field_k.setEditable(true);
        field_dmin.setEditable(true);
    }

    public class sListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (setPole()) {
                field_inform_program.setText("");
                String[] stringArray = inform_vector_temp.split("");
                int[] inarray = new int[stringArray.length];
                for (int i = 0; i < stringArray.length; i++) {
                    String numberAsString = stringArray[i];
                    inarray[i] = Integer.parseInt(numberAsString);
                }
                int[] xr = new int[n - k + 1];
                for (int i = 0; i < n - k + 1; i++) {
                    xr[i] = 0;
                }
                xr[n - k] = 1;
                int[] tempnotsyst = gf.multiply(xr, inarray);

                int[] tempnotsyst1 = tempnotsyst.clone();
                gf.remainder(tempnotsyst1, g);

                systcode_arrayint = gf.add(tempnotsyst, tempnotsyst1);
                systcode_string = "";
                for (int i = 0; i < systcode_arrayint.length; i++) {
                    systcode_string = systcode_string + Integer.toString(systcode_arrayint[i]);
                }
                field_true_coded_vector.setText(systcode_string);
                field_edited_coded_vector.setText(systcode_string);
                field_corrected_coded_vector.setText("");
                field_decoded_vector.setText("");
                field_inform_program.setText("");
                type_of_cod.setText("<html>Систематич. код. Обр. полином: " + outVector());
                flag_of_cod_type = 1;//выбрано систематическое кодирование
                button_decoding.setEnabled(true);//теперь можно декодировать
                field_infvector.setEnabled(false);//менять первоначальный вектор нельзя, поскольку мы еще не декодировали его
                field_edited_coded_vector.setEnabled(true);
                field_infvector.setDisabledTextColor(Color.black);
                field_infvector.setBackground(Color.white);
            } else {
                field_inform_program.setText("Длина. код. вектора должна быть " + k + ". Длина обр. полинома должна быть " + Integer.toString(n - k + 1));
            }
        }
    }

    public class nsListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (setPole()) {
                    field_inform_program.setText("");
                    int[] inarray = new int[inform_vector_temp.length()];
                    int tt = inform_vector_temp.length();
                    for (int i = 0; i < tt; i++) {
                        inarray[i] = Character.getNumericValue(inform_vector_temp.charAt(i));
                    }
                    notsystcode_arrayint = gf.multiply(g, inarray);
                    notsystcode_string = "";
                    for (int i = 0; i < notsystcode_arrayint.length; i++) {
                        notsystcode_string = notsystcode_string + Integer.toString(notsystcode_arrayint[i]);
                    }
                    field_true_coded_vector.setText(notsystcode_string);
                    field_edited_coded_vector.setText(notsystcode_string);
                    field_corrected_coded_vector.setText("");
                    field_decoded_vector.setText("");
                    field_inform_program.setText("");

                    type_of_cod.setText("<html>Несистематич. код. Обр. полином:" + outVector());
                    flag_of_cod_type = 2;//выбрано систематическое кодирование
                    button_decoding.setEnabled(true);//теперь можно декодировать
                    field_infvector.setEnabled(false);//менять первоначальный вектор нельзя, поскольку мы еще не декодировали его
                    field_edited_coded_vector.setEnabled(true);
                    field_infvector.setDisabledTextColor(Color.black);
                    field_infvector.setBackground(Color.white);
                } else {
                    field_inform_program.setText("Длина. код. вектора должна быть " + k + ". Длина обр. полинома должна быть " + Integer.toString(n - k + 1));
                }
            } catch (Exception t) {
                JOptionPane.showMessageDialog(null, "Некорректно заданы данные");
            }
        }
    }

    public boolean setPole() {
        try {
            n = Integer.parseInt(field_n.getText());
            k = Integer.parseInt(field_k.getText());
            inform_vector_temp = field_infvector.getText();
            primitive_poly_string = field_polinom.getText();
            primitive_poly_int = Integer.parseInt(primitive_poly_string, 2);
            gf.getInstance(2, primitive_poly_int);
            g = new int[primitive_poly_string.length()];

            dmin = Integer.parseInt(field_dmin.getText());
            for (int i = 0; i < primitive_poly_string.length(); i++) {
                g[i] = Character.getNumericValue(primitive_poly_string.charAt(i));
            }
            if ((dmin > 0) & (n > k) & (k > 0) & (inform_vector_temp.length() == k) & (primitive_poly_string.length() == (n - k + 1))) {
                field_polinom.setEditable(false);
                field_n.setEditable(false);
                field_k.setEditable(false);
                field_dmin.setEditable(false);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Некорректно заданы данные");
            return false;
        }
    }

    public String outVector() {
        String out = "";
        if (g[0] == 1) {
            out = "1";
        }
        int pow = 1;
        for (int i = 1; i < g.length; i++) {
            if (g[i] == 1) {
                if ((pow == 1) & (g[0] == 0)) {
                    out = out + "x";
                } else {
                    out = out + "+x<sup>" + Integer.toString(pow) + "</sup>";
                }
            }
            pow++;
        }
        out = out + "</html>";
        return out;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new CyclicCode();
            }
        });
    }
}