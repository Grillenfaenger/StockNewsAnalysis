package processing;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class StockEvaluatur {
	
	public abstract boolean getEvaluation(String ric, LocalDateTime articleDate) throws Exception;

}
