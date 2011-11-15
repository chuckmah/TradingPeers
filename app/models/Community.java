package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import siena.*;


@Table("communities")
public class Community extends Model{

	@Id(Generator.AUTO_INCREMENT)
    public Long id;
	
	@Column("name")
    @Max(255) @NotNull
	public String name;
		
    
    public String toString()  {
        return "Community(" + name + ")";
    }
    
    public static Community findByName(String name){
    	return Community.all(Community.class).filter("name", name).get();
    }
    

        
}
