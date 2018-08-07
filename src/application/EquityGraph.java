package application;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javafx.application.Platform;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
 
public class EquityGraph{
	private LineChart<Number, Number> lineChart;
	
	public EquityGraph(){
		generateGraphic();
	}
	
	public void generateGraphic(){          	
		//creating the chart
    	final NumberAxis xAxis = new NumberAxis();
    	final NumberAxis yAxis = new NumberAxis();
        yAxis.setForceZeroInRange(false);
    	
    	lineChart = new LineChart<Number,Number>(xAxis,yAxis);        	
    	lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true); //hide dots  
        lineChart.setLegendVisible(false); //hide legend
        
        //set the thickness of the line chart series   
        if(Settings.lineThickness > 4){
        	lineChart.setId("chart-series-line5");   	
        }else if(Settings.lineThickness > 3){
        	lineChart.setId("chart-series-line4");   	
        }else if(Settings.lineThickness > 2){
        	lineChart.setId("chart-series-line3");   	
        }else if(Settings.lineThickness > 1){
        	lineChart.setId("chart-series-line2");   	
        }else if(Settings.lineThickness >= 0){
        	lineChart.setId("chart-series-line1");   	
        }
        lineChart.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        
		Platform.runLater(new Runnable() {
		    @Override
		    public void run() { 
		    	XYChart.Series<Number, Number> series = new XYChart.Series<>();
		    	double equity = Settings.account;
		    	int counter = 1;	
		    	
				//add series to line chart
				lineChart.getData().add(series);   
				//set starting point
		    	series.getData().add(new XYChart.Data<Number, Number>(0,equity));
		    	 
				for(Trade t : Controller.trades){
					equity += (t.getGewinn()+t.getKommission()+t.getSwap());
					series.getData().add(new XYChart.Data<Number, Number>(counter, equity));	

					Tooltip.install(series.getData().get(counter).getNode(), new Tooltip(t.getId() + "\n" + t.getZeitAusstieg() + "\n" + NumbersCalculation.round(equity,2)));
					
	                StackPane stackPane =  (StackPane)series.getData().get(counter).getNode();
	                stackPane.setPrefWidth(Settings.lineThickness+3);
	                stackPane.setPrefHeight(Settings.lineThickness+3);
	                
	                counter++;
				}        
		    }
		});
        Controller.lineChart = lineChart;
	}
	
	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
}