import java.io.FileWriter;
import java.io.IOException;

public class LatestPulseSaver {


    private static String getFileName() {
        return CurrentScreen.INSTANCE.getDir()  + "\\latestPulse.txt";// + "\\urls.txt";
    }

    public static void saveLatestPulse(String text) {
        try {
            FileWriter writer = new FileWriter(getFileName());
            writer.write(text);
            writer.close();
            System.out.println("Successfully saved string to file.");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the string to file: " + e.getMessage());
        }
    }

}
