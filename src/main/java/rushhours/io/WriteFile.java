package rushhours.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class WriteFile {
    public static void saveFile(String filename, Stack<String> frames) {
        if (filename == null || filename.isEmpty()) return;

        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        String path = "test" + File.separator + filename;

        File directory = new File("test");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileWriter writer = new FileWriter(path)) {
            for (int i = frames.size() - 1; i >= 0; i--) {
                String plain = frames.get(i);
                writer.write(plain);
                writer.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Failed to save file: " + e.getMessage());
        }
    }
}
