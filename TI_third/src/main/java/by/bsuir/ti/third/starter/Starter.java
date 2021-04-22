package by.bsuir.ti.third.starter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public final class Starter extends Application {
    private static final String FIRST_SCENE = "firstScene.fxml";
    private static Scene firstScene;
    private static Stage firstStage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Scene getFirstScene() {
        return firstScene;
    }

    public static Stage getFirstStage() {
        return firstStage;
    }

    @Override
    public void start(Stage primaryStage) {
        firstStage = primaryStage;
        Scene scene = loadScene();
        firstScene = scene;
        if (scene == null) {
            errorAlert();
        } else {
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    private void errorAlert() {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setContentText("Can't load main scene");
        errorAlert.setHeaderText("Can't start the program");
        errorAlert.show();
    }

    public Scene loadScene() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(FIRST_SCENE))));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
        return scene;
    }
}
