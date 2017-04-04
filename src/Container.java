import java.util.ArrayList;

import javafx.util.Pair;

/**
 * Maintained as a Binary Search Tree
 *
 */
public class Container extends BurstTrieComponent {

	private Record rootRecord;	
//	private HashMap<Record> frequencies;
	private int limit = 0;
	private char character;
	private AccessTrie parent;
	
	//Constructors
	public Container() {
		super();
		this.rootRecord = null;
//		this.frequencies = null;
		this.limit = 0;
		character = '\0';
		parent = null;
	}

	public Container(Record rootNode, ArrayList<Record> frequencies,
			int limit, char character, AccessTrie parent) {
		super();
		this.rootRecord = rootNode;
//		this.frequencies = frequencies;
		this.limit = limit;
		this.character = character;
		this.parent = parent;
	}
	
	public void insertRecord(Record record) {
		
		//BaseCase is container is empty
		if (rootRecord == null) {
			rootRecord = record;
			rootRecord.incrementFrequency(1);
		
			//Non-trivial case: use recursive algorithm
		} else {
			performInsert(rootRecord, record);
		}
		
//		if (isRecordInserted) {
//			//Now we want to update the frequencies
////			updateFrequencies(record);
//		}
		
		if (limit > BurstTrieUtil.CONTAINER_LIMIT) {
			if (burstContainer()) {
//				System.out.println("Successful Burst!");
			}
		}
	}
	
	private boolean burstContainer() {
		return burstContainer(this);
	}
	
	public boolean burstContainer(Container container) {
		
		if (container == null || container.character == '\0' || container.parent == null) {
			System.out.println("NOT ENOUGH INFO TO BURST CONTAINER");
			return false;
		}
		
		if (container.limit <= 1) {
			//Not enough records to burst
			System.out.println("Not enough records to burst");
		}
		
		// Create new Access Trie, assign its character to be that of the
		// containers and assign to character pointer in parent of container 
		AccessTrie newTrie = new AccessTrie(container.getCharacter(), null);
		container.getParent().setPointer(container.getCharacter(), newTrie);
		
		//For each record, insert starting at Access
		ArrayList<Record> records = container.CollectAllRecords();
		//remove container records
		container.clear();
		
		//Re-add the values to each pointer with the leading characters stripped
		addListOfRecordsIntoNewAccessTrie(records, newTrie);
		
		return true;
	}
	
	private void addListOfRecordsIntoNewAccessTrie(ArrayList<Record> records, AccessTrie newTrie) {
		
		for (Record rec : records) {
			String recString = rec.getString();
			String newValue;// = recString.substring(1);
			char leadingChar;// = recString.charAt(0);
			
			//Empty string, move it to emptyString pointer
			if (recString.isEmpty()) {
				newTrie.setEmptyStringPointer(new Record("", null, null, rec.getFrequency()));
				
			//Length 1, add empty string to new container
			} else if (recString.length() == 1) {
				newValue = "";
				leadingChar = recString.charAt(0);
				//-1 to accoutn for insertion that will add
				Record newRecord = new Record(newValue, null, null, rec.getFrequency()-1);
				
				if (newTrie.isCharPointerAvailable(leadingChar)) {
					newTrie.getContainerForPosition(leadingChar).insertRecord(newRecord);
				} else {
					Container newContainer = new Container(null, null, 0,
							leadingChar, newTrie);
					newContainer.insertRecord(newRecord);
					newTrie.setPointer(leadingChar, newContainer);
				}
				
			//otherwise
			} else {
				newValue = recString.substring(1);
				leadingChar = recString.charAt(0);
				//-1 that will account for insertion that will add
				Record newRecord = new Record(newValue, null, null, rec.getFrequency()-1);
				
				if (newTrie.isCharPointerAvailable(leadingChar)) {
					newTrie.getContainerForPosition(leadingChar).insertRecord(newRecord);
				} else {
					Container newContainer = new Container(null, null, 0,
							leadingChar, newTrie);
					newContainer.insertRecord(newRecord);
					newTrie.setPointer(leadingChar, newContainer);
				}
			}
		}
	}
	
	/**
	 * Clear all records from this container
	 */
	public void clear() {
		removeRecordAndChildRecords(rootRecord);
		limit = 0;
		
	}
	
	public void removeRecordAndChildRecords(Record record) {
		if (record.getLeftRecord() != null) {
			removeRecordAndChildRecords(record.getLeftRecord());
		}
		
		if (record.getRightRecord() != null) {
			removeRecordAndChildRecords(record.getRightRecord());
		}
		
		record = null;
	}
	
	private ArrayList<Record> CollectAllRecords() {
		return CollectRecords(rootRecord);
	}
	
	public ArrayList<Record> CollectRecords(Record startingRecord) {
		ArrayList<Record> listToReturn = new ArrayList<Record>();
		
		listToReturn = searchAndReturnList(listToReturn, startingRecord);
		
		return listToReturn;
	}
	
	private ArrayList<Record> searchAndReturnList(ArrayList<Record> list, Record record) {
		
		list.add(record);
		
		if (record.getLeftRecord() != null) {
			list = searchAndReturnList(list, record.getLeftRecord());
		}
		
		if (record.getRightRecord() != null) {
			list = searchAndReturnList(list, record.getRightRecord());
		}
		
		return list;
	}
	
	/**
	 * if word in record, then we can start from that record and return everything
	 */
	
	

