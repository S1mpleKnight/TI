package by.bsuir.ti.fourth.util;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileWorker {
    public static void signTheFile(File file, BigInteger digest, byte[] digitalSignature) throws IOException {
        try (Writer writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            writer.write("\n");
            writer.write(digest.toString());
            writer.write(" ");
            writer.write(new String(digitalSignature));
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Can not sign the file");
        }
    }

    public static String[] takeDigestAndSignature(File file) throws IOException {
        List<String> strings;
        try (Stream<String> stringStream = Files.lines(file.toPath())) {
            strings = stringStream.collect(Collectors.toList());
            return strings.get(strings.size() - 1).split(" ");
        } catch (IOException e) {
            throw new IOException("Can not read file: " + file.getAbsolutePath());
        }
    }

    public static byte[] readFile(File file) throws IOException {
        byte[] arr = new byte[0];
        try {
            DataInputStream ds = new DataInputStream(new FileInputStream(file));
            arr = ds.readAllBytes();
            ds.close();
        } catch (IOException exception) {
            throw new IOException("Can not read file");
        }
        return arr;
    }
}
