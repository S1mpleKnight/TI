package by.bsuir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Util {
    public static void writeEncryptedFile(String str, String name){
        File file = new File("./encr.txt");
        try {
            BufferedWriter bf = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
            bf.write(name + "\n");
            bf.write(str);
            bf.flush();
            bf.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static List<String> readEncryptedFile(String str){
        List<String> list = new ArrayList<>();
        File file = new File(str);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            String s;
            while ((s = br.readLine()) != null){
                list.add(s + "\n");
            }
            br.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public static void fileRebuilding(List<Byte> bytes, String name){
        File file = new File(name);
        try {
            System.out.println(file.getAbsolutePath());
            System.out.println(name);
            file.createNewFile();
            FileOutputStream fs = new FileOutputStream(file);
            for (Byte bytek: bytes){
                fs.write(bytek);
            }
            fs.flush();
            fs.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static byte[] fileOpening(File file){
        byte[] arr = new byte[0];
        try {
            FileInputStream fs = new FileInputStream(file);
            arr = fs.readAllBytes();
            fs.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return arr;
    }
}
