package application.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.AutoCompleteComboBox;
import application.Trasportatore;
import application.database.PaesiDAO;
import application.database.TrasportatoriDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdateTrasportatoreController implements Initializable {

	private String trasportatore;
	
	@FXML
	private Button edit_trasportatore_button;
	
	@FXML
	private ScrollPane pane_fields;
	
	
	TextField field_trasportatore;
	TextField field_nome;
	TextField field_indirizzo;
	TextField field_cap;
	TextField field_citta;
	
	ComboBox<String> field_paese;
	AutoCompleteComboBox<String> combo_paese;
	
	TextField field_stato;
	TextField field_partitaiva;
	TextField field_iscrizionealbo;
	TextField field_mail1ritiro;
	TextField field_mail2ritiro;
	TextField field_mail1docs;
	TextField field_mail2docs;
	TextField field_note;
	
	
	public UpdateTrasportatoreController() {
		field_trasportatore = new TextField();
		field_nome = new TextField();
		field_indirizzo = new TextField();
		field_cap = new TextField();
		field_citta = new TextField();
		
		field_paese = new ComboBox<String>();
		combo_paese = new AutoCompleteComboBox<>(field_paese);
		
		field_stato = new TextField();
		field_partitaiva = new TextField();
		field_iscrizionealbo = new TextField();
		field_mail1ritiro = new TextField();
		field_mail2ritiro = new TextField();
		field_mail1docs = new TextField();
		field_mail2docs = new TextField();
		field_note = new TextField();
		
		EventHandler<ActionEvent> event_paese = 
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				PaesiDAO paesiDAO = new PaesiDAO();
				String sigla = field_paese.getValue();
				try {
					field_stato.setText(paesiDAO.getStatoBySigla(sigla));
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} 
		};
		field_paese.setOnAction(event_paese);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		
		Text label_trasportatore = new Text("Trasportatore");
		field_trasportatore.setDisable(true);
		pane.getChildren().add(label_trasportatore);
		pane.getChildren().add(field_trasportatore);
		
		Text label_nome = new Text("Nome");
		pane.getChildren().add(label_nome);
		pane.getChildren().add(field_nome);
		
		Text label_indirizzo = new Text("Indirizzo");
		pane.getChildren().add(label_indirizzo);
		pane.getChildren().add(field_indirizzo);
		
		Text label_cap = new Text("CAP");
		pane.getChildren().add(label_cap);
		pane.getChildren().add(field_cap);
		
		Text label_citta = new Text("Città");
		pane.getChildren().add(label_citta);
		pane.getChildren().add(field_citta);
		
		Text label_paese = new Text("Paese");
		try {
			PaesiDAO paesiDAO = new PaesiDAO();
			ObservableList<String> temp_list = paesiDAO.getAllSiglePaesi();
			field_paese.getItems().addAll(temp_list);
			field_paese.setValue(temp_list.get(0));
			field_stato.setText(paesiDAO.getStatoBySigla(temp_list.get(0)));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_paese);
		pane.getChildren().add(field_paese);
		
		Text label_stato = new Text("Stato");
		pane.getChildren().add(label_stato);
		pane.getChildren().add(field_stato);
		
		Text label_partitaiva = new Text("Partita IVA");
		pane.getChildren().add(label_partitaiva);
		pane.getChildren().add(field_partitaiva);
		
		Text label_iscrizionealbo = new Text("Iscrizione Albo");
		pane.getChildren().add(label_iscrizionealbo);
		pane.getChildren().add(field_iscrizionealbo);
		
		Text label_mail1ritiro = new Text("Mail ritiro (1)");
		pane.getChildren().add(label_mail1ritiro);
		pane.getChildren().add(field_mail1ritiro);
		
		Text label_mail2ritiro = new Text("Mail ritiro (2)");
		pane.getChildren().add(label_mail2ritiro);
		pane.getChildren().add(field_mail2ritiro);
		
		Text label_mail1docs = new Text("Mail documenti (1)");
		pane.getChildren().add(label_mail1docs);
		pane.getChildren().add(field_mail1docs);
		
		Text label_mail2docs = new Text("Mail documenti (2)");
		pane.getChildren().add(label_mail2docs);
		pane.getChildren().add(field_mail2docs);
		
		Text label_note = new Text("Note");
		pane.getChildren().add(label_note);
		pane.getChildren().add(field_note);
		
		for (Node n : pane.getChildren()) {
			n.getStyleClass().add("field");
		}
		pane_fields.setContent(pane);
	}

	public void populateFields(String trasportatore, String nome, String indirizzo, String cap, String citta, String paese, String stato, String partitaiva, String iscrizionealbo, String mail1ritiro, String mail2ritiro, String mail1docs, String mail2docs, String note) {
		this.trasportatore = trasportatore;
		field_trasportatore.setText(trasportatore);
		field_nome.setText(nome);
		field_indirizzo.setText(indirizzo);
		field_cap.setText(cap);
		field_citta.setText(citta);
		field_paese.setValue(paese);
		field_stato.setText(stato);
		field_partitaiva.setText(partitaiva);
		field_iscrizionealbo.setText(iscrizionealbo);
		field_mail1ritiro.setText(mail1ritiro);
		field_mail2ritiro.setText(mail2ritiro);
		field_mail1docs.setText(mail1docs);
		field_mail2docs.setText(mail2docs);
		field_note.setText(note);
	}
	
	private boolean validate() {

		/*if (field_nome.getText().trim().equals("")) {
			field_nome.getStyleClass().add("error_validation");
			return false;
		}
		if (field_indirizzo.getText().trim().equals("")) {
			field_indirizzo.getStyleClass().add("error_validation");
			return false;
		}
		if (field_cap.getText().trim().equals("")) {
			field_cap.getStyleClass().add("error_validation");
			return false;
		}
		if (field_citta.getText().trim().equals("")) {
			field_citta.getStyleClass().add("error_validation");
			return false;
		}
		if (field_paese.getValue().trim().equals("")) {
			field_paese.getStyleClass().add("error_validation");
			return false;
		}
		if (field_stato.getText().trim().equals("")) {
			field_stato.getStyleClass().add("error_validation");
			return false;
		}
		if (field_partitaiva.getText().trim().equals("")) {
			field_partitaiva.getStyleClass().add("error_validation");
			return false;
		}
		if (field_mail1ritiro.getText().trim().equals("")) {
			field_mail1ritiro.getStyleClass().add("error_validation");
			return false;
		}
		if (field_mail1docs.getText().trim().equals("")) {
			field_mail1docs.getStyleClass().add("error_validation");
			return false;
		}*/
		
		return true;
	}
	
	@FXML
	private void updateTrasportatore(ActionEvent event) throws SQLException {
		
		boolean valid = validate();
		
		if (valid) {
			String new_nome = field_nome.getText();
			String new_indirizzo = field_indirizzo.getText();
			String new_cap = field_cap.getText();
			String new_citta = field_citta.getText();
			String new_paese = field_paese.getValue();
			String new_stato = field_stato.getText();
			String new_partitaiva = field_partitaiva.getText();
			String new_iscrizionealbo = field_iscrizionealbo.getText();
			String new_mail1ritiro = field_mail1ritiro.getText();
			String new_mail2ritiro = field_mail2ritiro.getText();
			String new_mail1docs = field_mail1docs.getText();
			String new_mail2docs = field_mail2docs.getText();
			String new_note = field_note.getText();
			
			Trasportatore t = new Trasportatore(trasportatore, new_nome, new_indirizzo, new_cap, new_citta, new_paese, new_stato, new_partitaiva, new_iscrizionealbo, new_mail1ritiro, new_mail2ritiro, new_mail1docs, new_mail2docs, new_note);
			
			TrasportatoriDAO trasportatoriDAO = new TrasportatoriDAO();
			trasportatoriDAO.updateTrasportatoreByTrasportatore(t);
			
			Stage stage = (Stage)edit_trasportatore_button.getScene().getWindow();
			stage.close();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla i valori dei campi!");
			alert.show();
		}
	}
}
