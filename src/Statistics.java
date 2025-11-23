import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private int entryCount;

    public Statistics() {
        this.totalTraffic = 0;
        this.entryCount = 0;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();
        entryCount++;

        LocalDateTime entryTime = entry.getTime();
        if (minTime == null) {
            minTime = entryTime;
            maxTime = entryTime;
        } else {
            if (entryTime.isBefore(minTime)) {
                minTime = entryTime;
            }
            if (entryTime.isAfter(maxTime)) {
                maxTime = entryTime;
            }
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || entryCount == 0) {
            return 0.0;
        }

        long hoursBetween = Math.abs(ChronoUnit.HOURS.between(minTime, maxTime));
        if (hoursBetween == 0) {
            hoursBetween = 1;
        }

        return (double) totalTraffic / hoursBetween;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }

    public int getEntryCount() {
        return entryCount;
    }
}