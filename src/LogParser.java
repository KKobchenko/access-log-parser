import java.io.*;
import java.util.Scanner;

public class LogParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к файлу: ");
        String path = scanner.nextLine();

        analyzeFile(path);
    }

    public static void analyzeFile(String path) {
        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {

            int totalLines = 0;
            int maxLength = 0;
            int minLength = Integer.MAX_VALUE;

            String line;
            while ((line = reader.readLine()) != null) {
                int length = line.length();
                totalLines++;

                if (length > maxLength) maxLength = length;
                if (length < minLength) minLength = length;

                if (length > 1024) {
                    throw new LineTooLongException("Строка слишком длинная: " + length + " символов");
                }
            }

            System.out.println("Общее количество строк: " + totalLines);
            System.out.println("Самая длинная строка: " + maxLength);
            System.out.println("Самая короткая строка: " + minLength);

        } catch (LineTooLongException e) {
            System.out.println("ОШИБКА: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}