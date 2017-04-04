import java.util.Comparator;

import javafx.util.Pair;

public class SuggestionSorter implements Comparator<Pair<String, Integer>> {

	@Override
	public int compare(Pair<String, Integer> arg0, Pair<String, Integer> arg1) {
		if ( arg0 == null) {
			//both null
			if (arg1 == null) {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (arg1 == null) {
				return -1;
			}
		}
		if (arg0.getValue() > arg1.getValue()) {
			return -1;
		} else if (arg0.getValue() == arg1.getValue()){
			return 0;
		} else {
			return 1;
		}
	}


}
