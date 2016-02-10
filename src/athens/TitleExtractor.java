package athens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TitleExtractor {

	public static void main(String[] args) {
		String filename = args[0];
		
		try {
		    File inputFile = new File(filename);
		    if (! inputFile.exists() || inputFile.isDirectory()) {
		    	throw new IOException("There is no file for the given filename");
			}
		    Parser parser = new Parser(inputFile);
		    
		    File outputFile = new File("titles.txt");
		    if (outputFile.exists() || outputFile.isDirectory()) {
		    	parser.close();
				throw new IOException("There already exists a file for the given output filename");
			}
		    BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile));
	    
		    while (parser.hasNext()) {
				Page currentPage = parser.next();
				String lineSeparator = System.getProperty("line.separator");
				String currentPagetitle = currentPage.title + lineSeparator;
				outputWriter.append(currentPagetitle);
			}
		    outputWriter.close();		    		
		    parser.close();
		    
		} catch (IOException e) {
		    e.printStackTrace();
		} 	
	}
}
