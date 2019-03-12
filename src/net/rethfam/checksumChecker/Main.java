package net.rethfam.checksumChecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rethfam.checksumChecker.controllers.MainScreenController;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/MainScreen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root,640, 480);

        MainScreenController controller = loader.getController();
        controller.initForm(primaryStage);

        primaryStage.setTitle("Checksum Checker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
