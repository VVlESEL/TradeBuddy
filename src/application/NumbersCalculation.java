package application;
import javafx.scene.control.Label;

public class NumbersCalculation{

    private Label labelVerslusttradesInFolge;
    private Label labelAnzahlDerTrades;
    private Label labelNettoprofitGesamt;
    private Label labelDurchschnittGewinntrade;
    private Label labelSellTrades;
    private Label labelVerlustInFolge;
    private Label labelBuyTrades;
    private Label labelMaximumGewinnInFolge;
    private Label labelGroessterVerlusttrade;
    private Label labelErwartetesErgebnis;
    private Label labelBruttoprofit;
    private Label labelMaximumGewinntradesInFolge;
    private Label labelMaximalerRueckgangInProzent;
    private Label labelBruttoverlust;
    private Label labelDurchschnittVerlusttrade;
    private Label labelProfitfaktor;
    private Label labelGewinntrades;
    private Label labelMaximalerRueckgang;
    private Label labelGroessterGewinntrade;
    private Label labelVerlusttrades;

	NumbersCalculation(
		Label labelVerslusttradesInFolge,
	    Label labelAnzahlDerTrades,
	    Label labelNettoprofitGesamt,
	    Label labelDurchschnittGewinntrade,
	    Label labelSellTrades,
	    Label labelVerlustInFolge,
	    Label labelBuyTrades,
	    Label labelMaximumGewinnInFolge,
	    Label labelGroessterVerlusttrade,
	    Label labelErwartetesErgebnis,
	    Label labelBruttoprofit,
	    Label labelMaximumGewinntradesInFolge,
	    Label labelMaximalerRueckgangInProzent,
	    Label labelBruttoverlust,
	    Label labelDurchschnittVerlusttrade,
	    Label labelProfitfaktor,
	    Label labelGewinntrades,
	    Label labelMaximalerRueckgang,
	    Label labelGroessterGewinntrade,
	    Label labelVerlusttrades){		

		this.labelVerslusttradesInFolge = labelVerslusttradesInFolge;
		this.labelAnzahlDerTrades = labelAnzahlDerTrades;	
		this.labelNettoprofitGesamt = labelNettoprofitGesamt;
		this.labelDurchschnittGewinntrade = labelDurchschnittGewinntrade;
		this.labelSellTrades = labelSellTrades;
		this.labelVerlustInFolge = labelVerlustInFolge;
		this.labelBuyTrades = labelBuyTrades;
		this.labelMaximumGewinnInFolge = labelMaximumGewinnInFolge;
		this.labelGroessterVerlusttrade = labelGroessterVerlusttrade;
		this.labelErwartetesErgebnis = labelErwartetesErgebnis;
		this.labelBruttoprofit = labelBruttoprofit;
		this.labelMaximumGewinntradesInFolge = labelMaximumGewinntradesInFolge;
		this.labelMaximalerRueckgangInProzent = labelMaximalerRueckgangInProzent;
		this.labelBruttoverlust = labelBruttoverlust;
		this.labelDurchschnittVerlusttrade = labelDurchschnittVerlusttrade;
		this.labelProfitfaktor = labelProfitfaktor;
		this.labelGewinntrades = labelGewinntrades;
		this.labelMaximalerRueckgang = labelMaximalerRueckgang;
		this.labelGroessterGewinntrade = labelGroessterGewinntrade;
		this.labelVerlusttrades = labelVerlusttrades;
		
		calcNumbers();
	}
	
