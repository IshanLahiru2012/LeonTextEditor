package lk.ijse.dep11;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Scene1Controller {

    public AnchorPane rootScene;

    public void initialize()throws Exception{

        new Thread(()->{
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(()->{
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
                AnchorPane mainRoot = null;
                try {
                    mainRoot = fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Scene mainScene = new Scene(mainRoot);
                Stage stage = new Stage();
                stage.setScene(mainScene);
                stage.centerOnScreen();

                MainViewController mainViewController = fxmlLoader.getController();
                String title = mainViewController.getFileName();
                stage.setTitle(title);
                stage.show();

                Stage splashStage = (Stage) rootScene.getScene().getWindow();
                splashStage.close();


            });
        }).start();
    }
}
