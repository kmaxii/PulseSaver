import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatchPulse {

    public static void main(String[] args) {

        savehyperate("https://app.hyperate.io/5715");
    }


    public static boolean TestURL(String heartRateURL) {
        try {
            Document doc = Jsoup.connect(heartRateURL).get();
            return true;
        } catch (IOException e) {
            return false;
        }
    }



    public static String saveHeartRate(String heartRateURL) {

        if (heartRateURL.contains("watchpulse")) {
            return saveWatchPulse(heartRateURL);
        }

        if (heartRateURL.contains("hyperate")) {
            return savehyperate(heartRateURL);
        }

        return "WRONG URL";
    }

    private static String savehyperate(String hyperateURL) {
        try {
            Document doc = Jsoup.connect(hyperateURL).get();

            Elements data = doc.select(".heartrate");

            String html = data.get(0).toString();

            Pattern pattern = Pattern.compile("<p\\s+class=\"heartrate\"\\s+style=\"color:\\s*#[a-fA-F0-9]+;\\s*font-size:\\s*50px\">(\\d+)</p>");
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                String heartRate = matcher.group(1);
                System.out.println("Heart rate: " + heartRate);
                writeDataToCsv(heartRate);
                return heartRate;
            } else {
                System.out.println("No heart rate found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }


    private static String saveWatchPulse(String watchPulseURL) {
        try {
            Document doc = Jsoup.connect(watchPulseURL).get();

            Elements data = doc.select(".hr");

            String regex = "<p class=\"hr\" id=\"hr\">(\\d+)</p>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data.get(0).toString());
            if (matcher.find()) {
                String number = matcher.group(1);
                writeDataToCsv(number);
                System.out.println(number);
                return number;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }

    private static String CSV_FILE_PATH() {
        return CurrentScreen.INSTANCE.getDir() + "/latest.csv";
    }

    private static final String CSV_SEPARATOR = ",";

    public static void writeDataToCsv(String value) {
        try {
            boolean isNewFile = !new File(CSV_FILE_PATH()).exists();

            // Create the directory if it doesn't exist
            File dir = new File(CSV_FILE_PATH()).getParentFile();
            if (dir != null) {
                dir.mkdirs();
            }


            FileWriter csvWriter = new FileWriter(CSV_FILE_PATH(), true);

            if (isNewFile) {
                // Write header row
                csvWriter.append("Date");
                csvWriter.append(CSV_SEPARATOR);
                csvWriter.append("Value");
                csvWriter.append("\n");
            }

            // Write current time and value to new row
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            csvWriter.append(now.format(formatter));
            csvWriter.append(CSV_SEPARATOR);
            csvWriter.append(value);
            csvWriter.append("\n");

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void renameLatestCsv() {

        String directoryPath = CurrentScreen.INSTANCE.getDir();

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }
        File latestCsv = new File(directoryPath, "latest.csv");
        if (!latestCsv.exists()) {
            System.out.println("No latest.csv file found in the directory.");
            return;
        }

        int suffix = 1;
        String newFileName = "";

        newFileName = "results_" + suffix + ".csv";

        File newFile = new File(directoryPath, newFileName);
        while (newFile.exists()) {
            newFileName = "results_" + suffix + ".csv";
            newFile = new File(directoryPath, newFileName);
            suffix++;
        }
        if (latestCsv.renameTo(newFile)) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("Failed to rename the file.");
        }
    }


}
