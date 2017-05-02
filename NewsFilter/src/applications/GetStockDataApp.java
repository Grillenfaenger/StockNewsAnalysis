package applications;

import io.FileUtils;
import io.YahooQuoteCrawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import data.Frequency;

public class GetStockDataApp {
	
	public static void main(String[] args) throws IOException {
		
		List<String> rics = FileUtils.fileToList("output/rics.txt");
		Set<String> ricSet =  new HashSet<String>();
		ricSet.addAll(rics);
		
		Calendar start = Calendar.getInstance();
		start.set(2005, 3, 28);
		Calendar end = Calendar.getInstance();
		end.set(2005, 5, 3);
		
		getCourseFromYahoo(rics, start, end, Frequency.DAILY);
		
	}
	
private static void getCourseFromYahoo(List<String> rics, Calendar start, Calendar end, Frequency frq) throws IOException{
		
		// List of rics for which NO DATA could be found
		List<String> nodata = new ArrayList<String>();
		// List of rics for which data had been downloaded
		List<String> data = new ArrayList<String>();
		
		System.out.println(start.getMaximum(Calendar.MONTH));
		System.out.println(start.get(Calendar.DAY_OF_MONTH));
		System.out.println(start.get(Calendar.YEAR));
		
		System.out.println(end.get(Calendar.MONTH));
		System.out.println(end.get(Calendar.DAY_OF_MONTH));
		System.out.println(end.get(Calendar.YEAR));
		
		// Stocks traded on NYSE are listed without .N suffix on yahoo finaces
		for (String ric : rics) {
			if(ric.endsWith(".N")){
				ric = ric.substring(0, ric.length()-2);
			}
			System.out.println(ric);
			if(YahooQuoteCrawler.getQuoteTable(ric, start, end, frq)){
				data.add(ric);
			} else {
				nodata.add(ric);
			}
		}
		
		System.out.println("Downloaded quotes for "+  data.size() + " companies");
		FileUtils.printList(data, "output/", "data", ".txt");
		System.out.println("Unable to find any data for " + nodata.size() + " Rics.");
		FileUtils.printList(nodata, "output/", "nodata", ".txt");
		
	}

}
