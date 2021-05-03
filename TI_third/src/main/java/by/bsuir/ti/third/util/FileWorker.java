package by.bsuir.ti.third.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWorker {
    private static final int NUMBER_OF_RESULT_LINES = 20;

    public static void writeEncryptedFile(byte[] bytes, File file) {
        File encryptedFile = new File(file.getAbsolutePath() + ".encrypted");
        writeFile(bytes, encryptedFile);
    }

    public static void fileRebuilding(byte[] bytes, File file) {
        String name = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 10);
        File decryptedFile = new File(name);
        writeFile(bytes, decryptedFile);
    }

    private static void writeFile(byte[] bytes, File file) {
        boolean state = false;
        try {
            state = file.createNewFile();
            DataOutputStream ds = new DataOutputStream(new FileOutputStream(file));
            ds.write(bytes);
            ds.flush();
            ds.close();
        } catch (IOException exception) {
            System.out.println(state ? "Can not create file" : exception.getMessage());
            exception.printStackTrace();
        }
    }

    public static byte[] fileOpening(File file) {
        byte[] arr = new byte[0];
        try {
            DataInputStream ds = new DataInputStream(new FileInputStream(file));
            arr = ds.readAllBytes();
            ds.close();
        } catch (IOException exception) {
            System.out.println("Can not open file");
            exception.printStackTrace();
        }
        return arr;
    }

    public static void writeNumbers(List<BigInteger> numbers, String path) {
        File file = new File(path);
        try (Writer writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            file.createNewFile();
            for (int i = 0; i < numbers.size() && i / 10 < NUMBER_OF_RESULT_LINES; i++) {
                writer.write(numbers.get(i).toString());
                writer.write(" ");
                if (i != 0 && i % 10 == 0) {
                    writer.write("\n");
                }
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println("Can not write numbers");
            System.out.println(e.getMessage());
        }
    }

    public static List<String> readNumbers(String path) {
        List<String> strings = Collections.EMPTY_LIST;
        try (Stream<String> stream = Files.lines(Path.of(path))) {
            strings = stream.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Can not read numbers");
            e.printStackTrace();
        }
        return strings;
    }
}
