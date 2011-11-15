package models;

import java.math.BigDecimal;

import siena.*;


public class PortfolioStat {


	public Portfolio portfolio;
	
	public BigDecimal marketValue;
	
	public String marketValueChange;
	
	public Integer entriesTotal;
	
	public Integer transactionTotal;
	
	public PortfolioStat(Portfolio portfolio, Integer entriesTotal, Integer transactionTotal) {
		this.portfolio = portfolio;
		this.entriesTotal = entriesTotal;
		this.transactionTotal = transactionTotal;

	}
}
