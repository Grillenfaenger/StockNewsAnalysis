package applications;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.YahooQuoteCrawler;
import data.Article;
import data.Frequency;
import data.quotes.StockValueExtended;
import io.DbConnector;
import io.FileUtils;
import io.QuoteCSVReader;
import io.XLSReader;
import processing.RicProcessing;
import processing.RicProcessing.Keyword;

public class Application {
	
	public static Map<String,String> ricMap;
	static Pattern pattern = Pattern.compile("(.*?)<(.*?)>");
	
	public static void main(String[] args) throws Exception {
		
//		loadRicMap();
		
		
		
	
		Map<String, List<Article>> orderedNews = readNews();
		
//		System.out.println(filteredNews.get("RBS.L").get(0).printComplete());
		
		List<String> rics = FileUtils.fileToList("output/rics.txt");
		Set<String> ricSet =  new HashSet<String>();
		ricSet.addAll(rics);
		
		List<String> deRics = new ArrayList<String>();
		
		for(String ric : rics){
			if(ric.endsWith(".DE")) deRics.add(ric);
		}
		System.out.println(deRics.size() + " an der Börse Frankfurt gehandelte Unternehmen.");
		
		Calendar start = Calendar.getInstance();
		start.set(2005, 4, 1);
		Calendar end = Calendar.getInstance();
		end.set(2005, 4, 31);
		
		getCourseFromYahoo(rics, start, end, Frequency.DAILY);
		
		
//		noDataStats(filteredNews);
	
		
		
		
		Map<String, Set<String>> companyNames = RicProcessing.extractCompanyNamesByList(orderedNews, rics, "companyNamesAll");
		
		List<String> indices = RicProcessing.getIndices(rics, companyNames);
		
//		List<String> nodataRics = FileUtils.fileToList("output/nodata.txt");
//		RicProcessing.extractCompanyNamesByList(filteredNews, nodataRics, "noDataRics");
//		
//		List<String> dataRics = FileUtils.fileToList("output/data.txt");
//		RicProcessing.extractCompanyNamesByList(filteredNews, dataRics, "dataRics");
		
		String inputfilepath = "input/News_filtered_DE_1.1.xls";
		List<Article> articles = XLSReader.getArticlesFromXlsFile(inputfilepath);
		
		List<Article> filteredNews = RicProcessing.filterIndicesFromNews(articles, indices);
		System.out.println("overall Articles: " + filteredNews.size());
		List<Article> singleTopicArticles = RicProcessing.getSingleTopicArticles(filteredNews);
		System.out.println("Number of singeTopicArticles: " + singleTopicArticles.size() );
		ricSet.removeAll(indices);
		Map<String, List<Article>> orderArticlesBy = RicProcessing.orderArticlesBy(singleTopicArticles, ricSet, processing.RicProcessing.Keyword.RIC);
	}


	


	private static void noDataStats(Map<String, List<Article>> filteredNews) throws IOException {
		List<String> noDataRics = FileUtils.fileToList("output/nodata.txt");
		
		List<String> noDataArticles = new ArrayList<String>();
		
		for(String ric : noDataRics){
			if(filteredNews.containsKey(ric)){
				List<Article> list = filteredNews.get(ric);
			System.out.println("========\n"+ric+"\n========");
			noDataArticles.add("========\n"+ric+"\n========");
				
					System.out.println(list.get(0));
					noDataArticles.add(list.get(0).printComplete());
			
			}else {
				noDataArticles.add("no Article");
			}
		}
		FileUtils.printList(noDataArticles, "output/", "noDataArticles2", ".txt");
	}


	private static void loadRicMap() throws IOException {
		
		Map<String, String> map = new HashMap<String,String>();
		List<String> riclist = FileUtils.fileToList("input/news_export_EN_RICLIST_CI.txt");
		for(String ric : riclist){
			String[] split = ric.split("\t");
			if(split.length == 2){
				map.put(split[0], split[1]);
			} else {
				System.out.println("Error at" + split[0]);
			}
		}
		ricMap = map;
		
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
		
//		for(String ric : data){
//			nameEnterprise(ric);
//		}
		
	}


	private static void nameEnterprise(String ric) {
		
		if(ricMap.containsKey(ric)){
			System.out.println(ric + ": " + ricMap.get(ric));
		} else {
			System.out.println("Keine Auflösung von " + ric);
		}
		
		
	}


	public static void readCourse() throws NumberFormatException, IOException, ParseException {
		
		//Aktienverlauf einlesen
		String filePath = "input/AIG_daily2005.csv";
		String ric = "AIG.N";
		
		List<StockValueExtended> readStockCourseCSV = QuoteCSVReader.readStockCourseCSV(ric, filePath);
		
		for (StockValueExtended stockValueExtended : readStockCourseCSV) {
			System.out.println(stockValueExtended);
		}
		

	}
	
	public static Map<String, List<Article>> readNews() throws Exception {

		String inputfilepath = "input/News_filtered_DE_1.1.xls";
		String dbFilePath = "output/news_filtered.db";
		
	
		
		List<Article> articles = XLSReader.getArticlesFromXlsFile(inputfilepath);
		Set<String> tags = RicProcessing.createTagSet(articles);
		System.out.println("tags: " + tags.size());
		Set<String> rics = RicProcessing.createRicSet(articles);
		System.out.println("rics: " + rics.size());
		Map<String, List<Article>> filteredNews = RicProcessing.orderArticlesBy(articles, rics, Keyword.RIC);
		
		System.out.println("Extrahiert: " + filteredNews.size());
		
//		printCoverageCSV(filteredNews, "coverage");
		
		
		List<Article> singleTopicArticles = new ArrayList<Article>();
		// Artikel über genau ein Unternehmen
		for(Article article : articles){
			if(article.getRics().size() == 1){
				singleTopicArticles.add(article);
			}
		}
		System.out.println("Articles with only one Ric: " + singleTopicArticles.size());
		Set<String> singleRics = RicProcessing.createRicSet(singleTopicArticles);
		Map<String, List<Article>> singleTopicFilteredNews = RicProcessing.orderArticlesBy(singleTopicArticles, singleRics, Keyword.RIC);
		
		
//		printCoverageCSV(singleTopicFilteredNews, "singleCoverage");
		
		
		
		// Beispiel: Artikel über RWE
		List<Article> list = filteredNews.get("RWEG.DE");
		List<Article> singleTopic = new ArrayList<Article>();
		System.out.println("RWEG.DE in Document: " + list.size());
		for (Article article : list) {
			System.out.println(article);
			if(article.getRics().size() ==1){
				singleTopic.add(article);
			}
		}
		System.out.println("only RWE: " + singleTopic.size());
		for (Article article : singleTopic) {
			System.out.println(article.printComplete());
		}
		
		// RICS vs Tags
//		XLSReader.compareTags(tags, rics);
		
		return filteredNews;
		
	}


	private static void printCoverageCSV(Map<String, List<Article>> filteredNews, String fileName) throws IOException {
		List<String> singleCoverageCSV = new ArrayList<String>();
		String maxRic = null;
		int max = 0;
		for(String key : filteredNews.keySet()){
			int size = filteredNews.get(key).size();
			if(size > max){
				max = size;
				maxRic = key;
			}
			System.out.println(key + ": " + size);
			singleCoverageCSV.add(key+","+size);
		}
		System.out.println("max Articles: " + maxRic + ": " + max);
		FileUtils.printList(singleCoverageCSV, "output//", fileName, ".csv");
		
	}
		

}
