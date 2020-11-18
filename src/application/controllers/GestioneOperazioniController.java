package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfStamper;

import application.Operazione;
import application.database.ClientiDAO;
import application.database.OperazioniDAO;
import application.database.TrasportatoriDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class GestioneOperazioniController implements Initializable {

	double width = 0;
	double height = 0;
	
    @FXML
	private ScrollPane scroll_pane;
    
    TableView<Operazione> table_operazioni;
    
    @FXML
    private Button add_button;
    
    @FXML
    private Button button_refresh_table;
    
    @FXML
	DatePicker field_data_iniziale;
    
    @FXML
	private Button exit_button;
    
    OperazioniDAO operazioniDAO;
    
    DecimalFormat decimal_format;
    
    ZoneId defaultZoneId = ZoneId.of("Europe/Rome");
    
	public GestioneOperazioniController() {
		operazioniDAO = new OperazioniDAO();
		decimal_format = new DecimalFormat("#,##0.00€");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = resolution.getWidth();
		this.height = resolution.getHeight();
	}
	
	public void loadData(TableView<Operazione> table_operazioni, boolean refresh) throws SQLException {
		ObservableList<Operazione> list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoStampaCMR();
		table_operazioni.setItems(list_operazioni);
		
		if (refresh) {
			table_operazioni.refresh();
		}
	}
	
	@FXML
	private void refreshTable(ActionEvent event) {
		try {
			ObservableList<Operazione> list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoStampaCMR();
			table_operazioni.setItems(list_operazioni);
			table_operazioni.refresh();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTableOperazioni();
		table_operazioni.getStyleClass().add("table_view");
		button_refresh_table.setOnAction(this::refreshTable);
		
		field_data_iniziale.setPromptText("Data iniziale");
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
		field_data_iniziale.setOnAction(this::showOperazioniDal);
	}
	
	private void showOperazioniDal(ActionEvent event) {
		Date data_iniziale = null;
		if (field_data_iniziale.getValue() != null) {
			LocalDate temp_data_iniziale = field_data_iniziale.getValue();
			data_iniziale = Date.from(temp_data_iniziale.atStartOfDay(defaultZoneId).toInstant());	// plusDays(1).
		}
		
		try {
			ObservableList<Operazione> list_operazioni = operazioniDAO.getAllOperazioniOrderByIdNoStampaCMRFromDate(data_iniziale);
			table_operazioni.setItems(list_operazioni);
			
			table_operazioni.refresh();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initTableOperazioni() {
		table_operazioni = new TableView<Operazione>();
		table_operazioni.setPlaceholder(new Text("Nessun dato da visualizzare"));
		table_operazioni.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<Operazione, Integer> col_operazione_id = new TableColumn<Operazione, Integer>("ID");
		TableColumn<Operazione, Date> col_operazione_data = new TableColumn<Operazione, Date>("DATA");
		TableColumn<Operazione, Integer> col_operazione_cliente = new TableColumn<Operazione, Integer>("CLIENTE");
		TableColumn<Operazione, String> col_nome_cliente = new TableColumn<Operazione, String>("NOME CLIENTE");
		TableColumn<Operazione, String> col_operazione_numerofattura = new TableColumn<Operazione, String>("NUMERO FATTURA");
		TableColumn<Operazione, BigDecimal> col_operazione_importofattura = new TableColumn<Operazione, BigDecimal>("IMPORTO FATTURA");
		TableColumn<Operazione, BigDecimal> col_operazione_importobonifico = new TableColumn<Operazione, BigDecimal>("IMPORTO BONIFICO");
		TableColumn<Operazione, Boolean> col_operazione_esclusodocsdoganali = new TableColumn<Operazione, Boolean>("ESCLUSO DOC. DOGANALI");
		TableColumn<Operazione, String> col_operazione_note = new TableColumn<Operazione, String>("NOTE");
		TableColumn<Operazione, Integer> col_operazione_numerocolli = new TableColumn<Operazione, Integer>("NUMERO COLLI");
		TableColumn<Operazione, String> col_operazione_tipoimballo = new TableColumn<Operazione, String>("TIPO IMBALLO");
		TableColumn<Operazione, Float> col_operazione_pesolordo = new TableColumn<Operazione, Float>("PESO LORDO");
		TableColumn<Operazione, String> col_operazione_notetrasportatore = new TableColumn<Operazione, String>("NOTE TRASPORTATORE");
		TableColumn<Operazione, String> col_operazione_trasportatore = new TableColumn<Operazione, String>("TRASPORTATORE");
		TableColumn<Operazione, Boolean> col_operazione_stampacmr = new TableColumn<Operazione, Boolean>("STAMPA CMR");
		TableColumn<Operazione, Date> col_operazione_datacarico = new TableColumn<Operazione, Date>("DATA CARICO");
		TableColumn<Operazione, String> col_operazione_speddoganale = new TableColumn<Operazione, String>("SPED. DOGANALE");
		TableColumn<Operazione, String> col_operazione_mrn = new TableColumn<Operazione, String>("MRN");
		TableColumn<Operazione, String> col_operazione_informazioni = new TableColumn<Operazione, String>("INFORMAZIONI");
		TableColumn<Operazione, Boolean> col_operazione_ok = new TableColumn<Operazione, Boolean>("OK");
		TableColumn<Operazione, String> col_operazione_nomedest = new TableColumn<Operazione, String>("NOME DESTINAZIONE");
		TableColumn<Operazione, String> col_operazione_indirizzodest = new TableColumn<Operazione, String>("INDIRIZZO DESTINAZIONE");
		TableColumn<Operazione, String> col_operazione_capdest = new TableColumn<Operazione, String>("CAP DESTINAZIONE");
		TableColumn<Operazione, String> col_operazione_cittadest = new TableColumn<Operazione, String>("CITTA' DESTINAZIONE");
		TableColumn<Operazione, String> col_operazione_statodest = new TableColumn<Operazione, String>("STATO DESTINAZIONE");
		
		TableColumn<Operazione, Button> col_remove_operazione = new TableColumn<Operazione, Button>("ELIMINA");
		TableColumn<Operazione, Button> col_edit_operazione = new TableColumn<Operazione, Button>("COMPILA CMR");
		
		TableColumn<Operazione, Button> col_multiple_pdf = new TableColumn<Operazione, Button>("STAMPA DOCUMENTI PDF");
		TableColumn<Operazione, Button> col_stampa_documento_allegato = new TableColumn<Operazione, Button>("DOC. ALLEGATO");
		TableColumn<Operazione, Button> col_stampa_cmr = new TableColumn<Operazione, Button>("CMR");
		TableColumn<Operazione, Button> col_stampa_etichetta_collo = new TableColumn<Operazione, Button>("ETICHETTE COLLO");
		TableColumn<Operazione, CheckBox> col_ok = new TableColumn<Operazione, CheckBox>("OK");
		
		
		col_operazione_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_operazione_data.setCellValueFactory(new PropertyValueFactory<>("data"));
		col_operazione_cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
		col_nome_cliente.setCellValueFactory(new PropertyValueFactory<>("nome"));
		col_operazione_numerofattura.setCellValueFactory(new PropertyValueFactory<>("numerofattura"));
		col_operazione_importofattura.setCellValueFactory(new PropertyValueFactory<>("importofattura"));
		col_operazione_importobonifico.setCellValueFactory(new PropertyValueFactory<>("importobonifico"));
		col_operazione_esclusodocsdoganali.setCellValueFactory(new PropertyValueFactory<>("esclusodocsdoganali"));
		col_operazione_note.setCellValueFactory(new PropertyValueFactory<>("note"));
		col_operazione_numerocolli.setCellValueFactory(new PropertyValueFactory<>("numerocolli"));
		col_operazione_tipoimballo.setCellValueFactory(new PropertyValueFactory<>("tipoimballo"));
		col_operazione_pesolordo.setCellValueFactory(new PropertyValueFactory<>("pesolordo"));
		col_operazione_notetrasportatore.setCellValueFactory(new PropertyValueFactory<>("notetrasportatore"));
		col_operazione_trasportatore.setCellValueFactory(new PropertyValueFactory<>("trasportatore"));
		col_operazione_stampacmr.setCellValueFactory(new PropertyValueFactory<>("stampacmr"));
		col_operazione_datacarico.setCellValueFactory(new PropertyValueFactory<>("datacarico"));
		col_operazione_speddoganale.setCellValueFactory(new PropertyValueFactory<>("speddoganale"));
		col_operazione_mrn.setCellValueFactory(new PropertyValueFactory<>("mrn"));
		col_operazione_informazioni.setCellValueFactory(new PropertyValueFactory<>("informazioni"));
		col_operazione_ok.setCellValueFactory(new PropertyValueFactory<>("ok"));
		col_operazione_nomedest.setCellValueFactory(new PropertyValueFactory<>("nomedest"));
		col_operazione_indirizzodest.setCellValueFactory(new PropertyValueFactory<>("indirizzodest"));
		col_operazione_capdest.setCellValueFactory(new PropertyValueFactory<>("capdest"));
		col_operazione_cittadest.setCellValueFactory(new PropertyValueFactory<>("cittadest"));
		col_operazione_statodest.setCellValueFactory(new PropertyValueFactory<>("statodest"));
		
		col_remove_operazione.setCellValueFactory(new PropertyValueFactory<>("remove_operazione"));
		col_edit_operazione.setCellValueFactory(new PropertyValueFactory<>("edit_operazione"));
		
		col_multiple_pdf.setCellValueFactory(new PropertyValueFactory<>("stampe_pdf"));
		col_stampa_documento_allegato.setCellValueFactory(new PropertyValueFactory<>("stmpa_doc_allegato"));
		col_stampa_cmr.setCellValueFactory(new PropertyValueFactory<>("stampa_cmr"));
		col_stampa_etichetta_collo.setCellValueFactory(new PropertyValueFactory<>("stampa_etichetta_collo"));
		col_ok.setCellValueFactory(new PropertyValueFactory<>("check_ok"));
		
		/*** REMOVE OPERAZIONE ***/
		Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>> cellFactoryRemoveOperazione = new Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>>() {

			@Override
			public TableCell<Operazione, Button> call(TableColumn<Operazione, Button> param) {
				final TableCell<Operazione, Button> cell = new TableCell<Operazione, Button>() {
					private final Button remove_operazione = new Button("ELIMINA");

                    {
                    	remove_operazione.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                            
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Eliminazione operazione");
                            alert.setHeaderText("Vuoi eliminare l'operazione \"" + operazione.getId() + "\"");

                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if (result.get() == ButtonType.OK){
                                try {
                                	int rowUpdated = operazioniDAO.removeOperazioneById(operazione.getId());
                                	
                                	if (rowUpdated == 1) {
                                		loadData(table_operazioni, true);
                                	}
                                	else {
                                		Alert alert_no_delete = new Alert(AlertType.INFORMATION);
                                		alert_no_delete.setTitle("Eliminazione operazione");
                                		alert_no_delete.setHeaderText("Nessuna operazione eliminata");
                                	}
                                }
                                catch (SQLException e) {
                                	e.printStackTrace();
                                }
                            }
                            else {
                                // ... user chose CANCEL or closed the dialog
                            }
                        });
                    }

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(remove_operazione);
                        }
                    }
				};
				return cell;
			}
		};
		
		/*** EDIT OPERAZIONE ***/
		Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>> cellFactoryEditOperazione = new Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>>() {
            @Override
            public TableCell<Operazione, Button> call(final TableColumn<Operazione, Button> param) {
                final TableCell<Operazione, Button> cell = new TableCell<Operazione, Button>() {

                    private final Button edit_operazione = new Button("COMPILA CMR");

                    {
                    	edit_operazione.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                            
                            try {
                            	FXMLLoader loader = new FXMLLoader();
                    			URL fxmlUrl = getClass().getResource("/resources/fxml/update_operazione.fxml");
                    			loader.setLocation(fxmlUrl);
                    			Parent root = loader.load();
                    			
                    			UpdateOperazioneController updateOperazioneController = loader.getController();
                    			updateOperazioneController.populateFields(operazione.getId(), operazione.getData(), operazione.getCliente(), operazione.getNumerofattura(), operazione.getImportofattura(), operazione.getImportobonifico(), operazione.isEsclusodocsdoganali(), operazione.getNote(), operazione.getNumerocolli(), operazione.getTipoimballo(), operazione.getPesolordo(), operazione.getNotetrasportatore(), operazione.getTrasportatore(), operazione.isStampacmr(), operazione.getDatacarico(), operazione.getSpeddoganale(), operazione.getMrn(), operazione.getInformazioni(), operazione.isOk(), operazione.getNomedest(), operazione.getIndirizzodest(), operazione.getCapdest(), operazione.getCittadest(), operazione.getStatodest());
                    			
                    			Scene scene = new Scene(root, Color.TRANSPARENT);
                    			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
                    			
                    			Stage stage = new Stage();
                    			stage.initStyle(StageStyle.DECORATED);
                    			stage.setTitle("Modifica operazione");
                    			stage.setScene(scene);
                    			stage.show();
                    			
                    			stage.setOnHidden(e -> {
                    				try {
										loadData(table_operazioni, true);
									} catch (SQLException e1) {
										e1.printStackTrace();
									}
								});
                            }
                            catch (Exception e) {
                            	e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(edit_operazione);
                        }
                    }
                };
                return cell;
            }
        };
        
        /*** DOCUMENTO ALLEGATO ***/
		Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>> cellFactoryStampaDocumentoAllegato = new Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>>() {
            @Override
            public TableCell<Operazione, Button> call(final TableColumn<Operazione, Button> param) {
                final TableCell<Operazione, Button> cell = new TableCell<Operazione, Button>() {

                    private final Button stampa_documento_allegato = new Button("DOC. ALLEGATO");

                    {
                    	stampa_documento_allegato.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                    		
                            try {
                            	// SELEZIONE DELLA DIRECTORY DOVE SALVARE IL DOCUMENTO
                            	DirectoryChooser directoryChooser = new DirectoryChooser();
                            	directoryChooser.setTitle("Dove vuoi salvare il documento?");
                            	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
                            	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                            	directoryChooser.setInitialDirectory(new File(currentPath));
                            	Stage stage = (Stage) scroll_pane.getScene().getWindow();
                            	File selectedDirectory = directoryChooser.showDialog(stage);
                                
                            	ClientiDAO clientiDAO = new ClientiDAO();
                                HashMap<String, String> map_cliente = clientiDAO.getInfoCMRByNumeroSiDest(operazione.getCliente());
                                
                                // ... solo se UE, compila "Good Receipt Declaration" (Dichiarazione CMR)
                                if (map_cliente.get("ue").equals("true")) {
                                	
                                	/*** DICHIARAZIONE CMR ***/                            
                                    PdfReader pdfReader = new PdfReader(getClass().getResource("/resources/pdfs/dichiarazione_cmr.pdf"));
                                    String path_to_save = selectedDirectory.getAbsolutePath() + "/DichiarazioneCMR_" + map_cliente.get("nome") + ".pdf";
                                    PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path_to_save));

                                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    
                                    PdfContentByte content = null;
                                    
                                    // MODIFICA DEL DOCUMENTO PDF
                                    content = pdfStamper.getOverContent(1);
                                    
                                    bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    content.setFontAndSize(bf, 24);
                                    
                                    //... sul cliente/operazione
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNomedest(), 40, 720, 0);
                                    content.endText();
                                    
                                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    content.setFontAndSize(bf, 16);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getIndirizzodest(), 40, 690, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCapdest(), 40, 660, 0);
                                    content.endText();
                                    
                                    bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    content.setFontAndSize(bf, 16);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCittadest(), 120, 660, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getStatodest(), 300, 660, 0);
                                    content.endText();
                                    
                                    // "Declares"...
                                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    content.setFontAndSize(bf, 10);
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNumerofattura(), 200, 571, 0);
                                    content.endText();
                                    
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        			String date_str2 = simpleDateFormat.format(operazione.getData());
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, date_str2, 250, 571, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNumerocolli() + " Palette", 135, 551, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, String.valueOf(operazione.getPesolordo()), 270, 551, 0);
                                    content.endText();
                                    
                                    // ... parti da compilare solo se si conosce il Trasportatore
                                    if (operazione.getTrasportatore() != null && !operazione.getTrasportatore().equals("")) {
                                    	TrasportatoriDAO trasportatoriDAO = new TrasportatoriDAO();
                                        HashMap<String, String> map_trasportatore = trasportatoriDAO.getInfoCMRByTrasportatore(operazione.getTrasportatore());
                                        
                                        //... sul trasportatore
                                        content.beginText();
                                        content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatore.get("nome"), 250, 528, 0);
                                        content.endText();
                                        
                                        content.beginText();
                                        content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getIndirizzodest(), 170, 508, 0);
                                        content.endText();
                                        
                                        content.beginText();
                                        content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCittadest(), 280, 508, 0);
                                        content.endText();
                                        
                                        content.beginText();
                                        content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getStatodest(), 420, 508, 0);
                                        content.endText();
                                    }
                                    
                                    pdfStamper.close();
                                    
                                    // STAMPA DEL PDF APPENA GENERATO
                                }          
                                else {	// Generazione della Dichiarazione Doganale
                                	PdfReader pdfReader = new PdfReader(getClass().getResource("/resources/pdfs/dichiarazione_doganale.pdf"));
                                	String path_to_save = selectedDirectory.getAbsolutePath() + "/DichiarazioneDoganale_" + map_cliente.get("nome") + ".pdf";
                                    PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path_to_save));

                                    // MODIFICA DEL DOCUMENTO PDF
                                    BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    
                                    PdfContentByte content = null;
                                    // Pagina 1
                                    content = pdfStamper.getOverContent(1);
                                    content.setFontAndSize(bf, 10);
                                    String nome_cliente = clientiDAO.getNomeByNumero(operazione.getCliente());
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, nome_cliente, 65, 614, 0);
                                    content.endText();
                                    
                                    content.setFontAndSize(bf, 12);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_CENTER, operazione.getNumerofattura(), 423, 632, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        			String date_str = simpleDateFormat.format(operazione.getData());
                                    content.showTextAligned(PdfContentByte.ALIGN_CENTER, date_str, 515, 632, 0);
                                    content.endText();                                
                                    
                                    // Pagina 2
                                    bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                    content = pdfStamper.getOverContent(2);
                                    content.setFontAndSize(bf, 12);
                                    Date today = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_CENTER, sdf.format(today), 205, 169, 0);
                                    content.endText();
                                    
                                    pdfStamper.close();
                                    
                                    // STAMPA DEL PDF APPENA GENERATO
                                    
                                }
                                
                            }
                            catch (Exception e) {
                            	e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(stampa_documento_allegato);
                        }
                    }
                };
                return cell;
            }
        };
        
        /*** STAMPA CMR ***/
		Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>> cellFactoryStampaCMR = new Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>>() {
            @Override
            public TableCell<Operazione, Button> call(final TableColumn<Operazione, Button> param) {
                final TableCell<Operazione, Button> cell = new TableCell<Operazione, Button>() {

                    private final Button stampa_cmr = new Button("CMR");

                    {
                    	stampa_cmr.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                            
                            try {
                            	/*** MODULO CMR ***/
                            	DirectoryChooser directoryChooser = new DirectoryChooser();
                            	directoryChooser.setTitle("Dove vuoi salvare il documento?");
                            	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
                            	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                            	directoryChooser.setInitialDirectory(new File(currentPath));
                            	Stage stage = (Stage) scroll_pane.getScene().getWindow();
                            	File selectedDirectory = directoryChooser.showDialog(stage);
                                
                            	PdfReader pdfReader = new PdfReader(getClass().getResource("/resources/pdfs/modulo_cmr.pdf"));
                            	String path_to_save = selectedDirectory.getAbsolutePath() + "/ModuloCMR_" + operazione.getCliente() + ".pdf";
                                PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path_to_save));

                                // MODIFICA DEL DOCUMENTO PDF
                                BaseFont bf = BaseFont.createFont(BaseFont.TIMES_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                
                                PdfContentByte content = pdfStamper.getOverContent(1);
                                content.setFontAndSize(bf, 12);
                                ClientiDAO clientiDAO = new ClientiDAO();
                                
                                //OperazioniDAO operazioniDAO = new OperazioniDAO();
                                
                                //HashMap<String, String> map_cliente_operazione_dest = operazioniDAO.getInfoCMRByNumeroSiDest(operazione.getCliente());
                                
                                HashMap<String, String> map_cliente_original = clientiDAO.getInfoCMRByNumeroNoDest(operazione.getCliente());
                                
                                // Section 2
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_cliente_original.get("nome"), 34, 695, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_cliente_original.get("indirizzo"), 34, 682, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_cliente_original.get("cap"), 34, 669, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_cliente_original.get("citta"), 100, 669, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_cliente_original.get("stato"), 34, 656, 0);
                                content.endText();
                                
                                // Section 3
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNomedest(), 34, 610, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getIndirizzodest(), 34, 597, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCapdest(), 34, 583, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCittadest(), 100, 583, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getStatodest(), 240, 583, 0);
                                content.endText();
                                
                                content.setFontAndSize(bf, 12);
                                // Section 5
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNumerofattura(), 145, 512, 0);
                                content.endText();
                                
                                content.beginText();
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    			String date_str = simpleDateFormat.format(operazione.getData());
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, date_str, 145, 491, 0);
                                content.endText();
                                
                                // Section 7
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_CENTER, String.valueOf(operazione.getNumerocolli()), 150, 403, 0);
                                content.endText();
                                
                                // Section 8
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_CENTER, operazione.getTipoimballo(), 243, 403, 0);
                                content.endText();
                                
                                // Section 11
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_CENTER, String.valueOf(operazione.getPesolordo()), 470, 401, 0);
                                content.endText();
                                
                                // ... sezioni da compilare solo se si conosce il Trasportatore
                                if (operazione.getTrasportatore() != null && !operazione.getTrasportatore().equals("")) {
                                	TrasportatoriDAO trasportatoriDAO = new TrasportatoriDAO();
                                    HashMap<String, String> map_trasportatori = trasportatoriDAO.getInfoCMRByTrasportatore(operazione.getTrasportatore());
                                    
                                    // Section 13
                                    content.setFontAndSize(bf, 12);
                                    String note_trasportatore = operazione.getNotetrasportatore();
                                    if (note_trasportatore != null && !note_trasportatore.equals("")) {
                                    	String[] note_split = note_trasportatore.split("\\. ");
                                        
                                        float y_2 = 300;
                                        for (int k=0; k<note_split.length; k++) {
                                        	content.beginText();
                                        	content.showTextAligned(PdfContentByte.ALIGN_LEFT, note_split[k], 34, y_2, 0);
                                            content.endText();
                                            
                                            y_2 -= 15;
                                        }
                                    }
                                    
                                    // Section 16
                                    content.setFontAndSize(bf, 8);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("partitaiva"), 508, 689, 0);
                                    content.endText();
                                    
                                    content.setFontAndSize(bf, 12);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getTrasportatore(), 315, 660, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("indirizzo"), 315, 647, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("cap"), 315, 634, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("citta"), 365, 634, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("stato"), 490, 634, 0);
                                    content.endText();
                                    
                                    // Section 23
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getTrasportatore(), 200, 124, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("indirizzo"), 200, 112, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("cap"), 200, 100, 0);
                                    content.endText();
                                    
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("citta"), 245, 100, 0);
                                    content.endText();
                                    
                                    content.setFontAndSize(bf, 8);
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, map_trasportatori.get("stato"), 325, 90, 0);
                                    content.endText();
                                    
                                    content.setFontAndSize(bf, 12);
                                    // Section 21
                                    Date today = new Date();
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, sdf.format(today), 190, 180, 0);
                                    content.endText();
                                }
                                
                                pdfStamper.close();
                                
                                // STAMPA DEL PDF APPENA GENERATO
                                               
                            }
                            catch (Exception e) {
                            	e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(stampa_cmr);
                        }
                    }
                };
                return cell;
            }
        };
        
        /*** STAMPA ETICHETTA COLLO ***/
		Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>> cellFactoryStampaEtichettaCollo = new Callback<TableColumn<Operazione, Button>, TableCell<Operazione, Button>>() {
            @Override
            public TableCell<Operazione, Button> call(final TableColumn<Operazione, Button> param) {
                final TableCell<Operazione, Button> cell = new TableCell<Operazione, Button>() {

                    private final Button stampa_etichetta_collo = new Button("ETICHETTE COLLO");

                    {
                    	stampa_etichetta_collo.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                            
                            try {
                            	// SELEZIONE DELLA DIRECTORY DOVE SALVARE IL DOCUMENTO
                            	DirectoryChooser directoryChooser = new DirectoryChooser();
                            	directoryChooser.setTitle("Dove vuoi salvare il documento?");
                            	//File defaultDirectory = new File("/Users/davide/Desktop/");	// On Windows -> C:/Giunico3/Desktop/
                            	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                            	directoryChooser.setInitialDirectory(new File(currentPath));
                            	Stage stage = (Stage) scroll_pane.getScene().getWindow();
                            	File selectedDirectory = directoryChooser.showDialog(stage);
                                
                            	PdfReader pdfReader = new PdfReader(getClass().getResource("/resources/pdfs/etichetta_collo.pdf"));
                            	String path_to_save = selectedDirectory.getAbsolutePath() + "/EtichettaCollo_" + operazione.getCliente() + ".pdf";
                                PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(path_to_save));

                                // MODIFICA DEL DOCUMENTO PDF
                                BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                
                                PdfContentByte content = pdfStamper.getOverContent(1);
                                
                                String nome = operazione.getNomedest();
                                ArrayList<String> nn = new ArrayList<String>();
                                
                                int MAX = 15;
                                String temp = "";
                                int j = 1;
                                while (j <= nome.length()) {
                                	temp = temp + nome.charAt(j-1);
                                	if (j%MAX == 0) {
                                		nn.add(temp);
                                		temp = "";
                                	}
                                	j++;
                                }
                                nn.add(temp);
                                
                                content.setFontAndSize(bf, 56);
                                float y = 570;
                                for (String nome_part : nn) {
                                	content.beginText();                                
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, nome_part, 23, y, 0);
                                    content.endText();
                                    
                                    y -= 65;
                                }
                                
                                bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                content.setFontAndSize(bf, 30);
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getIndirizzodest(), 23, 420, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCapdest(), 23, 380, 0);
                                content.endText();
                                
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getCittadest(), 160, 380, 0);
                                content.endText();
                                
                                bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.WINANSI, BaseFont.EMBEDDED);
                                content.setFontAndSize(bf, 40);
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getStatodest(), 23, 320, 0);
                                content.endText();
                                
                                content.setFontAndSize(bf, 16);
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getNumerofattura(), 200, 283, 0);
                                content.endText();
                                
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    			String date_str = simpleDateFormat.format(operazione.getData());
                                content.beginText();
                                content.showTextAligned(PdfContentByte.ALIGN_LEFT, date_str, 320, 283, 0);
                                content.endText();
                                
                                // Sezione trasportatore
                                if (operazione.getTrasportatore() != null && !operazione.getTrasportatore().equals("")) {
                                	content.beginText();
                                    content.showTextAligned(PdfContentByte.ALIGN_LEFT, operazione.getTrasportatore(), 100, 252, 0);
                                    content.endText();
                                    
                                    content.setFontAndSize(bf, 26);
                                    String note_trasportatore = operazione.getNotetrasportatore();
                                    if (note_trasportatore != null && !note_trasportatore.equals("")) {
                                    	String[] note_split = note_trasportatore.split("\\. ");
                                        
                                        float y_2 = 200;
                                        for (int k=0; k<note_split.length; k++) {
                                        	content.beginText();
                                        	content.showTextAligned(PdfContentByte.ALIGN_LEFT, note_split[k], 23, y_2, 0);
                                            content.endText();
                                            
                                            y_2 -= 32;
                                        }
                                    }
                                }
                                
                                pdfStamper.close();
                                
                                Document document = new Document();
                                PdfReader reader = new PdfReader(path_to_save);
                                if (reader.getNumberOfPages() > 1) {
                                	PdfCopy copy = new PdfSmartCopy(document, new FileOutputStream(selectedDirectory.getAbsolutePath() + "/temp.pdf"));
                                    document.open();
                                    PdfImportedPage importedPage = copy.getImportedPage(reader, 1);
                                    for(int i=0; i<operazione.getNumerocolli(); i++) {
                                        copy.addPage(importedPage);
                                    }
                                    document.close();
                                    reader.close();
                                    
                                    File f = new File(path_to_save);
                                    File f2 = new File(selectedDirectory.getAbsolutePath() + "/temp.pdf");
                                    f.delete();
                                    f2.renameTo(f);
                                }
                                
                                // STAMPA DEL PDF APPENA GENERATO
                            }
                            catch (Exception e) {
                            	e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(stampa_etichetta_collo);
                        }
                    }
                };
                return cell;
            }
        };
        
        /*** CHECK OK => "stampato_cmr" ***/
		Callback<TableColumn<Operazione, CheckBox>, TableCell<Operazione, CheckBox>> cellFactoryOk = new Callback<TableColumn<Operazione, CheckBox>, TableCell<Operazione, CheckBox>>() {
            @Override
            public TableCell<Operazione, CheckBox> call(final TableColumn<Operazione, CheckBox> param) {
                final TableCell<Operazione, CheckBox> cell = new TableCell<Operazione, CheckBox>() {

                    private final CheckBox check_ok = new CheckBox("OK");

                    {
                    	check_ok.setOnAction((ActionEvent event) -> {
                    		Operazione operazione = getTableView().getItems().get(getIndex());
                            
                            try {
                            	if (check_ok.isSelected()) operazioniDAO.setStampaCMRById(operazione.getId(), true);
                            	else operazioniDAO.setStampaCMRById(operazione.getId(), false);
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
        
        col_nome_cliente.setCellFactory(col -> new TableCell<Operazione, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                }
                else {
                    Operazione operazione = getTableView().getItems().get(getIndex());
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
		    TableCell<Operazione, Date> cell = new TableCell<Operazione, Date>() {
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
		
		col_operazione_importofattura.setCellFactory(col -> new TableCell<Operazione, BigDecimal>() {
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
		col_operazione_importobonifico.setCellFactory(col -> new TableCell<Operazione, BigDecimal>() {
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
		
        col_remove_operazione.setCellFactory(cellFactoryRemoveOperazione);
		col_edit_operazione.setCellFactory(cellFactoryEditOperazione);
		col_stampa_documento_allegato.setCellFactory(cellFactoryStampaDocumentoAllegato);
		col_stampa_cmr.setCellFactory(cellFactoryStampaCMR);
		col_stampa_etichetta_collo.setCellFactory(cellFactoryStampaEtichettaCollo);
		col_ok.setCellFactory(cellFactoryOk);
		
		try {
			loadData(table_operazioni, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		col_multiple_pdf.getColumns().addAll(col_stampa_documento_allegato, col_stampa_cmr, col_stampa_etichetta_collo);
		
		table_operazioni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_operazioni.getColumns().addAll(col_operazione_cliente, col_nome_cliente, col_operazione_statodest, col_operazione_numerofattura, col_operazione_data, col_edit_operazione, col_multiple_pdf, col_ok);
		
		scroll_pane.setContent(table_operazioni);
	}
	
	@FXML
	private void addItems() {
		try {
        	FXMLLoader loader = new FXMLLoader();
        	URL fxmlUrl;
        	Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
        	
			fxmlUrl = getClass().getResource("/resources/fxml/add_operazione.fxml");
    		stage.setTitle("Aggiungi operazione");
			
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
			
			stage.setScene(scene);
			stage.show();
			
			stage.setOnHidden(e -> {
				try {
					loadData(table_operazioni, true);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			});
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
