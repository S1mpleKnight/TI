package by.bsuir.ti.third.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileWorker {
    public static void writeEncryptedFile(byte[] bytes, File file) {
        File encryptedFile = new File(file.getAbsolutePath() + ".encrypted");
        writeFile(bytes, file, encryptedFile);
    }

    public static void fileRebuilding(byte[] bytes, File file) {
        String name = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 10);
        File decryptedFile = new File(name);
        writeFile(bytes, file, decryptedFile);
    }

    private static void writeFile(byte[] bytes, File file, File decryptedFile) {
        boolean state = false;
        try {
            state = file.createNewFile();
            DataOutputStream ds = new DataOutputStream(new FileOutputStream(decryptedFile));
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
}
