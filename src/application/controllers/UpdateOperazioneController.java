package application.controllers;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import application.AutoCompleteComboBox;
import application.Operazione;
import application.database.ClientiDAO;
import application.database.OperazioniDAO;
import application.database.TrasportatoriDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class UpdateOperazioneController implements Initializable {

	Text label_data;
	Text label_datacarico;
	Text label_cliente;
	Text label_importofattura;
	Text label_importobonifico;
	Text label_numerocolli;
	Text label_pesolordo;
	
	Text nome_cliente;
	
	private Integer id;
	
	@FXML
	private Button edit_operazione_button;
	
	@FXML
	private ScrollPane pane_fields;
	
	TextField field_id;
	DatePicker field_data;
	
	ComboBox<Integer> field_cliente;
	AutoCompleteComboBox<Integer> combo_cliente;
	
	TextField field_numerofattura;
	TextField field_importofattura;
	TextField field_importobonifico;
	CheckBox check_esclusodocsdoganali;
	TextField field_esclusodocsdoganali;
	TextField field_note;
	TextField field_numerocolli;
	ComboBox<String> field_tipoimballo;
	TextField field_pesolordo;
	TextField field_notetrasportatore;
	
	ComboBox<String> field_trasportatore;
	AutoCompleteComboBox<String> combo_trasportatore;
	
	CheckBox check_stampacmr;
	DatePicker field_datacarico;
	
	ComboBox<String> field_speddoganale;
	AutoCompleteComboBox<String> combo_speddoganale;
	
	TextField field_mrn;
	ComboBox<String> field_informazioni;
	CheckBox check_ok;
	TextField field_nomedest;
	TextField field_indirizzodest;
	TextField field_capdest;
	TextField field_cittadest;
	TextField field_statodest;
	
	Text text_saldo;
	
	ZoneId defaultZoneId = ZoneId.of("Europe/Rome");
	
	DecimalFormat decimal_format;
	DecimalFormat decimal_format_inserted;
	
	public UpdateOperazioneController() {
		field_id = new TextField();
		field_data = new DatePicker();
		field_data.setConverter(new StringConverter<LocalDate>() {
		    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		    @Override
		    public String toString(LocalDate localDate) {
		        if (localDate==null) return "";
		        return dateTimeFormatter.format(localDate);
		    }

		    @Override
		    public LocalDate fromString(String dateString) {
		        if(dateString == null || dateString.trim().isEmpty()) {
		            return null;
		        }
		        return LocalDate.parse(dateString, dateTimeFormatter);
		    }
		});
		
		decimal_format = new DecimalFormat("#,##0.00");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		nome_cliente = new Text("");
		
		field_cliente = new ComboBox<Integer>();
		combo_cliente = new AutoCompleteComboBox<Integer>(field_cliente);
		
		field_numerofattura = new TextField();
		field_importofattura = new TextField();
		field_importobonifico = new TextField();
		check_esclusodocsdoganali = new CheckBox("Ecluso doc. doganali");
		field_note = new TextField();
		field_numerocolli = new TextField();
		field_tipoimballo = new ComboBox<String>();
		field_tipoimballo.getItems().addAll("Box", "Boxes", "Paletta", "Palette", "Pal+Box", "Pal+Boxes");
		field_pesolordo = new TextField();
		field_notetrasportatore = new TextField();
		
		field_trasportatore = new ComboBox<String>();
		combo_trasportatore = new AutoCompleteComboBox<>(field_trasportatore);
		
		check_stampacmr = new CheckBox("Stampa CMR");
		field_datacarico = new DatePicker();
		
		field_speddoganale = new ComboBox<String>();
		combo_speddoganale = new AutoCompleteComboBox<>(field_speddoganale);
		
		field_mrn = new TextField();
		field_informazioni = new ComboBox<String>();
		field_informazioni.getItems().addAll("In attesa visto uscire", "Nr. MRN estero", "Manca CMR");
		check_ok = new CheckBox("OK");
		field_nomedest = new TextField();
		field_indirizzodest = new TextField();
		field_capdest = new TextField();
		field_cittadest = new TextField();
		field_statodest = new TextField();
		
		EventHandler<ActionEvent> event_cliente = 
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				ClientiDAO clientiDAO = new ClientiDAO();
				
				String num_string = String.valueOf(field_cliente.getValue());
				if (!num_string.equals("null")) {
					int numero_cliente = Integer.parseInt(num_string);
					
					try {
						Map<String, String> dest_map = clientiDAO.getDefaultDestinazioneInfoByNumero(numero_cliente);
						
						nome_cliente.setText(dest_map.get("nome"));
						field_nomedest.setText(dest_map.get("destinazione"));
						field_indirizzodest.setText(dest_map.get("indirizzodest"));
						field_capdest.setText(dest_map.get("capdest"));
						field_cittadest.setText(dest_map.get("cittadest"));
						field_statodest.setText(dest_map.get("statodest"));
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			} 
		};
		field_cliente.setOnAction(event_cliente);
		
		
		EventHandler<ActionEvent> event_trasportatore = 
				new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e){
				TrasportatoriDAO trasportatoriDAO = new TrasportatoriDAO();
				
				String trasportatore = field_trasportatore.getValue();
				
				try {
					String note_trasportatore = trasportatoriDAO.getNoteByTrasportatore(trasportatore);
					
					field_notetrasportatore.setText(note_trasportatore);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} 
		};
		field_trasportatore.setOnAction(event_trasportatore);
		
		text_saldo = new Text();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		text_saldo.getStyleClass().add("text_saldo");
		
		final VBox pane = new VBox();
		pane.setAlignment(Pos.CENTER);
		
		Text label_id = new Text("ID");
		field_id.setDisable(true);
		pane.getChildren().add(label_id);
		pane.getChildren().add(field_id);
		
		label_data = new Text("Data operazione");
		pane.getChildren().add(label_data);
		pane.getChildren().add(field_data);
		
		label_cliente = new Text("Cliente");
		try {
			ClientiDAO clientiDAO = new ClientiDAO();
			ObservableList<Integer> temp_list = clientiDAO.getAllNumeroClienti();
			field_cliente.getItems().addAll(temp_list);
			field_cliente.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_cliente);
		pane.getChildren().add(field_cliente);
		
		nome_cliente.setDisable(true);
		pane.getChildren().add(nome_cliente);
		
		Text label_numerofattura = new Text("Numero Fattura");
		pane.getChildren().add(label_numerofattura);
		pane.getChildren().add(field_numerofattura);
		
		label_importofattura = new Text("Importo fattura");
		field_importofattura.setText("0,00");
		field_importofattura.setOnKeyPressed(this::insertedImportoFattura);
		pane.getChildren().add(label_importofattura);
		pane.getChildren().add(field_importofattura);
		
		//Text label_esclusodocsdoganali = new Text("Escluso doc. doganali");
		//pane.getChildren().add(label_esclusodocsdoganali);
		pane.getChildren().add(check_esclusodocsdoganali);
		
		label_importobonifico = new Text("Importo bonifico");
		field_importobonifico.setText("0,00");
		field_importobonifico.setOnKeyPressed(this::insertedImportoBonifico);
		pane.getChildren().add(label_importobonifico);
		pane.getChildren().add(field_importobonifico);
		
		Text label_note = new Text("Note operazione");
		pane.getChildren().add(label_note);
		pane.getChildren().add(field_note);
		
		// Riepilogo del saldo
		pane.getChildren().add(text_saldo);
		
		// START DATA FOR CMR
		Separator separator = new Separator(Orientation.HORIZONTAL);
		separator.setPrefWidth(400);
		pane.getChildren().add(separator);
		Text title_cmr = new Text("COMPILAZIONE DATI CMR");
		title_cmr.getStyleClass().add("title_separator");
		pane.getChildren().add(title_cmr);
		
		Text label_nomedest = new Text("Nome destinazione");
		pane.getChildren().add(label_nomedest);
		pane.getChildren().add(field_nomedest);
		
		Text label_indirizzodest = new Text("Indirizzo destinazione");
		pane.getChildren().add(label_indirizzodest);
		pane.getChildren().add(field_indirizzodest);
		
		Text label_capdest = new Text("CAP destinazione");
		pane.getChildren().add(label_capdest);
		pane.getChildren().add(field_capdest);
		
		Text label_cittadest = new Text("Città destinazione");
		pane.getChildren().add(label_cittadest);
		pane.getChildren().add(field_cittadest);
		
		Text label_statodest = new Text("Stato destinazione");
		pane.getChildren().add(label_statodest);
		pane.getChildren().add(field_statodest);
		
		label_numerocolli = new Text("Numero colli");
		pane.getChildren().add(label_numerocolli);
		field_numerocolli.setText("0");
		pane.getChildren().add(field_numerocolli);
		
		Text label_tipoimballo = new Text("Tipo imballo");
		pane.getChildren().add(label_tipoimballo);
		pane.getChildren().add(field_tipoimballo);
		
		label_pesolordo = new Text("Peso lordo (in kg)");
		pane.getChildren().add(label_pesolordo);
		field_pesolordo.setText("0.0");
		pane.getChildren().add(field_pesolordo);
		
		Text label_trasportatore = new Text("Trasportatore");
		try {
			TrasportatoriDAO trasportatoriDAO = new TrasportatoriDAO();
			ObservableList<String> temp_list = trasportatoriDAO.getAllTrasportatoriTrasportatori();
			field_trasportatore.getItems().addAll(temp_list);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_trasportatore);
		pane.getChildren().add(field_trasportatore);
		
		Text label_notetrasportatore = new Text("Note trasportatore");
		pane.getChildren().add(label_notetrasportatore);
		pane.getChildren().add(field_notetrasportatore);
		
		label_datacarico = new Text("Data carico");
		pane.getChildren().add(label_datacarico);
		pane.getChildren().add(field_datacarico);
		
		// Campi opzionali
		Text label_speddoganale = new Text("Spedizione doganale");
		try {
			OperazioniDAO operazioniDAO = new OperazioniDAO();
			ObservableList<String> temp_list = operazioniDAO.getAllSpedDoganali();
			field_speddoganale.getItems().addAll(temp_list);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		pane.getChildren().add(label_speddoganale);
		pane.getChildren().add(field_speddoganale);
		
		Text label_mrn = new Text("MRN");
		pane.getChildren().add(label_mrn);
		pane.getChildren().add(field_mrn);
		
		Text label_informazioni = new Text("Informazioni");
		pane.getChildren().add(label_informazioni);
		pane.getChildren().add(field_informazioni);
		
		//Text label_stampacmr = new Text("Stampa CMR");
		//pane.getChildren().add(label_stampacmr);
		pane.getChildren().add(check_stampacmr);
		
		//Text label_ok = new Text("OK");
		//pane.getChildren().add(label_ok);
		pane.getChildren().add(check_ok);
		
		for (Node n : pane.getChildren()) {
			n.getStyleClass().add("field");
		}
		pane_fields.setContent(pane);
	}
	
	private void insertedImportoFattura(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			try {
				String str_numero_cliente = String.valueOf(field_cliente.getValue());
				int new_cliente = Integer.parseInt(str_numero_cliente);
				
				ClientiDAO clientiDAO = new ClientiDAO();
				BigDecimal temp_saldo = clientiDAO.getSaldoCliente(new_cliente);
				try {
					BigDecimal x = new BigDecimal(field_importofattura.getText().trim().replaceAll(",", "."));
					temp_saldo = temp_saldo.subtract(x);
					text_saldo.setText("Saldo: " + decimal_format.format(temp_saldo));
				}
				catch (Exception ex) {
					text_saldo.setText("Saldo: " + decimal_format.format(clientiDAO.getSaldoCliente(new_cliente)));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void insertedImportoBonifico(KeyEvent event) {
		if (event.getCode() == KeyCode.TAB) {
			try {
				String str_numero_cliente = String.valueOf(field_cliente.getValue());
				int new_cliente = Integer.parseInt(str_numero_cliente);
				
				ClientiDAO clientiDAO = new ClientiDAO();
				BigDecimal temp_saldo = clientiDAO.getSaldoCliente(new_cliente);
				try {
					BigDecimal x = new BigDecimal(field_importobonifico.getText().trim().replaceAll(",", "."));
					temp_saldo = temp_saldo.add(x);
					text_saldo.setText("Saldo: " + decimal_format.format(temp_saldo));
				}
				catch (Exception ex) {
					text_saldo.setText("Saldo: " + decimal_format.format(clientiDAO.getSaldoCliente(new_cliente)));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void populateFields(int id, Date data, Integer cliente, String numerofattura, BigDecimal importofattura, BigDecimal importobonifico, boolean esclusodocsdoganali, String note, int numerocolli, String tipoimballo, float pesolordo, String notetrasportatore, String trasportatore, boolean stampacmr, Date datacarico, String speddoganale, String mrn, String informazioni, boolean ok, String nomedest, String indirizzodest, String capdest, String cittadest, String statodest) {
		this.id = id;
		field_id.setText(id + "");
		
		Date safeDate = new Date(data.getTime());
		field_data.setValue(safeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		field_cliente.setValue(cliente);
		field_numerofattura.setText(numerofattura);
		
		String temp_importofattura = decimal_format.format(importofattura);
		temp_importofattura = temp_importofattura.replaceAll("\\.", "");
		field_importofattura.setText(temp_importofattura);
		
		String temp_importobonifico = decimal_format.format(importobonifico);
		temp_importobonifico = temp_importobonifico.replaceAll("\\.", "");
		field_importobonifico.setText(temp_importobonifico);
		
		if (esclusodocsdoganali) check_esclusodocsdoganali.setSelected(true);
		else check_esclusodocsdoganali.setSelected(false);
		field_note.setText(note);
		field_numerocolli.setText(numerocolli + "");
		field_tipoimballo.setValue(tipoimballo);
		field_pesolordo.setText(pesolordo + "");
		field_notetrasportatore.setText(notetrasportatore);
		field_trasportatore.setValue(trasportatore);
		if (stampacmr) check_stampacmr.setSelected(true);
		else check_stampacmr.setSelected(false);
		
		Date safeDateCarico = new Date(datacarico.getTime());
		field_datacarico.setValue(safeDateCarico.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		field_speddoganale.setValue(speddoganale);
		field_mrn.setText(mrn);
		field_informazioni.setValue(informazioni);
		if (ok) check_ok.setSelected(true);
		else check_ok.setSelected(false);
		field_nomedest.setText(nomedest);
		field_indirizzodest.setText(indirizzodest);
		field_capdest.setText(capdest);
		field_cittadest.setText(cittadest);
		field_statodest.setText(statodest);
		
		try {
			String str_numero_cliente = String.valueOf(field_cliente.getValue());
			int new_cliente = Integer.parseInt(str_numero_cliente);
			
			ClientiDAO clientiDAO = new ClientiDAO();
			BigDecimal temp_saldo = clientiDAO.getSaldoCliente(new_cliente);
			text_saldo.setText("Saldo: " + decimal_format.format(temp_saldo));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean validate() {
		
		String str_numero_cliente = String.valueOf(field_cliente.getValue());
		if (str_numero_cliente.equals("null")) {
			label_cliente.getStyleClass().add("error_validation");
			return false;
		}
		if (field_data.getValue() == null) {
			label_data.getStyleClass().add("error_validation");
			return false;
		}
		/*if (field_data.getPromptText() == null) {
			field_data.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_numerofattura.getText().equals("")) {
			field_numerofattura.getStyleClass().add("error_validation");
			return false;
		}*/
		try {
			new BigDecimal(field_importofattura.getText().trim().replaceAll(",", "."));
		}
		catch (Exception e) {
			label_importofattura.getStyleClass().add("error_validation");
			return false;
		}
		try {
			new BigDecimal(field_importobonifico.getText().trim().replaceAll(",", "."));
		}
		catch (Exception e) {
			label_importobonifico.getStyleClass().add("error_validation");
			return false;
		}
		try {
			Integer.parseInt(field_numerocolli.getText().trim());
		}
		catch (Exception e) {
			label_numerocolli.getStyleClass().add("error_validation");
			return false;
		}
		/*if (field_tipoimballo.getText().equals("")) {
			field_tipoimballo.getStyleClass().add("error_validation");
			return false;
		}*/
		try {
			Float.parseFloat(field_pesolordo.getText().trim());
		}
		catch (Exception e) {
			label_pesolordo.getStyleClass().add("error_validation");
			return false;
		}
		/*if (field_trasportatore.getValue().equals("")) {
			field_trasportatore.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_datacarico.getPromptText() == null) {
			field_datacarico.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_speddoganale.getText().equals("")) {
			field_speddoganale.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_mrn.getText().equals("")) {
			field_mrn.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_nomedest.getText().equals("")) {
			field_nomedest.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_indirizzodest.getText().equals("")) {
			field_indirizzodest.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_capdest.getText().equals("")) {
			field_capdest.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_cittadest.getText().equals("")) {
			field_cittadest.getStyleClass().add("error_validation");
			return false;
		}*/
		/*if (field_statodest.getText().equals("")) {
			field_statodest.getStyleClass().add("error_validation");
			return false;
		}*/
		
		return true;
	}
	
	@FXML
	private void updateOperazione(ActionEvent event) throws SQLException {
		
		boolean valid = validate();
		
		if (valid) {
			Date new_data = new Date();
			if (field_data.getValue() != null) {
				LocalDate temp_data = field_data.getValue();
				new_data = Date.from(temp_data.atStartOfDay(defaultZoneId).plusDays(1).toInstant());
			}
			
			String str_numero_cliente = String.valueOf(field_cliente.getValue());
			int new_cliente = Integer.parseInt(str_numero_cliente);
			
			String new_numerofattura = field_numerofattura.getText();
			BigDecimal new_importofattura = new BigDecimal(field_importofattura.getText().trim().replaceAll(",", "."));
			BigDecimal new_importobonifico = new BigDecimal(field_importobonifico.getText().trim().replaceAll(",", "."));
			
			Boolean new_esclusodocsdoganali = null;
			if (check_esclusodocsdoganali.isSelected()) new_esclusodocsdoganali = true;
			else new_esclusodocsdoganali = false;
			
			String new_note = field_note.getText();
			int new_numerocolli = Integer.parseInt(field_numerocolli.getText());
			String new_tipoimballo = String.valueOf(field_tipoimballo.getValue());
			float new_pesolordo = Float.parseFloat(field_pesolordo.getText());
			String new_notetrasportatore = field_notetrasportatore.getText();
			String new_trasportatore = field_trasportatore.getValue();
			
			Boolean new_stampacmr = null;
			if (check_stampacmr.isSelected()) new_stampacmr = true;
			else new_stampacmr = false;
			
			Date new_datacarico = new Date();
			if (field_datacarico.getValue() != null) {
				LocalDate temp_datacarico = field_datacarico.getValue();
				new_datacarico = Date.from(temp_datacarico.atStartOfDay(defaultZoneId).plusDays(1).toInstant());
			}
			
			String new_speddoganale = field_speddoganale.getValue();
			String new_mrn = field_mrn.getText();
			String new_informazioni = String.valueOf(field_informazioni.getValue());
			
			Boolean new_ok = null;
			if (check_ok.isSelected()) new_ok = true;
			else new_ok = false;
			
			String new_nomedest = field_nomedest.getText();
			String new_indirizzodest = field_indirizzodest.getText();
			String new_capdest = field_capdest.getText();
			String new_cittadest = field_cittadest.getText();
			String new_statodest = field_statodest.getText();
			
			
			Operazione o = new Operazione(id, new_data, new_cliente, new_numerofattura, new_importofattura, new_importobonifico, new_esclusodocsdoganali, new_note, new_numerocolli, new_tipoimballo, new_pesolordo, new_notetrasportatore, new_trasportatore, new_stampacmr, new_datacarico, new_speddoganale, new_mrn, new_informazioni, new_ok, new_nomedest, new_indirizzodest, new_capdest, new_cittadest, new_statodest);
			
			OperazioniDAO operazioniDAO = new OperazioniDAO();
			operazioniDAO.updateOperazioneById(o);
			
			Stage stage = (Stage)edit_operazione_button.getScene().getWindow();
			stage.close();
		}
		else {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla i valori dei campi!");
			alert.show();
		}
	}
}
