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
		
		List<String> nodata = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		
		int month = start.get(Calendar.MONTH);
		
		System.out.println(month);
		System.out.println(start.get(Calendar.DAY_OF_MONTH));
		System.out.println(start.get(Calendar.YEAR));
		
		System.out.println(end.get(Calendar.MONTH));
		System.out.println(end.get(Calendar.DAY_OF_MONTH));
		System.out.println(end.get(Calendar.YEAR));
		
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
		
		System.out.println(data.size());
		FileUtils.printList(data, "output/", "data", ".txt");
		System.out.println(nodata.size());
		FileUtils.printList(nodata, "output/", "nodata", ".txt");
		
	}

}
