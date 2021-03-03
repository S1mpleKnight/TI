package by.bsuir.ti.first.starter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static final String MAIN_PAGE_FXML = "main.fxml";
    private static Stage mainStage;

    public static Stage getMainStage() {
        return mainStage;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource(MAIN_PAGE_FXML));
        Scene scene = new Scene(root);
        stage.setTitle("Ciphering");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}