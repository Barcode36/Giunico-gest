package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import application.AutoCompleteComboBox;
import application.Cliente;
import application.Operazione;
import application.Paese;
import application.TableUtils;
import application.Trasportatore;
import application.database.ClientiDAO;
import application.database.OperazioniDAO;
import application.database.PaesiDAO;
import application.database.TrasportatoriDAO;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class GestioneTabelleController implements Initializable {

	double width = 0;
	double height = 0;
    
	@FXML
	private Tab tab_clienti;
	@FXML
	private Tab tab_paesi;
	@FXML
	private Tab tab_trasportatori;
	@FXML
	private Tab tab_operazioni;
	
	
	@FXML
	private ScrollPane pane_clienti;
	@FXML
	private ScrollPane pane_paesi;
	@FXML
	private ScrollPane pane_trasportatori;
	@FXML
	private ScrollPane pane_operazioni;
	
	
	@FXML
	private Button add_button;
	@FXML
	private Button exit_button;
	
	
	@FXML
	private HBox hbox_1;
	@FXML
	private HBox hbox_2;
	
	ComboBox<Integer> field_cliente;
	AutoCompleteComboBox<Integer> combo_cliente;
	
	ComboBox<String> field_trasportatore;
	AutoCompleteComboBox<String> combo_trasportatore;
	
	ComboBox<String> field_numerofattura;
	AutoCompleteComboBox<String> combo_numerofattura;
	
	TableView<Cliente> table_clienti;
	TableView<Paese> table_paesi;
	TableView<Trasportatore> table_trasportatori;
	TableView<Operazione> table_operazioni;
	
	
	ClientiDAO clientiDAO;
	PaesiDAO paesiDAO;
	TrasportatoriDAO trasportatoriDAO;
	OperazioniDAO operazioniDAO;
	
	Text text_counter;
	
	DecimalFormat decimal_format;
	
	public GestioneTabelleController() {
		field_cliente = new ComboBox<Integer>();
		combo_cliente = new AutoCompleteComboBox<Integer>(field_cliente);
		
		field_trasportatore = new ComboBox<String>();
		combo_trasportatore = new AutoCompleteComboBox<String>(field_trasportatore);
		
		field_numerofattura = new ComboBox<String>();
		combo_numerofattura = new AutoCompleteComboBox<String>(field_numerofattura);
		
		clientiDAO = new ClientiDAO();
		paesiDAO = new PaesiDAO();
		trasportatoriDAO = new TrasportatoriDAO();
		operazioniDAO = new OperazioniDAO();
		
		decimal_format = new DecimalFormat("#,##0.00€");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		text_counter = new Text();
		
		Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
		this.width = resolution.getWidth();
		this.height = resolution.getHeight();
	}
	
	public void loadDataGeneral(TableView<Cliente> table_clienti, TableView<Paese> table_paesi, TableView<Trasportatore> table_trasportatori, TableView<Operazione> table_operazioni, boolean refresh) throws SQLException {
		ObservableList<Cliente> list_clienti = clientiDAO.getAllClientiOrderByNome();
		table_clienti.setItems(list_clienti);
			
		ObservableList<Paese> list_paesi = paesiDAO.getAllPaesiOrderBySigla();
		table_paesi.setItems(list_paesi);
		
		ObservableList<Trasportatore> list_trasportatori = trasportatoriDAO.getAllTrasportatoriOrderByTrasportatore();
		table_trasportatori.setItems(list_trasportatori);
		
		ObservableList<Operazione> list_operazioni = operazioniDAO.getAllOperazioniOrderById();
		table_operazioni.setItems(list_operazioni);
		
		if (refresh) {
			table_clienti.refresh();
			table_paesi.refresh();
			table_trasportatori.refresh();
			table_operazioni.refresh();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		field_cliente.setPromptText("Numero del cliente");
		try {
			ObservableList<Integer> temp_list = clientiDAO.getAllNumeroClienti();
			field_cliente.getItems().addAll(temp_list);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		field_trasportatore.setPromptText("Nome del trasportatore");
		try {
			ObservableList<String> temp_list = trasportatoriDAO.getAllTrasportatoriTrasportatori();
			field_trasportatore.getItems().addAll(temp_list);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		field_numerofattura.setPromptText("Numero della fattura");
		
		try {
			text_counter.setText("Totale clienti: " + clientiDAO.countClienti());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		text_counter.setId("counter");
		hbox_1.getChildren().add(text_counter);
		
		
		field_cliente.setEditable(true);
		field_trasportatore.setEditable(true);
		field_numerofattura.setEditable(true);
		
		field_cliente.getStyleClass().add("field");
		field_trasportatore.getStyleClass().add("field");
		field_numerofattura.getStyleClass().add("field");
		
		field_cliente.setVisible(false);
		field_trasportatore.setVisible(false);
		field_numerofattura.setVisible(false);
		
		hbox_2.getChildren().addAll(field_cliente, new Separator(), field_trasportatore, new Separator(), field_numerofattura);
		
		// PRIMA ERA setOnAction...
		//field_cliente.setOnKeyPressed(this::showOperazioniClienteTrasportatoreNumeroFattura);
		//field_trasportatore.setOnKeyPressed(this::showOperazioniClienteTrasportatoreNumeroFattura);
		//field_numerofattura.setOnKeyPressed(this::showOperazioniClienteTrasportatoreNumeroFattura);
		field_cliente.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				showOperazioniClienteTrasportatoreNumeroFattura();
			}
		});
		field_trasportatore.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				showOperazioniClienteTrasportatoreNumeroFattura();
			}
		});
		field_numerofattura.getEditor().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				showOperazioniClienteTrasportatoreNumeroFattura();
			}
		});
		//field_cliente.setOnAction(this::showOperazioniClienteTrasportatoreNumeroFattura);
		//field_trasportatore.setOnAction(this::showOperazioniClienteTrasportatoreNumeroFattura);
		//field_numerofattura.setOnAction(this::showOperazioniClienteTrasportatoreNumeroFattura);
		
        pane_clienti.setPrefWidth(width);
        pane_paesi.setPrefWidth(width);
        pane_trasportatori.setPrefWidth(width);
        pane_operazioni.setPrefWidth(width);
        
		initTableClienti();
		initTablePaesi();
		initTableTrasportatori();
		initTableOperazioni();
		
		// Set classes and ID for CSS
		table_clienti.getStyleClass().add("table_view");
        table_paesi.getStyleClass().add("table_view");
        table_trasportatori.getStyleClass().add("table_view");
        table_operazioni.getStyleClass().add("table_view");
        
        tab_clienti.getStyleClass().add("tab");
        tab_paesi.getStyleClass().add("tab");
        tab_trasportatori.getStyleClass().add("tab");
        tab_operazioni.getStyleClass().add("tab");
		
		add_button.setText("AGGIUNGI CLIENTE");
	}
	
	//private void showOperazioniClienteTrasportatoreNumeroFattura(ActionEvent event) {
	private void showOperazioniClienteTrasportatoreNumeroFattura() {
		try {
			String str_cliente = String.valueOf(field_cliente.getEditor().getText());
			String trasportatore = String.valueOf(field_trasportatore.getEditor().getText());
			String numerofattura = String.valueOf(field_numerofattura.getEditor().getText());
			
			ObservableList<Operazione> list_operazioni = null;
			ObservableList<Operazione> list_operazioni_trasportatore = null;
			ObservableList<Operazione> list_operazioni_cliente = null;
			ObservableList<Operazione> list_operazioni_numerofattura = null;
			
			table_operazioni.getItems().clear();
			
			if (str_cliente.equals("") || str_cliente == "null"){
				if (trasportatore.equals("") || trasportatore == "null") {
					if (numerofattura.equals("") || numerofattura == "null") {
						list_operazioni = operazioniDAO.getAllOperazioniOrderById();
						table_operazioni.setItems(list_operazioni);
					}
					else {
						list_operazioni_numerofattura = operazioniDAO.getAllOperazioniByNumeroFattura(numerofattura);
						table_operazioni.setItems(list_operazioni_numerofattura);
					}
				}
				else {
					if (numerofattura.equals("") || numerofattura == "null") {
						list_operazioni_trasportatore = operazioniDAO.getAllOperazioniByTrasportatore(trasportatore);
						table_operazioni.setItems(list_operazioni_trasportatore);
					}
					else {
						list_operazioni_numerofattura = operazioniDAO.getAllOperazioniByTrasportatoreAndNumeroFattura(trasportatore, numerofattura);
						table_operazioni.setItems(list_operazioni_numerofattura);
					}
				}
			}
			else {
				int numero_cliente = Integer.parseInt(str_cliente);
				
				if (trasportatore.equals("") || trasportatore == "null") {
					if (numerofattura.equals("") || numerofattura == "null") {
						list_operazioni_cliente = operazioniDAO.getAllOperazioniByCliente(numero_cliente);
						table_operazioni.setItems(list_operazioni_cliente);
					}
					else {
						list_operazioni_numerofattura = operazioniDAO.getAllOperazioniByClienteAndNumeroFattura(numero_cliente, numerofattura);
						table_operazioni.setItems(list_operazioni_numerofattura);
					}
				}
				else {
					if (numerofattura.equals("") || numerofattura == "null") {
						list_operazioni_cliente = operazioniDAO.getAllOperazioniByClienteAndTrasportatore(numero_cliente, trasportatore);
						table_operazioni.setItems(list_operazioni_cliente);
					}
					else {
						list_operazioni_numerofattura = operazioniDAO.getAllOperazioniByClienteAndTrasportatoreAndNumeroFattura(numero_cliente, trasportatore, numerofattura);
						table_operazioni.setItems(list_operazioni_numerofattura);
					}
				}
			}
			table_operazioni.refresh();
		}
		catch (SQLException se) {
			se.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla il valore dei campi!");
			alert.show();
		}
	}
	
	/***********************/
	/*** TABELLA CLIENTI ***/
	/***********************/
	public void loadDataClienti(TableView<Cliente> table_clienti, boolean refresh) throws SQLException {
		ObservableList<Cliente> list_clienti = clientiDAO.getAllClientiOrderByNome();
		table_clienti.setItems(list_clienti);
		
		if (refresh) {
			table_clienti.refresh();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initTableClienti() {
		table_clienti = new TableView<Cliente>();
		table_clienti.setPlaceholder(new Text("Nesun dato da visualizzare"));
		table_clienti.getSelectionModel().setCellSelectionEnabled(true);
		table_clienti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table_clienti);
		
		TableColumn<Cliente, Integer> col_cliente_numero = new TableColumn<Cliente, Integer>("NUMERO");
		TableColumn<Cliente, String> col_cliente_nome = new TableColumn<Cliente, String>("NOME");
		TableColumn<Cliente, String> col_cliente_alias = new TableColumn<Cliente, String>("ALIAS");
		TableColumn<Cliente, String> col_cliente_indirizzo = new TableColumn<Cliente, String>("INDIRIZZO");
		TableColumn<Cliente, String> col_cliente_cap = new TableColumn<Cliente, String>("CAP");
		TableColumn<Cliente, String> col_cliente_citta = new TableColumn<Cliente, String>("CITTA'");
		TableColumn<Cliente, String> col_cliente_paese = new TableColumn<Cliente, String>("PAESE");
		TableColumn<Cliente, String> col_cliente_email = new TableColumn<Cliente, String>("EMAIL");
		TableColumn<Cliente, String> col_cliente_destinazione = new TableColumn<Cliente, String>("DESTINAZIONE");
		TableColumn<Cliente, String> col_cliente_indirizzodest = new TableColumn<Cliente, String>("INDIRIZZO DEST");
		TableColumn<Cliente, String> col_cliente_capdest = new TableColumn<Cliente, String>("CAP DEST");
		TableColumn<Cliente, String> col_cliente_cittadest = new TableColumn<Cliente, String>("CITTA' DEST");
		TableColumn<Cliente, String> col_cliente_statodest = new TableColumn<Cliente, String>("STATO DEST");
		TableColumn<Cliente, Button> col_remove_cliente = new TableColumn<Cliente, Button>("ELIMINA");
		TableColumn<Cliente, Button> col_edit_cliente = new TableColumn<Cliente, Button>("MODIFICA");
		
		col_cliente_numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		col_cliente_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		col_cliente_alias.setCellValueFactory(new PropertyValueFactory<>("alias"));
		col_cliente_indirizzo.setCellValueFactory(new PropertyValueFactory<>("indirizzo"));
		col_cliente_cap.setCellValueFactory(new PropertyValueFactory<>("cap"));
		col_cliente_citta.setCellValueFactory(new PropertyValueFactory<>("citta"));
		col_cliente_paese.setCellValueFactory(new PropertyValueFactory<>("paese"));
		col_cliente_email.setCellValueFactory(new PropertyValueFactory<>("email"));
		col_cliente_destinazione.setCellValueFactory(new PropertyValueFactory<>("destinazione"));
		col_cliente_indirizzodest.setCellValueFactory(new PropertyValueFactory<>("indirizzodest"));
		col_cliente_capdest.setCellValueFactory(new PropertyValueFactory<>("capdest"));
		col_cliente_cittadest.setCellValueFactory(new PropertyValueFactory<>("cittadest"));
		col_cliente_statodest.setCellValueFactory(new PropertyValueFactory<>("statodest"));
		col_remove_cliente.setCellValueFactory(new PropertyValueFactory<>("remove_cliente"));
		col_edit_cliente.setCellValueFactory(new PropertyValueFactory<>("edit_cliente"));
		
		/*** REMOVE CLIENTE ***/
		Callback<TableColumn<Cliente, Button>, TableCell<Cliente, Button>> cellFactoryRemoveCliente = new Callback<TableColumn<Cliente, Button>, TableCell<Cliente, Button>>() {

			@Override
			public TableCell<Cliente, Button> call(TableColumn<Cliente, Button> param) {
				final TableCell<Cliente, Button> cell = new TableCell<Cliente, Button>() {
					private final Button remove_cliente = new Button("ELIMINA");

                    {
                    	remove_cliente.setOnAction((ActionEvent event) -> {
                            Cliente cliente = getTableView().getItems().get(getIndex());
                            
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Eliminazione cliente");
                            alert.setHeaderText("Vuoi eliminare il cliente \"" + cliente.getNome() + "\"");

                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if (result.get() == ButtonType.OK){
                                try {
                                	int rowUpdated = clientiDAO.removeClienteByNumero(cliente.getNumero());
                                	
                                	if (rowUpdated == 1) {
                                		loadDataClienti(table_clienti, true);
                                	}
                                	else {
                                		Alert alert_no_delete = new Alert(AlertType.INFORMATION);
                                		alert_no_delete.setTitle("Eliminazione cliente");
                                		alert_no_delete.setHeaderText("Nessun cliente eliminato");
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
                            setGraphic(remove_cliente);
                        }
                    }
				};
				return cell;
			}
		};
		
		/*** EDIT CLIENTE ***/
		Callback<TableColumn<Cliente, Button>, TableCell<Cliente, Button>> cellFactoryEditCliente = new Callback<TableColumn<Cliente, Button>, TableCell<Cliente, Button>>() {
            @Override
            public TableCell<Cliente, Button> call(final TableColumn<Cliente, Button> param) {
                final TableCell<Cliente, Button> cell = new TableCell<Cliente, Button>() {

                    private final Button edit_cliente = new Button("MODIFICA");

                    {
                    	edit_cliente.setOnAction((ActionEvent event) -> {
                            Cliente cliente = getTableView().getItems().get(getIndex());
                            
                            try {
                            	FXMLLoader loader = new FXMLLoader();
                    			URL fxmlUrl = getClass().getResource("/resources/fxml/update_cliente.fxml");
                    			loader.setLocation(fxmlUrl);
                    			Parent root = loader.load();
                    			
                    			UpdateClienteController updateClienteController = loader.getController();
                    			updateClienteController.populateFields(cliente.getNumero(), cliente.getNome(), cliente.getAlias(), cliente.getIndirizzo(), cliente.getCap(), cliente.getCitta(), cliente.getPaese(), cliente.getEmail(), cliente.getDestinazione(), cliente.getIndirizzodest(), cliente.getCapdest(), cliente.getCittadest(), cliente.getStatodest());
                    			
                    			Scene scene = new Scene(root, Color.TRANSPARENT);
                    			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
                    			
                    			Stage stage = new Stage();
                    			stage.initStyle(StageStyle.DECORATED);
                    			stage.setTitle("Modifica cliente");
                    			stage.setScene(scene);
                    			stage.show();
                    			
                    			stage.setOnHidden(e -> {
                    				try {
										loadDataClienti(table_clienti, true);
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
                            setGraphic(edit_cliente);
                        }
                    }
                };
                return cell;
            }
        };
        
		col_remove_cliente.setCellFactory(cellFactoryRemoveCliente);
		col_edit_cliente.setCellFactory(cellFactoryEditCliente);
		
		try {
			loadDataClienti(table_clienti, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table_clienti.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_clienti.getColumns().addAll(col_remove_cliente, col_edit_cliente, col_cliente_numero, col_cliente_nome, col_cliente_alias, col_cliente_indirizzo, col_cliente_cap, col_cliente_citta, col_cliente_paese, col_cliente_email, col_cliente_destinazione, col_cliente_indirizzodest, col_cliente_capdest, col_cliente_cittadest, col_cliente_statodest);
		pane_clienti.setContent(table_clienti);
	}
	
	/*********************/
	/*** TABELLA PAESI ***/
	/*********************/
	public void loadDataPaesi(TableView<Paese> table_paesi, boolean refresh) throws SQLException {
		ObservableList<Paese> list_paesi = paesiDAO.getAllPaesiOrderBySigla();
		table_paesi.setItems(list_paesi);
		
		if (refresh) {
			table_paesi.refresh();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initTablePaesi() {
		table_paesi = new TableView<Paese>();
		table_paesi.setPlaceholder(new Text("Nesun dato da visualizzare"));
		table_paesi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table_paesi.getSelectionModel().setCellSelectionEnabled(true);
		table_paesi.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table_paesi);
		
		TableColumn<Paese, String> col_paese_sigla = new TableColumn<Paese, String>("SIGLA");
		TableColumn<Paese, String> col_paese_stato = new TableColumn<Paese, String>("STATO");
		TableColumn<Paese, Boolean> col_paese_ue = new TableColumn<Paese, Boolean>("UE/EXTRA-UE");
		TableColumn<Paese, Button> col_remove_paese = new TableColumn<Paese, Button>("ELIMINA");
		TableColumn<Paese, Button> col_edit_paese = new TableColumn<Paese, Button>("MODIFICA");
		
		col_paese_sigla.setCellValueFactory(new PropertyValueFactory<>("sigla"));
		col_paese_stato.setCellValueFactory(new PropertyValueFactory<>("stato"));
		col_paese_ue.setCellValueFactory(new PropertyValueFactory<>("ue"));
		col_remove_paese.setCellValueFactory(new PropertyValueFactory<>("remove_paese"));
		col_edit_paese.setCellValueFactory(new PropertyValueFactory<>("edit_paese"));
		
		/*** REMOVE PAESE ***/
		Callback<TableColumn<Paese, Button>, TableCell<Paese, Button>> cellFactoryRemovePaese = new Callback<TableColumn<Paese, Button>, TableCell<Paese, Button>>() {

			@Override
			public TableCell<Paese, Button> call(TableColumn<Paese, Button> param) {
				final TableCell<Paese, Button> cell = new TableCell<Paese, Button>() {
					private final Button remove_paese = new Button("ELIMINA");

                    {
                    	remove_paese.setOnAction((ActionEvent event) -> {
                            Paese paese = getTableView().getItems().get(getIndex());
                            
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Eliminazione paese");
                            alert.setHeaderText("Vuoi eliminare il paese \"" + paese.getSigla() + "\"");

                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if (result.get() == ButtonType.OK){
                                try {
                                	int rowUpdated = paesiDAO.removePaeseBySigla(paese.getSigla());
                                	
                                	if (rowUpdated == 1) {
                                		loadDataPaesi(table_paesi, true);
                                	}
                                	else {
                                		Alert alert_no_delete = new Alert(AlertType.INFORMATION);
                                		alert_no_delete.setTitle("Eliminazione paese");
                                		alert_no_delete.setHeaderText("Nessun paese eliminato");
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
                            setGraphic(remove_paese);
                        }
                    }
				};
				return cell;
			}
		};
		
		/*** EDIT PAESE ***/
		Callback<TableColumn<Paese, Button>, TableCell<Paese, Button>> cellFactoryEditPaese = new Callback<TableColumn<Paese, Button>, TableCell<Paese, Button>>() {
            @Override
            public TableCell<Paese, Button> call(final TableColumn<Paese, Button> param) {
                final TableCell<Paese, Button> cell = new TableCell<Paese, Button>() {

                    private final Button edit_paese = new Button("MODIFICA");

                    {
                    	edit_paese.setOnAction((ActionEvent event) -> {
                    		Paese paese = getTableView().getItems().get(getIndex());
                            
                            try {
                            	FXMLLoader loader = new FXMLLoader();
                    			URL fxmlUrl = getClass().getResource("/resources/fxml/update_paese.fxml");
                    			loader.setLocation(fxmlUrl);
                    			Parent root = loader.load();
                    			
                    			UpdatePaeseController updatePaeseController = loader.getController();
                    			updatePaeseController.populateFields(paese.getSigla(), paese.getStato(), paese.isUe());
                    			
                    			Scene scene = new Scene(root, Color.TRANSPARENT);
                    			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
                    			
                    			Stage stage = new Stage();
                    			stage.initStyle(StageStyle.DECORATED);
                    			stage.setTitle("Modifica paese");
                    			stage.setScene(scene);
                    			stage.show();
                    			
                    			stage.setOnHidden(e -> {
                    				try {
										loadDataPaesi(table_paesi, true);
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
                            setGraphic(edit_paese);
                        }
                    }
                };
                return cell;
            }
        };
        
        col_paese_ue.setCellFactory(column -> {
		    TableCell<Paese, Boolean> cell = new TableCell<Paese, Boolean>() {

		        @Override
		        protected void updateItem(Boolean ue, boolean empty) {
		            super.updateItem(ue, empty);
		            if(empty) {
		                setText(null);
		            }
		            else {
		                setText(ue? "UE" : "extra-UE");
		            }
		        }
		    };

		    return cell;
		});
        
        col_remove_paese.setCellFactory(cellFactoryRemovePaese);
		col_edit_paese.setCellFactory(cellFactoryEditPaese);
		
		try {
			loadDataPaesi(table_paesi, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table_paesi.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_paesi.getColumns().addAll(col_remove_paese, col_edit_paese, col_paese_sigla, col_paese_stato, col_paese_ue);
		pane_paesi.setContent(table_paesi);
	}
	
	/*****************************/
	/*** TABELLA TRASPORTATORI ***/
	/*****************************/
	public void loadDataTrasportatori(TableView<Trasportatore> table_trasportatori, boolean refresh) throws SQLException {
		ObservableList<Trasportatore> list_trasportatori = trasportatoriDAO.getAllTrasportatoriOrderByTrasportatore();
		table_trasportatori.setItems(list_trasportatori);
		
		if (refresh) {
			table_trasportatori.refresh();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initTableTrasportatori() {
		table_trasportatori = new TableView<Trasportatore>();
		table_trasportatori.setPlaceholder(new Text("Nesun dato da visualizzare"));
		table_trasportatori.getSelectionModel().setCellSelectionEnabled(true);
		table_trasportatori.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table_trasportatori);
		
		TableColumn<Trasportatore, String> col_trasportatore_trasportatore = new TableColumn<Trasportatore, String>("TRASPORTATORE");
		TableColumn<Trasportatore, String> col_trasportatore_nome = new TableColumn<Trasportatore, String>("NOME");
		TableColumn<Trasportatore, String> col_trasportatore_indirizzo = new TableColumn<Trasportatore, String>("INDIRIZZO");
		TableColumn<Trasportatore, String> col_trasportatore_cap = new TableColumn<Trasportatore, String>("CAP");
		TableColumn<Trasportatore, String> col_trasportatore_citta = new TableColumn<Trasportatore, String>("CITTA");
		TableColumn<Trasportatore, String> col_trasportatore_paese = new TableColumn<Trasportatore, String>("PAESE");
		TableColumn<Trasportatore, String> col_trasportatore_stato = new TableColumn<Trasportatore, String>("STATO");
		TableColumn<Trasportatore, String> col_trasportatore_partitaiva = new TableColumn<Trasportatore, String>("PARTITA IVA");
		TableColumn<Trasportatore, String> col_trasportatore_iscrizionealbo = new TableColumn<Trasportatore, String>("ISCRIZIONE ALBO");
		
		TableColumn<Trasportatore, String> col_multiple_mailritiro = new TableColumn<Trasportatore, String>("EMAIL PER RITIRO");
		TableColumn<Trasportatore, String> col_trasportatore_mail1ritiro = new TableColumn<Trasportatore, String>("EMAIL 1");
		TableColumn<Trasportatore, String> col_trasportatore_mail2ritiro = new TableColumn<Trasportatore, String>("EMAIL 2");
		
		TableColumn<Trasportatore, String> col_multiple_maildocs = new TableColumn<Trasportatore, String>("EMAIL PER DOCUMENTI");
		TableColumn<Trasportatore, String> col_trasportatore_mail1docs = new TableColumn<Trasportatore, String>("EMAIL 1");
		TableColumn<Trasportatore, String> col_trasportatore_mail2docs = new TableColumn<Trasportatore, String>("EMAIL 2");
		TableColumn<Trasportatore, String> col_trasportatore_note = new TableColumn<Trasportatore, String>("NOTE");
		
		TableColumn<Trasportatore, Button> col_remove_trasportatore = new TableColumn<Trasportatore, Button>("ELIMINA");
		TableColumn<Trasportatore, Button> col_edit_trasportatore = new TableColumn<Trasportatore, Button>("MODIFICA");
		
		col_trasportatore_trasportatore.setCellValueFactory(new PropertyValueFactory<>("trasportatore"));
		col_trasportatore_nome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		col_trasportatore_indirizzo.setCellValueFactory(new PropertyValueFactory<>("indirizzo"));
		col_trasportatore_cap.setCellValueFactory(new PropertyValueFactory<>("cap"));
		col_trasportatore_citta.setCellValueFactory(new PropertyValueFactory<>("citta"));
		col_trasportatore_paese.setCellValueFactory(new PropertyValueFactory<>("paese"));
		col_trasportatore_stato.setCellValueFactory(new PropertyValueFactory<>("stato"));
		col_trasportatore_partitaiva.setCellValueFactory(new PropertyValueFactory<>("partitaiva"));
		col_trasportatore_iscrizionealbo.setCellValueFactory(new PropertyValueFactory<>("iscrizionealbo"));
		col_multiple_mailritiro.setCellValueFactory(new PropertyValueFactory<>("multiple_email_ritiro"));
		col_trasportatore_mail1ritiro.setCellValueFactory(new PropertyValueFactory<>("mail1ritiro"));
		col_trasportatore_mail2ritiro.setCellValueFactory(new PropertyValueFactory<>("mail2ritiro"));
		col_multiple_maildocs.setCellValueFactory(new PropertyValueFactory<>("multiple_email_docs"));
		col_trasportatore_mail1docs.setCellValueFactory(new PropertyValueFactory<>("mail1docs"));
		col_trasportatore_mail2docs.setCellValueFactory(new PropertyValueFactory<>("mail2docs"));
		col_trasportatore_note.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		col_remove_trasportatore.setCellValueFactory(new PropertyValueFactory<>("remove_trasportatore"));
		col_edit_trasportatore.setCellValueFactory(new PropertyValueFactory<>("edit_trasportatore"));
		
		/*** REMOVE TRASPORTATORE ***/
		Callback<TableColumn<Trasportatore, Button>, TableCell<Trasportatore, Button>> cellFactoryRemoveTrasportatore = new Callback<TableColumn<Trasportatore, Button>, TableCell<Trasportatore, Button>>() {

			@Override
			public TableCell<Trasportatore, Button> call(TableColumn<Trasportatore, Button> param) {
				final TableCell<Trasportatore, Button> cell = new TableCell<Trasportatore, Button>() {
					private final Button remove_trasportatore = new Button("ELIMINA");

                    {
                    	remove_trasportatore.setOnAction((ActionEvent event) -> {
                            Trasportatore trasportatore = getTableView().getItems().get(getIndex());
                            
                            Alert alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Eliminazione trasportatore");
                            alert.setHeaderText("Vuoi eliminare il trasportatore \"" + trasportatore.getTrasportatore() + "\"");

                            Optional<ButtonType> result = alert.showAndWait();
                            
                            if (result.get() == ButtonType.OK){
                                try {
                                	int rowUpdated = trasportatoriDAO.removeTrasportatoreByTrasportatore(trasportatore.getTrasportatore());
                                	
                                	if (rowUpdated == 1) {
                                		loadDataTrasportatori(table_trasportatori, true);
                                	}
                                	else {
                                		Alert alert_no_delete = new Alert(AlertType.INFORMATION);
                                		alert_no_delete.setTitle("Eliminazione trasportatore");
                                		alert_no_delete.setHeaderText("Nessun trasportatore eliminato");
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
                            setGraphic(remove_trasportatore);
                        }
                    }
				};
				return cell;
			}
		};
		
		/*** EDIT TRASPORTATORE ***/
		Callback<TableColumn<Trasportatore, Button>, TableCell<Trasportatore, Button>> cellFactoryEditTrasportatore = new Callback<TableColumn<Trasportatore, Button>, TableCell<Trasportatore, Button>>() {
            @Override
            public TableCell<Trasportatore, Button> call(final TableColumn<Trasportatore, Button> param) {
                final TableCell<Trasportatore, Button> cell = new TableCell<Trasportatore, Button>() {

                    private final Button edit_trasportatore = new Button("MODIFICA");

                    {
                    	edit_trasportatore.setOnAction((ActionEvent event) -> {
                    		Trasportatore trasportatore = getTableView().getItems().get(getIndex());
                            
                            try {
                            	FXMLLoader loader = new FXMLLoader();
                    			URL fxmlUrl = getClass().getResource("/resources/fxml/update_trasportatore.fxml");
                    			loader.setLocation(fxmlUrl);
                    			Parent root = loader.load();
                    			
                    			UpdateTrasportatoreController updateTrasportatoreController = loader.getController();
                    			updateTrasportatoreController.populateFields(trasportatore.getTrasportatore(), trasportatore.getNome(), trasportatore.getIndirizzo(), trasportatore.getCap(), trasportatore.getCitta(), trasportatore.getPaese(), trasportatore.getStato(), trasportatore.getPartitaiva(), trasportatore.getIscrizionealbo(), trasportatore.getMail1ritiro(), trasportatore.getMail2ritiro(), trasportatore.getMail1docs(), trasportatore.getMail2docs(), trasportatore.getNote());
                    			
                    			Scene scene = new Scene(root, Color.TRANSPARENT);
                    			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
                    			
                    			Stage stage = new Stage();
                    			stage.initStyle(StageStyle.DECORATED);
                    			stage.setTitle("Modifica trasportatore");
                    			stage.setScene(scene);
                    			stage.show();
                    			
                    			stage.setOnHidden(e -> {
                    				try {
										loadDataTrasportatori(table_trasportatori, true);
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
                            setGraphic(edit_trasportatore);
                        }
                    }
                };
                return cell;
            }
        };
        
        col_multiple_mailritiro.getColumns().addAll(col_trasportatore_mail1ritiro, col_trasportatore_mail2ritiro);
        col_multiple_maildocs.getColumns().addAll(col_trasportatore_mail1docs, col_trasportatore_mail2docs);
        col_remove_trasportatore.setCellFactory(cellFactoryRemoveTrasportatore);
		col_edit_trasportatore.setCellFactory(cellFactoryEditTrasportatore);
		
		try {
			loadDataTrasportatori(table_trasportatori, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table_trasportatori.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_trasportatori.getColumns().addAll(col_remove_trasportatore, col_edit_trasportatore, col_trasportatore_trasportatore, col_trasportatore_nome, col_trasportatore_indirizzo, col_trasportatore_cap, col_trasportatore_citta, col_trasportatore_paese, col_trasportatore_stato, col_trasportatore_partitaiva, col_trasportatore_iscrizionealbo, col_multiple_mailritiro, col_multiple_maildocs, col_trasportatore_note);
		pane_trasportatori.setContent(table_trasportatori);
	}
	
	/**************************/
	/*** TABELLA OPERAZIONI ***/
	/**************************/
	public void loadDataOperazioni(TableView<Operazione> table_operazioni, boolean refresh) throws SQLException {
		ObservableList<Operazione> list_operazioni = operazioniDAO.getAllOperazioniOrderById();
		table_operazioni.setItems(list_operazioni);
		
		if (refresh) {
			table_operazioni.refresh();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void initTableOperazioni() {
		table_operazioni = new TableView<Operazione>();
		table_operazioni.setPlaceholder(new Text("Nesun dato da visualizzare"));
		table_operazioni.getSelectionModel().setCellSelectionEnabled(true);
		table_operazioni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table_operazioni);
		
		TableColumn<Operazione, Integer> col_operazione_id = new TableColumn<Operazione, Integer>("ID");
		TableColumn<Operazione, Date> col_operazione_data = new TableColumn<Operazione, Date>("DATA");
		TableColumn<Operazione, Integer> col_operazione_cliente = new TableColumn<Operazione, Integer>("CLIENTE");
		TableColumn<Operazione, String> col_operazione_numerofattura = new TableColumn<Operazione, String>("NUMERO FATTURA");
		TableColumn<Operazione, BigDecimal> col_operazione_importofattura = new TableColumn<Operazione, BigDecimal>("IMPORTO FATTURA");
		TableColumn<Operazione, BigDecimal> col_operazione_importobonifico = new TableColumn<Operazione, BigDecimal>("IMPORTO BONIFICO");
		TableColumn<Operazione, Boolean> col_operazione_esclusodocsdoganali = new TableColumn<Operazione, Boolean>("ESCLUSO DOC. DOGANALI");
		TableColumn<Operazione, String> col_operazione_note = new TableColumn<Operazione, String>("NOTE");
		TableColumn<Operazione, Integer> col_operazione_numerocolli = new TableColumn<Operazione, Integer>("NUMERO COLLI");
		TableColumn<Operazione, String> col_operazione_tipoimballo = new TableColumn<Operazione, String>("TIPO IMBALLO");
		TableColumn<Operazione, Float> col_operazione_pesolordo = new TableColumn<Operazione, Float>("PESO LORDO (Kg)");
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
		TableColumn<Operazione, Button> col_edit_operazione = new TableColumn<Operazione, Button>("MODIFICA");
		
		col_operazione_id.setCellValueFactory(new PropertyValueFactory<>("id"));
		col_operazione_data.setCellValueFactory(new PropertyValueFactory<>("data"));
		col_operazione_cliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
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
                                		loadDataOperazioni(table_operazioni, true);
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

                    private final Button edit_operazione = new Button("MODIFICA");

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
										loadDataOperazioni(table_operazioni, true);
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
        col_operazione_datacarico.setCellFactory(column -> {
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
        col_operazione_esclusodocsdoganali.setCellFactory(column -> {
		    TableCell<Operazione, Boolean> cell = new TableCell<Operazione, Boolean>() {

		        @Override
		        protected void updateItem(Boolean item, boolean empty) {
		            super.updateItem(item, empty);
		            if(empty) {
		                setText(null);
		            }
		            else {
		                setText(item? "SI" : "NO");
		            }
		        }
		    };

		    return cell;
		});
        col_operazione_stampacmr.setCellFactory(column -> {
		    TableCell<Operazione, Boolean> cell = new TableCell<Operazione, Boolean>() {

		        @Override
		        protected void updateItem(Boolean item, boolean empty) {
		            super.updateItem(item, empty);
		            if(empty) {
		                setText(null);
		            }
		            else {
		                setText(item? "SI" : "NO");
		            }
		        }
		    };

		    return cell;
		});
        col_operazione_ok.setCellFactory(column -> {
		    TableCell<Operazione, Boolean> cell = new TableCell<Operazione, Boolean>() {

		        @Override
		        protected void updateItem(Boolean item, boolean empty) {
		            super.updateItem(item, empty);
		            if(empty) {
		                setText(null);
		            }
		            else {
		                setText(item? "SI" : "NO");
		            }
		        }
		    };

		    return cell;
		});
        
        col_remove_operazione.setCellFactory(cellFactoryRemoveOperazione);
		col_edit_operazione.setCellFactory(cellFactoryEditOperazione);
		
		try {
			loadDataOperazioni(table_operazioni, false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		table_operazioni.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_operazioni.getColumns().addAll(col_remove_operazione, col_edit_operazione, col_operazione_id, col_operazione_data, col_operazione_cliente, col_operazione_numerofattura, col_operazione_importofattura, col_operazione_esclusodocsdoganali, col_operazione_importobonifico, col_operazione_note, col_operazione_nomedest, col_operazione_indirizzodest, col_operazione_capdest, col_operazione_cittadest, col_operazione_statodest, col_operazione_numerocolli, col_operazione_tipoimballo, col_operazione_pesolordo, col_operazione_trasportatore, col_operazione_notetrasportatore, col_operazione_datacarico, col_operazione_speddoganale, col_operazione_mrn, col_operazione_informazioni, col_operazione_stampacmr, col_operazione_ok);
		pane_operazioni.setContent(table_operazioni);
	}
	

	@FXML
	private void changedTab() {
		if (tab_clienti.isSelected()) {
			field_cliente.setVisible(false);
			field_trasportatore.setVisible(false);
			field_numerofattura.setVisible(false);
			if (add_button != null) {
				add_button.setVisible(true);
				add_button.setText("AGGIUNGI CLIENTE");
			}
			try {
				text_counter.setText("Totale clienti: " + clientiDAO.countClienti());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (tab_paesi.isSelected()){
			field_cliente.setVisible(false);
			field_trasportatore.setVisible(false);
			field_numerofattura.setVisible(false);
			if (add_button != null) {
				add_button.setVisible(true);
				add_button.setText("AGGIUNGI PAESE");
			}
			try {
				text_counter.setText("Totale paesi: " + paesiDAO.countPaesi());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else if (tab_trasportatori.isSelected()){
			field_cliente.setVisible(false);
			field_trasportatore.setVisible(false);
			field_numerofattura.setVisible(false);
			if (add_button != null) {
				add_button.setVisible(true);
				add_button.setText("AGGIUNGI TRASPORTATORE");
			}
			try {
				text_counter.setText("Totale trasportatori: " + trasportatoriDAO.countTrasportatori());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			field_cliente.setVisible(true);
			field_trasportatore.setVisible(true);
			field_numerofattura.setVisible(true);
			if (add_button != null) add_button.setVisible(false);
			try {
				text_counter.setText("Totale operazioni: " + operazioniDAO.countOperazioni());
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void addItems() {
		try {
        	FXMLLoader loader = new FXMLLoader();
        	URL fxmlUrl;
        	Stage stage = new Stage();
			stage.initStyle(StageStyle.DECORATED);
        	
        	if (tab_clienti.isSelected()) {
        		fxmlUrl = getClass().getResource("/resources/fxml/add_cliente.fxml");
        		stage.setTitle("Aggiungi cliente");
        	}
        	else if (tab_paesi.isSelected()){
        		fxmlUrl = getClass().getResource("/resources/fxml/add_paese.fxml");
        		stage.setTitle("Aggiungi paese");
        	}
        	else if (tab_trasportatori.isSelected()){
        		fxmlUrl = getClass().getResource("/resources/fxml/add_trasportatore.fxml");
        		stage.setTitle("Aggiungi trasportatore");
        	}
        	else {
        		fxmlUrl = getClass().getResource("/resources/fxml/add_operazione.fxml");
        		stage.setTitle("Aggiungi operazione");
        	}
			
			loader.setLocation(fxmlUrl);
			Parent root = loader.load();
			
			Scene scene = new Scene(root, Color.TRANSPARENT);
			scene.getStylesheets().add(getClass().getResource("/resources/css/tabelle.css").toExternalForm());
			
			stage.setScene(scene);
			stage.show();
			
			stage.setOnHidden(e -> {
				try {
					loadDataGeneral(table_clienti, table_paesi, table_trasportatori, table_operazioni, true);
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
