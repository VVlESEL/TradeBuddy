package application;

import java.util.Comparator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Trade implements Comparator<Trade> {
	
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty strategie;
	private final SimpleStringProperty symbol;
	private final SimpleDoubleProperty lots;
	private final SimpleStringProperty typEinstieg;
	private final SimpleDoubleProperty einstieg;
	private final SimpleStringProperty zeitEinstieg;
	private final SimpleDoubleProperty ausstieg;
	private final SimpleStringProperty zeitAusstieg;
	private final SimpleDoubleProperty kommission;
	private final SimpleDoubleProperty swap;
	private final SimpleDoubleProperty gewinn;
	private final SimpleDoubleProperty stopp;
	private final SimpleDoubleProperty ziel;
	private final SimpleDoubleProperty crv;
	private final SimpleDoubleProperty rcrv;
	private final SimpleDoubleProperty risiko;
	private final SimpleStringProperty kommentar;
	
	public Trade(){
		this.id = new SimpleIntegerProperty(1);
		this.strategie = new SimpleStringProperty("");
		this.symbol = new SimpleStringProperty("");
		this.lots = new SimpleDoubleProperty(0);
		this.typEinstieg = new SimpleStringProperty("");
		this.einstieg = new SimpleDoubleProperty(0);
		this.zeitEinstieg = new SimpleStringProperty("");
		this.ausstieg = new SimpleDoubleProperty(0);
		this.zeitAusstieg = new SimpleStringProperty("");
		this.kommission = new SimpleDoubleProperty(0);
		this.swap = new SimpleDoubleProperty(0);
		this.gewinn = new SimpleDoubleProperty(0);
		this.stopp = new SimpleDoubleProperty(0);
		this.ziel = new SimpleDoubleProperty(0);
		this.crv = new SimpleDoubleProperty(0);
		this.rcrv = new SimpleDoubleProperty(0);
		this.risiko = new SimpleDoubleProperty(0);
		this.kommentar = new SimpleStringProperty("");
	}
	
	public Trade(String id, String strategie, String symbol, String lots, String typEinstieg, String eintieg, String zeitEinstieg, String ausstieg, 
			String zeitAusstieg, String kommission, String swap, String gewinn, String stopp, String ziel, String crv, String rcrv, String risiko, String kommentar){
		this.id = new SimpleIntegerProperty(Integer.valueOf(id));
		this.strategie = new SimpleStringProperty(strategie);
		this.symbol = new SimpleStringProperty(symbol);
		this.lots = new SimpleDoubleProperty(ParseDouble(lots));
		this.typEinstieg = new SimpleStringProperty(typEinstieg);
		this.einstieg = new SimpleDoubleProperty(ParseDouble(eintieg));
		this.zeitEinstieg = new SimpleStringProperty(zeitEinstieg);
		this.ausstieg = new SimpleDoubleProperty(ParseDouble(ausstieg));
		this.zeitAusstieg = new SimpleStringProperty(zeitAusstieg);
		this.kommission = new SimpleDoubleProperty(ParseDouble(kommission));
		this.swap = new SimpleDoubleProperty(ParseDouble(swap));
		this.gewinn = new SimpleDoubleProperty(ParseDouble(gewinn));
		this.stopp = new SimpleDoubleProperty(ParseDouble(stopp));
		this.ziel = new SimpleDoubleProperty(ParseDouble(ziel));
		this.crv = new SimpleDoubleProperty(ParseDouble(crv));
		this.rcrv = new SimpleDoubleProperty(ParseDouble(rcrv));
		this.risiko = new SimpleDoubleProperty(ParseDouble(risiko));
		this.kommentar = new SimpleStringProperty(kommentar);
	}

	public Integer getId() {
		return id.get();
	}

	public String getStrategie() {
		return strategie.get();
	}

	public String getSymbol() {
		return symbol.get();
	}

	public Double getLots() {
		return lots.get();
	}

	public String getTypEinstieg() {
		return typEinstieg.get();
	}

	public Double getEinstieg() {
		return einstieg.get();
	}

	public String getZeitEinstieg() {
		return zeitEinstieg.get();
	}

	public Double getAusstieg() {
		return ausstieg.get();
	}

	public String getZeitAusstieg() {
		return zeitAusstieg.get();
	}

	public Double getKommission() {
		return kommission.get();
	}

	public Double getSwap() {
		return swap.get();
	}
	
	public Double getGewinn() {
		return gewinn.get();
	}

	public Double getStopp() {
		return stopp.get();
	}

	public Double getZiel() {
		return ziel.get();
	}
	
	public Double getCrv() {
		return crv.get();
	}
	
	public Double getRcrv() {
		return rcrv.get();
	}
	
	public Double getRisiko() {
		return risiko.get();
	}
	
	public String getKommentar() {
		return kommentar.get();
	}
	
	static double ParseDouble(String strNumber){
		if (strNumber != null && strNumber.length() > 0){
			try{
	    		return Double.parseDouble(strNumber);
	         }catch(Exception e){
	        	 return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
	         }
		}
		else return 0;
	}

	@Override
	public int compare(Trade o1, Trade o2) {
		if(o1.zeitAusstieg.get().compareTo(o2.zeitAusstieg.get()) == 0){
			if(o1.id.get() < o2.id.get()) return -1;
			else return 1;
		}
		if(o1.zeitAusstieg.get().compareTo(o2.zeitAusstieg.get()) < 0) return -1;
		else if(o1.zeitAusstieg.get().compareTo(o2.zeitAusstieg.get()) > 0) return 1;
		return 0;
	}
}
