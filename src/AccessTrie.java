
public class AccessTrie extends BurstTrieComponent {

	//just the letters of the english alphabet for now
	BurstTrieComponent[] pointers = new BurstTrieComponent[26];
	Record emptyStringPointer = null;
	//the character for this access trie
	char character;
	
	//Constructors
	public AccessTrie() {
		super();
		character = '\0';
	}
	
	public AccessTrie(char character, Record emptyStringRecord) {
		super();
		this.character = character;
		this.emptyStringPointer = emptyStringRecord;
	}
	
	public void setPointer(char c, BurstTrieComponent btc) {
		pointers[BurstTrieUtil.charToNum(c)] = btc;
	}
	
	public BurstTrieComponent getComponentForPosition(char c) {
		return pointers[BurstTrieUtil.charToNum(c)];
		
	}
	
	/**
	 * When you know it will be a container
	 * @param c
	 * @return
	 */
	public Container getContainerForPosition(char c) {
		return (Container)pointers[BurstTrieUtil.charToNum(c)];
	}
	
	/**
	 * When you know it will be a trie
	 * @param c
	 * @return
	 */
	public AccessTrie getTrieForPosition(char c) {
		return (AccessTrie)pointers[BurstTrieUtil.charToNum(c)];
	}
	
	public boolean isCharPointerAvailable(char c) {
		return pointers[BurstTrieUtil.charToNum(c)] != null;
	}
	

	public BurstTrieComponent[] getPointers() {
		return pointers;
	}

	public void setPointers(BurstTrieComponent[] pointers) {
		this.pointers = pointers;
	}

	public Record getEmptyStringPointer() {
		return emptyStringPointer;
	}

	public void setEmptyStringPointer(Record emptyStringPointer) {
		this.emptyStringPointer = emptyStringPointer;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}
	
	
	
}
