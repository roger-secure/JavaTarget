import java.io.*;
import java.net.*;

public class all {
    public static void main(String[] args) {
        String urlStr = "ftp://ftp.bom.gov.au/anon/gen/fwo/IDV10450.txt";
        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                content.append(inputLine);
                content.append(System.lineSeparator());
            }

            in.close();

            // write to file
            String fileName = "input/for.txt";
            FileWriter writer = new FileWriter(fileName);
            writer.write(content.toString());
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Wait for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String inputFilePath = "input/for.txt";
        String trimmedFilePath = "input/trim_for.txt";
        String outputFilePath = "output/forecast.txt";
        String logFilePath = "logs/log.txt";
        boolean foundWeatherCondition = false;

        try {
            // Read input file and write trimmed content to file
            BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(trimmedFilePath));
            boolean startWriting = false;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("IDV10450")) {
                    startWriting = true;
                }
                if (startWriting) {
                    writer.write(line);
                    writer.newLine();
                }
                if (line.contains("Sun protection")) {
                    break;
                }
            }
            reader.close();
            writer.close();
            System.out.println("Trimmed content written to " + trimmedFilePath);

            // Check weather condition from the file
            String weatherCondition = readWeatherConditionFromFile();
            reader = new BufferedReader(new FileReader(trimmedFilePath));
            while ((line = reader.readLine()) != null) {
                if (line.contains(weatherCondition)) {
                    foundWeatherCondition = true;
                    break;
                }
            }
            reader.close();

            // Write result to output file
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFilePath));
            if (foundWeatherCondition) {
                outputWriter.write(weatherCondition + " found in input file.");
            } else {
                outputWriter.write(weatherCondition + " not found today.");
            }
            outputWriter.flush();
            outputWriter.close();
            System.out.println("Output written to " + outputFilePath);

            // Log process
            File logFile = new File(logFilePath);
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile, true));
            String timestamp = java.time.LocalDateTime.now().toString();
            String result = foundWeatherCondition ? weatherCondition + " found." : weatherCondition + " not found.";
            logWriter.write(timestamp + " - Processed file " + inputFilePath + ". Result: " + result + "\n");
            logWriter.flush();
            logWriter.close();
            System.out.println("Log entry written to " + logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readWeatherConditionFromFile() {
        String weatherCondition = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("input/weather_text.txt"))) {
            weatherCondition = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weatherCondition;
    }
}
