package lk.ijse.dep11;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/view/Scene1.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Leon Text Editor");
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        root.setBackground(Background.fill(Color.TRANSPARENT));
        mainScene.setFill(Color.TRANSPARENT);
        primaryStage.centerOnScreen();
        primaryStage.show();


        FadeTransition fadeIn = new FadeTransition(Duration.millis(3000) ,root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.playFromStart();

//        fadeIn.setOnFinished(e ->{
//            FadeTransition fadeOut = new FadeTransition(Duration.millis(3000) ,root);
//            fadeOut.setFromValue(1);
//            fadeOut.setToValue(0);
//            fadeOut.playFromStart();
//        });



    }
}
