package guava;

import com.google.common.base.Charsets;

import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IGuavaFile {
    public static void main(String[] args) {
        try {
            Path path = Paths.get("src/main/resources/db.data");
            List<String> readLines = Files.readAllLines(path, Charsets.UTF_8);
            System.out.println(String.join("\n", readLines));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
