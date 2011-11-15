package models;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import play.test.UnitTest;

import utils.TradingPeersTestHelper;

public class PortfolioEntriesTest extends UnitTest {

	@Before
	public void setUp() {
		
		TradingPeersTestHelper.loadTestData();
	
	}

	@Test
	public void testFindBy(){
		//Get The YHOO quote
		Quote quote = Quote.findBySymbol("CMK.TO");
		assertNotNull(quote);

    	//Retrieve user A portfolio
    	Portfolio portfolio = TradingPeersTestHelper.getUserTestAPortfolio();
    	
    	PortfolioEntry portfolioEntry = PortfolioEntry.findByPortoflioAndQuote(portfolio, quote);
    	assertNotNull(portfolioEntry);
		
	}
}
