package application;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            VBox root = loader.load();

            // Create the scene and set it to the primaryStage
            Scene scene = new Scene(root, 1337, 915);
            primaryStage.setTitle("Northeastern University Valet Parking Assistant");
            primaryStage.setScene(scene);
            primaryStage.show();
            scene.getStylesheets().add("file:resources/styles.css");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}