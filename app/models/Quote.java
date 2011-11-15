package models;

import java.math.BigDecimal;
import java.util.List;

import siena.*;

@Table("quotes")
public class Quote extends Model {

	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
   
	@Column("symbol")
    @Max(10) @NotNull
	public String symbol;
	
	@Column("companyName")
    @Max(255) @NotNull
	public String companyName;
	
	@Column("marketPrice")
	public BigDecimal marketPrice;
	
	public Quote(){
	}
	
	public Quote(String symbol){
		this.symbol = symbol;
	}
	
    public String toString()  {
        return "Symbol(" + symbol + ")";
    }
	
	public static Quote findBySymbol(String symbol) {
		return Quote.all(Quote.class).filter("symbol", symbol).get();
	}

	public static List<Quote> findAll() {
		
		return Quote.all(Quote.class).fetch();
	}

}
