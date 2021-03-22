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
        assert keyBitsArea != null : "fx:id=\"keyBitsArea\" was not injected: check your FXML file 'secondary.fxml'.";
        assert fileBitsArea != null : "fx:id=\"fileBitsArea\" was not injected: check your FXML file 'secondary.fxml'.";
        assert encryptedBitsArea != null : "fx:id=\"encryptedBitsArea\" was not injected: check your FXML file 'secondary.fxml'.";

    }
}
