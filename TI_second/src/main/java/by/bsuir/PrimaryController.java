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
            takeFile();
            if (file != null){
                pathField.setText(file.getAbsolutePath());
            }
        });

        encryptButton.setOnMouseClicked(e -> {
            if (file == null){
                showAlert(false, "Choose file");
                return;
            }
            int createdKey = createKey();
            if (createdKey == -1){
                showAlert(false, "Wrong key");
                return;
            }
            Cipher cipher = new LFSR(createdKey, Util.fileOpening(file));
            byte[] bytes = cipher.encrypt();
            Util.writeEncryptedFile(bytes, file);
            showAlert(true, "Encryption finished");
        });

        decryptButton.setOnMouseClicked(e -> {
            if (file == null){
                showAlert(false, "Choose file");
                return;
            }
            int createdKey = createKey();
            if (createdKey == -1){
                showAlert(false, "Wrong key");
                return;
            }
            byte[] encrypted = Util.readEncryptedFile(file);
            Cipher cipher = new LFSR(createKey(), encrypted);
            byte[] bytes = cipher.decrypt();
            Util.fileRebuilding(bytes, file);
            showAlert(true, "Decryption finished");

        });
    }

    private void takeFile() {
        File oldFile = file;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose file");
        file = chooser.showOpenDialog(App.getPrimaryStage());
        if (file == null){
            file = oldFile;
        }
    }

    private void showAlert(boolean state, String text){
        Alert alert = new Alert(state ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(state ? "Info" : "Error");
        alert.setHeaderText(state ? "Information" : "Error occurred");
        alert.setContentText(text);
        alert.showAndWait();
    }

    private int createKey() {
        String key = Arrays.stream(keyField.getText().trim()
                .split(""))
                .filter(str -> (str.equals("0") || str.equals("1")))
                .collect(Collectors.joining(""));
        if (key.length() == 0 || key.length() > 25) {
            return -1;
        } else {
            return calculateKey(key);
        }
    }

    private int calculateKey(String key){
        int result = 0;
        String[] split = key.split("");
        for (int i = 0; i < split.length; i++) {
            int temp = Integer.parseInt(split[i]);
            result += temp;
            if (i != split.length - 1) {
                result <<= 1;
            }
        }
        return result;
    }
}
