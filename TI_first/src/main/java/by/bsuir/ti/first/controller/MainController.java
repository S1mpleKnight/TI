package by.bsuir.ti.first.controller;

import by.bsuir.ti.first.cipher.api.Cipher;
import by.bsuir.ti.first.cipher.impl.Column;
import by.bsuir.ti.first.cipher.impl.Decimation;
import by.bsuir.ti.first.cipher.impl.Vigener;
import by.bsuir.ti.first.starter.App;
import by.bsuir.ti.first.util.FileWorker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MainController {

    @FXML
    private RadioButton decimationChoise;

    @FXML
    private RadioButton vigenerChoise;

    @FXML
    private RadioButton colomnChoise;

    @FXML
    private TextArea plainText;

    @FXML
    private Button encryptText;

    @FXML
    private Button decryptText;

    @FXML
    private TextArea magicText;

    @FXML
    private TextField pathField;

    @FXML
    private Button fileButton;

    @FXML
    private TextField keyField;

    @FXML
    private Button encryptFile;

    @FXML
    private Button decryptFile;

    @FXML
    private Label languageLabel;

    private File file;
    private Cipher cipher;

    @FXML
    void initialize() {
        fileButton.setOnMouseClicked(e -> takePath());

        encryptText.setOnMouseClicked(e -> {
            if (checkPlainTextField() && simpleKeyCheck() && keyValidation()) {
                cipher = choseCipher();
                String text = cipher.encrypt(Arrays.asList(plainText.getText().split("\\n")));
                magicText.setText(text);
            }
        });

        decryptText.setOnMouseClicked(e -> {
            if (checkMagicTextField() && simpleKeyCheck() && keyValidation()) {
                cipher = choseCipher();
                String text = cipher.decrypt(Arrays.asList(magicText.getText().split("\\n")));
                plainText.setText(text);
            }
        });

        encryptFile.setOnMouseClicked(e -> {
            if (checkFile() && simpleKeyCheck() && keyValidation()) {
                try {
                    List<String> list = FileWorker.readFile(file.getAbsolutePath());
                    String text = choseCipher().encrypt(list);
                    FileWorker.writeFile(file.getAbsolutePath(), text);
                    showStatus("Encryption");
                } catch (FileNotFoundException exception) {
                    fileAlert(exception.getLocalizedMessage());
                }
            }
        });

        decryptFile.setOnMouseClicked(e -> {
            if (checkFile() && simpleKeyCheck() && keyValidation()) {
                try {
                    List<String> list = FileWorker.readFile(file.getAbsolutePath());
                    String text = choseCipher().decrypt(list);
                    FileWorker.writeFile(file.getAbsolutePath(), text);
                    showStatus("Decryption");
                } catch (FileNotFoundException exception) {
                    fileAlert(exception.getLocalizedMessage());
                }
            }
        });

        vigenerChoise.setOnMouseClicked(e -> languageLabel.setText("Language: RU"));

        decimationChoise.setOnMouseClicked(e -> languageLabel.setText("Language: EN"));

        colomnChoise.setOnMouseClicked(e -> languageLabel.setText("Language: EN"));
    }

    private void showStatus(String str) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Complete");
        alert.setHeaderText("Information in file was updated");
        alert.setContentText(str + " ended");
        alert.showAndWait();
    }

    private Cipher choseCipher() {
        if (vigenerChoise.isSelected()) {
            return new Vigener(modifyVigenerKey(keyField.getText()));
        } else if (decimationChoise.isSelected()) {
            return new Decimation((Integer.parseInt(keyField.getText())));
        } else {
            return new Column(modifyColumnKey(keyField.getText()));
        }
    }

    private boolean keyValidation() {
        boolean state = false;
        if (vigenerChoise.isSelected()) {
            state = checkVigenerKey(keyField.getText());
        } else if (decimationChoise.isSelected()) {
            if (checkDecimationKey(keyField.getText())) {
                state = checkNumberDecimation(Integer.parseInt(keyField.getText()));
            }
        } else {
            state = checkColumnKey(keyField.getText());
        }
        return state;
    }

    private boolean checkColumnKey(String str) {
        if (str.trim().split("\\s+").length > 1) {
            keyAlert("Only one word needed");
            return false;
        } else if (Pattern.matches("[\\s+]+", str)) {
            keyAlert("Spaces are not key");
            return false;
        } else if (Pattern.matches("[a-zA-Z]+", str.trim())) {
            return true;
        } else {
            keyAlert("Wrong key");
            return false;
        }
    }

    private String modifyVigenerKey(String str) {
        return String.join("", str.toLowerCase().split("\\s+"));
    }

    private String modifyColumnKey(String str) {
        return str.trim().toLowerCase();
    }

    private boolean checkDecimationKey(String str) {
        if (Pattern.matches("[0-9]+", str)) {
            return true;
        } else {
            keyAlert("It should be a number");
            return false;
        }
    }

    private boolean checkVigenerKey(String str) {
        if (Pattern.matches("[\\s+]+", str)) {
            keyAlert("Spaces are not key");
            return false;
        } else if (Pattern.matches("[а-яА-Я ёЁ]+", str)) {
            return true;
        } else {
            keyAlert("Wrong key");
            return false;
        }
    }

    private boolean checkNumberDecimation(int key) {
        if (key <= 1) {
            keyAlert("Invalid key: banned value");
            return false;
        }
        return checkMultiplicity(key);
    }

    private boolean checkMultiplicity(int key) {
        int length = Decimation.getAlphabetLength();
        for (int i = 2; i < length; i++) {
            if (length % i == 0) {
                if (key % i == 0) {
                    keyAlert("Invalid key: multiplicity");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean simpleKeyCheck() {
        if (keyField.getText().length() != 0) {
            return true;
        } else {
            keyAlert("Key field is empty");
            return false;
        }
    }

    private boolean checkPlainTextField() {
        if (plainText.getText() == null || plainText.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Plain text area is empty");
            alert.setContentText("Fill plain text area");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean checkMagicTextField() {
        if (magicText.getText().length() != 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("Magic text area is empty");
            alert.setContentText("Fill magic text area");
            alert.showAndWait();
            return false;
        }
    }

    private boolean checkFile() {
        if (file != null) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mistake");
            alert.setHeaderText("There is no file chosen");
            alert.setContentText("Chose file to work");
            alert.showAndWait();
            return false;
        }
    }

    private File takeFileFromPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        return fileChooser.showOpenDialog(App.getMainStage());
    }

    private void keyAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Mistake");
        alert.setHeaderText(str);
        alert.setContentText("Enter your key");
        alert.showAndWait();
    }

    private void takePath() {
        File anotherFile = takeFileFromPath();
        if (anotherFile != null) {
            file = anotherFile;
            pathField.setText(file.getAbsolutePath());
        }
    }

    private void fileAlert(String str) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Mistake");
        alert.setHeaderText(str);
        alert.setContentText("Chose your file");
        alert.showAndWait();
    }
}
