package cn.rocket.hourglass.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * @author Rocket
 * @version 1.0
 */

public class Main extends Application {
    private static final String MainWindowFXML = "/cn/rocket/hourglass/resource/MainWindow.fxml";

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(MainWindowFXML))
        );
        primaryStage.setTitle("倒计时");
        primaryStage.getIcons().add(new Image(StaticVariables.HOURGLASS_ICON));
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        primaryStage.setScene(new Scene(root, 1200, 670));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
