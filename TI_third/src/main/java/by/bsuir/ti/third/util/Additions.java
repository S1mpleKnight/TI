package by.bsuir.ti.third.util;

import javafx.scene.control.Alert;

public final class Additions {
    public static void showAlert(boolean state, String message) {
        Alert alert = new Alert(state ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(state ? "Information" : "Error");
        alert.setHeaderText(state ? "Some info" : "Error happened");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean checkTextFormOfNumber(String text) {
        if (text == null || text.equals("")) {
            Additions.showAlert(false, "Fill the form");
            return false;
        } else if (!text.matches("[0-9]+")) {
            Additions.showAlert(false, "Invalid value");
            return false;
        } else {
            return true;
        }
    }
}
