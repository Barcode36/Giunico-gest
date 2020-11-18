package application.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.AutoCompleteComboBox;
import application.Cliente;
import application.database.ClientiDAO;
import application.database.PaesiDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class UpdateClienteController implements Initializable {

	private int numero;
	
	@FXML
	private Button edit_cliente_button;
	
	@FXML
	private ScrollPane pane_fields;
	
	
	TextField field_numero;
	TextField field_nome;
	TextField field_alias;
	TextField field_indirizzo;
	TextField field_cap;
	ComboBox<String> field_citta;
	AutoCompleteComboBox<String> combo_citta;
	
	ComboBox<String> field_paese;
	AutoCompleteComboBox<String> combo_paese;
	
	TextField field_email;
	TextField field_destinazione;
	TextField field_indirizzodest;
	TextField field_capdest;
	ComboBox<String> field_cittadest;
	AutoCompleteComboBox<String> combo_cittadest;
	ComboBox<String> field_statodest;
	AutoCompleteComboBox<String> combo_statodest;
	
	public UpdateClienteController() {
		field_numero = new TextField();
		field_nome = new TextField();
		field_alias = new TextField();
		field_indirizzo = new TextField();
		field_cap = new TextField();
		field_citta = new ComboBox<String>();
		combo_citta = new AutoCompleteComboBox<>(field_citta);
		
		field_paese = new ComboBox<String>();
		combo_paese = new AutoCompleteComboBox<>(field_paese);
		
		field_email = new TextField();
		field_destinazione = new TextField();
		field_indirizzodest = new TextField();
		field_capdest = new TextField();
		field_cittadest = new ComboBox<String>();
		combo_cittadest = new AutoCompleteComboBox<>(field_cittadest);
		field_statodest = new ComboBox<String>();
		combo_statodest = new AutoCompleteComboBox<>(field_statodest);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		
		Text label_numero = new Text("Numero");
		field_numero.setDisable(true);
		pane.getChildren().add(label_numero);
		pane.getChildren().add(field_numero);
		
		Text label_nome = new Text("Nome");
		pane.getChildren().add(label_nome);
		pane.getChildren().add(field_nome);
		
		Text label_alias = new Text("Alias");
		pane.getChildren().add(label_alias);
		pane.getChildren().add(field_alias);
		
		Text label_indirizzo = new Text("Indirizzo");
		pane.getChildren().add(label_indirizzo);
		pane.getChildren().add(field_indirizzo);
		
		Text label_cap = new Text("CAP");
		pane.getChildren().add(label_cap);
		pane.getChildren().add(field_cap);
		
		Text label_citta = new Text("Città");
		try {
			ClientiDAO clientiDAO = new ClientiDAO();
			ObservableList<String> temp_list = clientiDAO.getAllCittaDestAlreadyInserted();
			field_cittadest.getItems().addAll(temp_list);
			//field_citta.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_citta);
		pane.getChildren().add(field_citta);
		
		Text label_paese = new Text("Paese");		
		try {
			PaesiDAO paesiDAO = new PaesiDAO();
			ObservableList<String> temp_list = paesiDAO.getAllSiglePaesi();
			field_paese.getItems().addAll(temp_list);
			//field_paese.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_paese);
		pane.getChildren().add(field_paese);
		
		Text label_email = new Text("Email");
		pane.getChildren().add(label_email);
		pane.getChildren().add(field_email);
		
		Text label_destinazione = new Text("Destinazione");
		pane.getChildren().add(label_destinazione);
		pane.getChildren().add(field_destinazione);
		
		Text label_indirizzodest = new Text("Indirizzo destinazione");
		pane.getChildren().add(label_indirizzodest);
		pane.getChildren().add(field_indirizzodest);
		
		Text label_capdest = new Text("CAP destinazione");
		pane.getChildren().add(label_capdest);
		pane.getChildren().add(field_capdest);
		
		Text label_cittadest = new Text("Città destinazione");
		try {
			ClientiDAO clientiDAO = new ClientiDAO();
			ObservableList<String> temp_list = clientiDAO.getAllCittaDestAlreadyInserted();
			field_cittadest.getItems().addAll(temp_list);
			//field_citta.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_cittadest);
		pane.getChildren().add(field_cittadest);
		
		Text label_statodest = new Text("Stato destinazione");
		try {
			PaesiDAO paesiDAO = new PaesiDAO();
			ObservableList<String> temp_list = paesiDAO.getAllStatoPaesi();
			field_statodest.getItems().addAll(temp_list);
			//field_statodest.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_statodest);
		pane.getChildren().add(field_statodest);
		
		for (Node n : pane.getChildren()) {
			n.getStyleClass().add("field");
		}
		pane_fields.setContent(pane);
	}

	public void populateFields(int numero, String nome, String alias, String indirizzo, String cap, String citta, String paese, String email, String destinazione, String indirizzodest, String capdest, String cittadest, String statodest) {
		this.numero = numero;
		field_numero.setText(numero + "");
		field_nome.setText(nome);
		field_alias.setText(alias);
		field_indirizzo.setText(indirizzo);
		field_cap.setText(cap);
		field_citta.setValue(citta);
		field_paese.setValue(paese);
		field_email.setText(email);
		field_destinazione.setText(destinazione);
		field_indirizzodest.setText(indirizzodest);
		field_capdest.setText(capdest);
		field_cittadest.setValue(cittadest);
		field_statodest.setValue(statodest);
	}
	
	private boolean validate() {
		
		/*if (field_nome.getText().trim().equals("")) {
			field_nome.getStyleClass().add("error_validation");
			return false;
		}
		if (field_alias.getText().trim().equals("")) {
			field_alias.getStyleClass().add("error_validation");
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
		if (field_email.getText().trim().equals("")) {
			field_email.getStyleClass().add("error_validation");
			return false;
		}
		if (field_destinazione.getText().trim().equals("")) {
			field_destinazione.getStyleClass().add("error_validation");
			return false;
		}
		if (field_indirizzodest.getText().trim().equals("")) {
			field_indirizzodest.getStyleClass().add("error_validation");
			return false;
		}
		if (field_capdest.getText().trim().equals("")) {
			field_capdest.getStyleClass().add("error_validation");
			return false;
		}
		if (field_cittadest.getText().trim().equals("")) {
			field_cittadest.getStyleClass().add("error_validation");
			return false;
		}
		if (field_statodest.getText().trim().equals("")) {
			field_statodest.getStyleClass().add("error_validation");
			return false;
		}*/
		
		return true;
	}
	
	@FXML
	private void updateCliente(ActionEvent event) throws SQLException {
		boolean valid = validate();
		
		if (valid) {
			String new_nome = field_nome.getText();
			String new_alias = field_alias.getText();
			String new_indirizzo = field_indirizzo.getText();
			String new_cap = field_cap.getText();
			String new_citta = field_citta.getValue();
			
			String new_paese = field_paese.getValue();
			
			String new_email = field_email.getText();
			String new_destinazione = field_destinazione.getText();
			String new_indirizzodest = field_indirizzodest.getText();
			String new_capdest = field_capdest.getText();
			String new_cittadest = field_cittadest.getValue();
			String new_statodest = field_statodest.getValue();
			
			Cliente c = new Cliente(numero, new_nome, new_alias, new_indirizzo, new_cap, new_citta, new_paese, new_email, new_destinazione, new_indirizzodest, new_capdest, new_cittadest, new_statodest);
			
			ClientiDAO clientiDAO = new ClientiDAO();
			clientiDAO.updateClienteById(c);
			
			Stage stage = (Stage)edit_cliente_button.getScene().getWindow();
			stage.close();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla i valori dei campi!");
			alert.show();
		}
	}
}
