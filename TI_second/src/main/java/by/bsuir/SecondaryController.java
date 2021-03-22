package by.bsuir;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SecondaryController {

    @FXML
    private TextArea keyBitsArea;

    @FXML
    private TextArea fileBitsArea;

    @FXML
    private TextArea encryptedBitsArea;

    @FXML
    void initialize() {
        keyBitsArea.setWrapText(true);
        fileBitsArea.setWrapText(true);
        encryptedBitsArea.setWrapText(true);

        keyBitsArea.setText(PrimaryController.getCipher().showKeyBytes());
        fileBitsArea.setText(PrimaryController.getCipher().showTextBytes());
        encryptedBitsArea.setText(PrimaryController.getCipher().showEncryptedBytes());
    }
}
