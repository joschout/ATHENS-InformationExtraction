package athens.tutorial4.Dipre;

import athens.Page;
import athens.Parser;
import athens.Triple;
import athens.tutorial2.solution.Trie;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dipre {

	/**
	 * Finds patterns in Wikipedia from seed facts.
	 * For example:
	 *   Wikipedia="blah blah Elvis is married to Priscilla blah blah"
	 *   seeds={<Elvis,hasSpouse,Priscilla>}
	 *   Result={" is married to "}
	 * Hint: Use a regular expression to find patterns,
	 * and restrict patterns to 10-25 letters and spaces.
	 * Include the boundary spaces in the pattern.
	 */
	public static Trie findPatterns(File wikipedia, Set<Triple> seeds)
			throws IOException {
		Trie result = new Trie();
		try (Parser pages = new Parser(wikipedia)) {
			while (pages.hasNext()) {
				String pageContent = pages.next().content;
				for (Triple triple : seeds) {
					
					String subject = triple.subject;
					String object = triple.object;
					String regexPattern = subject + "( [a-z ]{10,25} )" + object;
					
					Pattern pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(pageContent);
					String foundPattern = "";
					
					
					while(matcher.find()){
					  // matcher.group(N) holds the N-th group of the match
					  // matcher.group() holds the entire match
						foundPattern = matcher.group(1);
						result.add(foundPattern);
						//System.out.println(foundPattern +"\n");
					}			
				}	
			}
		}
		return (result);
	}

	/**
	 * Given Wikipedia, a set of patterns, a relation name,
	 * and a set of entities, finds facts.
	 *
	 * For example:
	 *   (File) Wikipedia = "blah blah Barack is married to Michelle blah blah"
	 *   (Trie) patterns = {"is married to", "is in love with"}
	 *   (String) relation = "hasSpouse"
	 *   (Tie) entities = {Michelle, Barack, Elvis, Priscilla}
	 *   Result = {<Barack, hasSpouse, Michelle>}
	 *
	 * Hint: First find the subject, then the pattern, and then the object
	 */
	public static Set<Triple> findFacts(File wikipedia, Trie patterns,
			String relation, Trie entities) throws IOException {
		Set<Triple> result = new HashSet<>();
		try (Parser pages = new Parser(wikipedia)) {

			while (pages.hasNext()) {
				String pageContent = pages.next().content;

				for(int indexInPage = 0; indexInPage < pageContent.length(); indexInPage++){

					//find the length of the longest matching subject
					int lengthOfContainedSubject = entities.containedLength(pageContent, indexInPage);
					if (lengthOfContainedSubject > 0){//we found a matching subject
						String subjectFound = pageContent.substring(indexInPage, indexInPage+lengthOfContainedSubject);

						//find the length of the longest matching pattern
						int j = indexInPage+lengthOfContainedSubject;
						int lengthOfContainedPattern = patterns.containedLength(pageContent, j);
						if (lengthOfContainedPattern > 0){//we have found a matching pattern
							String patternFound = pageContent.substring(j, j+lengthOfContainedPattern);
							
							//find the length of the longest matching object
							int k = j + lengthOfContainedPattern;
							int lengthOfContainedObject = entities.containedLength(pageContent, k);
							if (lengthOfContainedObject > 0) {//we have found a matching object
								String objectFound = pageContent.substring(k, k +lengthOfContainedObject);
								
								Triple triple = new Triple(subjectFound, relation, objectFound);
								result.add(triple);
								//System.out.println(subjectFound + " " + patternFound + " " + objectFound);
								//System.out.println(triple.toString());
							}		
						}
					}
				}
			}
		}
		return (result);
	}

	/**
	 * Given
	 * - a wikipedia file
	 * - a file with entity names
	 * - a seed subject
	 * - a seed relation
	 * - a seed object
	 * - a number of iterations
	 * in the args[], runs DIPRE
	 *
	 * Hints:
	 * - first find patterns for the facts
	 * - then find facts for the patterns
	 * - iterate this
	 * - Keep one single set of facts, and one single Trie of patterns.
	 *   It is OK if this makes the algorithm find again the same patterns,
	 *   and find again the same facts.
	 */
	public static void main(String args[]) throws IOException {
		// === parsing of input === //
		File wikiFile = new  File(args[0]);
		File entityFile = new File(args[1]);
		String seedSubject = args[2];
		String seedRelation = args[3];
		String seedObject = args[4];
		int nbOfIterations = Integer.parseInt(args[5]);
		
		
		// === seed fact in the  set of facts=== //
		Set<Triple> seeds = new HashSet<Triple>();
		Triple testTriple = new Triple(seedSubject, seedRelation, seedObject);
		seeds.add(testTriple);
		
		// === trie of entities === //
		Trie entityTrie = new Trie(entityFile);
		
		// === the trie of patterns === //
		Trie patternsTrie = new Trie();
		
		Set<Triple> resultingFacts = new HashSet<>();
		
		for(int i = 0; i < nbOfIterations; i++){
			//find patterns for facts
			patternsTrie = findPatterns(wikiFile, seeds);
			
			//find facts for the patterns
			seeds = findFacts(wikiFile, patternsTrie, seedRelation, entityTrie);
			resultingFacts.addAll(seeds);
		}
		
		
		File outputFile = new File("facts2.txt");
		try(BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile))) {
			for (Triple fact: seeds) {
				outputWriter.append(String.valueOf(fact) + "\n");
				//System.out.println(fact);
			}	
		}
		
		

		


	}
}
