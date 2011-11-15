package services.financeinfo;

import java.math.BigDecimal;

public class QuoteInfo {

	
	private String symbol;
	
	private String companyName;
	
	private BigDecimal lastTradePriceOnly;
	
	private BigDecimal previousClose;
	
	private String changeinPercent;
	
	private String marketCapitalization;
	
	private BigDecimal volume;

	public QuoteInfo(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getLastTradePriceOnly() {
		return lastTradePriceOnly;
	}

	public void setLastTradePriceOnly(BigDecimal lastTradePriceOnly) {
		this.lastTradePriceOnly = lastTradePriceOnly;
	}

	public BigDecimal getPreviousClose() {
		return previousClose;
	}

	public void setPreviousClose(BigDecimal previousClose) {
		this.previousClose = previousClose;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	

	public String getChangeinPercent() {
		return changeinPercent;
	}

	public void setChangeinPercent(String changeinPercent) {
		this.changeinPercent = changeinPercent;
	}

	public String getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(String marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}


}
