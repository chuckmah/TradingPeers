package services.financeinfo;

import java.util.Date;

public class FinanceInfo {

	private String symbol;
	
	private String companyName;
	
	public FinanceInfo(String symbol) {
		this.symbol = symbol;
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

}
