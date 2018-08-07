package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class DatabaseConnection{
	public static Connection con;
	private static boolean hasData = false;
	
	public static ResultSet displayTrades() throws ClassNotFoundException, SQLException{
		if(con == null){
			getConnection();
		}
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT * FROM trades");
		return res;
	}
	
	public static void getConnection() throws ClassNotFoundException, SQLException{
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:trades1.db");
		initialise();
	}
	
	private static void initialise() throws SQLException{
		if(!hasData){
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='trades'");
			
			if(!res.next()){
				System.out.println("Building the trades table.");
				// need to build the table 
				state.execute("CREATE TABLE trades(ID integer,"
						+ "Strategie varchar(60)," 
						+ "Symbol varchar(60),"
						+ "Lots varchar(60),"
						+ "TypEinstieg varchar(60),"
						+ "Einstieg varchar(60),"
						+ "ZeitEinstieg varchar(60),"
						+ "Ausstieg varchar(60),"
						+ "ZeitAusstieg varchar(60),"
						+ "Kommission varchar(60),"
						+ "Swap varchar(60),"
						+ "Gewinn varchar(60),"
						+ "Stopp varchar(60),"
						+ "Ziel varchar(60),"
						+ "CRV varchar(60),"
						+ "RCRV varchar(60),"
						+ "Risiko varchar(60),"
						+ "Kommentar varchar(60),"
						+ "primary key(ID));");
			}
			
			state.close();
			res.close();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void refreshTable(TableView tableView) throws ClassNotFoundException, SQLException{
		FXCollections.sort(Controller.trades,new Trade());		
		tableView.getItems().clear();
		tableView.setItems(Controller.trades);	
		tableView.setTableMenuButtonVisible(true);
	}
	
	public static ObservableList<Trade> getTrades() throws ClassNotFoundException, SQLException{	
		ResultSet res = displayTrades();
		ObservableList<Trade> data = FXCollections.observableArrayList();

		while(res.next()){
			data.add(new Trade(
					Integer.toString(res.getInt(1)),
					res.getString(2),
					res.getString(3),
					res.getString(4),
					res.getString(5),
					res.getString(6),
					res.getString(7),
					res.getString(8),
					res.getString(9),
					res.getString(10),
					res.getString(11),
					res.getString(12),
					res.getString(13),
					res.getString(14),
					res.getString(15),
					res.getString(16),
					res.getString(17),
					res.getString(18)
			));
		}	
		res.close();	
		return data;
	}
}