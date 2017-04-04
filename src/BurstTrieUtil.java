import java.util.Comparator;


public final class BurstTrieUtil implements Comparator<Record> {
	
	public static int CONTAINER_LIMIT = 50;

	public static String getBareBonesWord(String word) {
		
		StringBuffer buffer = new StringBuffer();
		
		for (char c : word.toCharArray()) {
			if (c == '\n' || c== '\0') {
				
			} else {
				buffer.append(c);
			}
		}
		
		return buffer.toString();
	}
	
	public static String getPrefix(String entireWord, String suffix) {
		
		return entireWord.substring(0, entireWord.indexOf(suffix));
	}
	
	/*
	 * convert char to pointer
	 */
	public static int charToNum(char c) {
		
		int intToReturn = 0;
		switch (c) {
			case 'a': intToReturn = 0; break;
			case 'b': intToReturn = 1; break;
			case 'c': intToReturn = 2; break;
			case 'd': intToReturn = 3; break;
			case 'e': intToReturn = 4; break;
			case 'f': intToReturn = 5; break;
			case 'g': intToReturn = 6; break;
			case 'h': intToReturn = 7; break;
			case 'i': intToReturn = 8; break;
			case 'j': intToReturn = 9; break;
			case 'k': intToReturn = 10; break;
			case 'l': intToReturn = 11; break;
			case 'm': intToReturn = 12; break;
			case 'n': intToReturn = 13; break;
			case 'o': intToReturn = 14; break;
			case 'p': intToReturn = 15; break;
			case 'q': intToReturn = 16; break;
			case 'r': intToReturn = 17; break;
			case 's': intToReturn = 18; break;
			case 't': intToReturn = 19; break;
			case 'u': intToReturn = 20; break;
			case 'v': intToReturn = 21; break;
			case 'w': intToReturn = 22; break;
			case 'x': intToReturn = 23; break;
			case 'y': intToReturn = 24; break;
			case 'z': intToReturn = 25; break;
		}
		
		return intToReturn;
	}
	
	public static char numToChar(int i) {
		
		char charToReturn = '\0';
		switch (i) {
			case 0: charToReturn = 'a'; break;
			case 1: charToReturn = 'b'; break;
			case 2: charToReturn = 'c'; break;
			case 3: charToReturn = 'd'; break;
			case 4: charToReturn = 'e'; break;
			case 5: charToReturn = 'f'; break;
			case 6: charToReturn = 'g'; break;
			case 7: charToReturn = 'h'; break;
			case 8: charToReturn = 'i'; break;
			case 9: charToReturn = 'j'; break;
			case 10: charToReturn = 'k'; break;
			case 11: charToReturn = 'l'; break;
			case 12: charToReturn = 'm'; break;
			case 13: charToReturn = 'n'; break;
			case 14: charToReturn = 'o'; break;
			case 15: charToReturn = 'p'; break;
			case 16: charToReturn = 'q'; break;
			case 17: charToReturn = 'r'; break;
			case 18: charToReturn = 's'; break;
			case 19: charToReturn = 't'; break;
			case 20: charToReturn = 'u'; break;
			case 21: charToReturn = 'v'; break;
			case 22: charToReturn = 'w'; break;
			case 23: charToReturn = 'x'; break;
			case 24: charToReturn = 'y'; break;
			case 25: charToReturn = 'z'; break;
		}
		
		return charToReturn;
	}

	/**
	 * If equal: 		0
	 * If arg0 < arg1:	-1
	 *If arg0 > arg1:	1
	 */
	@Override
	public int compare(Record arg0, Record arg1) {
		
		String str0 = arg0.getString();
		String str1 = arg1.getString();
		
		//null checks
		if (str0.isEmpty()) {
			if (str1.isEmpty()) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (str1.isEmpty()) {
				return 1;
			}
		}
		
		//If both are non-empty then compare them
		for (int i = 0; i < str0.length(); i++) {
			
			//both strings are the same up to this point, but arg0 is longer
			if (i > str1.length()) {
				return 1;
			}
			
			//finally compare characters
			if (str0.charAt(i) < str1.charAt(i)) {
				return -1;
			} else if (str0.charAt(i) > str1.charAt(i)){
				return 1;
			}
		}
		
		//if we are here, both strings are currently the same
		if (str0.length() == str1.length()) {
			return 0;
		//otherwise str1 is longer
		} else {
			return -1;
		}
	}

	public static boolean isStringPrefix(String partialString, String fullString) {
		// null checks
		if (partialString.isEmpty()) {
			return true;
		} else {
			if (fullString.isEmpty()) {
				return false;
			}
		}

		if (fullString.length() < partialString.length()) {
			return false;
		}

		for (int i = 0; i < partialString.length(); i++) {
			if (partialString.charAt(i) != fullString.charAt(i)) {
				return false;
			}
		}

		return true;
		
	}
	
	public static int compareRecords(String str0, String str1) {
		// null checks
		if (str0.isEmpty()) {
			if (str1.isEmpty()) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (str1.isEmpty()) {
				return 1;
			}
		}

		// If both are non-empty then compare them
		for (int i = 0; i < str0.length(); i++) {

			// both strings are the same up to this point, but arg0 is longer
			if (i > str1.length()-1) {
				return 1;
			}

			// finally compare characters
			if (str0.charAt(i) < str1.charAt(i)) {
				return -1;
			} else if (str0.charAt(i) > str1.charAt(i)) {
				return 1;
			}
		}

		// if we are here, both strings are currently the same
		if (str0.length() == str1.length()) {
			return 0;
			// otherwise str1 is longer
		} else {
			return -1;
		}
	}
	
	/**
	 * If equal: 		0
	 * If arg0 < arg1:	-1
	 *If arg0 > arg1:	1
	 */
	public static int compareRecords(Record arg0, Record arg1) {
		
		String str0 = arg0.getString();
		String str1 = arg1.getString();
		
		//null checks
		if (str0.isEmpty()) {
			if (str1.isEmpty()) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (str1.isEmpty()) {
				return 1;
			}
		}
		
		//If both are non-empty then compare them
		for (int i = 0; i < str0.length(); i++) {
			
			//both strings are the same up to this point, but arg0 is longer
			if (i > str1.length()-1) {
				return 1;
			}
//			System.out.println("i: " + i);
//			System.out.println("str0: " + str0);
//			System.out.println("str1: " + str1);
			//finally compare characters
			if (str0.charAt(i) < str1.charAt(i)) {
				return -1;
			} else if (str0.charAt(i) > str1.charAt(i)){
				return 1;
			}
		}
		
		//if we are here, both strings are currently the same
		if (str0.length() == str1.length()) {
			return 0;
		//otherwise str1 is longer
		} else {
			return -1;
		}
	}
}
