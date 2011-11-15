package services;

import java.math.BigDecimal;
import java.util.List;

import services.financeinfo.QuoteInfo;

import models.Portfolio;
import models.Portfolio.WatchQuote;
import models.PortfolioEntry;
import models.PortfolioTransaction;
import models.Quote;
import models.User;

public class PortfolioServices {


	public static WatchQuote addWatchQuote(Portfolio portfolio, String symbol)
			throws ServicesException {
		QuoteInfo quoteInfo = null;

		quoteInfo = QuoteServices.getQuoteInfo(symbol);

		if (quoteInfo != null) {

			if (!portfolio.containWatchQuote(quoteInfo.getSymbol())) {
				Portfolio.WatchQuote wq = portfolio.addWatchQuote(
						quoteInfo.getSymbol(), quoteInfo.getCompanyName());
				portfolio.update();

				return wq;
			} else {
				throw new ServicesException("quote already exist");
			}

		}

		return null;
	}

	public static void removeWatchQuote(Portfolio portfolio, String symbol)
			throws ServicesException {

		if (portfolio.containWatchQuote(symbol)) {
			portfolio.removeWatchQuote(symbol);
		} else {
			throw new ServicesException("Can't find symbol : " + symbol);
		}
		portfolio.update();
	}
	
	
	public static void executeTransaction(Portfolio portfolio, String symbol,
			PortfolioTransaction.Type type, int quantity) throws ServicesException
	{
		Quote quote = QuoteServices.findQuote(symbol);


		BigDecimal price = quote.marketPrice;
		BigDecimal currentBalance = portfolio.balance;
		
		if(PortfolioTransaction.Type.BUY.equals(type)){
			int maxBuy = (int) (currentBalance.doubleValue() / price
					.doubleValue());
			
			if (quantity > 0 && quantity <= maxBuy) {
				TransactionServices.executeBuyTransaction(portfolio,
						quote, quantity);
			}else{
				throw new ServicesException("Wrong quantity (" + quantity
						+ ") the max quantiy you can buy is (" + maxBuy
						+ ") at actual price of " + price + " $");
			}
			
		}else if(PortfolioTransaction.Type.SELL.equals(type)){
			
			List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();
			
			PortfolioEntry portfolioEntryToSell = null;			
			for (PortfolioEntry portfolioEntry : portfolioEntries) {
				if(symbol.equals(portfolioEntry.quote.symbol)){
					portfolioEntryToSell = portfolioEntry;
				}
			}
			
			
			if(portfolioEntryToSell != null){
				int shareQty = portfolioEntryToSell.shareQty;
				if (quantity > 0 && quantity <= shareQty) {
					TransactionServices.executeSellTransaction(portfolio,
							quote, quantity);
				}else{
					throw new ServicesException("Wrong quantity (" + quantity
							+ ") the maximum quantiy you can sell is ("
							+ shareQty + ")");
				}
				
			}else{
				throw new ServicesException("Your portfolio do not contain any positions with symbol: "
						+ symbol);
			}
		}
	}

}
