package applications;

import io.FileUtils;
import io.QuoteCSVReader;
import io.XLSReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import processing.RicProcessing;
import processing.RicProcessing.Keyword;
import processing.StockEvaluatur;
import processing.VerySimpleStockEvaluator;
import data.Article;
import data.quotes.CompanyStockTables;
import data.TrainingData;

public class TrainingDataGeneratorApplication {

	public static void main(String[] args) throws Exception {
	
		// getArticles
		String inputfilepath = "input/News_filtered_DE_1.1.xls";
		List<String> indices = FileUtils.fileToList("output/indices.txt");
		List<Article> articles = XLSReader.getArticlesFromXlsFile(inputfilepath);
		articles = RicProcessing.filterIndicesFromNews(articles, indices);
		Set<String> ricSet = RicProcessing.createRicSet(articles);
		
		System.out.println("Number of different Rics in Articles: " + ricSet.size());
		
		// filterArticles: just one RIC and .DE
//		List<Article> singleTopicArticles = new ArrayList<Article>();
//		for(Article article : articles){
//			if(article.getRics().size() == 1){
//				singleTopicArticles.add(article);
//			}
//		}
//		System.out.println("Articles with only one Ric: " + singleTopicArticles.size());
//		Set<String> singleRics = RicProcessing.createRicSet(singleTopicArticles);
		Map<String, List<Article>> orderedNews = RicProcessing.orderArticlesBy(articles, ricSet, Keyword.RIC);
		
		
		System.out.println("Number of different Rics in orderedNews: " + orderedNews.size());
		
		Map<String, List<Article>> germanNews = new HashMap<String, List<Article>>();
		
		List<String> dataRics = FileUtils.fileToList("output/data.txt");
		
		System.out.println(dataRics.size() + "\n" + dataRics);
		
//		for(String ric : orderedNews.keySet()){
//			if( ric.endsWith(".DE")){
//				germanNews.put(ric, orderedNews.get(ric));
//			}
//		}
//		germanNews.clear();
		for(String ric : orderedNews.keySet()){
			if(dataRics.contains(ric)){
				germanNews.put(ric, orderedNews.get(ric));
			}
		}
		System.out.println("Artikel zu " +  germanNews.size() + " verschiedenen Unternehmen");
		
		List<String> remainingRics = new ArrayList<String>();
		remainingRics.addAll(germanNews.keySet());
		
		
		//	loadQuotes
		String quoteDir = "output/quotes";
		CompanyStockTables cst = QuoteCSVReader.readStockCoursesIntoMap(quoteDir, remainingRics);
		
		// initialize StockEvaluator
		StockEvaluatur stEval = new VerySimpleStockEvaluator(cst);
		
		// generate TrainingData
		List<TrainingData> trainingData = new ArrayList<TrainingData>();
		for(String ric : germanNews.keySet()){
			for(Article art : germanNews.get(ric)){
				TrainingData td = new TrainingData(ric,art,stEval);
				System.out.println(td);
				trainingData.add(td);
			}
		}
		
		System.out.println("Number of TrainingData Sets:"+ trainingData.size());
		

	}

}
