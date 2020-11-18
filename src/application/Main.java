package application;
	
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;


public class Main extends Application {
	
	int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();
    
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		// Responsive Design
        int sceneWidth = 0;
        int sceneHeight = 0;
        if (screenWidth <= 800 && screenHeight <= 600) {
            sceneWidth = 600;
            sceneHeight = 350;
        } else if (screenWidth <= 1280 && screenHeight <= 768) {
            sceneWidth = 800;
            sceneHeight = 450;
        } else if (screenWidth <= 1920 && screenHeight <= 1080) {
            sceneWidth = 1000;
            sceneHeight = 650;
        }
        
		try {
			primaryStage.setTitle("Giunico");
			
			// Add FXML to Scene
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/main.fxml");
			//URL fxmlUrl = getClass().getResource("/resources/fxml/main_reduced.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, sceneWidth, sceneHeight, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/main.css").toExternalForm());
			
			primaryStage.setResizable(true);
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			//primaryStage.setFullScreen(true);
			primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
