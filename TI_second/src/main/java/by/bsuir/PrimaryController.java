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
            String encryptedText = cipher.encrypt();
            Util.writeEncryptedFile(encryptedText, file.getAbsolutePath().trim());
            showAlert();
        });

        decryptButton.setOnMouseClicked(e -> {
            List<String> list = Util.readEncryptedFile(file.getAbsolutePath());
            Cipher cipher = new LFSR(createKey(), createText(list).getBytes(StandardCharsets.UTF_8));
            List<Byte> decryptedBytes = cipher.decrypt();
            Util.fileRebuilding(decryptedBytes, String.join("", list.get(0).split("\n")));
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

    private String createText(List<String> list){
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < list.size(); i++){
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}
