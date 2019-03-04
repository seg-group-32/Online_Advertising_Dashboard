package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static Stage theStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml"));
        primaryStage.setTitle("Advertisement Statistics Tool");
        primaryStage.setScene(new Scene(root, 1080, 720));
        primaryStage.show();
        theStage = primaryStage;
    }


    public static void launcher(String[] args) {
        launch(args);
    }
}
