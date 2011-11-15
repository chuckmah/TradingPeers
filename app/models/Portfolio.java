package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import siena.*;
import siena.embed.At;
import siena.embed.Embedded;
import siena.embed.EmbeddedList;



@Table("portfolios")
public class Portfolio extends Model {

	@Id(Generator.AUTO_INCREMENT)
    public Long id;

	@Column("User")
	public User user;
	
	@Column("balance")
    public BigDecimal balance;
    
	@Column("creationDate")
    public Date creationDate;
	
    @Embedded
    public List<WatchQuote> watchQuotes;
        
    @EmbeddedList
    public class WatchQuote {
      @At(0) public String symbol;
      @At(1) public String name;
    }
    
    
    @Filter("portfolio")
    public Query<PortfolioEntry> portfolioEntries;

    @Filter("portfolio")
    public Query<PortfolioTransaction> portfolioTransactions;
    
    public Portfolio(){
    	


    }
    
    public Portfolio (BigDecimal startingBalance){
    	
    	this();
    	this.balance = startingBalance;
    	this.creationDate = new Date();


    }
    
    public String toString()  {
        return "Id(" + id + ")";
    }

	public WatchQuote addWatchQuote(String symbol, String companyName) {
		if(watchQuotes == null){
			watchQuotes = new ArrayList<Portfolio.WatchQuote>();
		}
		
		WatchQuote watchQuote = siena.Util.createObjectInstance(WatchQuote.class);

		watchQuote.name = companyName;
		watchQuote.symbol = symbol;
		
		watchQuotes.add(watchQuote);
		
		return watchQuote;
	}

	public boolean containWatchQuote(String symbol) {
		if(watchQuotes != null){
			for (WatchQuote wq : watchQuotes) {
				if(symbol.equals(wq.symbol)){
					return true;
				}
			}
		}
		return false;
	}

	public void removeWatchQuote(String symbol) {
	
		if(watchQuotes != null){
			int index = 0 ;
			int indexToBeDeleted = 0;
			for (WatchQuote wq : watchQuotes) {
				if(symbol.equals(wq.symbol)){
					indexToBeDeleted = index;
					break;
				}
				index++;
			}
			watchQuotes.remove(indexToBeDeleted);
		}
		
	}



	public  List<PortfolioEntry> loadPortfolioEntries() {

		List<PortfolioEntry> entries = portfolioEntries.fetch();

		for (PortfolioEntry portfolioEntry : entries) {

			// load the quote
			portfolioEntry.quote.get();
		}

		return entries;
	}

	public  List<PortfolioTransaction> loadPortfolioTransactions() {

		List<PortfolioTransaction> transactions = portfolioTransactions
				.fetch();

		for (PortfolioTransaction transaction : transactions) {

			// load the quote
			transaction.quote.get();
		}

		return transactions;
	}
	



}
