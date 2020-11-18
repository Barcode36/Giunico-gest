package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import application.OperazioneExtended;
import application.PdfCreator;
import application.TableUtils;
import application.database.ClientiDAO;
import application.database.OperazioniDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class GestioneCMRController implements Initializable {

	double width = 0;
	double height = 0;
	
	@FXML
	private HBox hbox_2;
    
    @FXML
	private ScrollPane scroll_pane;
    
    TableView<OperazioneExtended> table_operazioni;
    @FXML
    private Button button_refresh_table;
    
    @FXML
    private Button button_stampa_per_raccoglitore;
    
    @FXML
    private Button button_sollecitabili_datacarico;
    
    @FXML
	private Button exit_button;
    
    private RadioButton radio_cmr;
    private RadioButton radio_doc_dog;
    private ToggleGroup tg;
    
    OperazioniDAO operazioniDAO;
    DecimalFormat decimal_format;
    
	public GestioneCMRController() {
		operazioniDAO = new OperazioniDAO();
		decimal_format = new DecimalFormat("#,##0.00€");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		tg = new ToggleGroup();
		radio_cmr = new RadioButton("CMR      ");
		radio_doc_dog = new RadioButton("DOC. DOG.");
		
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = resolution.getWidth();
		this.height = resolution.getHeight();
	}
	
	public void loadData(TableView<OperazioneExtended> table_operazioni, boolean refresh) throws SQLException {
		ObservableList<OperazioneExtended> list_operazioni = null;
		
		if (radio_cmr.isSelected())
			list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRSiUE();
		else
			list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRNoUE();
		
		table_operazioni.setItems(list_operazioni);
		
		if (refresh) {
			table_operazioni.refresh();
		}
	}
	
	@FXML
	private void refreshTable(ActionEvent event) {
		ObservableList<OperazioneExtended> list_operazioni = null;
		
		if (radio_cmr.isSelected())
			try {
				list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRSiUE();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		else
			try {
				list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRNoUE();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		
		table_operazioni.setItems(list_operazioni);
		table_operazioni.refresh();
	}
	
	private void refreshTable() {
		ObservableList<OperazioneExtended> list_operazioni = null;
		
		if (radio_cmr.isSelected())
			try {
				list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRSiUE();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		else
			try {
				list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoOkSiStampaCMRNoUE();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		
		table_operazioni.setItems(list_operazioni);
		table_operazioni.refresh();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		button_refresh_table.setOnAction(this::refreshTable);
		button_sollecitabili_datacarico.setVisible(false);
		
		radio_cmr.setToggleGroup(tg);
		radio_doc_dog.setToggleGroup(tg);
		radio_cmr.setSelected(true);
		
		radio_cmr.getStyleClass().add("radio");
		radio_doc_dog.getStyleClass().add("radio");
		
		radio_cmr.setOnAction(this::changeTable);
		radio_doc_dog.setOnAction(this::changeTable);
		
		hbox_2.getChildren().addAll(radio_cmr, radio_doc_dog);
		
		createTableOperazioni();
	}
	
	private void changeTable(ActionEvent event) {
		table_operazioni.getItems().clear();
		table_operazioni.refresh();
		
		if (radio_cmr.isSelected()) {
			radio_doc_dog.setSelected(false);
			button_sollecitabili_datacarico.setVisible(false);
		}
		else {
			radio_cmr.setSelected(false);
			button_sollecitabili_datacarico.setVisible(true);
		}
		
		createTableOperazioni();
	}
	
	@SuppressWarnings("unchecked")
	private void createTableOperazioni() {
		table_operazioni = new TableView<OperazioneExtended>();
		table_operazioni.setEditable(true);
		table_operazioni.getStyleClass().add("table_view");
		table_operazioni.setPlaceholder(new Text("Nessun dato da visualizzare"));
		table_operazioni.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table_operazioni.getSelectionModel().setCellSelectionEnabled(true);
		table_operazioni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table_operazioni);
		
		TableColumn<OperazioneExtended, Integer> col_operazione_id = new TableColumn<OperazioneExtended, Integer>("ID");
		TableColumn<OperazioneExtended, String> col_operazione_numerofattura = new TableColumn<OperazioneExtended, String>("NUMERO FT.");
		TableColumn<OperazioneExtended, Date> col_operazione_data = new TableColumn<OperazioneExtended, Date>("DATA");
		TableColumn<OperazioneExtended, Integer> col_operazione_cliente = new TableColumn<OperazioneExtended, Integer>("CLIENTE");
		TableColumn<OperazioneExtended, String> col_nome_cliente = new TableColumn<OperazioneExtended, String>("NOME CLIENTE");
		TableColumn<OperazioneExtended, String> col_operazione_statodest = new TableColumn<OperazioneExtended, String>("STATO");
		TableColumn<OperazioneExtended, String> col_operazione_trasportatore = new TableColumn<OperazioneExtended, String>("TRASPORTATORE");
		TableColumn<OperazioneExtended, Date> col_operazione_datacarico = new TableColumn<OperazioneExtended, Date>("DATA CARICO");
		TableColumn<OperazioneExtended, String> col_operazione_speddoganale = new TableColumn<OperazioneExtended, String>("SPED. DOG.");
		TableColumn<OperazioneExtended, String> col_operazione_mrn = new TableColumn<OperazioneExtended, String>("MRN");
		TableColumn<OperazioneExtended, String> col_operazione_informazioni = new TableColumn<OperazioneExtended, String>("INFORMAZIONI");
		TableColumn<OperazioneExtended, String> col_email_cliente = new TableColumn<OperazioneExtended, String>("INDIRIZZO EMAIL");
		TableColumn<OperazioneExtended, CheckBox> col_ok = new TableColumn<OperazioneExtended, CheckBox>("OK");
		
		// Set columns size
		col_operazione_id.setMaxWidth( 1f * Integer.MAX_VALUE * 3 ); // this
		col_operazione_numerofattura.setMaxWidth( 1f * Integer.MAX_VALUE * 6 );
		col_operazione_data.setMaxWidth( 1f * Integer.MAX_VALUE * 6 ); // this
		col_operazione_cliente.setMaxWidth( 1f * Integer.MAX_VALUE * 5 ); // this
		col_nome_cliente.setMaxWidth( 1f * Integer.MAX_VALUE * 12 );
		col_operazione_statodest.setMaxWidth( 1f * Integer.MAX_VALUE * 8 );
		col_operazione_trasportatore.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );
		col_operazione_datacarico.setMaxWidth( 1f * Integer.MAX_VALUE * 6 ); // this
		col_operazione_speddoganale.setMaxWidth( 1f * Integer.MAX_VALUE * 8 );
		col_operazione_mrn.setMaxWidth( 1f * Integer.MAX_VALUE * 8 );
		col_operazione_informazioni.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );
		col_email_cliente.setMaxWidth( 1f * Integer.MAX_VALUE * 12 );
		col_ok.setMaxWidth( 1f * Integer.MAX_VALUE * 3 );
		
		// Set columns editable
		col_operazione_informazioni.setEditable(true);
		col_email_cliente.setEditable(true);
		col_operazione_speddoganale.setEditable(true);
		col_operazione_mrn.setEditable(true);
		
		// Set cells editable
		col_operazione_informazioni.setCellFactory(TextFieldTableCell.forTableColumn());
		col_operazione_informazioni.setOnEditCommit(
				(TableColumn.CellEditEvent<OperazioneExtended, String> t) ->
					{
						try {
							operazioniDAO.updateInformazioniById((t.getTableView().getItems().get(t.getTablePosition().getRow())).getId(), t.getNewValue());
							refreshTable();
						}
						catch (SQLException e1) {
							e1.printStackTrace();
						}
					}   
                );		
		
		col_email_cliente.setCellFactory(TextFieldTableCell.forTableColumn());
		col_email_cliente.setOnEditCommit(
				(TableColumn.CellEditEvent<OperazioneExtended, String> t) ->
					{
						try {
							operazioniDAO.updateEmailById((t.getTableView().getItems().get(t.getTablePosition().getRow())).getId(), t.getNewValue());
							refreshTable();
						}
						catch (SQLException e1) {
							e1.printStackTrace();
						}
					}   
                );
		
		col_operazione_speddoganale.setCellFactory(TextFieldTableCell.forTableColumn());
		col_operazione_speddoganale.setOnEditCommit(
				(TableColumn.CellEditEvent<OperazioneExtended, String> t) ->
					{
						try {
							operazioniDAO.updateSpedDoganaleById((t.getTableView().getItems().get(t.getTablePosition().getRow())).getId(), t.getNewValue());
							refreshTable();
						}
						catch (SQLException e1) {
							e1.printStackTrace();
						}
					}   
                );
		
		col_operazione_mrn.setCellFactory(TextFieldTableCell.forTableColumn());
		col_operazione_mrn.setOnEditCommit(
				(TableColumn.CellEditEvent<OperazioneExtended, String> t) ->
					{
						try {
							operazioniDAO.updateMrnById((t.getTableView().getItems().get(t.getTablePosition().getRow())).getId(), t.getNewValue());
							refreshTable();
						}
						catch (SQLException e1) {
							e1.printStackTrace();
						}
					}   
                );
		
		
		col_operazione_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_operazione_data.setCellValueFactory(new PropertyValueFactory<>("data"));
		col_operazione_cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		col_nome_cliente.setCellValueFactory(new PropertyValueFactory<>("nomecliente"));
		col_operazione_numerofattura.setCellValueFactory(new PropertyValueFactory<>("numerofattura"));
		col_operazione_trasportatore.setCellValueFactory(new PropertyValueFactory<>("trasportatore"));
		col_operazione_datacarico.setCellValueFactory(new PropertyValueFactory<>("datacarico"));
		col_operazione_speddoganale.setCellValueFactory(new PropertyValueFactory<>("speddoganale"));
		col_operazione_mrn.setCellValueFactory(new PropertyValueFactory<>("mrn"));
		col_operazione_informazioni.setCellValueFactory(new PropertyValueFactory<>("informazioni"));
		col_operazione_statodest.setCellValueFactory(new PropertyValueFactory<>("statodest"));
		col_email_cliente.setCellValueFactory(new PropertyValueFactory<>("emailcliente"));
		col_ok.setCellValueFactory(new PropertyValueFactory<>("check_ok"));
		
		/*** CHECK OK ***/
		Callback<TableColumn<OperazioneExtended, CheckBox>, TableCell<OperazioneExtended, CheckBox>> cellFactoryOk = new Callback<TableColumn<OperazioneExtended, CheckBox>, TableCell<OperazioneExtended, CheckBox>>() {
            @Override
            public TableCell<OperazioneExtended, CheckBox> call(final TableColumn<OperazioneExtended, CheckBox> param) {
                final TableCell<OperazioneExtended, CheckBox> cell = new TableCell<OperazioneExtended, CheckBox>() {

                    private final CheckBox check_ok = new CheckBox("OK");

                    {
                    	check_ok.setOnAction((ActionEvent event) -> {
                    		OperazioneExtended operazione = getTableView().getItems().get(getIndex());
                            
                            try {
                            	if (check_ok.isSelected()) operazioniDAO.setOkById(operazione.getId(), true);
                            	else operazioniDAO.setOkById(operazione.getId(), false);
                            }
                            catch (Exception e) {
                            	e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(CheckBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(check_ok);
                        }
                    }
                };
                return cell;
            }
        };
        
        col_nome_cliente.setCellFactory(col -> new TableCell<OperazioneExtended, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                	OperazioneExtended operazione = getTableView().getItems().get(getIndex());
                    ClientiDAO clientiDAO = new ClientiDAO();
					String nome_cliente = "";
					try {
						nome_cliente = clientiDAO.getNomeByNumero(operazione.getCliente());
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					setText(nome_cliente);
                }
            }
        }); 
        
        col_operazione_data.setCellFactory(column -> {
		    TableCell<OperazioneExtended, Date> cell = new TableCell<OperazioneExtended, Date>() {
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
        
        col_operazione_datacarico.setCellFactory(column -> {
		    TableCell<OperazioneExtended, Date> cell = new TableCell<OperazioneExtended, Date>() {
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
        
        /*col_email_cliente.setCellFactory(col -> new TableCell<OperazioneExtended, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                	OperazioneExtended operazione = getTableView().getItems().get(getIndex());
                    ClientiDAO clientiDAO = new ClientiDAO();
					String email_cliente = "";
					try {
						email_cliente = clientiDAO.getEmailByNumeroCliente(operazione.getCliente());
					}
					catch (SQLException e) {
						e.printStackTrace();
					}
					setText(email_cliente);
                }
            }
        });*/
        
        col_ok.setCellFactory(cellFactoryOk);
        
        try {
			loadData(table_operazioni, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
        table_operazioni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        if (radio_cmr.isSelected())
        	table_operazioni.getColumns().addAll(col_operazione_id, col_operazione_numerofattura, col_operazione_data, col_operazione_cliente, col_nome_cliente, col_operazione_statodest, col_operazione_trasportatore, col_operazione_datacarico, col_operazione_informazioni, col_email_cliente, col_ok);
        else
        	table_operazioni.getColumns().addAll(col_operazione_id, col_operazione_numerofattura, col_operazione_data, col_operazione_cliente, col_nome_cliente, col_operazione_statodest, col_operazione_trasportatore, col_operazione_datacarico, col_operazione_speddoganale, col_operazione_mrn, col_operazione_informazioni, col_ok);
        
        scroll_pane.setContent(table_operazioni);
	}

	@FXML
	private void stampa_per_raccoglitore() {
		// iText code
		try {
			// SELEZIONE DELLA DIRECTORY DOVE SALVARE IL DOCUMENTO
	    	DirectoryChooser directoryChooser = new DirectoryChooser();
	    	directoryChooser.setTitle("Dove vuoi salvare il documento?");
	    	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
        	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        	directoryChooser.setInitialDirectory(new File(currentPath));
	    	Stage stage = (Stage) scroll_pane.getScene().getWindow();
	    	File selectedDirectory = directoryChooser.showDialog(stage);
	    	
	    	/*** Codice per CMR ***/
	    	if (radio_cmr.isSelected()) {
	    		String pdf_name = "CMR_controfirmati_raccoglitore.pdf";
		    	PdfCreator pdfCreator = new PdfCreator(selectedDirectory + "/" + pdf_name, false);
				Document document = pdfCreator.getDocument();
				
				/*** PDF HEADER ***/
				Paragraph header = new Paragraph("Ricevimento CMR controfirmati", pdfCreator.getPoppinsFont(30, true, BaseColor.BLACK));
				header.setAlignment(Paragraph.ALIGN_CENTER);
				
				/*** PDF BODY ***/
				Paragraph body = new Paragraph("");
				
				PdfPTable table = new PdfPTable(7);	// parameter is the number of columns
				int[] columnWidths = new int[]{100, 100, 150, 100, 150, 100, 100};
		        table.setWidths(columnWidths);
				table.setHeaderRows(1);
				table.setWidthPercentage(100);
				
				Phrase nr_ft = new Phrase("NR. FT.", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_nr_ft = new PdfPCell(nr_ft);
				cell_nr_ft = pdfCreator.formatHeaderCell(cell_nr_ft);
				
				Phrase data = new Phrase("DATA", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_data = new PdfPCell(data);
				cell_data = pdfCreator.formatHeaderCell(cell_data);
				
				Phrase nome_cliente = new Phrase("CLIENTE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_nome_cliente = new PdfPCell(nome_cliente);
				cell_nome_cliente = pdfCreator.formatHeaderCell(cell_nome_cliente);
				
				Phrase stato = new Phrase("STATO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_stato = new PdfPCell(stato);
				cell_stato = pdfCreator.formatHeaderCell(cell_stato);
				
				Phrase trasportatore = new Phrase("TRASPORTATORE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_trasportatore = new PdfPCell(trasportatore);
				cell_trasportatore = pdfCreator.formatHeaderCell(cell_trasportatore);
				
				Phrase data_carico = new Phrase("CARICO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_data_carico = new PdfPCell(data_carico);
				cell_data_carico = pdfCreator.formatHeaderCell(cell_data_carico);
				
				Phrase informazioni = new Phrase("INFO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_informazioni = new PdfPCell(informazioni);	
				cell_informazioni = pdfCreator.formatHeaderCell(cell_informazioni);
				
				table.addCell(cell_nr_ft);
				table.addCell(cell_data);
				table.addCell(cell_nome_cliente);
				table.addCell(cell_stato);
				table.addCell(cell_trasportatore);
				table.addCell(cell_data_carico);
				table.addCell(cell_informazioni);
				
				TextInputDialog dialog_ft1 = new TextInputDialog("");
				dialog_ft1.setHeaderText("Dalla fattura numero...");
		        dialog_ft1.showAndWait();
		        String ft1 = dialog_ft1.getEditor().getText().trim();
		        
		        TextInputDialog dialog_ft2 = new TextInputDialog("");
				dialog_ft2.setHeaderText("Alla fattura numero...");
		        dialog_ft2.showAndWait();
		        String ft2 = dialog_ft2.getEditor().getText().trim();
				
				ResultSet rs = operazioniDAO.getAllOperazioniByFattureSiUE(ft1, ft2);
				
				boolean background_color = false;
				while (rs.next()){
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String data_str = format.format(rs.getDate("data"));
					String datacarico_str = format.format(rs.getDate("datacarico"));
			        table.addCell(pdfCreator.createNewCell(rs.getString("numerofattura"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(data_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("nome"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("stato"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("trasportatore"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(datacarico_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("informazioni"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        background_color = !background_color;
			    }
				body.add(table);
				
				/*** PDF FOOTER ***/
				// generated when onEndPage event occur
				
				document.add(header);
				document.add(pdfCreator.emptyLines(2));
				document.add(body);
				pdfCreator.closeDocument();
	    	}
	    	/*** Codice per DOCUMENTI DOGANALI ***/
	    	else {
	    		String pdf_name = "Doc_Doganali_raccoglitore.pdf";
		    	PdfCreator pdfCreator = new PdfCreator(selectedDirectory + "/" + pdf_name, false);
				Document document = pdfCreator.getDocument();
				
				/*** PDF HEADER ***/
				Paragraph header = new Paragraph("Documenti Doganali sollecitabili", pdfCreator.getPoppinsFont(30, true, BaseColor.BLACK));
				header.setAlignment(Paragraph.ALIGN_CENTER);
				
				/*** PDF BODY ***/
				Paragraph body = new Paragraph("");
				
				PdfPTable table = new PdfPTable(9);	// parameter is the number of columns
				int[] columnWidths = new int[]{100, 100, 150, 100, 150, 100, 100, 100, 100};
		        table.setWidths(columnWidths);
				table.setHeaderRows(1);
				table.setWidthPercentage(100);
				
				Phrase nr_ft = new Phrase("NR. FT.", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_nr_ft = new PdfPCell(nr_ft);
				cell_nr_ft = pdfCreator.formatHeaderCell(cell_nr_ft);
				
				Phrase data = new Phrase("DATA", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_data = new PdfPCell(data);
				cell_data = pdfCreator.formatHeaderCell(cell_data);
				
				Phrase nome_cliente = new Phrase("CLIENTE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_nome_cliente = new PdfPCell(nome_cliente);
				cell_nome_cliente = pdfCreator.formatHeaderCell(cell_nome_cliente);
				
				Phrase stato = new Phrase("STATO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_stato = new PdfPCell(stato);
				cell_stato = pdfCreator.formatHeaderCell(cell_stato);
				
				Phrase trasportatore = new Phrase("TRASPORTATORE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_trasportatore = new PdfPCell(trasportatore);
				cell_trasportatore = pdfCreator.formatHeaderCell(cell_trasportatore);
				
				Phrase data_carico = new Phrase("CARICO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_data_carico = new PdfPCell(data_carico);
				cell_data_carico = pdfCreator.formatHeaderCell(cell_data_carico);
				
				Phrase sped_doganale = new Phrase("SPED. DOG.", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_sped_doganale = new PdfPCell(sped_doganale);
				cell_sped_doganale = pdfCreator.formatHeaderCell(cell_sped_doganale);
				
				Phrase mrn = new Phrase("MRN", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_mrn = new PdfPCell(mrn);
				cell_mrn = pdfCreator.formatHeaderCell(cell_mrn);
				
				Phrase informazioni = new Phrase("INFO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
				PdfPCell cell_informazioni = new PdfPCell(informazioni);	
				cell_informazioni = pdfCreator.formatHeaderCell(cell_informazioni);
				
				table.addCell(cell_nr_ft);
				table.addCell(cell_data);
				table.addCell(cell_nome_cliente);
				table.addCell(cell_stato);
				table.addCell(cell_trasportatore);
				table.addCell(cell_data_carico);
				table.addCell(cell_sped_doganale);
				table.addCell(cell_mrn);
				table.addCell(cell_informazioni);
				
				TextInputDialog dialog_ft1 = new TextInputDialog("");
				dialog_ft1.setHeaderText("Dalla fattura numero...");
		        dialog_ft1.showAndWait();
		        String ft1 = dialog_ft1.getEditor().getText().trim();
		        
		        TextInputDialog dialog_ft2 = new TextInputDialog("");
				dialog_ft2.setHeaderText("Alla fattura numero...");
		        dialog_ft2.showAndWait();
		        String ft2 = dialog_ft2.getEditor().getText().trim();
				
				ResultSet rs = operazioniDAO.getAllOperazioniByFattureNoUE(ft1, ft2);
				
				boolean background_color = false;
				while (rs.next()){
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
					String data_str = format.format(rs.getDate("data"));
					String datacarico_str = format.format(rs.getDate("datacarico"));
			        table.addCell(pdfCreator.createNewCell(rs.getString("numerofattura"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(data_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("nome"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("stato"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("trasportatore"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(datacarico_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("speddoganale"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("mrn"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        table.addCell(pdfCreator.createNewCell(rs.getString("informazioni"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
			        background_color = !background_color;
			    }
				body.add(table);
				
				/*** PDF FOOTER ***/
				// generated when onEndPage event occur
				
				document.add(header);
				document.add(pdfCreator.emptyLines(2));
				document.add(body);
				pdfCreator.closeDocument();
	    	}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void sollecitabili_data_carico() {
		// SELEZIONE DELLA DIRECTORY DOVE SALVARE IL DOCUMENTO
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setTitle("Dove vuoi salvare il documento?");
    	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
    	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
    	directoryChooser.setInitialDirectory(new File(currentPath));
    	Stage stage = (Stage) scroll_pane.getScene().getWindow();
    	File selectedDirectory = directoryChooser.showDialog(stage);
    	
		try {
			String pdf_name = "Doc_Doganali_sollecitabili.pdf";
	    	PdfCreator pdfCreator = new PdfCreator(selectedDirectory + "/" + pdf_name, false);
			Document document = pdfCreator.getDocument();
			
			/*** PDF HEADER ***/
			Paragraph header = new Paragraph("Documenti Doganali sollecitabili", pdfCreator.getPoppinsFont(30, true, BaseColor.BLACK));
			header.setAlignment(Paragraph.ALIGN_CENTER);
			
			/*** PDF BODY ***/
			Paragraph body = new Paragraph("");
			
			PdfPTable table = new PdfPTable(8);	// parameter is the number of columns
			int[] columnWidths = new int[]{100, 100, 150, 100, 150, 100, 100, 100};
	        table.setWidths(columnWidths);
			table.setHeaderRows(1);
			table.setWidthPercentage(100);
			
			Phrase nr_ft = new Phrase("NR. FT.", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_nr_ft = new PdfPCell(nr_ft);
			cell_nr_ft = pdfCreator.formatHeaderCell(cell_nr_ft);
			
			Phrase data = new Phrase("DATA", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_data = new PdfPCell(data);
			cell_data = pdfCreator.formatHeaderCell(cell_data);
			
			Phrase nome_cliente = new Phrase("CLIENTE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_nome_cliente = new PdfPCell(nome_cliente);
			cell_nome_cliente = pdfCreator.formatHeaderCell(cell_nome_cliente);
			
			Phrase stato = new Phrase("STATO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_stato = new PdfPCell(stato);
			cell_stato = pdfCreator.formatHeaderCell(cell_stato);
			
			Phrase trasportatore = new Phrase("TRASPORTATORE", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_trasportatore = new PdfPCell(trasportatore);
			cell_trasportatore = pdfCreator.formatHeaderCell(cell_trasportatore);
			
			Phrase data_carico = new Phrase("CARICO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_data_carico = new PdfPCell(data_carico);
			cell_data_carico = pdfCreator.formatHeaderCell(cell_data_carico);
			
			Phrase sped_doganale = new Phrase("SPED. DOG.", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_sped_doganale = new PdfPCell(sped_doganale);
			cell_sped_doganale = pdfCreator.formatHeaderCell(cell_sped_doganale);
			
			Phrase informazioni = new Phrase("INFO", pdfCreator.getPoppinsFont(12, true, BaseColor.WHITE));
			PdfPCell cell_informazioni = new PdfPCell(informazioni);	
			cell_informazioni = pdfCreator.formatHeaderCell(cell_informazioni);
			
			table.addCell(cell_nr_ft);
			table.addCell(cell_data);
			table.addCell(cell_nome_cliente);
			table.addCell(cell_stato);
			table.addCell(cell_trasportatore);
			table.addCell(cell_data_carico);
			table.addCell(cell_sped_doganale);
			table.addCell(cell_informazioni);
			
			ResultSet rs = operazioniDAO.getAllOperazioniNoUESollecitabili();
			
			boolean background_color = false;
			while (rs.next()){
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String data_str = format.format(rs.getDate("data"));
				String datacarico_str = format.format(rs.getDate("datacarico"));
		        table.addCell(pdfCreator.createNewCell(rs.getString("numerofattura"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(data_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(rs.getString("nome"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(rs.getString("stato"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(rs.getString("trasportatore"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(datacarico_str, 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(rs.getString("speddoganale"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        table.addCell(pdfCreator.createNewCell(rs.getString("informazioni"), 8, PdfPCell.ALIGN_CENTER, false, background_color));
		        background_color = !background_color;
		    }
			body.add(table);
			
			/*** PDF FOOTER ***/
			// generated when onEndPage event occur
			
			document.add(header);
			document.add(pdfCreator.emptyLines(2));
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
