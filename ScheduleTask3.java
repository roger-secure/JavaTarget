import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.File;

public class ScheduleTask3 {
    private static final String SCHEDULE_FILE_PATH = "input/time.txt";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = new Runnable() {
            @Override
            public void run() {
                System.out.println("run_two.java is going to run.");

                try {
                    String javaHome = System.getProperty("java.home");
                    String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
                    String classpath = System.getProperty("java.class.path");
                    String className = "run_two";
                    ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
                    builder.inheritIO();
                    Process process = builder.start();
                    process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        long initialDelay = getInitialDelay();
        long period = TimeUnit.DAYS.toSeconds(1);

        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);

        // Print the next scheduled run time
        printNextScheduledRunTime(initialDelay);
    }

    private static long getInitialDelay() {
        long nowMillis = System.currentTimeMillis();
        long scheduledMillis = getTodayScheduledMillis();
        if (nowMillis >= scheduledMillis) {
            scheduledMillis += TimeUnit.DAYS.toMillis(1L);
        }
        long delaySeconds = (scheduledMillis - nowMillis) / 1000L;
        return delaySeconds;
    }

    private static long getTodayScheduledMillis() {
        String scheduledTime = readScheduledTimeFromFile();
        Calendar scheduled = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        try {
            Date parsedDate = dateFormat.parse(scheduledTime);
            scheduled.setTime(parsedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        scheduled.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        scheduled.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        scheduled.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        return scheduled.getTimeInMillis();
    }

    private static void printNextScheduledRunTime(long initialDelaySeconds) {
        long currentTimeMillis = System.currentTimeMillis();
        long nextScheduledTimeMillis = currentTimeMillis + (initialDelaySeconds * 1000L);
        Date nextScheduledTime = new Date(nextScheduledTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Next scheduled run time: " + dateFormat.format(nextScheduledTime));
    }

    private static String readScheduledTimeFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SCHEDULE_FILE_PATH))) {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
