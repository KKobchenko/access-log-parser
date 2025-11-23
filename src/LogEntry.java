import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {
    private final String ipAddress;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {
        this.ipAddress = parseIPAddress(logLine);
        this.time = parseTime(logLine);
        this.method = parseMethod(logLine);
        this.path = parsePath(logLine);
        this.responseCode = parseResponseCode(logLine);
        this.dataSize = parseDataSize(logLine);
        this.referer = parseReferer(logLine);
        this.userAgent = new UserAgent(parseUserAgentString(logLine));
    }

    // Геттеры
    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getTime() { return time; }
    public HttpMethod getMethod() { return method; }
    public String getPath() { return path; }
    public int getResponseCode() { return responseCode; }
    public int getDataSize() { return dataSize; }
    public String getReferer() { return referer; }
    public UserAgent getUserAgent() { return userAgent; }

    // Методы парсинга
    private String parseIPAddress(String logLine) {
        try {
            return logLine.split(" ")[0];
        } catch (Exception e) {
            return "0.0.0.0";
        }
    }

    private LocalDateTime parseTime(String logLine) {
        try {
            int start = logLine.indexOf('[') + 1;
            int end = logLine.indexOf(']');
            String timeStr = logLine.substring(start, end);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            return LocalDateTime.parse(timeStr, formatter);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }

    private HttpMethod parseMethod(String logLine) {
        try {
            int start = logLine.indexOf('"') + 1;
            int end = logLine.indexOf(' ', start);
            String method = logLine.substring(start, end);
            return HttpMethod.valueOf(method);
        } catch (Exception e) {
            return HttpMethod.GET;
        }
    }

    private String parsePath(String logLine) {
        try {
            int start = logLine.indexOf('"') + 1;
            start = logLine.indexOf(' ', start) + 1;
            int end = logLine.indexOf(' ', start);
            if (end == -1) end = logLine.indexOf('"', start);
            return logLine.substring(start, end);
        } catch (Exception e) {
            return "/";
        }
    }

    private int parseResponseCode(String logLine) {
        try {
            String[] parts = logLine.split(" ");
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].matches("\\d{3}") && i > 0 && parts[i-1].matches("\\d+")) {
                    return Integer.parseInt(parts[i]);
                }
            }
            return 200;
        } catch (Exception e) {
            return 200;
        }
    }

    private int parseDataSize(String logLine) {
        try {
            String[] parts = logLine.split(" ");
            for (int i = 0; i < parts.length; i++) {
                // Ищем число после кода ответа
                if (i > 0 && parts[i-1].matches("\\d{3}") && parts[i].matches("\\d+")) {
                    int size = Integer.parseInt(parts[i]);
                    // Убедимся, что размер положительный
                    return Math.max(0, size);
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private String parseReferer(String logLine) {
        try {
            // Ищем referer между кавычками перед User-Agent
            int lastQuote = logLine.lastIndexOf('"');
            if (lastQuote == -1) return "";

            int prevQuote = logLine.lastIndexOf('"', lastQuote - 1);
            if (prevQuote == -1) return "";

            int startQuote = logLine.lastIndexOf('"', prevQuote - 1);
            if (startQuote == -1) return "";

            return logLine.substring(startQuote + 1, prevQuote);
        } catch (Exception e) {
            return "";
        }
    }

    private String parseUserAgentString(String logLine) {
        try {
            // User-Agent - последний элемент в кавычках
            int lastQuote = logLine.lastIndexOf('"');
            if (lastQuote == -1) return "";

            int startUserAgent = logLine.lastIndexOf('"', lastQuote - 1);
            if (startUserAgent == -1) return "";

            return logLine.substring(startUserAgent + 1, lastQuote);
        } catch (Exception e) {
            return "";
        }
    }
}