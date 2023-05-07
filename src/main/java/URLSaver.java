import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class URLSaver {

    private static String getFileName() {
        return CurrentScreen.INSTANCE.getDir()  + "\\url.txt";// + "\\urls.txt";
    }

    public static void saveURLToFile( String text) {
        try {
            FileWriter writer = new FileWriter(getFileName());
            writer.write(text);
            writer.close();
            System.out.println("Successfully saved string to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the string to file: " + e.getMessage());
        }
    }

    public static String readUrlFromFile() {
        StringBuilder urlBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFileName()));
            String line = reader.readLine();
            while (line != null) {
                urlBuilder.append(line);
                line = reader.readLine();
            }
            reader.close();
            System.out.println("Successfully read URL from file.");
        } catch (IOException e) {
            //System.out.println("An error occurred while reading the URL from file: " + e.getMessage());
        }
        return urlBuilder.toString();
    }
}
