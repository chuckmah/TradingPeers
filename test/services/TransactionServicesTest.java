package services;

import java.math.BigDecimal;

import models.Portfolio;
import models.PortfolioEntry;
import models.PortfolioTransaction;
import models.Quote;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.modules.siena.SienaFixtures;
import play.test.UnitTest;
import utils.TradingPeersTestHelper;

public class TransactionServicesTest extends UnitTest {

	@Before
	public void setUp() {

		TradingPeersTestHelper.loadTestData();
		
		
	}

	@Test
	public void executeBuyTransactionTest() {
		
		//Get The YHOO quote
		Quote quote = Quote.findBySymbol("YHOO");
		assertNotNull(quote);

    	//Retrieve user A portfolio
    	Portfolio portfolio = TradingPeersTestHelper.getUserTestAPortfolio();
    	
    	
    	
    	PortfolioTransaction portfolioTransaction = null;
    	
    	try {
    		portfolioTransaction = TransactionServices.executeBuyTransaction(portfolio, quote, 1);
		} catch (ServicesException e) {
			fail(e.getMessage());
		}
    	
    	//Assert that the PortfolioTransaction is created.
    	assertNotNull(portfolioTransaction);
    	
    	//Assert that the PortfolioEntry is created.
    	PortfolioEntry portfolioEntry = PortfolioEntry.findByPortoflioAndQuote(portfolio, quote);
    	assertNotNull(portfolioEntry);
    	
    	
    	//Assert that the balance is updated.
    	assertEquals(9634,portfolio.balance.longValue());
    	
	}

	@Test
	public void executeSellTransactionTest() {
		//Get The YHOO quote
		Quote quote = Quote.findBySymbol("CMK.TO");
		assertNotNull(quote);

    	//Retrieve user A portfolio
    	Portfolio portfolio = TradingPeersTestHelper.getUserTestAPortfolio();
    	
    	
    	PortfolioTransaction portfolioTransaction = null;
    	try {
    		portfolioTransaction = TransactionServices.executeSellTransaction(portfolio, quote, 100);
		} catch (ServicesException e) {
			fail(e.getMessage());
		}
    	
    	//Assert that the PortfolioTransaction is created.
    	assertNotNull(portfolioTransaction);
    	
    	//Assert that the PortfolioEntry is deleted.
    	PortfolioEntry portfolioEntry = PortfolioEntry.findByPortoflioAndQuote(portfolio, quote);
    	assertNull(portfolioEntry);
    	
    	//Assert that the balance is updated.
    	assertEquals(10000, portfolio.balance.longValue());

	}
}
