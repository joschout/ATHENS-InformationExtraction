package athens.tutorial5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import athens.Page;
import athens.Parser;
import athens.Triple;
import athens.tutorial3.Extractor;

public class ImprovedTypeExtractor extends Extractor {

	@Override
	public Triple extract(Page page) {
		String firstSentence = page.firstSentence();
		
		String determinerL = "/DT";
		String nounL = "/NN";
		String adjectiveL  = "(/JJ|/JJS)";
		String verbL = "(/VBD|/VBZ)";
		String adverbL = "/RB";
		String prepositionL = "/IN";
		
		String wordPar = "(\\w+)";
		String word = "\\w+";
		String verb = wordPar + verbL;
		String determiner = wordPar + determinerL;
		String adjective = wordPar + adjectiveL;
		String noun = word + nounL;	
		String multipleAdjs = "(" +adjective +"(\\s+)?" + ")*";
		 
		String labeledTitle=(page.title+" ").replaceAll(" ", "/[a-z]+ ");
		
		String regexPattern = labeledTitle  + verb + "(\\s+)" + determiner + "(\\s+)" +multipleAdjs + "(" + noun+ ")";
		
		Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(firstSentence);
		String foundType = "";
		
		
		Triple triple = null;
		
		while(matcher.find()){
		  // matcher.group(N) holds the N-th group of the match
		  // matcher.group() holds the entire match
			
			int nbOfGroups = matcher.groupCount();
			
		
			foundType = matcher.group(nbOfGroups);
			
			foundType = foundType.replaceAll("/[a-zA-Z]+","");
			
			triple = new Triple(page.title, "type", foundType);
			break;
		}
		return triple;
		
	}
	
	@Override
	public void run(File wikipedia) throws IOException {
		
		File outputFile = new File("type_extractor_pos_improved_evaluation.txt");
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
		  new ImprovedTypeExtractor().run(new File(args[0])); 
	}
}