	public void calcNumbers(){
		double profit = 0, wonTradesProfit = 0, lostTradesLoss = 0;
		double maxDrawdown = 0, maxDrawdownInPercent = 0, maxProfit = 0, tempDrawdown = 0;
		int tradesCounter = 0, sellPositions = 0, sellPositionsWon = 0, buyPositions = 0, buyPositionsWon = 0;
		int wonTrades = 0, lostTrades = 0;
		double biggestWonTrade = 0, biggestLostTrade = 0;
		int maxWinRow = 0, maxLooseRow = 0, tempWinRow = 0, tempLooseRow = 0; 
		double tempWinRowProfit = 0, tempLooseRowProfit = 0, maxWinRowProfit = 0, maxLooseRowProfit = 0;

		//calclate maxDD and profit
		double maxAccountBalance = Settings.account;
		double tempDrawdownAccountBalance = Settings.account;
		for(Trade t : Controller.trades){
			profit += (t.getGewinn() + t.getKommission() + t.getSwap());
			if(profit >= maxProfit){
				maxProfit = profit;
				tempDrawdown = profit;
				maxAccountBalance = Settings.account + profit;
				tempDrawdownAccountBalance = Settings.account + profit;
			}
			else{
				tempDrawdown += t.getGewinn();
				tempDrawdownAccountBalance += t.getGewinn();
				if(maxAccountBalance != 0){
					double drawdownInPercent = ((maxAccountBalance-tempDrawdownAccountBalance) / maxAccountBalance) * 100;
					maxDrawdownInPercent = Math.max(drawdownInPercent, maxDrawdownInPercent);
				}
				if(tempDrawdown <= maxProfit - maxDrawdown) maxDrawdown = maxProfit - tempDrawdown;
			}		
		}
		if(Settings.account == 0) maxDrawdownInPercent = 99.99;
		
		//calculate other numbers
		for(Trade t : Controller.trades){
			double tradeProfit = t.getGewinn() + t.getKommission() + t.getSwap();
			
			//calculate profit, profitBuy, profitSell 
			if(tradeProfit >= 0) wonTradesProfit += tradeProfit;
			else lostTradesLoss += tradeProfit;
			
			//calculate amount of trades
			tradesCounter++; 
			if(tradeProfit >= 0) wonTrades++;
			else lostTrades++;
			if(t.getTypEinstieg().equals("Long")){
				buyPositions++;
				if(tradeProfit >= 0){
					buyPositionsWon++;
				}			
			}else{
				sellPositions++;
				if(tradeProfit >= 0){
					sellPositionsWon++;
				}
			}

			//calculate biggest win and biggest loss
			if(tradeProfit > biggestWonTrade) biggestWonTrade = tradeProfit;
			if(tradeProfit < biggestLostTrade) biggestLostTrade = tradeProfit;

			//calculate max win streak
			if(tradeProfit >= 0){
				tempWinRow++;
				tempWinRowProfit += tradeProfit;
				maxWinRowProfit = Math.max(tempWinRowProfit,maxWinRowProfit);
				maxWinRow = Math.max(tempWinRow, maxWinRow);
			}else{
				tempWinRow = 0;
				tempWinRowProfit = 0;
			}
			if(tradeProfit <= 0){
				tempLooseRow++;
				tempLooseRowProfit += tradeProfit;
				maxLooseRowProfit = Math.min(tempLooseRowProfit,maxLooseRowProfit);
				maxLooseRow = Math.max(tempLooseRow, maxLooseRow);
			}else{
				tempLooseRow = 0;
				tempLooseRowProfit = 0;
			}
		}

		//profit
		labelNettoprofitGesamt.setText(String.valueOf(round(profit,2)));
		//profit factor
		labelProfitfaktor.setText(String.valueOf(Math.abs(round(wonTradesProfit/lostTradesLoss,2))));
		//trades amount
		labelAnzahlDerTrades.setText(String.valueOf(tradesCounter));

		//wonTradesProfit
		labelBruttoprofit.setText(String.valueOf(round(wonTradesProfit,2)));
		//expectedProfit
		labelErwartetesErgebnis.setText(String.valueOf(round(profit/tradesCounter,2)));
		//max Drawdown
		labelMaximalerRueckgang.setText(String.valueOf(Math.abs(round(maxDrawdown,2))));
		//max Drawdown in percent
		labelMaximalerRueckgangInProzent.setText(String.valueOf(Math.abs(round(maxDrawdownInPercent,2))) + "%");
		//sellPositions and won
		double percent =  round((sellPositionsWon/(double)sellPositions)*100,2);
		labelSellTrades.setText(String.valueOf(sellPositions) + " (" + percent + "%)");
		//win trades and percent of all 
		percent = round((buyPositionsWon+sellPositionsWon)/(double)tradesCounter*100,2);
		labelGewinntrades.setText(String.valueOf(buyPositionsWon+sellPositionsWon) + " (" + String.valueOf((double)percent) + "%)");		
		//biggest win
		labelGroessterGewinntrade.setText(String.valueOf(round(biggestWonTrade,2)));
		//avg win
		labelDurchschnittGewinntrade.setText(String.valueOf(round(wonTradesProfit/wonTrades,2)));
		//bigges win streak
		labelMaximumGewinntradesInFolge.setText(String.valueOf(maxWinRow));
		//bigges win streak money
		labelMaximumGewinnInFolge.setText(String.valueOf(round(maxWinRowProfit,2)));	
		
		//lostTradesLoss
		labelBruttoverlust.setText(String.valueOf(round(lostTradesLoss,2)));
		//buyPositins and won
		percent = round((buyPositionsWon/(double)buyPositions)*100,2);
		labelBuyTrades.setText(String.valueOf(buyPositions) + " (" + String.valueOf(percent) + "%)");
		//lost trades and percent of all
		percent = round((tradesCounter-buyPositionsWon-sellPositionsWon)/(double)tradesCounter*100,2);
		labelVerlusttrades.setText(String.valueOf(tradesCounter-buyPositionsWon-sellPositionsWon) + " (" + String.valueOf(percent) + "%)");		
		//biggest loss
		labelGroessterVerlusttrade.setText(String.valueOf(round(biggestLostTrade,2)));
		//avg loss
	    labelDurchschnittVerlusttrade.setText(String.valueOf(round(lostTradesLoss/lostTrades,2)));
		//bigges loss streak
		labelVerslusttradesInFolge.setText(String.valueOf(maxLooseRow));
		//bigges loss streak
		labelVerlustInFolge.setText(String.valueOf(round(maxLooseRowProfit,2)));
	}

	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
