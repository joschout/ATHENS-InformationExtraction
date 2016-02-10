package athens.tutorial2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * It defines the methods that define a Trie data structure.
 * @author Luis Galárraga.
 *
 */
public class Trie {
	
	boolean containsWordUpUntilHere = false;
	
	//maps a character to a subtree
	public Map<String, Trie> subtries = new HashMap<String, Trie>();

	/**
	 * Adds a string to the trie.
	 * @param word
	 * @return true if the trie changed as a result of this operation, that is if
	 * the new string was not in the dictionary.
	 */
	public boolean add(String str) {
		int strLength = str.length();
		//the given string is the empty string
		if (strLength == 0) {
			containsWordUpUntilHere = true;
			return true;
		}
			
		/*
		 * The trie does not yet contain the word.
		 * There are two possibilities:
		 * 	1. The tree partially contains the word.
		 * 		We will have to go further down the tree until
		 * 		we get to the part we have to add.
		 * 	2. Trie does not contain a prefix of the given word
		 * Actually, they ar both the same case.
		 */
		
		/*
		 * check if we already have a subtree 
		 *starting with the initial character of the given word
		 */
		String headOfStr = str.substring(0, 1);
		
		Trie trieForFirstChar = subtries.get(headOfStr);
		if (trieForFirstChar==null) {//already has a subtrie
			trieForFirstChar= new Trie();
			subtries.put(headOfStr, trieForFirstChar);
		}
		trieForFirstChar.add(str.substring(1));
//			
//			
//			//get the subtrie
//			Trie trie = subtries.get(headOfStr);
//			
//			if (strLength == 1) {//the string contains 1 char
//				trie.containsWordUpUntilHere = true;
//			}else {//the string contains more than 1 char
//				String tailOfStr = str.substring(1, str.length());
//				trie.add(tailOfStr);
//			}				
//		}else {//does not yet have a subtrie
//			Trie newSubTrie = new Trie();
//			subtries.put(headOfStr, newSubTrie);
//
//			if(strLength == 1){
//				newSubTrie.containsWordUpUntilHere = true;
//			}else {
//				String tailOfStr = str.substring(1, str.length());
//				newSubTrie.add(tailOfStr);
//			}
//		}
		return true;
	}

	/**
	 * Checks whether a string exists in the trie.
	 * @param str
	 * @return true if the string is in the trie, false otherwise.
	 */
	public boolean contains(String str) {
		int strLength = str.length();
		if(strLength == 0){
			return this.containsWordUpUntilHere;
		}
		
		//is there a subtrie for the first char of the string
		String headOfStr = str.substring(0, 1);
		
		Trie trieForFirstChar = subtries.get(headOfStr);
			
		if (trieForFirstChar==null) {//there is no subtrie for the first char
			return false;
		}

		return trieForFirstChar.contains(str.substring(1));
	}

	/**
	 * Given a string and a starting position (<var>startPos</var>), it returns the length
	 * of the longest word in the trie that starts in the string at the given position.
	 * For example,
	 * if the trie contains words "New York", and "New York City", containedLength("I live in New York City!", 10)
	 * returns 13, that is the length of the longest word ("New York City") registered in the 
     * trie that starts at position 10.
	 * Hint: proceed as in the lecture
	 * @param s
	 * @param startPos
	 * @return int
	 */
	public int containedLength(String s, int startPos) {
		// Take the first char from the starting position
		if (s.length() == startPos) {
			return -1;
		}
		String firstChar = s.substring(startPos, startPos+1);
		
		Trie subtrie=subtries.get(firstChar);
		if(subtrie==null){
			if(containsWordUpUntilHere) return(0);
			return(-1);
		}
		int length=subtrie.containedLength(s, startPos+1);
		if(length==-1) {
			if(containsWordUpUntilHere) return(0);
			return(-1);
		}
		return(length+1);
	}
//	
//		//check if there is a subtree for the char
//		if(this.subtries.containsKey(firstChar)){//there is a subtree for the char
//			//get the subtrie for the char
//			Trie subTrie = subtries.get(firstChar);
//			
//			/*
//			 * Get the length of the longest word in the subtrie,
//			 * starting one character further in the trie.
//			 */
//			int length = subTrie.containedLength(s, startPos+1);
//			
//			/*
//			 * three possibilities:
//			 * 	1) there is a word-ending in the subtrie
//			 *    --> the length = the length in the subtree + 1 (the EDGE going to THIS node)
//			 *  2) there is no word-ending in the subtrie
//			 *  	a) there is a word-ending in this node
//			 *  		--) return 1
//			 *  	b) there is no word-ending in this node
//			 *  		--> return -1 (the amount of edges the caller has is FALSE)
//			 */
//			if (length == -1) {//there is no word-ending in the subtree
//				if (containsWordUpUntilHere) {//if there is a word ending here
//					return 1;
//				}else {//if there is no word ending here
//					return -1;
//				}
//			}else{// there is a word-ending in the subtree
//				return length + 1;
//			}
//		}else{//there is no subtree for the char
//			return -1;
//		}
//	}
//
	/** Constructs a Trie from the lines of a file*/
	public Trie(File file) throws IOException {
		try(BufferedReader in=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"))) {
			String line;
			while((line=in.readLine())!=null) {
				add(line);
			}
		}
	}

	/** Constructs an empty Trie*/
	public Trie() {
	}

	/** Constructs a Trie from a collection*/
	public Trie(Iterable<String> collection) {
		for(String s : collection) add(s);
	}

	/** Use this to test your implementation. Provide the file with list of Wikipedia titles as argument to this program.*/
	public static void main(String[] args) throws IOException {
		Trie trie = new Trie(new File(args[0]));

		for(String test : Arrays.asList("Dudweiler","Elvis Presley","Juan Pihuave", "Brooklyn")) {
			System.out.println(test + " is in trie: " + trie.contains(test));
		}
		System.out.println(trie.containedLength("I live in Brooklyn!", 10));
		

	}
}
