package by.bsuir.ti.third.controller;

import by.bsuir.ti.third.starter.Starter;
import by.bsuir.ti.third.util.FileWorker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Objects;

public class ResultController {
    private static final String THIRD_SCENE = "thirdScene.fxml";
    private static final String ENCRYPTED_NUMBERS = "logs/encrypt.txt";
    @FXML
    private TextArea resultArea;
    @FXML
    private Button previousButton;

    @FXML
    void initialize() {
        resultArea.setText(String.join("\n", FileWorker.readNumbers(ENCRYPTED_NUMBERS)));


        previousButton.setOnAction(e -> {
            Starter.getFirstStage().setScene(loadScene());
        });
    }

    private Scene loadScene() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(THIRD_SCENE))));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return scene;
    }
}
