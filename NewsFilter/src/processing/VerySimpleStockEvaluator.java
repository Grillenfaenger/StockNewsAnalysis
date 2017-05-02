package processing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TreeMap;

import Exceptions.NoQuoteDataException;
import data.quotes.CompanyStockTables;
import data.quotes.StockTable;
import data.quotes.StockValueCore;

public class VerySimpleStockEvaluator extends StockEvaluatur {

	private CompanyStockTables cst;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE");
	
	public VerySimpleStockEvaluator(CompanyStockTables cst){
		this.cst = cst;
	}
	
	@Override
	public boolean getEvaluation(String ric, LocalDateTime articleDate) {
		
		LocalDate articleDay = articleDate.toLocalDate();
		StockTable ricQuotes = cst.companyStocks.get(ric);
		
		float courseBefore = getcourseBefore(articleDay, ricQuotes);
		float courseAfter = getCourseAfter(articleDay, ricQuotes);
		
		if(courseAfter > courseBefore){
			System.out.println("evaluation: true, "+ courseAfter + " > " + courseBefore);
		} else {
			System.out.println("evaluation: false, " + courseAfter + " <= " + courseBefore);
		}
		if(courseAfter-courseBefore<=0.0) return false;
		else return true;
	}

	private float getcourseBefore(LocalDate articleDay, StockTable ricQuotes) {
		LocalDate justBefore = ricQuotes.stockTable.lowerKey(articleDay);
		if(justBefore.equals(null)){
			throw new NoQuoteDataException(articleDay.toString());
		}
		return ricQuotes.stockTable.get(justBefore).getClose();
	}

	private float getCourseAfter(LocalDate articleDay, StockTable ricQuotes) {
		LocalDate justAfter = ricQuotes.stockTable.higherKey(articleDay);
		if(justAfter.equals(null)){
			throw new NoQuoteDataException(articleDay.toString());
		}
		return ricQuotes.stockTable.get(justAfter).getOpen();
	}
	
//	@SuppressWarnings("deprecation")
//	private boolean whileOpen(LocalDateTime news) {
//		if(!isBeforeOpening(news) && !isAfterClosing(news)) return true;
//		else return false;
//	}
//
//	private boolean isAfterClosing(LocalDateTime news) {
//		
//		int closingHour = 17;
//		int closingMinutes = 30;
//		
//		if(news.get(ChronoField.HOUR_OF_DAY) > closingHour) {return true;}
//		else if(news.get(ChronoField.HOUR_OF_DAY) == closingHour && news.get(ChronoField.MINUTE_OF_HOUR) > closingMinutes ){ return true;}
//		else return false;
//	}
//
//	private boolean isBeforeOpening(LocalDateTime news) {
//		int openingHour = 9;
//		
//		if(news.get(ChronoField.HOUR_OF_DAY) < openingHour) return true;
//		else return false;
//	}


	
	
	

}
