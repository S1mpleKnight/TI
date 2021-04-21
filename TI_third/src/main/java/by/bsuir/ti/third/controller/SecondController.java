package by.bsuir.ti.third.controller;

import by.bsuir.ti.third.starter.Starter;
import by.bsuir.ti.third.util.Additions;
import by.bsuir.ti.third.util.SomeMath;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.util.List;

public class SecondController {
    private BigInteger g;
    private BigInteger x;
    private BigInteger k;
    private ObservableList<BigInteger> list;
    private List<BigInteger> roots;
    @FXML
    private ComboBox<BigInteger> gListComboBox;
    @FXML
    private TextField xField;
    @FXML
    private TextField kField;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;

    public BigInteger getG() {
        return g;
    }

    public BigInteger getX() {
        return x;
    }

    public BigInteger getK() {
        return k;
    }

    @FXML
    void initialize() {

        roots = SomeMath.takePrimitiveRoots(FirstController.getP());
        list = FXCollections.observableList(roots);
        gListComboBox.getItems().addAll(list);

        previousButton.setOnAction(e -> {
            Starter.getFirstStage().setScene(Starter.getFirstScene());
        });

        gListComboBox.setOnAction(e -> {
            g = gListComboBox.getValue();
        });

        nextButton.setOnAction(e -> {
            if (inputValidation()){
                x = new BigInteger(xField.getText());
                k = new BigInteger(kField.getText());

            }
        });
    }

    private boolean inputValidation() {
        if (g == null) {
            Additions.showAlert(false, "Choose g");
            return false;
        } else {
            return validateFields();
        }
    }

    private boolean validateFields() {
        if (checkText()) {
            return checkValues();
        } else {
            return false;
        }
    }

    private boolean checkValues() {
        if (!checkXValue()) {
            Additions.showAlert(false, "Invalid X");
            return false;
        }
        if (!checkKValue()) {
            Additions.showAlert(false, "Invalid K");
            return false;
        }
        return true;
    }

    private boolean checkXValue() {
        BigInteger number = new BigInteger(xField.getText());
        return (number.compareTo(BigInteger.ONE) > 0)
                && (number.compareTo(FirstController.getP().subtract(BigInteger.ONE)) < 0);
    }

    private boolean checkKValue() {
        BigInteger number = new BigInteger(kField.getText());
        return (number.compareTo(BigInteger.ONE) > 0)
                && (number.compareTo(FirstController.getP().subtract(BigInteger.ONE)) < 0)
                && (number.gcd(FirstController.getP().subtract(BigInteger.ONE)).compareTo(BigInteger.ONE) == 0);
    }

    private boolean checkTextX() {
        String x = xField.getText();
        return Additions.checkTextFormOfNumber(x);
    }

    private boolean checkTextK() {
        String k = kField.getText();
        return Additions.checkTextFormOfNumber(k);
    }

    private boolean checkText() {
        if (!checkTextX()) {
            Additions.showAlert(false, "Invalid X value");
            return false;
        }
        if (!checkTextK()) {
            Additions.showAlert(false, "Invalid K value");
            return false;
        }
        return true;
    }
}