	/**
	 * Update frequencies so we do not have to re traverse the tree, cost more
	 * time and space before hand, saves on searching.
	 * 
	 * Frequencies will only store up to the top 10
	 * 
	 * @param record
	 */
//	private void updateFrequencies(Record record) {
//		
//		//First check if there is room, then just add
//		if (frequencies)
//		
//		for (Record rec : frequencies) {
//			
//			if (rec.equals(record)) {
//				
//				break;//no need to search anymore
//			}
//			int oldValue = frequencies.get(record);
//			frequencies.put(record, oldValue+1);
//		}
//	}
	
	public boolean performInsert(Record rootRecord, Record recordToAdd) {
		if (addRecord(rootRecord, recordToAdd) != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * heavy lifting recursive method
	 * @param currentRecord
	 * @param recordToAdd
	 * @return
	 */
	private Record addRecord(Record currentRecord, Record recordToAdd) {
		Record recordToReturn = null;
		
		//record to add is smaller
		if (BurstTrieUtil.compareRecords(recordToAdd, currentRecord) == -1) {
			
			Record rec = currentRecord.getLeftRecord();
			if (rec != null) {
				recordToReturn = addRecord(rec, recordToAdd);
			} else {
				currentRecord.setLeftRecord(recordToAdd);
				recordToReturn = recordToAdd;
				limit += 1;
				recordToAdd.incrementFrequency(1);
				
			}
		//record to add is bigger
		} else if (BurstTrieUtil.compareRecords(recordToAdd, currentRecord) == 1) {
			
			Record rec = currentRecord.getRightRecord();
			if (rec != null) {
				recordToReturn = addRecord(rec, recordToAdd);
			} else {
				currentRecord.setRightRecord(recordToAdd);
				recordToReturn = recordToAdd;
				limit += 1;
				recordToAdd.incrementFrequency(1);
			}
		//record is the same
		} else {
			currentRecord.incrementFrequency(1);
			recordToReturn = currentRecord;
		}
		
		return recordToReturn;
	}

	public BurstTrieComponent searchForString(String string) {
		BurstTrieComponent recordToReturn = null;
//		System.out.println("CONTAINER--- searchForString method");
		if (rootRecord != null) {
//			System.out.println("CONTAINER--- searchAndComapre method");
			recordToReturn = searchAndCompareString(rootRecord, string);
		}

		return recordToReturn;
	}
	
	public Record searchAndCompareString(Record record, String string) {
		Record recordToReturn = null;
//		System.out.println("string is: " + string);
//		System.out.println("record.getString(): " + record.getString());
		int result = BurstTrieUtil.compareRecords(string, record.getString());
//		System.out.println("result is: " + result);
		// First check to see if string we are looking for is contained in the
		// record at the start
//		System.out.println("BurstTrieUtil.isStringPrefix(string, record.getString()): " + BurstTrieUtil.isStringPrefix(string, record.getString()));
//		System.out.println("string.isEmpty: " + string.isEmpty());
//		
		if (BurstTrieUtil.isStringPrefix(string, record.getString())) {
			recordToReturn = record; 
			
		} else {
			// string is less than record string
			if (result == -1) {
				if (record.getLeftRecord() != null) {
					recordToReturn = searchAndCompareString(
							record.getLeftRecord(), string);

					// Nothing found so we are out of luck
				} else {
					recordToReturn = null;
				}

				// string greater than record string
			} else if (result == 1) {

				if (record.getRightRecord() != null) {
					recordToReturn = searchAndCompareString(
							record.getRightRecord(), string);

					// Nothing found so we are out of luck
				} else {
					recordToReturn = null;
				}
			}
		}
		

		
		return recordToReturn;
	}
	
	

	public Record getRootNode() {
		return rootRecord;
	}

	public void setRootNode(Record rootNode) {
		this.rootRecord = rootNode;
	}

//	public HashMap<String, Integer> getFrequencies() {
//		return frequencies;
//	}
//
//	public void setFrequencies(HashMap<String, Integer> frequencies) {
//		this.frequencies = frequencies;
//	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public AccessTrie getParent() {
		return parent;
	}

	public void setParent(AccessTrie parent) {
		this.parent = parent;
	}

	/**
	 * For testing purposes
	 * @param args
	 */
	public static void main(String args[]) {
		
		//Some testing
		AccessTrie trie = new AccessTrie('t', null);
		Container container = new Container(null, null, 0, 't', trie);
		trie.setPointer('t', container);
		
		container.insertRecord(new Record("he", null, null, 0));
		container.insertRecord(new Record("he", null, null, 0));
		container.insertRecord(new Record("hroughout", null, null, 0));
		container.insertRecord(new Record("hread", null, null, 0));
		container.insertRecord(new Record("his", null, null, 0));
		container.insertRecord(new Record("hat", null, null, 0));
		container.insertRecord(new Record("here", null, null, 0));
		container.insertRecord(new Record("herefore", null, null, 0));
		container.insertRecord(new Record("herefore", null, null, 0));
		
		System.out.println(container.getRootNode());
		System.out.println(container.getRootNode().getLeftRecord());
		System.out.println(container.getRootNode().getRightRecord());
		System.out.println(container.getRootNode().getRightRecord().getLeftRecord());
		System.out.println(container.getRootNode().getRightRecord().getLeftRecord().getLeftRecord());
		System.out.println(container.getRootNode().getRightRecord().getLeftRecord().getLeftRecord().getLeftRecord());
		System.out.println(container.getRootNode().getRightRecord().getLeftRecord().getLeftRecord().getLeftRecord().getRightRecord());
		
		
		
	}
}
