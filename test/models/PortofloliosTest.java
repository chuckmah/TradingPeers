package models;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.Logger;
import play.modules.siena.SienaFixtures;
import play.test.UnitTest;
import utils.TradingPeersTestHelper;

public class PortofloliosTest  extends UnitTest{

	
	@Before
	public void setUp() {
		
		TradingPeersTestHelper.loadTestData();
	
	}
	
	

	
	@Test
	public void testPortofolioEntries(){

    	Portfolio portfolio = TradingPeersTestHelper.getUserTestAPortfolio();
    	
    	List<PortfolioEntry> entries = portfolio.portfolioEntries.fetch();
    	assertNotNull(portfolio);
    	
    	for (PortfolioEntry portfolioEntry : entries) {
    		Quote quote = portfolioEntry.quote;
    		quote.get();
        	assertNotNull(quote);
		}
	}
	
	

	
}
