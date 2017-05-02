package processing;

import java.time.LocalDateTime;
import java.util.Date;

import exceptions.NoQuoteDataException;

public abstract class StockEvaluatur {
	
	public abstract boolean getEvaluation(String ric, LocalDateTime articleDate) throws NoQuoteDataException;

}
