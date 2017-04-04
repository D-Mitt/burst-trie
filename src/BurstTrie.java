import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javafx.util.Pair;

/**
 * The Brust Trie Main Class
 * @author Dan
 *
 */
public class BurstTrie {

	private AccessTrie rootAccessTrie;
	private String inputFile;
	
	//Constructors
	public BurstTrie() {
		rootAccessTrie = null;
	}
	
	public BurstTrie(AccessTrie trie, String input) {
		this.rootAccessTrie = trie;
		this.inputFile = input;
	}
	
	public BurstTrie(String input) {
		this.rootAccessTrie = null;
		this.inputFile = input;
	}
	
	/**
	 * Assumes text file has one word per line
	 * @param input alternate path if different from one used in constructor
	 * @return boolean to determine if populating tree was successful
	 */
	public boolean populateTreeFromTextFile(String input) {
		
		boolean isComplete = false;
		
		String path = input;
		if (input.isEmpty()) {
			path = inputFile;
		}
		
		BufferedReader br = null;
		String word;
		
		try {			
			br = new BufferedReader(new FileReader(path));

			while ((word = br.readLine()) != null) {
				//insert word
				insertWord(BurstTrieUtil.getBareBonesWord(word));
			}
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {
			try {
				br.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		return isComplete;
	}
	
	public void insertWord(String word) {
//		Pair<BurstTrieComponent, Boolean> comp = getComponent;
		
		//Base Case:
		if (rootAccessTrie == null) {
			rootAccessTrie = new AccessTrie('\0', null);
		}
		
		insertString(rootAccessTrie, word);	
	}
	
	private void insertString(BurstTrieComponent btc, String word) {
		
		if (btc instanceof Container) {
			((Container) btc).insertRecord(new Record(word, null, null, 0));
			
		} else if (btc instanceof AccessTrie) {
			
			String newValue;
			char leadingChar;
			
			if (word.isEmpty()) {
				
				if (((AccessTrie) btc).getEmptyStringPointer() == null) {
					((AccessTrie) btc).setEmptyStringPointer(new Record("", null, null, 1));
				} else {
					((AccessTrie) btc).getEmptyStringPointer().incrementFrequency(1);
				}
				
			} else {
				
				leadingChar = word.charAt(0);
				if (word.length() == 1) {
					newValue = "";
				} else {
					newValue = word.substring(1);
				}
				
				//if the next component is null, make a new container to add the string
				BurstTrieComponent nextBtc = ((AccessTrie) btc).getComponentForPosition(leadingChar);
				if (nextBtc == null) {
					Container newContainer = new Container(null, null, 0,
							leadingChar, (AccessTrie) btc);
					
					((AccessTrie) btc).setPointer(leadingChar, newContainer);
					insertString(newContainer, newValue);
				} else {
					if (nextBtc instanceof Container) {
						((Container) nextBtc).insertRecord(new Record(newValue,
								null, null, 0));
					} else if (nextBtc instanceof AccessTrie) {
						insertString(nextBtc, newValue);
					}
				}
			} 
		}
	}
	/**
	 * 					// TODO: non frequecies
	 * @param input
	 * @param numOfSuggestions
	 * @param returnHighest
	 * @return
	 */
	public ArrayList<Pair<String, Integer>> getSuggestions(String input, int numOfSuggestions,
			boolean returnHighest) {
//System.out.println("gettign suggestions: ");
		ArrayList<Pair<String, Integer>> list = new ArrayList<Pair<String, Integer>>();
		Pair<String, BurstTrieComponent> btc = searchForString(input);
//		System.out.println("made it this far");

		// if it is a record, that means we were in a container
		BurstTrieComponent comp = btc.getValue();
		if (comp == null) {
//			System.out.println("Comp is null");
		}
		String prefix = btc.getKey();
		
		//Get characters from input should match everything from node down
		
		if (comp != null) {
			
			if (returnHighest) {
				
				if (comp instanceof Record) {
//System.out.println("comp is record");
					// Now perform search from this node forward and add the
					// rest of the frequencies
					// make sure to not add current record unless it is in
					// not a word in container container
//System.out.println("input: " + input);
//System.out.println("((Record) comp).getString()): " + ((Record) comp).getString());
//System.out.println("prefix: " + prefix);
					if (input.equals(prefix + ((Record) comp).getString())) {
						list = addRightChildrenToList((Record) comp, list,
								prefix, ((Record) comp).getString());
					} else {
						list = addRecordAndChildrenToList((Record) comp, list,
								prefix, ((Record) comp).getString());
					}
					
				// if access trie
				} else if (comp instanceof AccessTrie) {
					list = addTrieChildrenToList((AccessTrie) comp, list,
							prefix);
				}

				// sort the list from highest to lowest frequency
				Collections.sort(list, new SuggestionSorter());
				// take the top numOfSuggestions and put to new list
				ArrayList<Pair<String, Integer>> highestFreqList = new ArrayList<Pair<String, Integer>>();

				for (int i = 0; i < Math.min(numOfSuggestions, list.size()); i++) {
					// Then add the prefix to each one
					highestFreqList.add(list.get(i));
				}
				// debug
				
//				System.out.println(highestFreqList);
				return highestFreqList;
			} else {
				
			}
		} else {
//			System.out.println("No suggestions found");
		}
//System.out.println(list);
		return list;
	}
	
	private ArrayList<Pair<String, Integer>> addTrieChildrenToList(
			AccessTrie trie, ArrayList<Pair<String, Integer>> list,
			String prefix) {

		BurstTrieComponent[] btcArray = trie.getPointers();
		for (int i = 0; i < btcArray.length; i++) {

			BurstTrieComponent btc = btcArray[i];

			if (btc instanceof Container) {
				list = addRecordAndChildrenToList(
						((Container) btc).getRootNode(), list, prefix
								+ BurstTrieUtil.numToChar(i), "");
				
			} else if (btc instanceof AccessTrie) {
				list = addTrieAndChildrenToList((AccessTrie) btc, list, prefix
						+ BurstTrieUtil.numToChar(i));
			}
		}
		
		return list;
	}
	
	private ArrayList<Pair<String, Integer>> addTrieAndChildrenToList(
			AccessTrie trie, ArrayList<Pair<String, Integer>> list,
			String prefix) {
		
		if (trie.getEmptyStringPointer() != null) {
			list.add(new Pair<String, Integer>(prefix, trie
					.getEmptyStringPointer().getFrequency()));
		}

		BurstTrieComponent[] btcArray = trie.getPointers();
		for (int i = 0; i < btcArray.length; i++) {

			BurstTrieComponent btc = btcArray[i];

			if (btc instanceof Container) {
				list = addRecordAndChildrenToList(
						((Container) btc).getRootNode(), list, prefix
								+ BurstTrieUtil.numToChar(i), "");
				
			} else if (btc instanceof AccessTrie) {
				list = addTrieAndChildrenToList((AccessTrie) btc, list, prefix
						+ BurstTrieUtil.numToChar(i));
			}
		}
		
		return list;
	}
	
	private ArrayList<Pair<String, Integer>> addChildrenToList(
			Record record, ArrayList<Pair<String, Integer>> list, String prefix, String subString) {
		if (record.getLeftRecord() != null) {
			addRecordAndChildrenToList(record.getLeftRecord(), list, prefix, subString);
		}
		
		//Only right side because it matched completely
		if (record.getRightRecord() != null) {
			addRecordAndChildrenToList(record.getRightRecord(), list, prefix, subString);
		}
		
		return list;
	}
	
	private ArrayList<Pair<String, Integer>> addRightChildrenToList(
			Record record, ArrayList<Pair<String, Integer>> list, String prefix, String subString) {
		
		//Only right side because it matched completely
		if (record.getRightRecord() != null) {
			addRecordAndChildrenToList(record.getRightRecord(), list, prefix, subString);
		}
		
		return list;
	}
	
	private ArrayList<Pair<String, Integer>> addRecordAndChildrenToList(
			Record record, ArrayList<Pair<String, Integer>> list, String prefix, String subString) {

		if (BurstTrieUtil.isStringPrefix(subString, record.getString())) {
			list.add(new Pair<String, Integer>(prefix + record.getString(),
					record.getFrequency()));
		}
		
		if (record.getLeftRecord() != null) {
			addRecordAndChildrenToList(record.getLeftRecord(), list, prefix, subString);
		}
		
		if (record.getRightRecord() != null) {
			addRecordAndChildrenToList(record.getRightRecord(), list, prefix, subString);
		}
		
		return list;
		
	}
	
	
	public Pair<String, BurstTrieComponent> searchForString(String string) {
		Pair<String, BurstTrieComponent> btcToReturn = null;
//		System.out.println("begin search for string");
		if (rootAccessTrie != null) {
//			System.out.println("root not null");
			btcToReturn = searchForString(rootAccessTrie, string, "");
		}
		
		return btcToReturn;
	}
	
	public Pair<String, BurstTrieComponent> searchForString(
			BurstTrieComponent btc, String string, String prefix) {
		Pair<String, BurstTrieComponent> btcToReturn = null;
//		System.out.println("string is: " + string);
//		System.out.println("prefix is: " + prefix);
		// if container
		if (btc instanceof Container) {
//			System.out.println("btc is container:");
			btcToReturn = new Pair<String, BurstTrieComponent>(prefix,
					((Container) btc).searchForString(string));
	
		// if access trie
		} else if (btc instanceof AccessTrie) {
//System.out.println("btc is an accesstrie");
			String newValue;
			char leadingChar;

			if (string.isEmpty()) {
//				System.out.println("string is empty");
				btcToReturn = new Pair<String, BurstTrieComponent>(prefix,
						(AccessTrie) btc);

			} else {

				leadingChar = string.charAt(0);
				if (string.length() == 1) {
					newValue = "";
				} else {
					newValue = string.substring(1);
				}

				BurstTrieComponent nextBtc = ((AccessTrie) btc)
						.getComponentForPosition(leadingChar);

				if (nextBtc == null) {
//					System.out.println("nextbtc is null");
					btcToReturn = new Pair<String, BurstTrieComponent>(prefix,
							(AccessTrie) btc);
				} else {
//System.out.println("nextbtc is not null");
					if (nextBtc instanceof Container) {
//						System.out.println("nextbtc is container");
//						System.out.println("new value: " + newValue);
						btcToReturn = new Pair<String, BurstTrieComponent>(
								prefix + leadingChar,
								((Container) nextBtc).searchForString(newValue));

					} else if (nextBtc instanceof AccessTrie) {
//						System.out.println("nextbtc is accesstrie");
						btcToReturn = searchForString(nextBtc, newValue, prefix
								+ leadingChar);
					}
				}
			}
		}

		return btcToReturn;
	}
	
	/**
	 * Just calls search for string then checks if it it is a word
	 * @param word
	 * @return
	 */
	public Record searchForWord(String word) {
		Record recordToReturn = null;

		return recordToReturn;
	}
	
	
	public AccessTrie getRootAccessTrie() {
		return rootAccessTrie;
	}

	public void setRootAccessTrie(AccessTrie rootAccessTrie) {
		this.rootAccessTrie = rootAccessTrie;
	}
	
public String speedTest(String input) {
		
		BufferedReader br = null;
		String word;
		long startTime = 0L;
		long endTime = 0L;
		double overall = 0;
		for (int i = 0; i < 10; i++) {
		try {			
			br = new BufferedReader(new FileReader(input));
			startTime = System.currentTimeMillis();
			while ((word = br.readLine()) != null) {
				//insert word
//				System.out.println(word);
				getSuggestions(word, 5, true);
			}
			endTime = System.currentTimeMillis();
			br.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {
			try {
				br.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		long timeDiff = endTime - startTime;
		
		double sec = (double)timeDiff / (double)1000;
		overall += sec;
		}
		return "average time in seconds: " + overall/10;
		
//		return "";
		
	}

	public static void main(String args[]) {
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("STARTING at: " + new Date().toString());
		BurstTrie bt = new BurstTrie(null, "Resources/word_list_large.txt");
		bt.populateTreeFromTextFile("");
		System.out.println("ENDING at: " + new Date().toString());
		
		System.out.println("10 words: " + bt.speedTest("Resources/word_list_small_10.txt"));
		System.out.println("100 words: " + bt.speedTest("Resources/word_list_small_100.txt"));
		System.out.println("1000 words: " + bt.speedTest("Resources/word_list_small_1000.txt"));
		System.out.println("10000 words: " + bt.speedTest("Resources/word_list_small_10000.txt"));
		System.out.println("100000 words: " + bt.speedTest("Resources/word_list_small_100000.txt"));
		
//		System.out.println("total time taken: " + (System.currentTimeMillis()-startTime) + " milli-seconds");
//		
//		System.out.println("Speed Test: " + new Date().toString());
//		long speedTestStartTime = System.currentTimeMillis();
//		for (int j = 0; j < 100; j++) {
//			bt.getSuggestions("the", 10, true);
//		}
//		
//		System.out.println("ENDING Speed Test at: " + new Date().toString());
//		System.out.println("total time taken: " + (System.currentTimeMillis()-speedTestStartTime) + " milli-seconds");
	}
}
