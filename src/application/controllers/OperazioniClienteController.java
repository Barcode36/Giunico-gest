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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import application.AutoCompleteComboBox;
import application.ClienteSaldoDettaglio;
import application.PdfCreator;
import application.database.ClientiDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class OperazioniClienteController implements Initializable {

	double width = 0;
	double height = 0;
	
    @FXML
    private HBox hbox_1;
    
    @FXML
	private Button exit_button;
    
    ComboBox<Integer> field_cliente;
	AutoCompleteComboBox<Integer> combo_cliente;
	
	@FXML
	DatePicker field_data_iniziale;
	@FXML
	DatePicker field_data_finale;
	
	@FXML
	Text info_cliente;
	
	@FXML
	Text riepilogo_fatture;
	@FXML
	Text riepilogo_bonifici;
	@FXML
	Text riepilogo_saldo;
	
	@FXML
	private ScrollPane scroll_pane;
	
	@FXML
	private Button generate_pdf_button;
	
	TableView<ClienteSaldoDettaglio> table_clienti;
    
    ClientiDAO clientiDAO;
    
    ZoneId defaultZoneId = ZoneId.of("Europe/Rome");
	
	DecimalFormat decimal_format;
	
	public OperazioniClienteController() {		
		field_cliente = new ComboBox<Integer>();
		combo_cliente = new AutoCompleteComboBox<Integer>(field_cliente);
		
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
		field_cliente.setPromptText("Numero del cliente");
		
		try {
			ClientiDAO clientiDAO = new ClientiDAO();
			ObservableList<Integer> temp_list = clientiDAO.getAllNumeroClienti();
			field_cliente.getItems().addAll(temp_list);
			//field_cliente.setValue(temp_list.get(0));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		field_cliente.getStyleClass().add("field");
		hbox_1.getChildren().add(field_cliente);
		
		field_data_iniziale.getStyleClass().add("field");
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
		
		field_data_finale.getStyleClass().add("field");
		field_data_finale.setConverter(new StringConverter<LocalDate>() {
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
		
		field_cliente.setOnAction(this::showOperazioniCliente);
		field_data_iniziale.setOnAction(this::showOperazioniCliente);
		field_data_finale.setOnAction(this::showOperazioniCliente);
		
		generate_pdf_button.setOnAction(this::generatePdfOperazioniCliente);
	}
	
	private Map<String, BigDecimal> loadDataClientiAll(int numero_cliente, Date data_iniziale, Date data_finale) throws SQLException {
		ObservableList<ClienteSaldoDettaglio> list_clienti_saldo_dettaglio = null;
		
		if (data_iniziale == null && data_finale == null) {
			list_clienti_saldo_dettaglio = clientiDAO.getAllClientiWithSaldoDettaglio(numero_cliente);
		}
		else if (data_iniziale == null && data_finale != null) {
			list_clienti_saldo_dettaglio = clientiDAO.getAllClientiWithSaldoDettaglio(numero_cliente, data_finale);
		}
		else if (data_iniziale != null && data_finale == null) {
			list_clienti_saldo_dettaglio = clientiDAO.getAllClientiWithSaldoDettaglio(data_iniziale, numero_cliente);
		}
		else {
			list_clienti_saldo_dettaglio = clientiDAO.getAllClientiWithSaldoDettaglio(numero_cliente, data_iniziale, data_finale);
		}
		
		// Subtract or Add importofattura/importobonifico from the list to have the correct Saldo for each operation
		for (int i=0; i<list_clienti_saldo_dettaglio.size(); i++) {
			if (i != 0) {
				BigDecimal actual_saldo = list_clienti_saldo_dettaglio.get(i-1).getSaldo().subtract(list_clienti_saldo_dettaglio.get(i).getImportofattura()).add(list_clienti_saldo_dettaglio.get(i).getImportobonifico());
				list_clienti_saldo_dettaglio.get(i).setSaldo(actual_saldo);
			}
		}
		
		
		table_clienti.setItems(list_clienti_saldo_dettaglio);
		table_clienti.refresh();
		
		Map<String, BigDecimal> riepilogo = new HashMap<String, BigDecimal>();
		BigDecimal tot_fatture = BigDecimal.ZERO;
		BigDecimal tot_bonifici = BigDecimal.ZERO;
		BigDecimal tot_saldo = BigDecimal.ZERO;
		
		for (int i=0; i<list_clienti_saldo_dettaglio.size(); i++) {
			tot_fatture = tot_fatture.add(list_clienti_saldo_dettaglio.get(i).getImportofattura());
			tot_bonifici = tot_bonifici.add(list_clienti_saldo_dettaglio.get(i).getImportobonifico());
		}
		tot_saldo = tot_bonifici.subtract(tot_fatture);
		
		riepilogo.put("tot_fatture", tot_fatture);
		riepilogo.put("tot_bonifici", tot_bonifici);
		riepilogo.put("tot_saldo", tot_saldo);
		
		return riepilogo;
	}
	
	@SuppressWarnings("unchecked")
	private void createTable(int numero_cliente, Date data_iniziale, Date data_finale) {
		table_clienti = new TableView<ClienteSaldoDettaglio>();
		table_clienti.setPlaceholder(new Text("Nessun dato da visualizzare"));
		table_clienti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table_clienti.getStyleClass().add("table_view");
		
		TableColumn<ClienteSaldoDettaglio, Date> col_cliente_data = new TableColumn<ClienteSaldoDettaglio, Date>("DATA OPERAZIONE");
		TableColumn<ClienteSaldoDettaglio, String> col_cliente_numerofattura = new TableColumn<ClienteSaldoDettaglio, String>("NR. FATTURA");
		TableColumn<ClienteSaldoDettaglio, BigDecimal> col_cliente_importofattura = new TableColumn<ClienteSaldoDettaglio, BigDecimal>("IMPORTO FATTURA");
		TableColumn<ClienteSaldoDettaglio, BigDecimal> col_cliente_importobonifico = new TableColumn<ClienteSaldoDettaglio, BigDecimal>("IMPORTO BONIFICO");
		TableColumn<ClienteSaldoDettaglio, String> col_cliente_noteoperazione = new TableColumn<ClienteSaldoDettaglio, String>("NOTE OPERAZIONE");
		TableColumn<ClienteSaldoDettaglio, BigDecimal> col_cliente_saldo = new TableColumn<ClienteSaldoDettaglio, BigDecimal>("SALDO");
		
		col_cliente_data.setCellValueFactory(new PropertyValueFactory<>("data"));
		col_cliente_numerofattura.setCellValueFactory(new PropertyValueFactory<>("numerofattura"));
		col_cliente_importofattura.setCellValueFactory(new PropertyValueFactory<>("importofattura"));
		col_cliente_importobonifico.setCellValueFactory(new PropertyValueFactory<>("importobonifico"));
		col_cliente_noteoperazione.setCellValueFactory(new PropertyValueFactory<>("note"));
		col_cliente_saldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
		
		try {
			Map<String, BigDecimal> riepilogo = loadDataClientiAll(numero_cliente, data_iniziale, data_finale);
			
			riepilogo_fatture.setText("TOTALE IMPORTO FATTURE: " + decimal_format.format(riepilogo.get("tot_fatture")));
			riepilogo_bonifici.setText("TOTALE IMPORTO BONIFICI: " + decimal_format.format(riepilogo.get("tot_bonifici")));
			riepilogo_saldo.setText("SALDO TOTALE: " + decimal_format.format(riepilogo.get("tot_saldo")));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		col_cliente_data.setCellFactory(column -> {
		    TableCell<ClienteSaldoDettaglio, Date> cell = new TableCell<ClienteSaldoDettaglio, Date>() {
		        private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		        @Override
		        protected void updateItem(Date item, boolean empty) {
		            super.updateItem(item, empty);
		            if(empty) {
		                setText(null);
		            }
		            else {
		                setText(format.format(item));
		            }
		        }
		    };

		    return cell;
		});
		col_cliente_importofattura.setCellFactory(col -> new TableCell<ClienteSaldoDettaglio, BigDecimal>() {
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
		col_cliente_importobonifico.setCellFactory(col -> new TableCell<ClienteSaldoDettaglio, BigDecimal>() {
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
		col_cliente_saldo.setCellFactory(col -> new TableCell<ClienteSaldoDettaglio, BigDecimal>() {
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
		table_clienti.getColumns().addAll(col_cliente_data, col_cliente_numerofattura, col_cliente_importofattura, col_cliente_importobonifico, col_cliente_noteoperazione, col_cliente_saldo);
		scroll_pane.setContent(table_clienti);
	}

	private void showOperazioniCliente(ActionEvent event) {
		
		try {
			int numero_cliente = Integer.parseInt(String.valueOf(field_cliente.getValue()));
			
			Date data_iniziale = null;
			Date data_finale = null;
			if (field_data_iniziale.getValue() != null) {
				LocalDate temp_data_iniziale = field_data_iniziale.getValue();
				data_iniziale = Date.from(temp_data_iniziale.atStartOfDay(defaultZoneId).toInstant());
			}
			if (field_data_finale.getValue() != null) {
				LocalDate temp_data_finale = field_data_finale.getValue();
				data_finale = Date.from(temp_data_finale.atStartOfDay(defaultZoneId).toInstant());
			}
			
			createTable(numero_cliente, data_iniziale, data_finale);
			
			try {
				Map<String, String> map = clientiDAO.getInfoByNumeroCliente(numero_cliente);
				
				info_cliente.setText("Saldo per il cliente \"" + map.get("nome") + "\" (" + map.get("stato")+", " + map.get("citta") + ")");
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla il valore del campo!");
			alert.show();
		}
	}
	
	private void generatePdfOperazioniCliente(ActionEvent event) {
		
		int numero_cliente = Integer.parseInt(String.valueOf(field_cliente.getValue()));
		
		String pdf_name = numero_cliente + ".pdf"; 
		
		// iText code
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
			Paragraph header = new Paragraph("Operazioni cliente " + numero_cliente, pdfCreator.getPoppinsFont(30, true, BaseColor.BLACK));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			
			/*** PDF BODY ***/
			Paragraph body = new Paragraph();
			
			PdfPTable table = new PdfPTable(6);	// parameter is the number of columns
			int[] columnWidths = new int[]{100, 100, 200, 200, 150, 150};
            table.setWidths(columnWidths);
			table.setHeaderRows(1);
			table.setWidthPercentage(100);
			
			Phrase data = new Phrase("DATA", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_data = new PdfPCell(data);
			cell_data = pdfCreator.formatHeaderCell(cell_data);
			
			Phrase numero_fattura = new Phrase("NR. FT.", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_numero_fattura = new PdfPCell(numero_fattura);
			cell_numero_fattura = pdfCreator.formatHeaderCell(cell_numero_fattura);
			
			Phrase importo_fattura = new Phrase("IMPORTO FT.", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_importo_fattura = new PdfPCell(importo_fattura);	
			cell_importo_fattura = pdfCreator.formatHeaderCell(cell_importo_fattura);
			
			Phrase importo_bonifico = new Phrase("IMPORTO BON.", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_importo_bonifico = new PdfPCell(importo_bonifico);
			cell_importo_bonifico = pdfCreator.formatHeaderCell(cell_importo_bonifico);
			
			Phrase note = new Phrase("NOTE", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_note = new PdfPCell(note);
			cell_note = pdfCreator.formatHeaderCell(cell_note);
			
			Phrase saldo = new Phrase("SALDO", pdfCreator.getPoppinsFont(18, true, BaseColor.WHITE));
			PdfPCell cell_saldo = new PdfPCell(saldo);
			cell_saldo = pdfCreator.formatHeaderCell(cell_saldo);
			
			table.addCell(cell_data);
			table.addCell(cell_numero_fattura);
			table.addCell(cell_importo_fattura);
			table.addCell(cell_importo_bonifico);
			table.addCell(cell_note);
			table.addCell(cell_saldo);
			
			boolean background_color = false;
			for(int i=0; i<table_clienti.getItems().size(); i++){
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String data_str = format.format(table_clienti.getItems().get(i).getData());
		        table.addCell(pdfCreator.createNewCell(data_str, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getNumerofattura(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(decimal_format.format(table_clienti.getItems().get(i).getImportofattura()), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(decimal_format.format(table_clienti.getItems().get(i).getImportobonifico()), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(table_clienti.getItems().get(i).getNote(), PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(decimal_format.format(table_clienti.getItems().get(i).getSaldo()), PdfPCell.ALIGN_CENTER, false, background_color));
		        background_color = !background_color;
		    }
			body.add(table);
			
			String text = "";
			text += riepilogo_fatture.getText();
			text += "\n" + riepilogo_bonifici.getText();
			text += "\n" + riepilogo_saldo.getText();
			
			Paragraph riepilogo_par = new Paragraph(text, pdfCreator.getPoppinsFont(16, true, BaseColor.BLACK));
			riepilogo_par.setAlignment(Paragraph.ALIGN_CENTER);
			
			/*** PDF FOOTER ***/
			// generated when onEndPage event occur
			
			document.add(header);
			document.add(riepilogo_par);
			document.add(pdfCreator.emptyLines(1));
			document.add(body);
			pdfCreator.closeDocument();
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
