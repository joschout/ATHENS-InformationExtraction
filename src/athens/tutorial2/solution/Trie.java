/**
 * Solution Trie implementation for logistic purposes.
 */
package athens.tutorial2.solution;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author galarrag
 *
 */
public class Trie extends athens.tutorial2.Trie {

	/** Holds the children */
	protected Map<Character, Trie> children;

	/** true if this is a word */
	protected boolean isWord = false;

	public Trie() {
		children= new HashMap<Character, Trie>();
	}

	public Trie(File f) throws IOException {
       super(f);
	}

	public Trie(Iterable<String> collection) throws IOException {
       super(collection);
	}

	@Override
	public boolean add(String s) {
	  boolean a=add(s, 0);
   	  return (a);
	}

	/** Adds a sequence starting from start position */
	protected boolean add(String s, int start) {
		if (s.length() == start) {
			if (isWord) return (false);
			isWord = true;
			return (true);
		}
		Character c = s.charAt(start);
		if(children==null) children= new HashMap<Character, Trie>();
		if (children.get(c) == null)	children.put(c, new Trie());
		return (children.get(c).add(s, start + 1));
	}

	@Override
	public boolean contains(String s) {
		return (contains(s, 0));
	}

	/** TRUE if the trie contains the sequence from start position on */
	protected boolean contains(String cs, int start) {
		if (cs.length() == start) return (isWord);
		Character c = cs.charAt(start);
		if (children.get(c) == null) return (false);
		return (children.get(c).contains(cs, start + 1));
	}

	@Override
	public String toString() {
	  return "Trie with " + children.size() + " children";
	}

	/**
	 * Returns the length of the longest contained subsequence, starting from
	 * start position.
	 */
	public int containedLength(String s, int startPos) {
		if(s.length()<= startPos) return (isWord?0:-1);
		Character c = s.charAt(startPos);
		if (children.get(c) == null) return (isWord?0:-1);
		int subtreelength = children.get(c).containedLength(s, startPos + 1);
		if (subtreelength == -1) return (isWord?0:-1);
		return (subtreelength + 1);
	}


	/** Adds all Strings in this trie to the list*/
	protected void allStrings(List<String> result, StringBuilder current) {
		if(isWord) result.add(current.toString());
		for(Character child : children.keySet()) {
			current.append(child);
			children.get(child).allStrings(result,current);
			current.setLength(current.length()-1);
		}
	}


	/** Use this to test your implementation*/
	public static void main(String[] args) throws IOException {
		Trie trie = new Trie();

		trie.add("Elvis Presley");
		trie.add("Elvis");
		trie.add("Juan Pihuave");

		for(String test : Arrays.asList("Brooklyn","Dudweiler","Elvis Presley","Juan Pihuave")) {
			System.out.println(test + " is in trie: " + trie.contains(test));
		}
	}
}
