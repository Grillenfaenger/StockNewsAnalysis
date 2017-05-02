package data.quotes;

import java.util.HashMap;
import java.util.Map;

public class CompanyStockTables {
	
	/**
	 * A map of StockTables by Company-RIC keys.
	 */
	public Map<String,StockTable> companyStocks; 
	
	public CompanyStockTables(){
		companyStocks = new HashMap<String,StockTable>();
	}

}
