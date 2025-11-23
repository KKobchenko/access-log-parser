import java.io.*;
import java.util.Scanner;

public class LogParser {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите путь к файлу: ");
        String path = scanner.nextLine();

        analyzeFileWithObjects(path);
    }

    public static void analyzeFileWithObjects(String path) {
        Statistics stats = new Statistics();
        int totalLines = 0;
        int tooLongLines = 0;

        try (FileReader fileReader = new FileReader(path);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;

                if (line.length() > 1024) {
                    tooLongLines++;
                    continue;
                }

                try {
                    LogEntry entry = new LogEntry(line);

                    // ПРОВЕРКА: если размер данных отрицательный, покажем это
                    if (entry.getDataSize() < 0) {
                        System.out.println("ОТРИЦАТЕЛЬНЫЙ РАЗМЕР: " + entry.getDataSize());
                        System.out.println("Строка: " + line.substring(0, Math.min(200, line.length())));
                        break; // Остановимся на первой найденной ошибке
                    }

                    stats.addEntry(entry);
                } catch (Exception e) {
                    // Пропускаем некорректные строки
                }
            }

            // Вывод результатов
            System.out.println("Общее количество строк: " + totalLines);
            System.out.println("Обработано записей: " + stats.getEntryCount());
            System.out.println("Пропущено строк: " + (totalLines - stats.getEntryCount() - tooLongLines + tooLongLines));

            if (stats.getEntryCount() > 0) {
                System.out.printf("Средний трафик в час: %.2f байт/час%n", stats.getTrafficRate());
                System.out.println("Общий трафик: " + stats.getTotalTraffic() + " байт");
                System.out.println("Период данных: " + stats.getMinTime() + " - " + stats.getMaxTime());
            }

        } catch (Exception e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}