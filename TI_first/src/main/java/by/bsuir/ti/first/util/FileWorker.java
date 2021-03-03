package by.bsuir.ti.first.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWorker {

    public static List<String> readFile(String path) throws FileNotFoundException {
        File file = new File(path);
        List<String> list;
        if (file.exists()) {
            try (Stream<String> stream = Files.lines(file.toPath())) {
                list = stream.collect(Collectors.toList());
            } catch (IOException e) {
                System.out.println("Exception while reading file: " + path);
                throw new IllegalStateException("File have not been read");
            }
        } else {
            throw new FileNotFoundException("File is not exist: " + path);
        }
        return list;
    }

    public static void writeFile(String path, String text) {
        File file = new File(path);
        try {
            if (file.createNewFile()) {
                System.out.println("New out file created");
            }
        } catch (IOException e) {
            System.out.println("File to write have not been created");
            throw new IllegalStateException("Can not write to file");
        }
        try (Writer writer = new FileWriter(file)) {
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Exception while writing file: " + path);
            throw new IllegalStateException("File have not been read");
        }
    }

}


