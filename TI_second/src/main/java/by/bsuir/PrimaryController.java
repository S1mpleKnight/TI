package by.bsuir;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PrimaryController {
    private static Cipher cipher;
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

    public static Cipher getCipher() {
        return cipher;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrimaryController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @FXML
    void initialize() {
        selectButton.setOnMouseClicked(e -> {
            takeFile();
            if (file != null) {
                pathField.setText(file.getAbsolutePath());
            }
        });

        encryptButton.setOnMouseClicked(e -> {
            if (file == null) {
                showAlert(false, "Choose file");
                return;
            }
            int createdKey = createKey();
            if (createdKey == -1) {
                showAlert(false, "Wrong key");
                return;
            }
            encryption(createdKey);

            showResults();
        });

        decryptButton.setOnMouseClicked(e -> {
            if (file == null) {
                showAlert(false, "Choose file");
                return;
            }
            int createdKey = createKey();
            if (createdKey == -1) {
                showAlert(false, "Wrong key");
                return;
            }
            showAlert(true, "Decryption started");
            decryption();

            showResults();
        });
    }

    private void decryption() {
        byte[] encrypted = Util.readEncryptedFile(file);
        cipher = new LFSR(createKey(), encrypted);
        byte[] bytes = cipher.decrypt();
        Util.fileRebuilding(bytes, file);
    }

    private void encryption(int createdKey) {
        cipher = new LFSR(createdKey, Util.fileOpening(file));
        byte[] bytes = cipher.encrypt();
        Util.writeEncryptedFile(bytes, file);
    }

    private void showResults() {
        try {
            Scene scene = new Scene(loadFXML("secondary"));
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Results");
            stage.setResizable(false);
            stage.show();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void takeFile() {
        File oldFile = file;
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose file");
        file = chooser.showOpenDialog(App.getPrimaryStage());
        if (file == null) {
            file = oldFile;
        }
    }

    private void showAlert(boolean state, String text) {
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
        } else if (key.matches("[0]*")) {
            return -1;
        } else {
            return calculateKey(key);
        }
    }

    private int calculateKey(String key) {
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
