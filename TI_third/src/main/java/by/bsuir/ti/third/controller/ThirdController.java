package by.bsuir.ti.third.controller;

import by.bsuir.ti.third.cipher.ElGAmalCipher;
import by.bsuir.ti.third.cipher.SimpleCipher;
import by.bsuir.ti.third.starter.Starter;
import by.bsuir.ti.third.util.Additions;
import by.bsuir.ti.third.util.FileWorker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ThirdController {
    private static final String SECOND_SCENE = "secondScene.fxml";
    private static File file;
    @FXML
    private TextField filePathField;
    @FXML
    private Button chooseFileButton;
    @FXML
    private Button encryptButton;
    @FXML
    private Button decryptButton;
    @FXML
    private Button previousButton;

    @FXML
    void initialize() {
        filePathField.setText(file == null ? "" : file.getAbsolutePath());

        encryptButton.setOnAction(e -> {
            checkFile();
            SimpleCipher cipher = new ElGAmalCipher(FirstController.getP(), SecondController.getG(),
                    SecondController.getK(), SecondController.getX());
            byte[] encrypted = cipher.encrypt(FileWorker.fileOpening(file));
            FileWorker.writeEncryptedFile(encrypted, file);
        });

        decryptButton.setOnAction(e -> {
            checkFile();
            SimpleCipher cipher = new ElGAmalCipher(FirstController.getP(), SecondController.getG(),
                    SecondController.getK(), SecondController.getX());
            byte[] decrypted = cipher.decrypt(FileWorker.fileOpening(file));
            FileWorker.fileRebuilding(decrypted, file);
        });

        previousButton.setOnAction(e -> {
            Starter.getFirstStage().setScene(loadScene());
        });

        chooseFileButton.setOnAction(e -> {
            takeFile();
            if (file != null) {
                filePathField.setText(file.getAbsolutePath());
            }
        });
    }

    private void checkFile() {
        if (file == null) {
            Additions.showAlert(false, "Choose file");
        }
    }

    private void takeFile() {
        File oldFile = file;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose file");
        file = chooser.showOpenDialog(Starter.getFirstStage());
        if (file == null) {
            file = oldFile;
        }
    }

    private Scene loadScene() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(SECOND_SCENE))));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return scene;
    }
}
