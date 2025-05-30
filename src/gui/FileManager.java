package gui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {
    public static void saveResults(String filePath, List<String[]> data) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Algoritm,Operatie,Timp(ms)\n");
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.write("\n");
            }
        }
    }
}
