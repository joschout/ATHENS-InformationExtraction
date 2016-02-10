package athens.tutorial3;

import java.io.File;
import java.io.IOException;

import athens.Page;
import athens.Parser;
import athens.Triple;

/**
 * 
 * @author Fabian M. Suchanek
 * 
 * Abstract super class for all Wikipedia extractors
 */

public abstract class Extractor {

	/** Returns a fact (triple) found in a page.
	 *  @param A wikipedia page.
	 *  @return A triple <subject, predicate, object> or null if no information
	 *  could be extracted.
	 **/
	public abstract Triple extract(Page page);
	
	public void run(File wikipedia) throws IOException {
		try(Parser pages = new Parser(wikipedia)) {
			while(pages.hasNext()) {
				Triple triple = extract(pages.next());
				if (triple != null) {
					System.out.println(triple);
				}
			}
		}
	}
}
