import java.util.*;
public class DataSheet {
     private String id;
     private String currency;
     
    
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getCurrency() {
		return currency;
	}


	public void setCurrency(String currency) {
		this.currency = currency;
	}


	public DataSheet(String randomUUID, String i) {
		this.id = randomUUID;
		this.currency=i;
	}
	
}
