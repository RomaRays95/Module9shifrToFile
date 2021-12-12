import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException{
        int keyPass = 97;
        String[][] key = createKey(keyPass);
        File fileToCode = new File("code.txt");
        File fileToDecode = new File("decode.txt");
        if (fileToCode.exists()) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm, dd.MM - ");
            String text = "Зашифровано в " + dateFormat.format(calendar.getTime()) + scanText("code.txt");
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) != '\n') code.append(coding(text.substring(i, i + 1), key));
            }
            String result = code.toString();
            FileOutputStream fos = new FileOutputStream("ResultOfCoding.txt");
            fos.write(result.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }
        if (fileToDecode.exists()){
            String text = scanText("decode.txt");
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < text.length(); i += 2) {
                code.append(decoding(text.substring(i, i + 2), key));
            }
            String result = code.toString();
            FileOutputStream fos = new FileOutputStream("ResultOfDecoding.txt");
            fos.write(result.getBytes(StandardCharsets.UTF_8));
            fos.close();
        }

    }

    private static String coding(String s, String[][] key) {
        for (String[] strings : key) {
            if (s.equalsIgnoreCase(strings[0])) {
                Random random = new Random();
                return strings[random.nextInt(3) + 1];
            }
        }
        return "00";
    }

    private static String decoding(String s, String[][] key) {
        for (String[] strings : key) {
            if (s.equalsIgnoreCase(strings[1]) || s.equalsIgnoreCase(strings[2]) || s.equalsIgnoreCase(strings[3])) {
                return strings[0];
            }
        }
        return "\n";
    }

    private static String[][] createKey(int keyPass) {
        String[][] key = new String[77][4];
        key[0][0] = "'";
        key[1][0] = ".";
        key[2][0] = ",";
        key[3][0] = "?";
        key[4][0] = " ";
        int j = 5;
        for (int i = 0; i < 26; i++) {
            key[j][0] = "" + (char) (i + 97);
            j++;
        }
        for (int i = 0; i < 32; i++) {
            key[j][0] = "" + (char) (i + 1072);
            j++;
        }
        for (int i = 0; i < 10; i++) {
            key[j][0] = "" + (char) (i + 48);
            j++;
        }
        key[74][0] = "ё";
        key[75][0] = "!";
        key[73][0] = "-";
        key[76][0] = ":";
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
        return key;
    }

    private static boolean noRepeatCurrentColumn(int i, int column, String code, String[][] key) {
        boolean result = false;
        for (int j = 0; j < i; j++) {
            if (code.equals(key[j][column])) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean noRepeatFullColumn(int column, String code, String[][] key) {
        boolean result = false;
        for (String[] strings : key) {
            if (code.equals(strings[column])) {
                result = true;
                break;
            }
        }
        return result;
    }


    private static String scanText(String str) {
        Path path1 = Paths.get(str);
        String input = null;
        try {
            input = Files.readString(path1, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            // Handle exception
        }
        return input;
    }
}
