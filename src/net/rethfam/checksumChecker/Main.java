package net.rethfam.checksumChecker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rethfam.checksumChecker.controllers.MainScreenController;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        //Load Icon Images to list to be used in the title bar and taskbar
        List<Image> icons = new ArrayList<Image>();

        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-512x512.png")));
        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-256x256.png")));
        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-128x128.png")));
        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-64x64.png")));
        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-32x32.png")));
        icons.add(new Image(this.getClass().getResourceAsStream("images/checkImage-16x16.png")));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/MainScreen.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root,640, 480);

        MainScreenController controller = loader.getController();
        controller.initForm(primaryStage);

        primaryStage.setTitle("Checksum Checker");
        primaryStage.getIcons().addAll(icons);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
