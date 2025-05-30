package utils;

public class TimerUtil {
    private long startTime;
    private String taskName;

    public TimerUtil(String taskName) {
        this.taskName = taskName;
    }

    public void start() {
        startTime = System.nanoTime();
        System.out.println("Start: " + taskName);
    }

    public double stopAndGet() {
        long elapsedNs = System.nanoTime() - startTime;
        double elapsedMs = elapsedNs / 1_000_000.0; // convertim la milisecunde cu zecimale
        System.out.printf("%s finalizat in %.2f ms.%n", taskName, elapsedMs);
        return elapsedMs;
    }
}
