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
import java.util.List;

public class FileWorker {
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

    public static void writeNumbers(List<BigInteger> numbers, String path){
        try (Writer writer = new FileWriter(path, StandardCharsets.UTF_8)){
            for (int i = 0; i < numbers.size(); i++){
                writer.write(numbers.get(i).toString());
                writer.write(" ");
                if (i % 10 == 0) {
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("Can not write numbers");
            System.out.println(e.getMessage());
        }
    }
}
