package by.bsuir.ti.fourth.controller;

import by.bsuir.ti.fourth.encryption.impl.RSA;
import by.bsuir.ti.fourth.hash.api.HashFunction;
import by.bsuir.ti.fourth.hash.impl.SHA1;
import by.bsuir.ti.fourth.math.Math;
import by.bsuir.ti.fourth.starter.Main;
import by.bsuir.ti.fourth.util.FileWorker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class PrimaryController {
    private static File file;

    @FXML
    private TextField pathField;
    @FXML
    private Button chooseFileButton;
    @FXML
    private TextField pValueField;
    @FXML
    private TextField qValueField;
    @FXML
    private TextField dValueField;
    @FXML
    private Button creatingSignatureButton;
    @FXML
    private TextField eSignatureValueField;
    @FXML
    private TextField rSignatureValueField;
    @FXML
    private TextField hashSignatureValueField;
    @FXML
    private TextField signatureValueField;
    @FXML
    private TextField eAuthValueField;
    @FXML
    private TextField rAuthValueField;
    @FXML
    private Button checkButton;
    @FXML
    private TextField hashAuthValueField;
    @FXML
    private TextField signatureAuthValueField;
    @FXML
    private TextField mAuthValueField;
    @FXML
    private TextField sAuthValueField;

    @FXML
    void initialize() {
        pathField.setText(file == null ? "" : file.getAbsolutePath());


        chooseFileButton.setOnAction(e -> {
            takeFile();
            if (file != null) {
                pathField.setText(file.getAbsolutePath());
            }
        });

        creatingSignatureButton.setOnAction(e -> {
            if (!checkSignArgs()) {
                return;
            }
            byte[] hashedData = calculateDigest();
            if (hashedData == null) {
                return;
            }
            byte[] digitalSignature = calculateDigitalSignature(hashedData);
            try {
                FileWorker.signTheFile(file, new BigInteger(hashSignatureValueField.getText()), digitalSignature);
            } catch (IOException ioException) {
                resetSignResultFields();
                showAlert("Can not sign the file", false);
            }
        });
    }

    private void resetSignResultFields(){
        hashSignatureValueField.setText("");
        signatureValueField.setText("");
        eSignatureValueField.setText("");
        rSignatureValueField.setText("");
    }

    private void takeFile() {
        File oldFile = file;
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Text files", "*.txt"));
        chooser.setTitle("Choose file");
        file = chooser.showOpenDialog(Main.getStage());
        if (file == null) {
            file = oldFile;
        }
    }

    private byte[] calculateDigitalSignature(byte[] hashedData) {
        RSA cipher = new RSA(pValueField.getText(), qValueField.getText(), dValueField.getText());
        eSignatureValueField.setText(cipher.getE().toString());
        rSignatureValueField.setText(cipher.getR().toString());
        byte[] encryptedDigest = cipher.encrypt(hashedData);
        signatureValueField.setText(new BigInteger(encryptedDigest).toString());
        return encryptedDigest;
    }

    private byte[] calculateDigest() {
        HashFunction function = new SHA1();
        byte[] hashedData;
        try {
            hashedData = function.takeDigestInBytes(FileWorker.readFile(file));
            hashSignatureValueField.setText(function.takeDigestAsNumber(hashedData));
        } catch (IOException ioException) {
            showAlert(ioException.getMessage(), false);
            return null;
        }
        return hashedData;
    }

    private void showAlert(String message, boolean isInfo) {
        Alert alert = new Alert(isInfo ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(isInfo ? "Information" : "Error");
        alert.setHeaderText(isInfo ? "Some information" : "Errors occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkSignArgs() {
        return checkFile() && checkSignNumbers();
    }

    private boolean checkSignNumbers() {
        if (!checkPValue()) {
            return false;
        }
        if (!checkQValue()) {
            return false;
        }
        BigInteger r = new BigInteger(pValueField.getText()).multiply(new BigInteger(qValueField.getText()));
        if (r.compareTo(BigInteger.valueOf(255)) <= 0){
            showAlert("P & Q are too low", false);
            return false;
        }
        return checkDValue(r);
    }

    private boolean checkPValue() {
        if (pValueField.getText() == null) {
            showAlert("Fill p field", false);
            return false;
        } else if (!pValueField.getText().matches("[0-9]+")) {
            showAlert("Incorrect p", false);
            return false;
        } else if (!Math.isPrime(new BigInteger(pValueField.getText()))) {
            showAlert("P is not prime", false);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkQValue() {
        if (qValueField.getText() == null) {
            showAlert("Fill q field", false);
            return false;
        } else if (!qValueField.getText().matches("[0-9]+")) {
            showAlert("Incorrect q", false);
            return false;
        } else if (!Math.isPrime(new BigInteger(qValueField.getText()))) {
            showAlert("Q is not prime", false);
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDValue(BigInteger r) {
        if (dValueField.getText() == null) {
            showAlert("Fill d field", false);
            return false;
        } else if (!dValueField.getText().matches("[0-9]+")) {
            showAlert("Incorrect d", false);
            return false;
        } else {
            BigInteger d = new BigInteger(dValueField.getText());
            if (!((d.compareTo(BigInteger.ONE) > 0) && (d.compareTo(Math.calculateEulerFunction(r)) < 0))) {
                showAlert("Incorrect d", false);
                return false;
            }
            if (Math.greatCommonDivisor(d, r).compareTo(BigInteger.ONE) != 0) {
                showAlert("D is not prime for r", false);
                return false;
            }
            return true;
        }
    }

    private boolean checkFile() {
        if (pathField.getText().equals("")) {
            showAlert("Choose file", false);
            return false;
        } else {
            return true;
        }
    }
}
