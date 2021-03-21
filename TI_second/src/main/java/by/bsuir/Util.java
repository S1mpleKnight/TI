package by.bsuir;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Util {
    public static void writeEncryptedFile(byte[] bytes, File file) {
        File encryptedFile = new File(file.getAbsolutePath() + ".encrypted");
        try {
            DataOutputStream ds = new DataOutputStream(new FileOutputStream(encryptedFile));
            ds.write(bytes);
            ds.flush();
            ds.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static byte[] readEncryptedFile(File file) {
        byte[] bytes = new byte[0];
        try {
            DataInputStream ds = new DataInputStream(new FileInputStream(file));
            bytes = ds.readAllBytes();
            ds.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return bytes;
    }

    public static void fileRebuilding(byte[] bytes, File file) {
        String name = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 10);
        File decryptedFile = new File(name);
        try {
            file.createNewFile();
            DataOutputStream ds = new DataOutputStream(new FileOutputStream(decryptedFile));
            for (Byte bytek : bytes) {
                ds.writeByte(bytek);
            }
            ds.flush();
            ds.close();
        } catch (IOException exception) {
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
            exception.printStackTrace();
        }
        return arr;
    }
}
