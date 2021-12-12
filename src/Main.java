import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int keyPass = keyOfCode();
        String[][] key = createKey(keyPass);

        label:
        while (true){
            String s = scanAction();
            switch (s) {
                case "f":
                    break label;
                case "c": {
//                Кодируем
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm, dd.MM - ");
                    String text = "Зашифровано в " + dateFormat.format(calendar.getTime()) + scanText();
                    StringBuilder code = new StringBuilder();
                    for (int i = 0; i < text.length(); i++) {
                        code.append(coding(text.substring(i, i + 1), key));
                    }
                    System.out.println("Результат:");
                    System.out.println(code);
                    break;
                }
                case "d": {
//                Расшифровка
                    String text = scanText();
                    StringBuilder code = new StringBuilder();
                    for (int i = 0; i < text.length(); i += 2) {
                        code.append(decoding(text.substring(i, i + 2), key));
                    }
                    System.out.println("Результат:");
                    System.out.println(code);
                    break;
                }
            }
        }
    }

    private static String coding(String s, String[][] key){
        for (String[] strings : key) {
            if (s.equalsIgnoreCase(strings[0])) {
                Random random = new Random();
                return strings[random.nextInt(3) + 1];
            }
        }
        return "00";
    }

    private static String[][] createKey(int keyPass){
        String[][] key = new String[77][4];
        key[0][0] = ")";
        key[1][0] = ".";
        key[2][0] = ",";
        key[3][0] = "?";
        key[4][0] = " ";
        int j = 5;
        for (int i = 0; i < 26; i++) {
            key[j][0] = "" + (char)(i+97);
            j++;
        }
        for (int i = 0; i < 32; i++) {
            key[j][0] = "" + (char)(i+1072);
            j++;
        }
        for (int i = 0; i < 10; i++) {
            key[j][0] = "" + (char)(i+48);
            j++;
        }
        key[74][0] = "ё";
        key[75][0] = "!";
        key[73][0] = "-";
        key[76][0] = "(";
        Random rand = new Random(keyPass);
        for (int i = 0; i < key.length; i++) {
            do key[i][1] = key[rand.nextInt(26) + 5][0] + key[rand.nextInt(26) + 5][0];
            while (noRepeatCurrentColumn(i, 1, key[i][1], key)
            );
        }
        for (int i = 0; i < key.length; i++) {
            do key[i][2] = key[rand.nextInt(26) + 5][0] + key[rand.nextInt(26) + 5][0];
            while (noRepeatCurrentColumn(i, 2, key[i][2], key) || noRepeatFullColumn(1, key[i][2], key)
            );
        }
        for (int i = 0; i < key.length; i++) {
            do key[i][3] = key[rand.nextInt(26) + 5][0] + key[rand.nextInt(26) + 5][0];
            while (noRepeatCurrentColumn(i, 3, key[i][3], key) || noRepeatFullColumn(1, key[i][3], key)
                    || noRepeatFullColumn(2, key[i][3], key));
        }
        return  key;
    }

    private static boolean noRepeatCurrentColumn(int i, int column, String code, String[][] key){
        boolean result = false;
        for (int j = 0; j < i; j++) {
            if (code.equals(key[j][column])) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean noRepeatFullColumn(int column, String code, String[][] key){
        boolean result = false;
        for (String[] strings : key) {
            if (code.equals(strings[column])) {
                result = true;
                break;
            }
        }
        return result;
    }


    private static String decoding(String s, String[][] key){
        for (String[] strings : key) {
            if (s.equalsIgnoreCase(strings[1]) || s.equalsIgnoreCase(strings[2]) || s.equalsIgnoreCase(strings[3])) {
                return strings[0];
            }
        }
        return "0";
    }

    private static String scanAction(){
        System.out.println("Введите команду: 'c' - закодировать, 'd' - декодировать, 'f' - закончить работу программмы");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static String scanText(){
        System.out.println("Введите текст");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private static int keyOfCode(){
        System.out.println("Введите ключ шифрования");
        Scanner sc = new Scanner(System.in);
        while (!sc.hasNextInt()) {
            System.out.println("Ключ шифрования состоит из цифр. Повторите");
            sc.nextLine();
        }
        return sc.nextInt();
    }
}
