package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainController implements Initializable {
	
    double width = 0;
    double height = 0;
    
	@FXML
	private ImageView logo_giunico;
	@FXML
	private Button button_gestione_tabelle;
	@FXML
	private Button button_operazioni_cliente;
	@FXML
	private Button button_visualizza_clienti;
	@FXML
	private Button button_gestione_operazioni;
	@FXML
	private Button button_gestione_cmr;
	@FXML
	private Button button_fatturati;
	
	
	public MainController() {
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = resolution.getWidth();
		this.height = resolution.getHeight();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button_gestione_tabelle.setOnAction(this::manage_gestione_tabelle);
		button_operazioni_cliente.setOnAction(this::manage_operazioni_cliente);
		button_visualizza_clienti.setOnAction(this::manage_visualizza_clienti);
		button_gestione_operazioni.setOnAction(this::manage_gestione_operazioni);
		button_gestione_cmr.setOnAction(this::manage_gestione_cmr);
		button_fatturati.setOnAction(this::manage_fatturati);
	}
	
	// Click on "GESTIONE TABELLE" button
	@FXML
	private void manage_gestione_tabelle(ActionEvent event) {
        
		try {
			Stage stage = (Stage) button_gestione_tabelle.getScene().getWindow();
			
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/gestione_tabelle.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
			
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
	    }
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// Click on "OPERAZIONI CLIENTE" button
	@FXML
	private void manage_operazioni_cliente(ActionEvent event) {
        
		try {
			Stage stage = (Stage) button_operazioni_cliente.getScene().getWindow();
			
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/operazioni_cliente.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/operazioni_cliente.css").toExternalForm());
			
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
	    }
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// Click on "VISUALIZZA CLIENTI" button
	@FXML
	private void manage_visualizza_clienti(ActionEvent event) {
        
		try {
			Stage stage = (Stage) button_visualizza_clienti.getScene().getWindow();
			
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/visualizza_clienti.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/visualizza_clienti.css").toExternalForm());
			
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
	    }
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}
	
	// Click on "GESTIONE OPERAZIONI" button
	@FXML
	private void manage_gestione_operazioni(ActionEvent event) {
        
		try {
			Stage stage = (Stage) button_gestione_operazioni.getScene().getWindow();
			
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/gestione_operazioni.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/gestione_operazioni.css").toExternalForm());
			
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
	    }
		catch(Exception e) {
	        e.printStackTrace();
	    }
	}

	// Click on "GESTIONE CMR" button
	@FXML
	private void manage_gestione_cmr(ActionEvent event) {
			
		try {
			Stage stage = (Stage) button_gestione_cmr.getScene().getWindow();
				
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/gestione_cmr.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
				
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/gestione_cmr.css").toExternalForm());
				
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
		   }
		catch(Exception e) {
			e.printStackTrace();
		   }
	}
		
	// Click on "FATTURATI" button
	@FXML
	private void manage_fatturati(ActionEvent event) {
			
		try {
			Stage stage = (Stage) button_fatturati.getScene().getWindow();
				
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/fatturati.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
				
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/fatturati.css").toExternalForm());
				
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
