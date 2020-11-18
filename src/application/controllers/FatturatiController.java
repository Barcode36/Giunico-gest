package application.controllers;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import application.AutoCompleteComboBox;
import application.ClienteFatturato;
import application.database.ClientiDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class FatturatiController implements Initializable {
	
	double width = 0;
    double height = 0;
    
    @FXML
    private BorderPane border_pane;
    
    @FXML
    private HBox hbox_1;
    @FXML
    private HBox hbox_2;
    
    @FXML
    private VBox vbox;
    
    @FXML
	private Button exit_button;
    
    ComboBox<Integer> field_cliente;
	AutoCompleteComboBox<Integer> combo_cliente;
	
	DatePicker field_data_iniziale;
	DatePicker field_data_finale;
	
	@FXML
	private ScrollPane scroll_pane;
	
	private TableView<ClienteFatturato> table_fatturati;
	
	@FXML
	private ListView<String> list_recap;
	
	ClientiDAO clientiDAO;
	DecimalFormat decimal_format;
	
	ZoneId defaultZoneId = ZoneId.of("Europe/Rome");
	
	public FatturatiController() {
		field_cliente = new ComboBox<Integer>();
		combo_cliente = new AutoCompleteComboBox<Integer>(field_cliente);
		
		clientiDAO = new ClientiDAO();
		decimal_format = new DecimalFormat("#,##0.00€");
		decimal_format.setDecimalFormatSymbols((DecimalFormatSymbols.getInstance(Locale.ITALIAN)));
		decimal_format.setDecimalSeparatorAlwaysShown(true);
		
		field_data_iniziale = new DatePicker();
		field_data_iniziale.setPromptText("Data iniziale");
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
		
		field_data_finale = new DatePicker();
		field_data_finale.setPromptText("Data finale");
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
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		hbox_1.getChildren().add(field_cliente);
		hbox_2.getChildren().addAll(field_data_iniziale, new Separator(), field_data_finale);
		
		field_data_iniziale.setVisible(true);
		field_data_iniziale.getStyleClass().add("field");
		field_data_iniziale.setOnAction(this::showFatturatiCliente);
		
		field_data_finale.setVisible(true);
		field_data_finale.getStyleClass().add("field");
		field_data_finale.setOnAction(this::showFatturatiCliente);
		
		field_cliente.setVisible(true);
		field_cliente.getStyleClass().add("field");
		field_cliente.setOnAction(this::showFatturatiCliente);
		
		initTable();
	}
	
	@SuppressWarnings("unchecked")
	private void initTable() {
		table_fatturati = new TableView<ClienteFatturato>();
		table_fatturati.setPlaceholder(new Text("Nessun dato da visualizzare"));
		table_fatturati.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table_fatturati.getStyleClass().add("table_view");
		table_fatturati.getSelectionModel().setCellSelectionEnabled(true);
		table_fatturati.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		list_recap.setPlaceholder(new Text("Nessun dato da visualizzare"));
		list_recap.getStyleClass().add("list_view");
		list_recap.setMaxWidth(width);
		list_recap.setPrefWidth(width);
		list_recap.setPrefHeight(90);
		//list_recap.autosize();
		//list_recap.setFixedCellSize(100);
		//list_recap.setMaxWidth(list_recap.getItems().size() * 100);
		
		TableColumn<ClienteFatturato, String> col_cliente_numero = new TableColumn<ClienteFatturato, String>("NUMERO CLIENTE");
		TableColumn<ClienteFatturato, BigDecimal> col_gennaio = new TableColumn<ClienteFatturato, BigDecimal>("GENNAIO");
		TableColumn<ClienteFatturato, BigDecimal> col_febbraio = new TableColumn<ClienteFatturato, BigDecimal>("FEBBRAIO");
		TableColumn<ClienteFatturato, BigDecimal> col_marzo = new TableColumn<ClienteFatturato, BigDecimal>("MARZO");
		TableColumn<ClienteFatturato, BigDecimal> col_aprile = new TableColumn<ClienteFatturato, BigDecimal>("APRILE");
		TableColumn<ClienteFatturato, BigDecimal> col_maggio = new TableColumn<ClienteFatturato, BigDecimal>("MAGGIO");
		TableColumn<ClienteFatturato, BigDecimal> col_giugno = new TableColumn<ClienteFatturato, BigDecimal>("GIUGNO");
		TableColumn<ClienteFatturato, BigDecimal> col_luglio = new TableColumn<ClienteFatturato, BigDecimal>("LUGLIO");
		TableColumn<ClienteFatturato, BigDecimal> col_agosto = new TableColumn<ClienteFatturato, BigDecimal>("AGOSTO");
		TableColumn<ClienteFatturato, BigDecimal> col_settembre = new TableColumn<ClienteFatturato, BigDecimal>("SETTEMBRE");
		TableColumn<ClienteFatturato, BigDecimal> col_ottobre = new TableColumn<ClienteFatturato, BigDecimal>("OTTOBRE");
		TableColumn<ClienteFatturato, BigDecimal> col_novembre = new TableColumn<ClienteFatturato, BigDecimal>("NOVEMBRE");
		TableColumn<ClienteFatturato, BigDecimal> col_dicembre = new TableColumn<ClienteFatturato, BigDecimal>("DICEMBRE");
		
		col_cliente_numero.setId("column_cliente");
		col_cliente_numero.setCellValueFactory(new PropertyValueFactory<>("numero"));
		col_gennaio.setCellValueFactory(new PropertyValueFactory<>("fatturato_gennaio"));
		col_febbraio.setCellValueFactory(new PropertyValueFactory<>("fatturato_febbraio"));
		col_marzo.setCellValueFactory(new PropertyValueFactory<>("fatturato_marzo"));
		col_aprile.setCellValueFactory(new PropertyValueFactory<>("fatturato_aprile"));
		col_maggio.setCellValueFactory(new PropertyValueFactory<>("fatturato_maggio"));
		col_giugno.setCellValueFactory(new PropertyValueFactory<>("fatturato_giugno"));
		col_luglio.setCellValueFactory(new PropertyValueFactory<>("fatturato_luglio"));
		col_agosto.setCellValueFactory(new PropertyValueFactory<>("fatturato_agosto"));
		col_settembre.setCellValueFactory(new PropertyValueFactory<>("fatturato_settembre"));
		col_ottobre.setCellValueFactory(new PropertyValueFactory<>("fatturato_ottobre"));
		col_novembre.setCellValueFactory(new PropertyValueFactory<>("fatturato_novembre"));
		col_dicembre.setCellValueFactory(new PropertyValueFactory<>("fatturato_dicembre"));
		
		try {
			loadDataAllFatture(-1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		col_gennaio.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_febbraio.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_marzo.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_aprile.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_maggio.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_giugno.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_luglio.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_agosto.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_settembre.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_ottobre.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_novembre.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		col_dicembre.setCellFactory(col -> new TableCell<ClienteFatturato, BigDecimal>() {
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
		
		table_fatturati.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		table_fatturati.getColumns().addAll(col_cliente_numero, col_gennaio, col_febbraio, col_marzo, col_aprile, col_maggio, col_giugno, col_luglio, col_agosto, col_settembre, col_ottobre, col_novembre, col_dicembre);
		scroll_pane.setContent(table_fatturati);
		
		list_recap.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        
                        setWrapText(true);
                        if (item == null || empty) {
                            setText(null);
                            setAlignment(Pos.CENTER);
                            setPrefWidth(width/13);
                        } else {
                            setText(item);
                            setAlignment(Pos.CENTER);
                            setPrefWidth(width/13);
                        }
                    }
                };
            }
        });
		list_recap.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	private void loadDataAllFatture(int numero_cliente) throws SQLException  {
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
		
		HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti = null;
		
		if (data_iniziale == null && data_finale == null) {
			if (numero_cliente == -1) mappa_fatturati_clienti = clientiDAO.getAllFatturati();
			else mappa_fatturati_clienti = clientiDAO.getAllFatturati(numero_cliente);
		}
		else if (data_iniziale == null && data_finale != null) {
			if (numero_cliente == -1) mappa_fatturati_clienti = clientiDAO.getAllFatturatiAl(data_finale);
			else mappa_fatturati_clienti = clientiDAO.getAllFatturati(numero_cliente, data_finale);
		}
		else if (data_iniziale != null && data_finale == null) {
			if (numero_cliente == -1) mappa_fatturati_clienti = clientiDAO.getAllFatturatiDal(data_iniziale);
			else mappa_fatturati_clienti = clientiDAO.getAllFatturati(data_iniziale, numero_cliente);
		}
		else {
			if (numero_cliente == -1) mappa_fatturati_clienti = clientiDAO.getAllFatturati(data_iniziale, data_iniziale);
			else mappa_fatturati_clienti = clientiDAO.getAllFatturati(numero_cliente, data_iniziale, data_finale);
		}
		
		ObservableList<ClienteFatturato> list_clienti_fatturato = FXCollections.observableArrayList();
		
		ClienteFatturato last_recap_fatturato = new ClienteFatturato(0);
		
		for (Integer k : mappa_fatturati_clienti.keySet()) {
			ClienteFatturato cf = new ClienteFatturato(k);
			cf.populateFatturati(mappa_fatturati_clienti.get(k));
			
			last_recap_fatturato.populateFatturati(mappa_fatturati_clienti.get(k));
			
			list_clienti_fatturato.add(cf);
		}
		
		list_recap.getItems().clear();
		list_recap.getItems().add(last_recap_fatturato.getNumero());
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_gennaio()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_febbraio()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_marzo()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_aprile()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_maggio()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_giugno()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_luglio()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_agosto()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_settembre()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_ottobre()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_novembre()));
		list_recap.getItems().add(decimal_format.format(last_recap_fatturato.getFatturato_dicembre()));
		list_recap.refresh();
		
		table_fatturati.setItems(list_clienti_fatturato);
		table_fatturati.refresh();
		
		createLineChart(mappa_fatturati_clienti);
	}
	
	private void showFatturatiCliente(ActionEvent event) {
		try {
			String cliente_str = String.valueOf(field_cliente.getValue());
			if (cliente_str.equals("null") || cliente_str == null || cliente_str.equals("")) {
				loadDataAllFatture(-1);
			}
			else {
				int numero_cliente = Integer.parseInt(cliente_str);
				loadDataAllFatture(numero_cliente);
			}
		}
		catch (Exception e) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Controlla il valore del campo!");
			alert.show();
		}
	}
	
	private void createLineChart(HashMap<Integer, HashMap<Integer, BigDecimal>> mappa_fatturati_clienti) {
		
		ArrayList<BigDecimal> list_importo = new ArrayList<BigDecimal>(12);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		list_importo.add(BigDecimal.ZERO);
		
		for (Integer k : mappa_fatturati_clienti.keySet()) {
			if (mappa_fatturati_clienti.get(k).containsKey(1)) list_importo.set(0, list_importo.get(0).add(mappa_fatturati_clienti.get(k).get(1)));
			if (mappa_fatturati_clienti.get(k).containsKey(2)) list_importo.set(1, list_importo.get(1).add(mappa_fatturati_clienti.get(k).get(2)));
			if (mappa_fatturati_clienti.get(k).containsKey(3)) list_importo.set(2, list_importo.get(2).add(mappa_fatturati_clienti.get(k).get(3)));
			if (mappa_fatturati_clienti.get(k).containsKey(4)) list_importo.set(3, list_importo.get(3).add(mappa_fatturati_clienti.get(k).get(4)));
			if (mappa_fatturati_clienti.get(k).containsKey(5)) list_importo.set(4, list_importo.get(4).add(mappa_fatturati_clienti.get(k).get(5)));
			if (mappa_fatturati_clienti.get(k).containsKey(6)) list_importo.set(5, list_importo.get(5).add(mappa_fatturati_clienti.get(k).get(6)));
			if (mappa_fatturati_clienti.get(k).containsKey(7)) list_importo.set(6, list_importo.get(6).add(mappa_fatturati_clienti.get(k).get(7)));
			if (mappa_fatturati_clienti.get(k).containsKey(8)) list_importo.set(7, list_importo.get(7).add(mappa_fatturati_clienti.get(k).get(8)));
			if (mappa_fatturati_clienti.get(k).containsKey(9)) list_importo.set(8, list_importo.get(8).add(mappa_fatturati_clienti.get(k).get(9)));
			if (mappa_fatturati_clienti.get(k).containsKey(10)) list_importo.set(9, list_importo.get(9).add(mappa_fatturati_clienti.get(k).get(10)));
			if (mappa_fatturati_clienti.get(k).containsKey(11)) list_importo.set(10, list_importo.get(10).add(mappa_fatturati_clienti.get(k).get(11)));
			if (mappa_fatturati_clienti.get(k).containsKey(12)) list_importo.set(12, list_importo.get(11).add(mappa_fatturati_clienti.get(k).get(12)));
		}
		
		//defining the axes
		final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Fatturato");
        yAxis.setAutoRanging(true);
        //yAxis.setLowerBound(0);
        //yAxis.setUpperBound(1000000);
        
        final AreaChart<String, Number> line_chart = new AreaChart<String, Number>(xAxis, yAxis);
        line_chart.setTitle("Fatturato per mese");
        
        //defining a series
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName("FatturatoPerMese");
        
        //populating the series with data
        series.getData().add(new XYChart.Data<String, Number>("GEN", list_importo.get(0)));
        series.getData().add(new XYChart.Data<String, Number>("FEB", list_importo.get(1)));
        series.getData().add(new XYChart.Data<String, Number>("MAR", list_importo.get(2)));
        series.getData().add(new XYChart.Data<String, Number>("APR", list_importo.get(3)));
        series.getData().add(new XYChart.Data<String, Number>("MAG", list_importo.get(4)));
        series.getData().add(new XYChart.Data<String, Number>("GIU", list_importo.get(5)));
        series.getData().add(new XYChart.Data<String, Number>("LUG", list_importo.get(6)));
        series.getData().add(new XYChart.Data<String, Number>("AGO", list_importo.get(7)));
        series.getData().add(new XYChart.Data<String, Number>("SET", list_importo.get(8)));
        series.getData().add(new XYChart.Data<String, Number>("OTT", list_importo.get(9)));
        series.getData().add(new XYChart.Data<String, Number>("NOV", list_importo.get(10)));
        series.getData().add(new XYChart.Data<String, Number>("DEC", list_importo.get(11)));
        
        line_chart.getData().add(series);
        line_chart.setLegendVisible(false);
        line_chart.setPrefHeight(250);
        
        if (vbox.getChildren().size() >= 3) vbox.getChildren().remove(2);
        vbox.getChildren().add(line_chart);
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
