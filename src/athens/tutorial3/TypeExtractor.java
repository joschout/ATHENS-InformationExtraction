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

public class TypeExtractor extends Extractor {

	String predicate = "type";
	
	@Override
	public Triple extract(Page page) {
		
		String firstSentence = page.firstSentence();
		String article = "(a|an|the)";		
		String isA = "((is " + article +")|(was " + article + "))";

		String word = "(\\w+)";
		
		String memberOf = " ((member|sort|type|way) of)?";
		
		String ordinalAdjective = "(first|second|third|fourth|sixth|seventh|eighth|nineth|tenth)";
		String adjective = ordinalAdjective;
		
		//String endWord = "(\\w+(\\s+\\w+)*)";
		
	    String regexPattern = page.title + "(\\s+\\w+)*"  + " " + isA  + memberOf + " "+ word;
		
		Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(firstSentence);
		String foundType = "";
		
		Triple triple = null;
		
		while(matcher.find()){
		  // matcher.group(N) holds the N-th group of the match
		  // matcher.group() holds the entire match
			foundType = matcher.group(9);
			
			
			triple = new Triple(page.title, this.predicate, foundType);
			break;
		}
		return triple;
	}

	
	@Override
	public void run(File wikipedia) throws IOException {
		
		File outputFile = new File("type_extractor_evaluation.txt");
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
		  new TypeExtractor().run(new File(args[0])); 
	}
	
	
}
