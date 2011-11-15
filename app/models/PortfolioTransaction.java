package models;

import java.math.BigDecimal;
import java.util.Date;

import siena.*;


@Table("portfolio_transactions")
public class PortfolioTransaction extends Model {
	
	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
	@Column("portfolio")
	public Portfolio portfolio;
	
	@Column("quote")
	public Quote quote;
    
	@Column("quantity")
	public int quantity;
	
	@Column("unitPrice")
	public BigDecimal unitPrice;
	
	@Column("executionDate")
	public Date executionDate;
	
	@Column("type")
	public Type type;
	
	public PortfolioTransaction(){
		
	}
	
	 public static enum Type{
     	BUY,
     	SELL
     };
     
	public PortfolioTransaction(Portfolio portfolio, Quote quote ,BigDecimal unitPrice, int qty,  Date executionDate, Type type){
		this.portfolio = portfolio;
		this.quote = quote;
		this.unitPrice = unitPrice;
		this.executionDate = executionDate;
		this.type = type;
		this.quantity = qty;
	}
	
	public BigDecimal getAmount(){
		return unitPrice.multiply(new BigDecimal(quantity));
	}
	
}
