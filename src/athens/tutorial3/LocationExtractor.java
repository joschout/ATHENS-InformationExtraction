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

public class LocationExtractor extends Extractor {

	String predicate = "locatedIn";
	
	@Override
	public Triple extract(Page page) {
		
		String firstSentence = page.firstSentence();
		String article = "(a|an|the)?";		
		String isA = "((is " + article +")|(was " + article + "))";

		String word = "(\\w+)";
		String place = "(district|city|country|county|(federal state))";
		String placed = " ((place|located)? in)";
	
		
	    String regexPattern = page.title + "( )?((is located in)|(is a (county|town|province|district|region|city|country) ((located in)|in)))( )?(the (north|south|east|west|(\\w+ region)) of)?( )?(the)?( )?(\\w+)";
		
		Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(firstSentence);
		String foundLocation = "";
		
		Triple triple = null;
		
		while(matcher.find()){
		  // matcher.group(N) holds the N-th group of the match
		  // matcher.group() holds the entire match
			foundLocation = matcher.group(15);
			
			
			triple = new Triple(page.title, this.predicate, foundLocation);
			break;
		}
		return triple;
	}

	@Override
	public void run(File wikipedia) throws IOException {
		
		File outputFile = new File("location_extractor_evaluation.txt");
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
		  new LocationExtractor().run(new File(args[0])); 
	}
}
