package data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import processing.StockEvaluatur;

public class TrainingData {
	
	private UUID id;
	private String content;
	private boolean evaluation;
	
	private LocalDateTime date;
	private String ric;
	
	public TrainingData(String ric, Article article, StockEvaluatur eval) throws Exception{
		this.id = UUID.randomUUID();
		article.setId(id);
		this.content = article.getContent();
		this.evaluation = eval.getEvaluation(ric, article.getDate());
		
		this.date = article.date;
		this.ric = ric;
	}

	@Override
	public String toString() {
		return "TrainingData [id=" + id + ", content=..."
				+ ", evaluation=" + evaluation + ", date=" + date + ", rics="
				+ ric + "]";
	}

	
	
	
	
	

}
