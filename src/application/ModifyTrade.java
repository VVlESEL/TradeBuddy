package application;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class ModifyTrade implements Initializable {
	@FXML
	private ComboBox<String> cbSymbol;
	
	@FXML 
	private TextField tfID;

	@FXML
	private TextField tfAusstieg;

	@FXML
	private TextField tfKommission;
	
	@FXML
	private TextField tfSwap;

	@FXML
	private ComboBox<String> cbStrategie;

	@FXML
	private TextField tfZeitEinstieg;

	@FXML
	private TextField tfLots;

	@FXML
	private ComboBox<String> cbTyp;

	@FXML
	private TextField tfRisiko;

	@FXML
	private TextField tfGewinn;

	@FXML
	private TextField tfZiel;

	@FXML
	private TextField tfStopp;

	@FXML
	private TextField tfEinstieg;

	@FXML
	private TextField tfKommentar;

	@FXML
	private TextField tfZeitAusstieg;

	Trade trade;
	TableView<Trade>tableView;
	Controller controller;

	public ModifyTrade(Trade trade, TableView<Trade> tableView, Controller controller){
		this.trade = trade;	
		this.tableView = tableView;
		this.controller = controller;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbStrategie.getItems().addAll(FXCollections.observableList(Strategies.strategies));
		cbSymbol.getItems().addAll(FXCollections.observableList(Symbols.symbols));
		cbTyp.getItems().addAll("Long","Short");
		
		tfID.setText(trade.getId().toString());
		cbStrategie.getSelectionModel().select(trade.getStrategie());
		cbSymbol.getSelectionModel().select(trade.getSymbol());
		tfLots.setText(trade.getLots().toString());
		cbTyp.getSelectionModel().select(trade.getTypEinstieg());
		tfEinstieg.setText(trade.getEinstieg().toString());
		tfZeitEinstieg.setText(trade.getZeitEinstieg());
		tfAusstieg.setText(trade.getAusstieg().toString());
		tfZeitAusstieg.setText(trade.getZeitAusstieg());
		tfKommission.setText(trade.getKommission().toString());
		tfSwap.setText(trade.getSwap().toString());
		tfGewinn.setText(trade.getGewinn().toString());
		tfStopp.setText(trade.getStopp().toString());
		tfZiel.setText(trade.getZiel().toString());
		tfRisiko.setText(trade.getRisiko().toString());
		tfKommentar.setText(trade.getKommentar());
	}
	
    @FXML
    void modifyTrade(ActionEvent event) {
    	double id = trade.getId();
    	try{
    		id = Integer.parseInt(tfID.getText());
    	}catch(Exception e){}
    	
    	double lots = trade.getLots();
    	try{
    		lots = Double.parseDouble(tfLots.getText());
    	}catch(Exception e){}
    	
    	double einstieg = trade.getEinstieg();
    	try{
    		einstieg = Double.parseDouble(tfEinstieg.getText());
    	}catch(Exception e){}
    	
    	double ausstieg = trade.getAusstieg();
    	try{
    		ausstieg = Double.parseDouble(tfAusstieg.getText());
    	}catch(Exception e){}
    	
    	double kommission = trade.getKommission();
    	try{
    		kommission = Double.parseDouble(tfKommission.getText());
    	}catch(Exception e){}
    	
    	double swap = trade.getSwap();
    	try{
    		swap = Double.parseDouble(tfSwap.getText());
    	}catch(Exception e){}
    	
    	double gewinn = trade.getGewinn();
    	try{
    		gewinn = Double.parseDouble(tfGewinn.getText());
    	}catch(Exception e){}
    	
    	double stopp = trade.getStopp();
    	try{
    		stopp = Double.parseDouble(tfStopp.getText());
    	}catch(Exception e){}
    	
    	double ziel = trade.getZiel();
    	try{
    		ziel = Double.parseDouble(tfZiel.getText());
    	}catch(Exception e){}
    	
    	double crv = trade.getCrv();   	
    	if(ziel != 0 && stopp != 0 && einstieg != 0){
			crv = (ziel-einstieg)/(einstieg-stopp);
    	}
    	
		double rcrv = trade.getRcrv();
		if(stopp != 0 && einstieg != 0 && ausstieg != 0){	
			rcrv = (ausstieg-einstieg)/(einstieg-stopp);
		}
    	
    	double risiko = trade.getRisiko();
    	try{
    		risiko = Double.parseDouble(tfRisiko.getText());
    	}catch(Exception e){}
    	
    	//check if textfield for entry time is valid
    	String zeitEinstieg = trade.getZeitEinstieg();
    	String format = "yyyy.MM.dd HH:mm";
    	if(tfZeitEinstieg.getText().length() >= 10){
	    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	    	sdf.setLenient(false);
	    	try {
	    	    Date date = sdf.parse(tfZeitEinstieg.getText());
	    	    if (!sdf.format(date).equals(tfZeitEinstieg.getText())) {
	    	        throw new ParseException(tfZeitEinstieg.getText() + " is not a valid format for " + format, 0);
	    	    }else{
	    	    	zeitEinstieg = tfZeitEinstieg.getText();
	    	    }
	    	} catch (ParseException ex) {
	        	format = "yyyy.MM.dd";
	        	sdf = new SimpleDateFormat(format);
	        	try {
	        	    Date date = sdf.parse(tfZeitEinstieg.getText().substring(0,10));
	        	    if (!sdf.format(date).equals(tfZeitEinstieg.getText().substring(0,10))) {
	        	        throw new ParseException(tfZeitEinstieg.getText()+ " is not a valid format for " + format, 0);
	        	    }else{
	        	    	zeitEinstieg = tfZeitEinstieg.getText().substring(0,10);	
	        	    }
	        	} catch (ParseException ex2) {
	        	    ex2.printStackTrace();
	        	}
	    	}
    	}
    	
    	//check if textfield for exit time is valid
    	String zeitAusstieg = trade.getZeitAusstieg();
    	format = "yyyy.MM.dd HH:mm";
    	if(tfZeitAusstieg.getText().length() >= 10){
	    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	    	sdf.setLenient(false);
	    	try {
	    	    Date date = sdf.parse(tfZeitAusstieg.getText());
	    	    if (!sdf.format(date).equals(tfZeitAusstieg.getText())) {
	    	        throw new ParseException(tfZeitAusstieg.getText() + " is not a valid format for " + format, 0);
	    	    }else{
	    	    	zeitAusstieg = tfZeitAusstieg.getText();
	    	    }
	    	} catch (ParseException ex) {
	        	format = "yyyy.MM.dd";
	        	sdf = new SimpleDateFormat(format);
	        	try {
	        	    Date date = sdf.parse(tfZeitAusstieg.getText().substring(0,10));
	        	    if (!sdf.format(date).equals(tfZeitAusstieg.getText().substring(0,10))) {
	        	        throw new ParseException(tfZeitAusstieg.getText()+ " is not a valid format for " + format, 0);
	        	    }else{
	        	    	zeitAusstieg = tfZeitAusstieg.getText().substring(0,10);	
	        	    }
	        	} catch (ParseException ex2) {
	        	    ex2.printStackTrace();
	        	}
	    	}
    	}
    	 	
		try {			
			PreparedStatement prep = DatabaseConnection.con.prepareStatement("UPDATE trades SET "
					+ "ID = '"+id+"',"
					+ "Strategie = '"+cbStrategie.getSelectionModel().getSelectedItem()+"',"
					+ "Symbol = '"+cbSymbol.getSelectionModel().getSelectedItem()+"',"
					+ "Lots = '"+lots+"',"
				    + "TypEinstieg = '"+cbTyp.getSelectionModel().getSelectedItem()+"',"
				    + "Einstieg = '"+einstieg+"',"
				    + "ZeitEinstieg = '"+zeitEinstieg+"',"
				    + "Ausstieg = '"+ausstieg+"',"
				    + "ZeitAusstieg = '"+zeitAusstieg+"',"
				    + "Kommission = '"+kommission+"',"
				    + "Swap = '"+swap+"',"
					+ "Gewinn = '"+gewinn+"',"
					+ "Stopp = '"+stopp+"',"
					+ "Ziel = '"+ziel+"',"
					+ "CRV = '"+NumbersCalculation.round(crv,2)+"',"
					+ "RCRV = '"+NumbersCalculation.round(rcrv,2)+"',"
					+ "Risiko = '"+risiko+"',"
					+ "Kommentar = '"+tfKommentar.getText()+"'"
					+ "WHERE ID LIKE '"+trade.getId()+"';");
			prep.execute();
			prep.close();
			Controller.trades = DatabaseConnection.getTrades();
			DatabaseConnection.refreshTable(tableView);
		} catch (SQLException | ClassNotFoundException ex) {
			ex.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			alert.setTitle("Fehler");
			alert.setContentText("Eine Angabe konnte nicht korrekt verarbeitet werden oder die Order Nummer ist bereits vergeben.");
			alert.showAndWait();
		}					
		controller.updateGraphic();
		controller.updateNumbers();
        Stage stage = (Stage)tfLots.getScene().getWindow();
        stage.close();
    }

    @FXML
    void closeWindow(ActionEvent event) {
    	//default
    	Trade trade = Controller.trades.get(Controller.trades.size()-1);
    	if(trade.getStrategie().equals("")
    			&& trade.getSymbol().equals("")
    			&& trade.getLots() == 0
    			&& trade.getTypEinstieg().equals("")
    			&& trade.getEinstieg() == 0
    			&& trade.getZeitEinstieg().equals("")
    			&& trade.getAusstieg() == 0
    			&& trade.getZeitAusstieg().equals("")
    			&& trade.getKommission() == 0
    	    	&& trade.getSwap() == 0
    			&& trade.getGewinn() == 0
    			&& trade.getStopp() == 0
    			&& trade.getZiel() == 0
    			&& trade.getRisiko() == 0
    			&& trade.getKommentar().equals("")){
			try {
				PreparedStatement prep = DatabaseConnection.con.prepareStatement("DELETE FROM trades WHERE id = " + trade.getId());
				prep.execute();			
				prep.close();	
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	
        Stage stage = (Stage)tfLots.getScene().getWindow();
        stage.close();
    }
}
