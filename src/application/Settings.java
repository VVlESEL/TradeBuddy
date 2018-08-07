package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Settings implements Initializable{	
	private Controller controller;
	
	//settings
	public static double account = 0;
	public static double lineThickness = 3;

    @FXML
    private TextField tfKontogroesse;

    @FXML
    private Button bAbbrechen;

    @FXML
    private Slider sliderLiniendicke;

    @FXML
    private Button bBestaetignen;
    
    public Settings(Controller controller){
    	this.controller = controller;
    	loadSettings();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tfKontogroesse.setText(String.valueOf(account));
		sliderLiniendicke.setValue(lineThickness);
	}
	
    @FXML
    void updateSettings(ActionEvent event) {
    	lineThickness = sliderLiniendicke.getValue();
    	try{
    		account = Double.parseDouble(tfKontogroesse.getText());
    		closeWindow(event);
    		controller.updateGraphic();
    		controller.updateNumbers();
    	}catch(Exception e){
            tfKontogroesse.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    	}	
    	
    	//store settings in settings table
    	try {
	    	Statement state = DatabaseConnection.con.createStatement(); 
	    	state.execute("UPDATE settings SET Account = '" + account + "', Linesize = '"+ lineThickness +"' WHERE ID LIKE 1;");
	    	state.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    static void loadSettings() {
    	try {
			Statement state = DatabaseConnection.con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='settings'");

			if(!res.next()){
				System.out.println("Building the settings table with prepoulated values.");
				// need to build the table 
				state.execute("CREATE TABLE settings(ID integer,"
						+ "Account double," 
						+ "Linesize double,"
						+ "primary key(ID));");
				state.execute("INSERT INTO settings (ID, Account, Linesize) VALUES (1, 0, 3);");
			}
			
			res = state.executeQuery("SELECT * FROM settings;");
			while(res.next()) {
				account = res.getDouble("Account");
				lineThickness = res.getDouble("Linesize");
			}
			
			state.close();
			res.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage)bAbbrechen.getScene().getWindow();
        stage.close();
    }
}
