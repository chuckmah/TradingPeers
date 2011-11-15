package services;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import play.Logger;



import services.financeinfo.*;

import models.Quote;


public class QuoteServices {

	/**
	 * Find a quote in the system, if it's not exist then it's call the addQuote Method.
	 * @param symbol
	 * @return
	 * @throws ServicesException
	 */
	public static Quote findQuote(String symbol) throws ServicesException {

		Quote quote = null;

		quote = Quote.findBySymbol(symbol);

		if (quote == null) {
			quote = addQuote(symbol);

		} else {
			refreshQuote(quote);
		}

		return quote;
	}
	
	/**
	 * Create a quote in the system using the FinanceInfoManager.
	 * 
	 * @param symbol
	 * @return
	 * @throws ServicesException
	 */
	public static Quote addQuote(String symbol) throws ServicesException {
		Quote quote = null;

		if (StringUtils.isEmpty(symbol)) {
			return null;
		}

		if (quote == null) {

			QuoteInfo quoteInfo = FinanceInfoManager.getQuoteInfo(symbol);

			if (quoteInfo != null) {
				quote = new Quote(symbol);

				quote.companyName = quoteInfo.getCompanyName();
				quote.marketPrice = quoteInfo.getLastTradePriceOnly();

				quote.insert();
			} else {
				throw new ServicesException("invalid symbol " + symbol);
			}
		}

		return quote;

	}
	
	/**
	 * Refresh a system quote using the FinanceInfoManager.
	 * @param quote
	 * @return
	 */
	private static Quote refreshQuote(Quote quote) {

		QuoteInfo quoteInfo = FinanceInfoManager.getQuoteInfo(quote.symbol);

		if (quoteInfo != null) {

			quote.companyName = quoteInfo.getCompanyName();
			quote.marketPrice = quoteInfo.getLastTradePriceOnly();

		} else {
			return null;
		}

		quote.save();

		return quote;
	}
	
	public static void refreshAllQuotes() {

		Logger.debug("Entering QuotesController.refreshAllQuotes()");
		
		List<Quote> quotes = Quote.findAll();
		
		Logger.debug("Refreshing " + quotes.size() + " quotes");
		
		String[] symbols = new String[quotes.size()];
		

		for (int i = 0; i < quotes.size(); i++) {
			
			if(quotes.get(i) instanceof Quote){
				Quote quote = quotes.get(i);
				symbols[i] = quote.symbol;
			}else{
				Logger.error("unknow quote element");
				return;
			}
			

		}

		Map<String, QuoteInfo> quoteInfos = FinanceInfoManager
				.getQuoteInfo(symbols);
		
		for (Quote quote : quotes) {
			QuoteInfo quoteInfo = quoteInfos.get(quote.symbol);

			if (quoteInfo != null) {
				boolean hasChanged = false;
				if (!quote.companyName.equals(quoteInfo.getCompanyName())) {
					quote.companyName = quoteInfo.getCompanyName();
					hasChanged = true;
				}
				if (!quote.marketPrice
						.equals(quoteInfo.getLastTradePriceOnly())) {
					quote.marketPrice = quoteInfo.getLastTradePriceOnly();
					hasChanged = true;
				}

				if (hasChanged) {
					quote.save();
				}

			} else {
				Logger.error("Error could not refresh quote: " + quote.symbol);
			}
		}

		Logger.debug("Exiting QuotesController.refreshAllQuotes()");
		
	}
	
	/**
	 * Validate a symbol & Company Name by returning the Company name using the FinanceInfoManager.
	 * 
	 * @param symbol
	 * @return
	 * @throws ServicesException
	 */
	public static QuoteInfo getQuoteInfo(String symbol) throws ServicesException {
		if (StringUtils.isEmpty(symbol)) {
			throw new ServicesException("Symbol can't be null or empty");
		}
		
		QuoteInfo quoteInfo = FinanceInfoManager.getQuoteInfo(symbol);
		if (quoteInfo != null) {
			return quoteInfo;
		} else {
			throw new ServicesException();
		}

	}
	

	
}
