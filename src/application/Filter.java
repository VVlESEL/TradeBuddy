package application;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class Filter implements Initializable{
	TableView<Trade> tableView;
	CheckBox[] boxesSymbol, boxesStrategy;
	Controller controller;
	final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
	
	static LocalDate startDate = null;
	static LocalDate endDate = null;
	static boolean longTrades = true;
	static boolean shortTrades = true;
	static HashMap<String,Boolean> hmStrategies = new HashMap<>();
	static HashMap<String,Boolean> hmSymbols = new HashMap<>();

    @FXML
    private CheckBox cbBuy;
    
    @FXML
    private CheckBox cbSell;

    @FXML
    private DatePicker dpStartdatum;

    @FXML
    private DatePicker dpEnddatum;

    @FXML
    private TextField tfMinCrv;
   
    @FXML
    private TextField tfMaxCrv;

    @FXML
    private TextField tfMinRcrv;
    
    @FXML
    private TextField tfMaxRcrv;

    @FXML
    private TextField tfMinRisiko;
    
    @FXML
    private TextField tfMaxRisiko;
    
    @FXML
    private FlowPane paneSymbol;
    
    @FXML
    private FlowPane paneStrategie;

    @FXML
    void filterTrades(ActionEvent event) {	
    	filterAnwenden();	
    	saveFilterSettings();
    }
	
	@FXML
	public void resetFilter(){
		filterReset();
	}

	public Filter(TableView<Trade> tableView, Controller controller){	    	    
		this.tableView = tableView;
		this.controller = controller;
	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Controller.trades = DatabaseConnection.getTrades();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
	    //Filter erstellen 
	    generateSymbolFilter();
	    generateStrategyFilter();

	    loadFilterSettings();
	}	

	public void filterAnwenden(){		
		try {
			Controller.trades = DatabaseConnection.getTrades();
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		
		dpStartdatum.setValue(dpStartdatum.getConverter()
			    .fromString(dpStartdatum.getEditor().getText()));
		dpEnddatum.setValue(dpEnddatum.getConverter()
			    .fromString(dpEnddatum.getEditor().getText()));
		
		//eingaben überprüfen
		if(dpStartdatum.getValue() == null) dpStartdatum.setValue(LocalDate.parse("1900.01.01", formatter));
		if(dpEnddatum.getValue() == null) dpEnddatum.setValue(LocalDate.parse("2999.01.01", formatter));

		LocalDate minDate = null, maxDate = null;

		minDate = dpStartdatum.getValue();
		maxDate = dpEnddatum.getValue();
		
		double minCrv = Trade.ParseDouble(tfMinCrv.getText());
		if(minCrv <= 0){
			minCrv = -99999;
			tfMinCrv.setText(Double.toString(minCrv));
		}
		double maxCrv = Trade.ParseDouble(tfMaxCrv.getText());
		if(maxCrv <= 0){
			maxCrv = 99999;
			tfMaxCrv.setText(Double.toString(maxCrv));
		}

		double minRcrv = Trade.ParseDouble(tfMinRcrv.getText());
		if(minRcrv <= 0){
			minRcrv = -99999;
			tfMinRcrv.setText(Double.toString(minRcrv));
		}
		double maxRcrv = Trade.ParseDouble(tfMaxRcrv.getText());
		if(maxRcrv <= 0){
			maxRcrv = 99999;
			tfMaxRcrv.setText(Double.toString(maxRcrv));
		}
	
		double minRisiko = Trade.ParseDouble(tfMinRisiko.getText());
		if(minRisiko <= 0){
			minRisiko = -99999;
			tfMinRisiko.setText(Double.toString(minRisiko));
		}
		double maxRisiko = Trade.ParseDouble(tfMaxRisiko.getText());
		if(maxRisiko <= 0){
			maxRisiko = 99999;
			tfMaxRisiko.setText(Double.toString(maxRisiko));
		}
		

		ObservableList<Trade> removeTrades = FXCollections.observableArrayList();
		for(Trade t : Controller.trades){

			//buy or sell filter
			if(!((cbBuy.isSelected() && t.getTypEinstieg().equals("Long") || (cbSell.isSelected() && t.getTypEinstieg().equals("Short"))))){
				removeTrades.add(t);
				continue;
			}
			
			//time filter
//			LocalDate entryTime = LocalDate.parse(t.getZeitEinstieg().substring(0,10), formatter);
			LocalDate exitTime = LocalDate.parse(t.getZeitAusstieg().substring(0,10), formatter);
			if(!(exitTime.compareTo(minDate) >= 0 && exitTime.compareTo(maxDate) <= 0)){
				removeTrades.add(t);
				continue;
			} 	

			//crv filter
			if(t.getCrv() < minCrv || t.getCrv() > maxCrv){
				removeTrades.add(t); 	
				continue;
			}

			//rcrv filter
			if(t.getRcrv() < minRcrv || t.getRcrv() > maxRcrv){
				removeTrades.add(t);
				continue;
			}

			//risiko filter
			if(t.getRisiko() < minRisiko || t.getRisiko() > maxRisiko){
				removeTrades.add(t);
				continue;
			}

			//symbol filter
			boolean symbolCheck = false;
			for(int j = 0; j < boxesSymbol.length; j++){
				if(boxesSymbol[j].getText().equals(t.getSymbol()) && boxesSymbol[j].isSelected() == true)
					symbolCheck = true;
			}
			if(!symbolCheck){
				removeTrades.add(t);
				continue;
			}
			
			//strategy filter
			boolean strategyCheck = false;
			for(int k = 0; k < boxesStrategy.length; k++){
				if(boxesStrategy[k].getText().equals(t.getStrategie()) && boxesStrategy[k].isSelected() == true){
					strategyCheck = true;
				}
			}
			if(!strategyCheck){
				removeTrades.add(t);
				continue;
			}
		}
		Controller.trades.removeAll(removeTrades);
		try {
			DatabaseConnection.refreshTable(tableView);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		controller.updateGraphic();
		controller.updateNumbers();
	}
	
	private void loadFilterSettings(){
	    //load filter settings
    	cbBuy.setSelected(longTrades);
    	cbSell.setSelected(shortTrades);
    	dpStartdatum.setValue(startDate);
    	dpEnddatum.setValue(endDate);
    	for(CheckBox cb : boxesStrategy){
    		if(hmStrategies.containsKey(cb.getText()))
    			cb.setSelected(hmStrategies.get(cb.getText()));
    	}
    	for(CheckBox cb : boxesSymbol){
    		if(hmSymbols.containsKey(cb.getText()))
    			cb.setSelected(hmSymbols.get(cb.getText()));
    	}
	}
	
	private void saveFilterSettings(){
    	//save filter settings
    	longTrades = cbBuy.isSelected();
    	shortTrades = cbSell.isSelected();
    	startDate = dpStartdatum.getValue();
    	endDate = dpEnddatum.getValue();
    	hmSymbols.clear();
    	hmStrategies.clear();
    	for(CheckBox cb : boxesStrategy){
    		hmStrategies.put(cb.getText(), cb.isSelected());
    	}
    	for(CheckBox cb : boxesSymbol){
    		hmSymbols.put(cb.getText(), cb.isSelected());
    	}
	}
	
	public void generateSymbolFilter(){
		ArrayList<String> arrayList = new ArrayList<>();
		for(Trade t : Controller.trades){
			if(!arrayList.contains(t.getSymbol())){
				arrayList.add(t.getSymbol());
			}
		}
		
		boxesSymbol = new CheckBox[arrayList.size()];
		for(int i = 0; i < arrayList.size(); i++){
			boxesSymbol[i] = new CheckBox(arrayList.get(i));
			boxesSymbol[i].setPadding(new Insets(0,10,0,0));
			boxesSymbol[i].setSelected(true);
			paneSymbol.getChildren().add(boxesSymbol[i]);
		}
	}
	
	public void generateStrategyFilter(){
		ArrayList<String> arrayList = new ArrayList<>();
		for(Trade t : Controller.trades){
			if(!arrayList.contains(t.getStrategie())){
				arrayList.add(t.getStrategie());
			}
		}
		
		boxesStrategy = new CheckBox[arrayList.size()];
		for(int i = 0; i < arrayList.size(); i++){
			boxesStrategy[i] = new CheckBox(arrayList.get(i));
			boxesStrategy[i].setPadding(new Insets(0,10,0,0));
			boxesStrategy[i].setSelected(true);
			paneStrategie.getChildren().add(boxesStrategy[i]);
		}
	}
	
	public void filterReset(){
		startDate = null;
		endDate = null;
		longTrades = true;
		shortTrades = true;
		hmStrategies.clear();
		hmSymbols.clear();
		
		for(CheckBox cb : boxesSymbol){
			cb.setSelected(true);
		}
		for(CheckBox cb : boxesStrategy){
			cb.setSelected(true);
		}
		
		loadFilterSettings();
		
		try {
			Controller.trades = DatabaseConnection.getTrades();
			DatabaseConnection.refreshTable(tableView);
		} catch (ClassNotFoundException | SQLException e1) {
			e1.printStackTrace();
		}
		controller.updateGraphic();
		controller.updateNumbers();
	}
}
