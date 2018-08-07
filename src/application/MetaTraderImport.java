package application;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Scanner;

import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MetaTraderImport{
	static final long serialVersionUID = 13;	
	@SuppressWarnings("rawtypes")
	TableView tableView;
	Controller controller;
	Window window;
	File file;

    void openSearch() throws SQLException {		
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Backtest auswählen");
			file = chooser.showOpenDialog(window);

			addImports();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @SuppressWarnings("rawtypes")
	public MetaTraderImport(TableView tableView, Controller controller, Window window){
    	this.controller = controller;
    	this.tableView = tableView;
    	this.window = window;
    	
    	try {
			openSearch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
	public void addImports() throws ClassNotFoundException, SQLException{
		
		if(DatabaseConnection.con == null){
			DatabaseConnection.getConnection();
		}
		PreparedStatement prep = DatabaseConnection.con.prepareStatement("INSERT OR IGNORE INTO trades VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);");

		try{
			Scanner scanner = new Scanner(new FileReader(file));
			scanner.useDelimiter(";");
			scanner.useLocale(Locale.US);
		
			while(scanner.hasNext()) {
				try{		
					prep.setInt(1, scanner.nextInt());
					prep.setString(2, ""); //strategy
					prep.setString(3, scanner.next()); //symbol
					prep.setString(4, scanner.next()); //lots
					prep.setString(5, scanner.next()); //long short
					
					String entry = scanner.next();
					prep.setString(6, entry); //entry
					
					prep.setString(7, scanner.next()); //entry time
					String exit = scanner.next();
					
					prep.setString(8, exit); //exit 
					prep.setString(9, scanner.next()); //exit time
					prep.setString(10, scanner.next()); //commission
					prep.setString(11, scanner.next()); //swap
					prep.setString(12, scanner.next()); //profit
					String sl = scanner.next();
					prep.setString(13, sl); //sl
					String tp = scanner.next();
					prep.setString(14, tp); //tp
					
					double crv = 0;
					if(Trade.ParseDouble(tp) != 0 && Trade.ParseDouble(sl) != 0 && Trade.ParseDouble(entry) != 0){
						crv = (Trade.ParseDouble(tp)-Trade.ParseDouble(entry))/(Trade.ParseDouble(entry)-Trade.ParseDouble(sl));
			    	}
					prep.setString(15, Double.toString(crv)); //crv
		
					double rcrv = 0;
					if(Trade.ParseDouble(sl) != 0 && Trade.ParseDouble(entry) != 0 && Trade.ParseDouble(exit) != 0){	
						rcrv = (Trade.ParseDouble(exit)-Trade.ParseDouble(entry))/(Trade.ParseDouble(entry)-Trade.ParseDouble(sl));
					}	
					prep.setString(16, Double.toString(rcrv)); //rcrv
					
					prep.setString(17, ""); //risk
					prep.setString(18, scanner.next()); //comment
					prep.addBatch();
					
					if(scanner.hasNextLine()) scanner.nextLine();	
				} catch(Exception e){e.printStackTrace();}	
			}		   
			prep.executeBatch();
			prep.close();
			scanner.close();
		} catch (FileNotFoundException e) { 
			e.printStackTrace();
			Alert alert = new Alert(AlertType.ERROR);
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			alert.setTitle("Fehler");
			alert.setContentText("Der Import konnte nicht ausgeführt werden. Das kann an dem Format der .txt Datei liegen. Ein neuer MT4 Export kann das Problem lösen.");
			alert.showAndWait();
		}
		
		Controller.trades = DatabaseConnection.getTrades();
		DatabaseConnection.refreshTable(tableView);
		controller.updateGraphic();
		controller.updateNumbers();
	}
}
