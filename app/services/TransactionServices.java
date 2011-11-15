package services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


import models.Portfolio;
import models.PortfolioEntry;
import models.PortfolioTransaction;
import models.Quote;

/**
 * 
 * @author chuck
 *
 */
public class TransactionServices  {

	/**
	 * 
	 * Execute a buy transaction ,
	 * 	- Update/Create the PortfolioEntry object
	 * 	- Create the PortfolioTransaction object
	 * 	- Update the portfolio balance.
	 * 
	 * @param portfolio
	 * @param quote
	 * @param quantity
	 * @throws ServicesException
	 */
	public static PortfolioTransaction executeBuyTransaction(Portfolio portfolio, Quote quote,
			int quantity) throws ServicesException  {
		BigDecimal marketPrice = quote.marketPrice;

		BigDecimal transactionPrice = marketPrice.multiply(new BigDecimal(
				quantity));
		BigDecimal newBalance = portfolio.balance.subtract(transactionPrice);

		PortfolioTransaction transaction = new PortfolioTransaction(portfolio,
				quote, marketPrice, quantity, new Date(),
				PortfolioTransaction.Type.BUY);

		portfolio.balance = newBalance;

		List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();

		PortfolioEntry position = null;
		for (PortfolioEntry portfolioEntry : portfolioEntries) {
			if (quote.symbol.equals(portfolioEntry.quote.symbol)) {
				position = portfolioEntry;
			}
		}

		if (position == null) {
			position = new PortfolioEntry(quote, portfolio);
			position.averagePrice = quote.marketPrice;
			position.shareQty = quantity;
			position.insert();

		} else {
			BigDecimal previousAveragePrice = position.averagePrice;
			int previousQuantity = position.shareQty;
			BigDecimal previousTotalPrice = previousAveragePrice
					.multiply(new BigDecimal(previousQuantity));
			position.averagePrice = (previousTotalPrice.add(transactionPrice))
					.divide(new BigDecimal(previousQuantity + quantity), 2,
							RoundingMode.HALF_UP);
			position.shareQty = previousQuantity + quantity;
			position.update();

		}

		transaction.insert();
		portfolio.update();
		
		return transaction;

	}

	public static PortfolioTransaction executeSellTransaction(Portfolio portfolio, Quote quote,
			int quantity) throws ServicesException {

		BigDecimal marketPrice = quote.marketPrice;

		BigDecimal transactionPrice = marketPrice.multiply(new BigDecimal(
				quantity));
		BigDecimal newBalance = portfolio.balance.add(transactionPrice);

		PortfolioTransaction transaction = new PortfolioTransaction(portfolio,
				quote, marketPrice, quantity, new Date(),
				PortfolioTransaction.Type.SELL);

		transaction.insert();

		portfolio.balance = newBalance;

		portfolio.update();

		List<PortfolioEntry> portfolioEntries = portfolio.loadPortfolioEntries();

		PortfolioEntry position = null;
		for (PortfolioEntry portfolioEntry : portfolioEntries) {
			if (quote.symbol.equals(portfolioEntry.quote.symbol)) {
				position = portfolioEntry;
			}
		}

		int sharesOwned = position.shareQty;

		if (quantity < sharesOwned) {

			position.shareQty = sharesOwned - quantity;
			position.update();
		} else if (quantity == sharesOwned) {

			position.delete();

		} else {
			throw new ServicesException(
					"Could not execute transaction, qty should not excess # of shared owned.");
		}
		
		return transaction;

	}

}
