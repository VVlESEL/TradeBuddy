package application;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller implements Initializable {
	
	public static ObservableList<Trade> trades;
    public static LineChart<Number, Number> lineChart;
	
    @FXML
    private VBox graphicVBox;
    
    @FXML
    private TableView<Trade> tableView;

    @FXML
    private ComboBox<String> cbSymbol;
    
    @FXML
    private Label labelVerslusttradesInFolge;

    @FXML
    private Label labelAnzahlDerTrades;

    @FXML
    private Label labelNettoprofitGesamt;

    @FXML
    private Label labelDurchschnittGewinntrade;

    @FXML
    private Label labelSellTrades;

    @FXML
    private Label labelVerlustInFolge;

    @FXML
    private Label labelBuyTrades;

    @FXML
    private Label labelMaximumGewinnInFolge;

    @FXML
    private Label labelGroessterVerlusttrade;

    @FXML
    private Label labelErwartetesErgebnis;
    
    @FXML
    private Label labelBruttoprofit;

    @FXML
    private Label labelMaximumGewinntradesInFolge;

    @FXML
    private Label labelMaximalerRueckgangInProzent;

    @FXML
    private Label labelBruttoverlust;

    @FXML
    private Label labelDurchschnittVerlusttrade;

    @FXML
    private Label labelProfitfaktor;

    @FXML
    private Label labelGewinntrades;

    @FXML
    private Label labelMaximalerRueckgang;

    @FXML
    private Label labelGroessterGewinntrade;

    @FXML
    private Label labelVerlusttrades;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("View is now loaded!");
        
        buildTable();       
        Settings.loadSettings();
        updateGraphic();
        updateNumbers();
        try {
			Strategies.loadStrategies();
			Symbols.loadSymbols();
		} catch (SQLException e) {
			e.printStackTrace();
		}  
    }

    @FXML
    void openFilter(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("FilterPopup.fxml"));
            Filter controller = new Filter(tableView, this);
            loader.setController(controller);       
            Parent root = (Parent)loader.load();
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setTitle("Filter");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void openEinstellungen(ActionEvent event) {
		try {		
			FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsPopup.fxml"));
	        Settings controller = new Settings(this);
	        loader.setController(controller);       
	        Parent root;
			root = (Parent) loader.load();        
			Stage stage = new Stage();
	        stage.setAlwaysOnTop(true);
	        stage.setTitle("Einstellungen");
	        stage.setScene(new Scene(root));
	        stage.setResizable(false);
	        stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void closeProgram(ActionEvent event) {
    	Platform.exit();
    }
    
    @FXML
    void openNeuesSymbol(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("SymbolsStrategiesPopup.fxml"));
            Symbols controller = new Symbols();
            loader.setController(controller);       
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setTitle("Symbole");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();  
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void openNeueStrategie(ActionEvent event) {
    	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("SymbolsStrategiesPopup.fxml"));
            Strategies controller = new Strategies();
            loader.setController(controller);       
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setTitle("Strategien");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void print(ActionEvent event){
    	String string = "Nettoprofit gesamt:\t\t\t\t" + labelNettoprofitGesamt.getText()
    			+ "\nProfitfaktor:\t\t\t\t\t"+ labelProfitfaktor.getText()
    			+ "\nAnzahl der Trades:\t\t\t\t" + labelAnzahlDerTrades.getText()
    			+ "\n\nBruttoprofit:\t\t\t\t\t" + labelBruttoprofit.getText()
    			+ "\nBruttoverlust:\t\t\t\t\t" + labelBruttoverlust.getText()
    			+ "\nErwartetes Ergebnis:\t\t\t" + labelErwartetesErgebnis.getText()
    			+ "\nMaximaler Rückgang:\t\t\t" + labelMaximalerRueckgang.getText()
    			+ "\nMaximaler Rückgang in %:\t\t" + labelMaximalerRueckgangInProzent.getText()			    			
    			+ "\n\nBuy-Trades (davon gewonnen):\t" + labelBuyTrades.getText()
    			+ "\nSell-Trades (davon gewonnen):\t" + labelSellTrades.getText()
    			+ "\n\nGewinntrades (in % von Ges.):\t\t" + labelGewinntrades.getText()
    			+ "\nVerlusttrades (in % von Ges.):\t\t" + labelVerlusttrades.getText()
    			+ "\nGrößter Gewinntrade:\t\t\t" + labelGroessterGewinntrade.getText() + "\tVerlusttrade:\t\t\t" + labelGroessterVerlusttrade.getText()
    			+ "\nDurchschnitt Gewinntrade:\t\t" + labelDurchschnittGewinntrade.getText() + "\tVerlusttrade:\t\t\t" + labelDurchschnittVerlusttrade.getText()
    			+ "\nMaximum Gewinntrades in Folge:\t" + labelMaximumGewinntradesInFolge.getText() + "\tVerlusttrades in Folge:\t" + labelVerslusttradesInFolge.getText()
    			+ "\nMaximum Gewinn in Folge:\t\t" + labelMaximumGewinnInFolge.getText() + "\tVerlust in Folge:\t\t" + labelVerlustInFolge.getText();
    	
        TextArea taKennzahlen = new TextArea();
        taKennzahlen.setText(string);
        taKennzahlen.setId("taKennzahlen");
        taKennzahlen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
               
        VBox page1 = new VBox();
        page1.getChildren().add(taKennzahlen);	
        Region region = new Region();
        region.setPrefSize(10, 160);
        page1.getChildren().add(region);
        page1.getChildren().add(lineChart);

        Printer printer = Printer.getDefaultPrinter();
	    PageLayout pageLayout = printer.getDefaultPageLayout();
        lineChart.setMaxWidth(pageLayout.getPrintableWidth());
        lineChart.setMinWidth(pageLayout.getPrintableWidth());
        lineChart.setMinHeight(350);
        lineChart.setMaxHeight(350);
        
	    PrinterJob job = PrinterJob.createPrinterJob();
	    if (job != null) {       
	        if(job.printPage(page1)) {
	           job.endJob();
	        }
	    }
	    lineChart.setMinWidth(0);
	    lineChart.setMaxWidth(Integer.MAX_VALUE);
	    lineChart.setMinHeight(0);
	    lineChart.setMaxHeight(Integer.MAX_VALUE);
	    graphicVBox.getChildren().add(lineChart);
    }
    
    @FXML
    void openInfo(ActionEvent event) {        
       	try {
    		FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoPopup.fxml"));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            stage.setAlwaysOnTop(true);
            stage.setTitle("Info");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @FXML
    void openBacktestImport(ActionEvent event) {
    	new MetaTraderImport(tableView, this, tableView.getScene().getWindow());
    }

	@SuppressWarnings("unchecked")
	private void buildTable(){
        try {
        	TableColumn<Trade, Integer> column1 = new TableColumn<>("ID");
            column1.setCellValueFactory(new PropertyValueFactory<Trade, Integer>("id"));
            TableColumn<Trade, String> column2 = new TableColumn<>("Strategie");
            column2.setCellValueFactory(new PropertyValueFactory<>("strategie"));
            TableColumn<Trade, String> column3 = new TableColumn<>("Symbol");
            column3.setCellValueFactory(new PropertyValueFactory<>("symbol"));
            TableColumn<Trade, Double> column4 = new TableColumn<>("Lots");
            column4.setCellValueFactory(new PropertyValueFactory<>("lots")); 
            TableColumn<Trade, String> column5 = new TableColumn<>("Typ");
            column5.setCellValueFactory(new PropertyValueFactory<>("typEinstieg"));
            TableColumn<Trade, Double> column6 = new TableColumn<>("Einstieg");
            column6.setCellValueFactory(new PropertyValueFactory<>("Einstieg"));
            TableColumn<Trade, String> column7 = new TableColumn<>("Zeit Einstieg");
            column7.setCellValueFactory(new PropertyValueFactory<>("zeitEinstieg"));
            TableColumn<Trade, Double> column8 = new TableColumn<>("Ausstieg");
            column8.setCellValueFactory(new PropertyValueFactory<>("ausstieg"));
            TableColumn<Trade, String> column9 = new TableColumn<>("Zeit Ausstieg");
            column9.setCellValueFactory(new PropertyValueFactory<>("zeitAusstieg"));
            TableColumn<Trade, Double> column10 = new TableColumn<>("Kommission");
            column10.setCellValueFactory(new PropertyValueFactory<>("kommission"));
            TableColumn<Trade, Double> column11 = new TableColumn<>("Swap");
            column11.setCellValueFactory(new PropertyValueFactory<>("swap"));
            TableColumn<Trade, Double> column12 = new TableColumn<>("Gewinn");
            column12.setCellValueFactory(new PropertyValueFactory<>("gewinn"));
            TableColumn<Trade, Double> column13 = new TableColumn<>("Stopp");
            column13.setCellValueFactory(new PropertyValueFactory<>("stopp"));
            TableColumn<Trade, Double> column14 = new TableColumn<>("Ziel");
            column14.setCellValueFactory(new PropertyValueFactory<>("ziel"));
            TableColumn<Trade, Double> column15 = new TableColumn<>("CRV");
            column15.setCellValueFactory(new PropertyValueFactory<>("crv"));
            TableColumn<Trade, Double> column16 = new TableColumn<>("RCRV");
            column16.setCellValueFactory(new PropertyValueFactory<>("rcrv"));            
            TableColumn<Trade, Double> column17 = new TableColumn<>("Risiko");
            column17.setCellValueFactory(new PropertyValueFactory<>("risiko"));
            TableColumn<Trade, String> column18 = new TableColumn<>("Kommentar");
            column18.setCellValueFactory(new PropertyValueFactory<>("kommentar"));
            
            tableView.getColumns().clear();
            tableView.getColumns().addAll(column1, column2, column3, column4, column5, column6, column7, column8, column9, column10, column11, column12, column13, column14, column15, column16, column17, column18);
            
            trades = DatabaseConnection.getTrades();            
			DatabaseConnection.refreshTable(tableView);			
			
			//change selection mode of tableview to multple
			tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			
			//create menu on right click
		    ContextMenu cm = new ContextMenu();
		    
		    MenuItem mi1 = new MenuItem("Neuer Trade...");
		    mi1.setOnAction((ActionEvent e)->{
				try {			
					PreparedStatement prep = DatabaseConnection.con.prepareStatement("INSERT INTO trades (Strategie, Symbol, Lots, TypEinstieg, Einstieg, "
							+ "ZeitEinstieg, Ausstieg, ZeitAusstieg, Kommission, Swap, Gewinn, Stopp, Ziel, Risiko, Kommentar) "
							+ "VALUES('','','','','','','','','','','','','','','');");
					prep.execute();
					prep.close();
					
					Controller.trades = DatabaseConnection.getTrades();

				    Trade trade = trades.get(trades.size()-1);
					
				    FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyTradePopup.fxml"));
			        ModifyTrade controller = new ModifyTrade(trade,tableView,this);
			        loader.setController(controller);       
			        Parent root;
					root = (Parent)loader.load();				
					Stage stage = new Stage();
			        stage.setAlwaysOnTop(true);
			        stage.setTitle("Trade Nr. " + trade.getId());
			        stage.setScene(new Scene(root));
			        stage.setResizable(false);
			        stage.show();
				} catch (SQLException | IOException | ClassNotFoundException ex) {
					ex.printStackTrace();
				}					
		    });
		    cm.getItems().add(mi1);
		    
		    MenuItem mi2 = new MenuItem("Bearbeiten...");
		    mi2.setOnAction((ActionEvent e)->{
			    Trade trade = tableView.getSelectionModel().getSelectedItem();
			    
			    if(trade != null) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyTradePopup.fxml"));
			        ModifyTrade controller = new ModifyTrade(trade,tableView,this);
			        loader.setController(controller);       
			        Parent root;
					try {
						root = (Parent)loader.load();				
						Stage stage = new Stage();
				        stage.setAlwaysOnTop(true);
				        stage.setTitle("Trade Nr. " + trade.getId());
				        stage.setScene(new Scene(root));
				        stage.setResizable(false);
				        stage.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}        
			    }
		    });
		    cm.getItems().add(mi2);
		    
		    MenuItem mi3 = new MenuItem("Löschen...");
		    mi3.setOnAction((ActionEvent e)->{
		    	//delete a single trade/row or multiple rows
			    ObservableList<Trade> tempTrades = tableView.getSelectionModel().getSelectedItems();
			    PreparedStatement prep = null;
			    for(Trade t : tempTrades) {
					try {			
						prep = DatabaseConnection.con.prepareStatement("DELETE FROM trades WHERE ID LIKE '"+t.getId()+"';");
						prep.execute();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}								    	
			    }
			    try {						
			    	Controller.trades = DatabaseConnection.getTrades();
					DatabaseConnection.refreshTable(tableView);
					prep.close();
				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				updateGraphic();
				updateNumbers();	
		    });
		    cm.getItems().add(mi3);

		    Controller thisController = this;
		    tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		    	@Override
		        public void handle(MouseEvent t) {
		            if(t.getButton() == MouseButton.SECONDARY){
		            	cm.show(tableView , t.getScreenX() , t.getScreenY());
		            }
		            else if(t.getButton() == MouseButton.PRIMARY){
		            	cm.hide();
		            }
		            if(t.getButton().equals(MouseButton.PRIMARY)){
		                if(t.getClickCount() == 2){
		    			    Trade trade = tableView.getSelectionModel().getSelectedItem();
		    			    
		    			    if(trade != null) {
		    					FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyTradePopup.fxml"));
		    			        ModifyTrade controller = new ModifyTrade(trade,tableView,thisController);
		    			        loader.setController(controller);       
		    			        Parent root;
		    					try {
		    						root = (Parent)loader.load();				
		    						Stage stage = new Stage();
		    				        stage.setAlwaysOnTop(true);
		    				        stage.setTitle("Trade Nr. " + trade.getId());
		    				        stage.setScene(new Scene(root));
		    				        stage.setResizable(false);
		    				        stage.show();
		    					} catch (IOException e1) {
		    						e1.printStackTrace();
		    					}        
		    			    }		                	
		                }
		            }
		        }
		    });
        } catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Fehler");
			alert.setContentText("Das Programm konnte nicht gestartet werden. Das kann daran liegen, dass das Tool in ein Verzeichnis installiert"
					+"wurde, welches Administratorrechte benötigt. Das Problem kann behoben werden, indem in ein anderes Verzeichnis installiert"
					+"wird oder aber indem das Tool immer durch 'Rechtsklick -> Als Administrator ausführen' gestartet wird. Sollten diese Schritte"
					+"nicht zur Lösung des Problems führen, dann kontaktieren Sie bitte den Softwarehersteller unter info@bmtrading.de");
			alert.showAndWait();
		}   
    }
    
    public void updateGraphic(){
    	//update line chart
    	new EquityGraph();   
    	//remove last line chart if it exists
    	try{graphicVBox.getChildren().remove(0);} catch(Exception e) {System.out.println("Grafik wird erstellt.");}
    	//show new line chart
    	graphicVBox.getChildren().add(lineChart);
    }
    
    public void updateNumbers(){ 	
    	new NumbersCalculation(  	     
	        labelVerslusttradesInFolge,
	        labelAnzahlDerTrades,
	        labelNettoprofitGesamt,
	        labelDurchschnittGewinntrade,
	        labelSellTrades,
	        labelVerlustInFolge,
	        labelBuyTrades,
	        labelMaximumGewinnInFolge,
	        labelGroessterVerlusttrade,
	        labelErwartetesErgebnis,
	        labelBruttoprofit,
	        labelMaximumGewinntradesInFolge,
	        labelMaximalerRueckgangInProzent,
	        labelBruttoverlust,
	        labelDurchschnittVerlusttrade,
	        labelProfitfaktor,
	        labelGewinntrades,
	        labelMaximalerRueckgang,
	        labelGroessterGewinntrade,
	        labelVerlusttrades);
    }
}