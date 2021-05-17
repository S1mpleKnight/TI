package by.bsuir.ti.fourth.starter;

import by.bsuir.ti.fourth.hash.SHA1;
import javafx.application.Application;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Main extends Application {

    public static void main(String[] args) {
        String value = "Hello World!";
        String sha1 = "";
        // With the java libraries
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes(StandardCharsets.UTF_8));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The sha1 of \"" + value + "\" is:");
        System.out.println(sha1);
        System.out.println();

        String data = "Hello World!";
        System.out.println(data);
        String digest = new SHA1().getDigestOfString(data.getBytes());
        System.out.println(digest);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
}
