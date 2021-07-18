package cn.rocket.hourglass.main;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/** The main class.
 * @author Rocket
 * @version 1.0
 */

public class Main extends Application {
    static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource(StaticVariables.MainWindowFXML))
        );
        stage = primaryStage;
        primaryStage.setTitle("\u5012\u8ba1\u65f6");  //倒计时
        primaryStage.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ESCAPE));
        primaryStage.getIcons().add(new Image(StaticVariables.HOURGLASS_ICON));
        primaryStage.setOnCloseRequest(event -> System.exit(1));
        primaryStage.setScene(new Scene(root, 1200, 670));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
