package application;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Strategies implements Initializable{
	
//	static ObservableList<?> strategies = FXCollections.observableList(null);
	static List<String> strategies = new ArrayList<>();
	
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
		if(!strategies.contains(tfHinzufuegen.getText()) && !tfHinzufuegen.getText().isEmpty()){
			strategies.add(tfHinzufuegen.getText());
			try {
				Statement state = DatabaseConnection.con.createStatement();
				state.execute("INSERT INTO strategies VALUES ('"+tfHinzufuegen.getText()+"')");			
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			tfHinzufuegen.setText("");
	    	cbEntfernen.getItems().clear();
	    	cbEntfernen.getItems().addAll(FXCollections.observableList(strategies));
		} else{
			tfHinzufuegen.setText("");			
		}
    }

    @FXML
    void delete(ActionEvent event) {
    	strategies.remove(cbEntfernen.getValue().toString());
		try {
			Statement state = DatabaseConnection.con.createStatement();
			state.execute("DELETE FROM strategies WHERE Strategie LIKE '"+cbEntfernen.getValue().toString()+"'");			
			state.close(); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	cbEntfernen.getItems().clear();
    	cbEntfernen.getItems().addAll(FXCollections.observableList(strategies));
    }
	
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	cbEntfernen.getItems().clear();
		cbEntfernen.getItems().addAll(FXCollections.observableList(strategies));
	}
	
	public static void loadStrategies() throws SQLException{
		Statement state = DatabaseConnection.con.createStatement();
		ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='strategies'");
		
		if(!res.next()){
			System.out.println("Building the strategies table with prepoulated values.");
			// need to build the table 
			state.execute("CREATE TABLE strategies(Strategie varchar(60) PRIMARY KEY)");
		}

		res = state.executeQuery("SELECT * FROM strategies");
		while(res.next()){
			strategies.add(res.getString(1));	
		}
		state.close();
		res.close();
	}
}
