package controllers;

import models.Portfolio;
import models.PortfolioTransaction;
import models.User;

import org.apache.commons.lang.StringUtils;

import controllers.Secure.Security;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import services.PortfolioServices;
import services.ServicesException;

@With(Secure.class)
public class MyPortfolio extends Controller {

	
	public static void index() {

	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
	  	
		Portfolio portfolio = 	 user.getPortfolio();
		
		if (portfolio != null) {
			renderArgs.put("portfolio", portfolio);
			renderArgs.put("portfolioEntries", portfolio.loadPortfolioEntries());
			renderArgs.put("transactions", portfolio.loadPortfolioTransactions());
		}
		

		render();
	}
	
	public static void retrieveWatchQuotes() {

	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
		
		Portfolio portfolio = user.getPortfolio();

		renderJSON(portfolio.watchQuotes);
		
	}

	public static void addQuoteToWatch(String symbol) {

	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
		
		Portfolio portfolio =  user.getPortfolio();
		
		try {

			renderJSON(PortfolioServices.addWatchQuote(portfolio, symbol));
			
		} catch (ServicesException e) {
			error(e.getMessage());
		}


	}

	public static void removeQuoteToWatch(String symbol) {

	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);
		
		Portfolio portfolio = user.getPortfolio();

		try {
			PortfolioServices.removeWatchQuote(portfolio, symbol);
		} catch (ServicesException e) {
			error(e.getMessage());
		}
		
	}
	
	public static void addTransaction(Long portfolioId, String symbol,
			String type, String quantity) {
		
	  	//user object is retrieved and put as object with name user in the request.
	  	User user = User.findByEmail(Security.connected());

	  	renderArgs.put("user", user);

		Portfolio portfolio = user.getPortfolio();
		
		if(StringUtils.isEmpty(symbol)){
			flash.error("Symbol is empty");
			index();
		}
		
		PortfolioTransaction.Type transactionType  = PortfolioTransaction.Type.valueOf(type.toUpperCase()) ;
		if(transactionType == null){
			flash.error("Can't parse transaction type : " + type);
			index();
		}
		
		int qty = -1;
		try {
			qty = Integer.parseInt(quantity);
		} catch (NumberFormatException e) {
			flash.error("Unable to parse quantity " + quantity);
			index();
		}

		try {
			PortfolioServices.executeTransaction(portfolio, symbol, transactionType, qty);
		} catch (ServicesException e) {
			flash.error(e.getMessage());
			index();
		}
		
		
		index();
		
	}
}
