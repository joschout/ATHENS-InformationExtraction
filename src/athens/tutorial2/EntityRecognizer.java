package athens.tutorial2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import athens.Page;
import athens.Parser;

/**
 * This class implements a entity recognizer based on dictionary of 
 * entities.
 */
class EntityRecognizer {
	

	/**
	 * Given as arguments (1) a Wikipedia file and (2) a file with a list of entities, 
	 * this program reports mentions of entities in the content of articles. 
	 * An output record consists of:
	 * <ul>
	 * <li>The entity mentioned</li>
	 * <li>TAB (\t)
	 * <li>The title of the article where the mention occurs.</li>
	 * <li>NEWLINE (\n)
	 * </ul>
	 * The method should print one record per line.
	 */
	public static void main(String args[]) throws IOException {
		String filenameOfWikiFile = args[0];
		String filenameOfEntityFile = args[1];
		
		File wikiFile = new File(filenameOfWikiFile);
	    if (! wikiFile.exists() || wikiFile.isDirectory()) {
	    	throw new IOException("There is no file for the given filenameOfWikiFile");
		}
	    
	    Parser parser = new Parser(wikiFile);
	    
	    File entityFile = new File(filenameOfEntityFile);
	    if (! entityFile.exists() || entityFile.isDirectory()) {
	    	throw new IOException("There is no file for the given filenameOfEntityFile");
		}
	    Trie trie = new Trie(entityFile);
	    
	    File outputFile = new File("recognizedEntities.txt");
	    if (outputFile.exists() || outputFile.isDirectory()) {
	    	parser.close();
			//throw new IOException("There already exists a file for the given output filename");
		}
	    BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
	    
	    while (parser.hasNext()) {
			Page currentPage = parser.next();
			String tempContent = currentPage.content;
			for(int i = 0; i < tempContent.length(); i++){
				int length = trie.containedLength(tempContent, i);
				if (length == -1) {
					
				}else {
					String entityMentioned = tempContent.substring(i, i+length);
					String articleTitle = currentPage.title;
					outputWriter.append(entityMentioned + "\t" + articleTitle + "\n");
				}
			}
	
		}
	    outputWriter.close();		    		
	    parser.close();
 
	}

}