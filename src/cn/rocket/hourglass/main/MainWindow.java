package cn.rocket.hourglass.main;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

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
    @FXML
    ImageView mainbg;
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
    void nextPage() {
        mainbg.setVisible(false);
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

    private class Target implements Runnable {

        @Override
        public void run() {
            long startMillis = System.currentTimeMillis();
            try {
                for (int i = 15000; i >= 5000; i--) {
                    if (!timing)
                        return;
                    if (i % 3 != 0)
                        Thread.sleep(1);
                    int finalI = i;
                    Platform.runLater(() -> lb.setText(String.format("%d.%03d", finalI / 1000, finalI % 1000)));
                    if (pauseB)
                        LockSupport.park();
                }
                Platform.runLater(() -> lb.setTextFill(Paint.valueOf("RED")));
                for (int i = 5000; i >= 0; i--) {
                    int finalI = i;
                    if (!timing)
                        return;
                    Thread.sleep(1);
                    Platform.runLater(() -> lb.setText(String.format("%d.%03d", finalI / 1000, finalI % 1000)));
                    if (pauseB)
                        LockSupport.park();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long endMillis = System.currentTimeMillis();
            System.out.println((endMillis - startMillis) / 1000.0);
            Platform.runLater(() -> {
                start.setVisible(true);
                stop.setVisible(false);
                pause.setVisible(false);
                restart.setVisible(false);
            });
        }


    }
}
