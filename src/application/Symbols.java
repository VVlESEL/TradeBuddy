package application;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Symbols implements Initializable {
	static ArrayList<String> symbols = new ArrayList<>();
	
    @FXML
    private ComboBox<String> cbEntfernen;

    @FXML
    private TextField tfHinzufuegen;

    @FXML
    private Button bAbbrechen;

    @FXML
    private Button bHinzufuegen;

    @FXML
    private Button bEntfernen;

    @FXML
    void closeWindow(ActionEvent event) {
        Stage stage = (Stage) bAbbrechen.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    void add(ActionEvent event) {
		if(!symbols.contains(tfHinzufuegen.getText())){
			symbols.add(tfHinzufuegen.getText());
			try {
				Statement state = DatabaseConnection.con.createStatement();
				state.execute("INSERT INTO symbols VALUES ('"+tfHinzufuegen.getText()+"')");			
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			tfHinzufuegen.setText("");
	    	cbEntfernen.getItems().clear();
	    	cbEntfernen.getItems().addAll(FXCollections.observableList(symbols));
		} else{
			tfHinzufuegen.setText("");			
		}
    }

    @FXML
    void delete(ActionEvent event) {
    	symbols.remove(cbEntfernen.getValue().toString());
		try {
			Statement state = DatabaseConnection.con.createStatement();
			state.execute("DELETE FROM symbols WHERE Symbol LIKE '"+cbEntfernen.getValue().toString()+"'");			
			state.close(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
    	cbEntfernen.getItems().clear();
    	cbEntfernen.getItems().addAll(FXCollections.observableList(symbols));
    }
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	cbEntfernen.getItems().clear();
    	cbEntfernen.getItems().addAll(FXCollections.observableList(symbols));
	}
    
	public static void loadSymbols() throws SQLException{
		Statement state = DatabaseConnection.con.createStatement();
		ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='symbols'");
		
		if(!res.next()){
			System.out.println("Building the symbols table with prepoulated values.");
			// need to build the table 
			state.execute("CREATE TABLE symbols(Symbol varchar(60) PRIMARY KEY)");
		}

		res = state.executeQuery("SELECT * FROM symbols");
		while(res.next()){
			symbols.add(res.getString(1));	
		}
		state.close();
		res.close();
	}
}
