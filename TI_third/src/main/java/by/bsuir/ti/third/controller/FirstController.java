package by.bsuir.ti.third.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Objects;

import by.bsuir.ti.third.starter.Starter;
import by.bsuir.ti.third.util.Additions;
import by.bsuir.ti.third.util.SomeMath;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public final class FirstController {
    private static final String SECOND_SCENE = "secondScene.fxml";
    private static BigInteger p;
    @FXML
    private TextField pNumberField;
    @FXML
    private Button next;

    @FXML
    void initialize() {
        if (p != null){
            pNumberField.setText(p.toString());
        }

        next.setOnAction(e -> {
            if (validateField()){
                p = new BigInteger(pNumberField.getText());
                Starter.getFirstStage().setScene(loadScene());
            }
        });
    }

    private boolean validateField(){
        if (checkField()){
            return checkNumber();
        } else {
            return false;
        }
    }

    private boolean checkField(){
        String text = pNumberField.getText();
        return Additions.checkTextFormOfNumber(text);
    }

    private boolean checkNumber(){
        BigInteger number = new BigInteger(pNumberField.getText());
        if (number.compareTo(BigInteger.valueOf(256)) < 1){
            Additions.showAlert(false, "Very low p-value");
            return false;
        }
        boolean result = SomeMath.isPrime(number);
        if (!result){
            Additions.showAlert(false, "This number is not prime");
        }
        return result;
    }

    public static BigInteger getP() {
        return p;
    }

    private Scene loadScene() {
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(SECOND_SCENE))));
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
        }
        return scene;
    }
}
