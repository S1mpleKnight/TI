package by.bsuir.ti.fourth.starter;

import by.bsuir.ti.fourth.hash.impl.SHA1;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

public class Main extends Application {

    public static void main(String[] args) {
//        String value = "Hello World!";
//        String sha1 = "";
//        // With the java libraries
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-1");
//            digest.reset();
//            digest.update(value.getBytes(StandardCharsets.UTF_8));
//            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("The sha1 of \"" + value + "\" is:");
//        System.out.println(sha1);
//        System.out.println();
//
//        String data = "Hello World!";
//        System.out.println(data);
//        String digest = new SHA1().takeDigestInHexString(data.getBytes());
//        System.out.println(digest);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Node node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("primary.fxml")));
            Group group = new Group();
            group.getChildren().add(node);
            Scene scene = new Scene(group);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle("Lab_4");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
