package applications;

import io.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticsApp {

	public static void main(String[] args) throws IOException {
		deDataStats();

	}
	
	private static void deDataStats() throws IOException {
		List<String> nodata = FileUtils.fileToList("output/nodata.txt");
		List<String> data = FileUtils.fileToList("output/data.txt");
		
		List<String> deNoData = new ArrayList<String>();
		List<String> deData = new ArrayList<String>();
		
		for(String ric : nodata){
			if(ric.endsWith(".DE")){
				deNoData.add(ric);
			}
		}
		
		for(String ric : data){
			if(ric.endsWith(".DE")){
				deData.add(ric);
			}
		}
		
		System.out.println(".DE with Data: " + deData.size() + "\n" + deData);
		System.out.println(".DE with NoData: " + deNoData.size() + "\n" + deNoData);
	}

}
