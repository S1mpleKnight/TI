package by.bsuir;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
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


    }
}
