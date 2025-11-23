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
        int totalLines = 0;
        int yandexBotCount = 0;
        int googlebotCount = 0;
        int tooLongLines = 0;

        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;

                // Проверка длины строки (но не останавливаем выполнение)
                if (line.length() > 1024) {
                    tooLongLines++;
                    continue; // Пропускаем эту строку, но продолжаем обработку
                }

                // Извлечение User-Agent по формату лога
                String userAgent = extractUserAgentFromLog(line);
                if (userAgent != null) {
                    String botName = extractBotName(userAgent);
                    if ("YandexBot".equals(botName)) {
                        yandexBotCount++;
                    } else if ("Googlebot".equals(botName)) {
                        googlebotCount++;
                    }
                }
            }

            // Вывод результатов
            System.out.println("Общее количество строк в файле: " + totalLines);
            if (tooLongLines > 0) {
                System.out.println("Пропущено строк длиннее 1024 символов: " + tooLongLines);
            }

            int processedLines = totalLines - tooLongLines;
            if (processedLines > 0) {
                double yandexShare = (double) yandexBotCount / processedLines * 100;
                double googleShare = (double) googlebotCount / processedLines * 100;

                System.out.printf("Доля запросов от YandexBot: %.2f%% (%d запросов)%n", yandexShare, yandexBotCount);
                System.out.printf("Доля запросов от Googlebot: %.2f%% (%d запросов)%n", googleShare, googlebotCount);
            } else {
                System.out.println("Нет строк для анализа (все строки слишком длинные)");
            }

        } catch (Exception e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    // Метод для извлечения User-Agent из строки лога по формату
    private static String extractUserAgentFromLog(String logLine) {
        try {
            // User-Agent - последний элемент в кавычках
            int lastQuote = logLine.lastIndexOf('"');
            if (lastQuote == -1) return null;

            // Ищем начало User-Agent (кавычки перед ним)
            int startUserAgent = logLine.lastIndexOf('"', lastQuote - 1);
            if (startUserAgent == -1) return null;

            // User-Agent между этими кавычками
            return logLine.substring(startUserAgent + 1, lastQuote);

        } catch (Exception e) {
            return null;
        }
    }

    // Метод для извлечения имени бота из User-Agent
    private static String extractBotName(String userAgent) {
        try {
            // Ищем часть в первых скобках
            int startBracket = userAgent.indexOf('(');
            int endBracket = userAgent.indexOf(')');

            if (startBracket == -1 || endBracket == -1) {
                return null;
            }

            String firstBrackets = userAgent.substring(startBracket + 1, endBracket);

            // Разделяем по точке с запятой
            String[] parts = firstBrackets.split(";");
            if (parts.length >= 2) {
                // Берем второй фрагмент, очищаем от пробелов
                String fragment = parts[1].trim();
                // Отделяем часть до слэша
                int slashIndex = fragment.indexOf('/');
                if (slashIndex != -1) {
                    return fragment.substring(0, slashIndex).trim();
                }
                return fragment;
            }
        } catch (Exception e) {
            // Если что-то пошло не так, игнорируем эту строку
        }
        return null;
    }
}