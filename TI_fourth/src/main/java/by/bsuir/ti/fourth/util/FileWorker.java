package by.bsuir.ti.fourth.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    public static void signTheFile(File file, byte[] digitalSignature) throws IOException {
        try (Writer writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            writer.write(new String(digitalSignature));
            writer.flush();
        } catch (IOException e) {
            throw new IOException("Can not sign the file");
        }
    }

    public static void signFile(File file, byte[] digitalSignature) throws IOException {
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true));
            dos.write(digitalSignature);
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new IOException("Can not sign th file: " + file.getAbsolutePath());
        }
    }

    public static byte[] readFile(File file) throws IOException {
        byte[] arr;
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
