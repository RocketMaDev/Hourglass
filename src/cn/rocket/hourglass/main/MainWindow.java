package cn.rocket.hourglass.main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.locks.LockSupport;

/**
 * The controller class of MainWindow.fxml
 *
 * @author Rocket
 * @version 1.0
 */
public class MainWindow {
    private static final ImageView PAUSE_IMAGE_VIEW = new ImageView(new Image(StaticVariables.PAUSE));
    private static final ImageView START_IMAGE_VIEW = new ImageView(new Image(StaticVariables.START));

    private static final Image SPRING = new Image(StaticVariables.SPRING);
    private static final Image FLOWER = new Image(StaticVariables.FLOWER);
    private static final Image AUTUMN = new Image(StaticVariables.AUTUMN);
    private static final Image MOON = new Image(StaticVariables.MOON);
    private static final Image RIVER = new Image(StaticVariables.RIVER);
    private static final Image CLOUD = new Image(StaticVariables.insider ? StaticVariables.CLOUD : StaticVariables.CLOUD_ORIGINAL);

    @FXML
    GridPane gridPane;
    @FXML
    JFXButton cloud;
    @FXML
    JFXButton river;
    @FXML
    JFXButton autumn;
    @FXML
    JFXButton moon;
    @FXML
    JFXButton flower;
    @FXML
    JFXButton spring;
    @FXML
    Label copyright;
    @FXML
    ImageView bg;
    @FXML
    JFXButton fullscreenBtn;
    @FXML
    AnchorPane mainPane;
    @FXML
    ImageView nextPage;
    @FXML
    JFXButton pause;
    @FXML
    JFXButton stop;
    @FXML
    JFXButton restart;
    @FXML
    Label lb;
    @FXML
    JFXButton start;
    private boolean timing;
    private boolean pauseB;
    private Thread timingThread;

    @FXML
    void initialize() {
        timing = false;
        pauseB = false;
        start.setText("");
        start.setGraphic(new ImageView(new Image(StaticVariables.START)));
        stop.setText("");
        stop.setGraphic(new ImageView(new Image(StaticVariables.STOP)));
        pause.setText("");
        pause.setGraphic(PAUSE_IMAGE_VIEW);
        restart.setText("");
        restart.setGraphic(new ImageView(new Image(StaticVariables.RESTART)));
    }

    @FXML
    void nextPageM() {
        nextPage.setVisible(false);
        start.requestFocus();
    }

    @FXML
    void startM() {
        start.setVisible(false);
        stop.setVisible(true);
        pause.setVisible(true);
        restart.setVisible(true);
        lb.setTextFill(Paint.valueOf("BLACK"));
        timing = true;
        timingThread = new Thread(new Target());
        timingThread.start();
    }

    @FXML
    void pauseM() {
        if (pauseB)
            resetPause();
        else {
            pauseB = true;
            pause.setGraphic(START_IMAGE_VIEW);
        }

    }

    private void resetPause() {
        pauseB = false;
        pause.setGraphic(PAUSE_IMAGE_VIEW);
        LockSupport.unpark(timingThread);
    }

    @FXML
    void stopM() throws InterruptedException {
        timing = false;
        if (pauseB)
            resetPause();
        timingThread.join();
        stop.setVisible(false);
        pause.setVisible(false);
        restart.setVisible(false);
        start.setVisible(true);
    }

    @FXML
    void restartM() throws InterruptedException {
        timing = false;
        if (pauseB)
            resetPause();
        timingThread.join();
        lb.setText("15.000");
        lb.setTextFill(Paint.valueOf("BLACK"));
        timing = true;
        timingThread = new Thread(new Target());
        timingThread.start();
    }

    @FXML
    void fullscreenM() {
        fullscreenBtn.setVisible(false);
        mainPane.setVisible(true);
        Main.stage.setFullScreen(true);
        double width = Main.stage.getWidth();
        double height = Main.stage.getHeight();
        nextPage.setFitWidth(width);
        nextPage.setFitHeight(height);
        bg.setFitWidth(width);
        bg.setFitHeight(height);
        gridPane.setLayoutX((width - gridPane.getWidth()) / 2);
        start.setLayoutX((width - start.getWidth()) / 2);

        double limitedWidth = width - 2 * 380;
        double spaceWidth = (limitedWidth - 6 * spring.getWidth()) / 5;
        spring.setLayoutX(380);
        flower.setLayoutX(380 + spring.getWidth() + spaceWidth);
        autumn.setLayoutX(380 + 2 * (spring.getWidth() + spaceWidth));
        moon.setLayoutX(380 + 3 * (spring.getWidth() + spaceWidth));
        river.setLayoutX(380 + 4 * (spring.getWidth() + spaceWidth));
        cloud.setLayoutX(380 + 5 * (spring.getWidth() + spaceWidth));
    }

    @FXML
    void openLink() {
        if (Desktop.isDesktopSupported())
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/RocketMaDev/Hourglass"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
    }

    @FXML
    void springM() {
        bg.setImage(SPRING);
    }

    @FXML
    void flowerM() {
        bg.setImage(FLOWER);
    }

    @FXML
    void autumnM() {
        bg.setImage(AUTUMN);
    }

    @FXML
    void moonM() {
        bg.setImage(MOON);
    }

    @FXML
    void riverM() {
        bg.setImage(RIVER);
    }

    @FXML
    void cloudM() {
        bg.setImage(CLOUD);
    }

    @FXML
    void returnFullScreen() {
        Main.stage.setFullScreen(true);
    }

    @FXML
    void showBg() {
        nextPage.setVisible(true);
    }

    private class Target implements Runnable {
        private long endMillis;
        private long currentMillis;

        private boolean sleep() throws InterruptedException {
            if (!timing)
                return true;
            Thread.sleep(1);
            if (pauseB) {
                long distance = endMillis - currentMillis;
                LockSupport.park();
                endMillis = System.currentTimeMillis() + distance;
            }
            currentMillis = System.currentTimeMillis();
            Platform.runLater(() -> lb.setText(String.format("%d.%03d", (endMillis - currentMillis) / 1000, (endMillis - currentMillis) % 1000)));
            return false;
        }

        @Override
        public void run() {
            long startMillis = System.currentTimeMillis();
            try {
//                    Platform.runLater(() -> lb.setText(String.format("%d.%03d", finalI / 1000, finalI % 1000)));
                endMillis = System.currentTimeMillis() + 15000;
                currentMillis = endMillis - 15000;
                while (endMillis - currentMillis > 5000)
                    if (sleep())
                        return;
                Platform.runLater(() -> lb.setTextFill(Paint.valueOf("RED")));
                while (endMillis - currentMillis > 0)
                    if (sleep())
                        return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long finalMillis = System.currentTimeMillis();
            System.out.println((finalMillis - startMillis) / 1000.0);
            Platform.runLater(() -> {
                lb.setText("0.000");
                start.setVisible(true);
                stop.setVisible(false);
                pause.setVisible(false);
                restart.setVisible(false);
            });
        }


    }
}
