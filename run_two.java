import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class run_two {
    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Task to run all.java
        Runnable allTask = () -> {
            try {
                String javaHome = System.getProperty("java.home");
                String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
                String classpath = System.getProperty("java.class.path");
                String className = "all";
                ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
                builder.inheritIO();
                Process process = builder.start();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Task to run GM4.java after 10 seconds
        Runnable gm4Task = () -> {
            try {
                String javaHome = System.getProperty("java.home");
                String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
                String classpath = System.getProperty("java.class.path");
                String className = "GM4";
                ProcessBuilder builder = new ProcessBuilder(javaBin, "-cp", classpath, className);
                builder.inheritIO();
                Process process = builder.start();
                process.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        // Schedule the tasks
        scheduler.schedule(allTask, 0, TimeUnit.SECONDS);
        scheduler.schedule(gm4Task, 10, TimeUnit.SECONDS);

        // Shutdown the scheduler when done
        scheduler.shutdown();
    }
}
