package by.bsuir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class PrimaryController {
    private File file;

    @FXML
    private TextField keyField;

    @FXML
    private TextField pathField;

    @FXML
    private Button selectButton;

    @FXML
    private Button encryptButton;

    @FXML
    private Button decryptButton;

    @FXML
    void initialize() {
        selectButton.setOnMouseClicked(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose file");
            file = chooser.showOpenDialog(App.getPrimaryStage());
            pathField.setText(file.getAbsolutePath());
        });

        encryptButton.setOnMouseClicked(e -> {
            Cipher cipher = new LFSR(createKey(), Util.fileOpening(file));
            byte[] bytes = cipher.encrypt();
            Util.writeEncryptedFile(bytes, file);
            showAlert();
        });

        decryptButton.setOnMouseClicked(e -> {
            byte[] encrypted = Util.readEncryptedFile(file);
            Cipher cipher = new LFSR(createKey(), encrypted);
            byte[] bytes = cipher.decrypt();
            Util.fileRebuilding(bytes, file);
            showAlert();
        });
    }

    private void showAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.showAndWait();
    }

    private int createKey(){
        String key = Arrays.stream(keyField.getText()
                .split(""))
                .filter(str -> (str.equals("0") || str.equals("1")))
                .collect(Collectors.joining(""));
        return Integer.parseInt(key);
    }
}
