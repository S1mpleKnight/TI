package by.bsuir.ti.third.controller;

import by.bsuir.ti.third.cipher.ElGAmalCipher;
import by.bsuir.ti.third.cipher.SimpleCipher;
import by.bsuir.ti.third.decorator.OutputDecorator;
import by.bsuir.ti.third.starter.Starter;
import by.bsuir.ti.third.util.Additions;
import by.bsuir.ti.third.util.FileWorker;
import by.bsuir.ti.third.util.SomeMath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class ThirdController {
    private static final String SECOND_SCENE = "secondScene.fxml";
    private static final String RESULT_SCENE = "resultScene.fxml";
    private static final String DECRYPT_STATUS = "decrypt";
    private static final String ENCRYPT_STATUS = "encrypt";
    private static String status;

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
    private Button nextButton;

    @FXML
    void initialize() {
        filePathField.setText(file == null ? "" : file.getAbsolutePath());
        nextButton.setDisable(true);

        encryptButton.setOnAction(e -> {
            if (checkFile()) {
                SimpleCipher cipher = new ElGAmalCipher(FirstController.getP(), SecondController.getG(),
                        SecondController.getK(), SecondController.getX());
                cipher = new OutputDecorator(cipher);
                List<BigInteger> encrypted = cipher.encrypt(FileWorker.fileOpening(file));
                byte[] bytes = SomeMath.takeEncryptedBytes(encrypted);
                FileWorker.writeEncryptedFile(bytes, file);
                Additions.showAlert(true, "Encryption ended");
                status = ENCRYPT_STATUS;
                nextButton.setDisable(false);
            }
        });

        decryptButton.setOnAction(e -> {
            if (checkFile()) {
                SimpleCipher cipher = new ElGAmalCipher(FirstController.getP(), SecondController.getG(),
                        SecondController.getK(), SecondController.getX());
                cipher = new OutputDecorator(cipher);
                List<BigInteger> decrypted = cipher.decrypt(FileWorker.fileOpening(file));
                byte[] bytes = SomeMath.takeDecryptedBytes(decrypted);
                FileWorker.fileRebuilding(bytes, file);
                Additions.showAlert(true, "Decryption ended");
                status = DECRYPT_STATUS;
                nextButton.setDisable(false);
            }
        });

        previousButton.setOnAction(e -> {
            Starter.getFirstStage().setScene(loadScene(SECOND_SCENE));
        });

        nextButton.setOnAction(e -> {
            Starter.getFirstStage().setScene(loadScene(RESULT_SCENE));
        });

        chooseFileButton.setOnAction(e -> {
            takeFile();
            if (file != null) {
                filePathField.setText(file.getAbsolutePath());
            }
        });
    }

    private boolean checkFile() {
        if (file == null) {
            Additions.showAlert(false, "Choose file");
            return false;
        } else if (!file.exists()){
            Additions.showAlert(false, "File does not exist");
            return false;
        } else {
            return true;
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

    private Scene loadScene(String path) {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path))));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return scene;
    }

    public static String getStatus() {
        return status;
    }
}
