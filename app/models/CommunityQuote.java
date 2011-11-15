package models;

import java.util.ArrayList;
import java.util.List;

public class CommunityQuote {

	public Community community;
	
	public Quote quote;
	
	public List<PortfolioEntry> portfolioEntries;
	
	public List<PortfolioTransaction> portfolioTransactions;
	
	public CommunityQuote(Community community, Quote quote){
		this.community = community;
		this.quote = quote;
		this.portfolioEntries = new ArrayList<PortfolioEntry>();
		this.portfolioTransactions = new ArrayList<PortfolioTransaction>();
	}
	
}
