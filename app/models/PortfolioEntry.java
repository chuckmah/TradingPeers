package models;

import java.math.BigDecimal;
import java.util.List;

import siena.*;







@Table("portfolioEntries")
public class PortfolioEntry extends Model {

	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
	@Column("portfolio")
	public Portfolio portfolio;
	
	@Column("quote")
	public Quote quote;
    
	@Column("shareQty")
    public int shareQty;

	@Column("averagePrice")
    public BigDecimal averagePrice;
    
    public PortfolioEntry (){   	
    }
    
    public PortfolioEntry (Quote quote, Portfolio portfolio){
    	this.portfolio = portfolio;
    	this.quote = quote;
    }

/*	public static List<PortfolioEntry> findByPortoflioId(Long id) {
		return PortfolioEntry.all(PortfolioEntry.class).filter("id", id).fetch();
	}*/
    
	public static PortfolioEntry findByPortoflioAndQuote(Portfolio portfolio, Quote quote) {
		return PortfolioEntry.all(PortfolioEntry.class).filter("portfolio", portfolio).filter("quote", quote).get();
	}
	
  /*  public static List<PortfolioEntry> findByQuote(Quote quote){
    	return Portfolio.find("select p from PortfolioEntry p where p.communityQuote.quote.id= ?", quote.id).fetch();

    }
    */

}
