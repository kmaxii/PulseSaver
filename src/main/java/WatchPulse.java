import netscape.javascript.JSObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WatchPulse {

   /* public static void main(String[] args) {


        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                savehyperate("https://app.hyperate.io/5715");
            }
        };

        // Schedule the task to run every second
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }*/

    public static void main(String[] args) {
        String apiUrl = "https://dev.pulsoid.net/api/v1/data/heart_rate/latest?response_mode=text_plain_only_heart_rate";
        String token = "70baab5c-0175-44f4-b237-818b3a85e126";
        String contentType = "application/json";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", contentType);

            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputReader.close();


            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private static Connection hyperateCon = null;


    private static String savehyperate(String hyperateURL) {
        String apiUrl = "https://dev.pulsoid.net/api/v1/data/heart_rate/latest?response_mode=text_plain_only_heart_rate";
        String token = "70baab5c-0175-44f4-b237-818b3a85e126";
        String contentType = "application/json";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setRequestProperty("Content-Type", contentType);

            BufferedReader inputReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = inputReader.readLine()) != null) {
                response.append(inputLine);
            }
            inputReader.close();


            System.out.println(response.toString());

            writeDataToCsv(response.toString());
            return response.toString();

        } catch (Exception e) {
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
