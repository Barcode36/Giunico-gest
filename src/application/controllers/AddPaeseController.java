package application.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.database.PaesiDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AddPaeseController implements Initializable {
	
	Text label_sigla;
	
	TextField field_sigla;
	TextField field_stato;
	ToggleGroup tg;
	RadioButton radio_si;
	RadioButton radio_no;
	
	@FXML
	private ScrollPane pane_fields;
	
	@FXML
	private Button add_paese_button;
	
	
	public AddPaeseController() {
		field_sigla = new TextField();
		field_stato = new TextField();
		tg = new ToggleGroup();
		radio_si = new RadioButton("SI");
		radio_no = new RadioButton("NO");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		
		label_sigla = new Text("Sigla");
		pane.getChildren().add(label_sigla);
		pane.getChildren().add(field_sigla);
		
		Text label_stato = new Text("Stato");
		pane.getChildren().add(label_stato);
		pane.getChildren().add(field_stato);
		
		Text label_ue = new Text("UE");
		radio_si.setToggleGroup(tg);
		radio_si.setSelected(true);
		radio_no.setToggleGroup(tg);
		
		pane.getChildren().add(label_ue);
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		radio_si.setPadding(new Insets(0, 10, 0, 0));
		radio_no.setPadding(new Insets(0, 0, 0, 10));
		hbox.getChildren().addAll(radio_si, radio_no);
		pane.getChildren().add(hbox);
		
		for (Node n : pane.getChildren()) {
			n.getStyleClass().add("field");
		}
		pane_fields.setContent(pane);
	}
	
	private boolean validate() {
		
		if (field_sigla.getText().trim().equals("")) {
			label_sigla.getStyleClass().add("error_validation");
			return false;
		}
		/*if (field_stato.getText().trim().equals("")) {
			field_stato.getStyleClass().add("error_validation");
			return false;
		}
		RadioButton rb = (RadioButton)tg.getSelectedToggle();
		if (rb == null || rb.getText().trim().equals("")) { 
			return false;
        }*/
		
		return true;
	}

	@FXML
	private void addPaese() throws SQLException {
		boolean valid = validate();
		
		if (valid) {
			String sigla = field_sigla.getText();
			String stato = field_stato.getText();
			Boolean ue = null;
			RadioButton rb = (RadioButton)tg.getSelectedToggle();
			if (rb != null) { 
	            if (rb.getText() == "SI") ue = true;
	            else ue = false;
	        }
			
			PaesiDAO paesiDAO = new PaesiDAO();
			paesiDAO.addPaese(sigla, stato, ue);
			
			Stage stage = (Stage)add_paese_button.getScene().getWindow();
			stage.close();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla i valori dei campi!");
			alert.show();
		}
	}
}
