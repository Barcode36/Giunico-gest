package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import application.ClienteSaldo;
import application.PdfCreator;
import application.database.ClientiDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class VisualizzaClientiController implements Initializable {

	double width = 0;
    double height = 0;
    
    @FXML
	private Button exit_button;
    
    @FXML
    private DatePicker field_data_iniziale;
    
    ToggleGroup tg;
    @FXML
    private RadioButton radio_totale;
    @FXML
    private RadioButton radio_da_pagare;
    @FXML
    private RadioButton radio_anticipati;
    @FXML
    private CheckBox check_ue;
    
    @FXML
    private ScrollPane scroll_pane;
    
    @FXML
    private Text riepilogo_saldo;
    
    @FXML
	private Button generate_pdf_button;
    
    TableView<ClienteSaldo> table_clienti;
    
    ClientiDAO clientiDAO;
    DecimalFormat decimal_format;
    
    ZoneId defaultZoneId = ZoneId.of("Europe/Rome");
    
	public VisualizzaClientiController() {
		clientiDAO = new ClientiDAO();
		decimal_format = new DecimalFormat("#,##0.00€");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = resolution.getWidth();
		this.height = resolution.getHeight();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		table_clienti = new TableView<ClienteSaldo>();
		table_clienti.setPlaceholder(new Text("Nessun dato da visualizzare"));
		createTable("pag", false, true);
		
		scroll_pane.setPrefWidth(width);
		
		tg = new ToggleGroup();
		radio_totale.setToggleGroup(tg);
		radio_da_pagare.setToggleGroup(tg);
		radio_anticipati.setToggleGroup(tg);
		
		radio_totale.setOnAction(this::changeTable);
		radio_da_pagare.setOnAction(this::changeTable);
		radio_anticipati.setOnAction(this::changeTable);
		check_ue.setOnAction(this::changeTable);
		
		generate_pdf_button.setOnAction(this::generatePdfVisualizzaClienti);
		
		field_data_iniziale.setConverter(new StringConverter<LocalDate>() {
		    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
		field_data_iniziale.setPromptText("Data iniziale");
		field_data_iniziale.getStyleClass().add("field");
		field_data_iniziale.setOnAction(this::changeTable);
	}
	
	private void changeTable(ActionEvent event) {
		table_clienti.getItems().clear();
		table_clienti.setPlaceholder(new Text("Nessun dato da visualizzare"));
		table_clienti.refresh();
		
		if (radio_totale.isSelected()) {
			radio_da_pagare.setSelected(false);
			radio_anticipati.setSelected(false);
			check_ue.setSelected(false);
			check_ue.setDisable(true);
			
			createTable("tot", false, false);
		}
		else if (radio_da_pagare.isSelected()){
			radio_totale.setSelected(false);
			radio_anticipati.setSelected(false);
			check_ue.setDisable(false);
			
			if (check_ue.isSelected()) {
				createTable("pag", true, false);
			}
			else {
				createTable("pag", false, false);
			}
		}
		else if (radio_anticipati.isSelected()){
			radio_totale.setSelected(false);
			radio_da_pagare.setSelected(false);
			check_ue.setDisable(false);
			
			if (check_ue.isSelected()) {
				createTable("ant", true, false);
			}
			else {
				createTable("ant", false, false);
			}
		}
	}
	
	private BigDecimal loadDataClienti(String type, boolean ue) throws SQLException {
		ObservableList<ClienteSaldo> list_clienti_saldo;
		
		Date data_iniziale = null;
		if (field_data_iniziale.getValue() != null) {
			LocalDate temp_data_iniziale = field_data_iniziale.getValue();
			data_iniziale = Date.from(temp_data_iniziale.atStartOfDay(defaultZoneId).toInstant());
		}
		
		if (type.equals("tot")) {
			if (data_iniziale != null) list_clienti_saldo = clientiDAO.getAllClientiWithSaldoDal(data_iniziale);
			else list_clienti_saldo = clientiDAO.getAllClientiWithSaldo();
		}
		else {
			if (data_iniziale != null) {
				if (ue == true) list_clienti_saldo = clientiDAO.getAllClientiWithSaldoDal(ue, data_iniziale);
				else list_clienti_saldo = clientiDAO.getAllClientiWithSaldoDal(data_iniziale);
			}
			else {
				if (ue == true) list_clienti_saldo = clientiDAO.getAllClientiWithSaldo(ue);
				else list_clienti_saldo = clientiDAO.getAllClientiWithSaldo();
			}
			
			ArrayList<ClienteSaldo> list_temp = new ArrayList<ClienteSaldo>();
			for (ClienteSaldo c : list_clienti_saldo) {
				list_temp.add(c);
			}
			
			if (type.equals("pag")) {
				if (!list_temp.isEmpty()) {
					for (int i=0; i<list_temp.size(); i++) {
						ClienteSaldo cs = list_temp.get(i);
						
						// list_clienti_saldo.contains(cs) && 
						if (cs.getSaldo().signum() >= 0) {
							list_clienti_saldo.remove(cs);
						}
					}
				}
			}
			else if (type.equals("ant")){
				if (!list_temp.isEmpty()) {
					for (int i=0; i<list_temp.size(); i++) {
						ClienteSaldo cs = list_temp.get(i);
						
						// list_clienti_saldo.contains(cs) && 
						if (cs.getSaldo().signum() < 0) {
							list_clienti_saldo.remove(cs);
						}
					}
				}
			}
		}
		
		BigDecimal saldo_tot = BigDecimal.ZERO;
		for (int i=0; i<list_clienti_saldo.size(); i++) {
			saldo_tot = saldo_tot.add(list_clienti_saldo.get(i).getSaldo());
		}
		
		table_clienti.setItems(list_clienti_saldo);
		table_clienti.refresh();
		
		return saldo_tot;
	}
	
	@SuppressWarnings("unchecked")
	private void createTable(String type, boolean ue, boolean first_time) {
		table_clienti = new TableView<ClienteSaldo>();
		table_clienti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table_clienti.getStyleClass().add("table_view");
		
		TableColumn<ClienteSaldo, Integer> col_cliente_numero = new TableColumn<ClienteSaldo, Integer>("NUMERO");
		TableColumn<ClienteSaldo, String> col_cliente_nome = new TableColumn<ClienteSaldo, String>("NOME");
		TableColumn<ClienteSaldo, String> col_cliente_alias = new TableColumn<ClienteSaldo, String>("ALIAS");
		TableColumn<ClienteSaldo, String> col_cliente_citta = new TableColumn<ClienteSaldo, String>("CITTA'");
		TableColumn<ClienteSaldo, String> col_cliente_paese = new TableColumn<ClienteSaldo, String>("PAESE");
		TableColumn<ClienteSaldo, BigDecimal> col_saldo_cliente = new TableColumn<ClienteSaldo, BigDecimal>("SALDO");
		
		col_cliente_numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		col_cliente_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		col_cliente_alias.setCellValueFactory(new PropertyValueFactory<>("alias"));
		col_cliente_citta.setCellValueFactory(new PropertyValueFactory<>("citta"));
		col_cliente_paese.setCellValueFactory(new PropertyValueFactory<>("paese"));
		col_saldo_cliente.setCellValueFactory(new PropertyValueFactory<>("saldo"));
		
		BigDecimal saldo = BigDecimal.ZERO;
		try {
			saldo = loadDataClienti(type, ue);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		col_saldo_cliente.setCellFactory(col -> new TableCell<ClienteSaldo, BigDecimal>() {
	        @Override
	        protected void updateItem(BigDecimal item, boolean empty) {
	            super.updateItem(item, empty);
	            if(empty) {
	                setText(null);
	            }
	            else {
	                setText(decimal_format.format(item));
	            }
	        }
        });        
		
		table_clienti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_clienti.getColumns().addAll(col_cliente_numero, col_cliente_nome, col_cliente_alias, col_cliente_citta, col_cliente_paese, col_saldo_cliente);
		scroll_pane.setContent(table_clienti);
		
		if (type.equals("pag")) {
			riepilogo_saldo.setText("Saldo clienti da pagare: " + decimal_format.format(saldo));
		}
		else if (type.equals("ant")) {
			riepilogo_saldo.setText("Saldo clienti anticipato: " + decimal_format.format(saldo));
		}
		else {
			riepilogo_saldo.setText("");
		}
	}
	
	private void generatePdfVisualizzaClienti(ActionEvent event) {
		String pdf_name = "";
		String title = "";
		if (radio_totale.isSelected()) {
			pdf_name = "totale_clienti";
			title = "TOTALE CLIENTI";
		}
		else if (radio_da_pagare.isSelected()){
			pdf_name = "clienti_da_pagare";
			title = "CLIENTI DA PAGARE";
			if (check_ue.isSelected()) {
				pdf_name += "_ue";
				title += " (UE)";
			}
		}
		else if (radio_anticipati.isSelected()){
			pdf_name = "clienti_anticipati";
			title = "CLIENTI ANTICIPATI";
			if (check_ue.isSelected()) {
				pdf_name += "_ue";
				title += " (UE)";
			}
		}
		pdf_name += ".pdf";
		
		// iText code
		// https://www.vogella.com/tutorials/JavaPDF/article.html
		try {
			DirectoryChooser directoryChooser = new DirectoryChooser();
        	directoryChooser.setTitle("Dove vuoi salvare il documento?");
        	
        	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
        	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        	directoryChooser.setInitialDirectory(new File(currentPath));
        	Stage stage = (Stage) scroll_pane.getScene().getWindow();
        	File selectedDirectory = directoryChooser.showDialog(stage);
        	
			PdfCreator pdfCreator = new PdfCreator(selectedDirectory + "/" + pdf_name, true);
			Document document = pdfCreator.getDocument();
			
			/*** PDF HEADER ***/
			Paragraph header = new Paragraph(title, pdfCreator.getPoppinsFont(30, true, BaseColor.BLACK));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			
			/*** PDF BODY ***/
			Paragraph body = new Paragraph();
			
			PdfPTable table = new PdfPTable(6);	// parameter is the number of columns
			int[] columnWidths = new int[]{130, 250, 200, 150, 100, 150};
            table.setWidths(columnWidths);
			table.setHeaderRows(1);
			table.setWidthPercentage(100);
			
			Phrase numero = new Phrase("NUMERO", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_numero = new PdfPCell(numero);
			cell_numero =  pdfCreator.formatHeaderCell(cell_numero);
			
			Phrase nome = new Phrase("NOME", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_nome = new PdfPCell(nome);
			cell_nome =  pdfCreator.formatHeaderCell(cell_nome);
			
			Phrase alias = new Phrase("ALIAS", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_alias = new PdfPCell(alias);	
			cell_alias =  pdfCreator.formatHeaderCell(cell_alias);
			
			Phrase citta = new Phrase("CITTA'", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_citta = new PdfPCell(citta);
			cell_citta =  pdfCreator.formatHeaderCell(cell_citta);
			
			Phrase paese = new Phrase("PAESE", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_paese = new PdfPCell(paese);
			cell_paese =  pdfCreator.formatHeaderCell(cell_paese);
			
			Phrase saldo = new Phrase("SALDO", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_saldo = new PdfPCell(saldo);
			cell_saldo =  pdfCreator.formatHeaderCell(cell_saldo);
			
			table.addCell(cell_numero);
			table.addCell(cell_nome);
			table.addCell(cell_alias);
			table.addCell(cell_citta);
			table.addCell(cell_paese);
			table.addCell(cell_saldo);
			
			boolean background_color = false;
			for(int i=0; i<table_clienti.getItems().size(); i++){
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getNumero() + "", PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getNome(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getAlias(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getCitta(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getPaese(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(decimal_format.format(table_clienti.getItems().get(i).getSaldo()), PdfPCell.ALIGN_RIGHT, false, background_color));
		        background_color = !background_color;
		    }
			body.add(table);
			
			String text = riepilogo_saldo.getText();
			
			Paragraph riepilogo = new Paragraph(text, pdfCreator.getPoppinsFont(16, true, BaseColor.BLACK));
			riepilogo.setAlignment(Paragraph.ALIGN_CENTER);
			
			/*** PDF FOOTER ***/
			// generated when onEndPage event occur
			
			document.add(header);
			document.add(riepilogo);
			document.add(pdfCreator.emptyLines(1));
			document.add(body);
	        document.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void exit() {
        
		try {
			Stage stage = (Stage) exit_button.getScene().getWindow();
		
			FXMLLoader loader = new FXMLLoader();
			URL fxmlUrl = getClass().getResource("/resources/fxml/main.fxml");
			//URL fxmlUrl = getClass().getResource("/resources/fxml/main_reduced.fxml");
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, width, height, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/main.css").toExternalForm());
			
			stage.setResizable(true);
			//stage.setMaximized(true);
			stage.setScene(scene);
			//stage.setFullScreen(true);
			stage.show();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
