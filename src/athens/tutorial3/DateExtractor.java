package athens.tutorial3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import athens.Page;
import athens.Parser;
import athens.Triple;

public class DateExtractor extends Extractor {

	
	
	String predicate = "hasDate";
	
	@Override
	public Triple extract(Page page) {
		
		String year = "(\\d\\d\\d\\d)";
		String months = "(January|February|March|April|May|June|July|August|September|October|November|December)";
		String day = "((\\d\\d)|\\d)";
		
	    String month_dayComma_year = months + " "+day + ", " + year;
	    String date = month_dayComma_year;
	    
		Pattern pattern = Pattern.compile(date);
		Matcher matcher = pattern.matcher(page.content);
		String foundDate = "";
		
		Triple triple = null;
		
		while(matcher.find()){
		  // matcher.group(N) holds the N-th group of the match
		  // matcher.group() holds the entire match
			foundDate = matcher.group();
			triple = new Triple(page.title, this.predicate, foundDate);
			break;
		}
	

		return triple;
	}
	
	@Override
	public void run(File wikipedia) throws IOException {
		
		File outputFile = new File("date_extractor_evaluation.txt");
		try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
			try(Parser pages = new Parser(wikipedia)) {
				int i = 0;
				while(pages.hasNext() && i < 20) {
					Triple triple = extract(pages.next());
					if (triple != null) {
						outputWriter.append(String.valueOf(triple) + "\n");
						i++;
					}
				}
			}
		}
	}
	
	public static void main(String args[]) throws IOException { 
		  new DateExtractor().run(new File(args[0])); 
	}

}
