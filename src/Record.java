
public class Record extends BurstTrieComponent {

	private String string;
	private Record leftRecord;
	private Record rightRecord;
	private int frequency;
	
	//Constructors
	public Record() {
		string = "";
		leftRecord = null;
		rightRecord = null;
		frequency = 0;
	}
	
	public Record(String string, Record leftRecord, Record rightRecord, int frequency) {
		super();
		this.string = string;
		this.leftRecord = leftRecord;
		this.rightRecord = rightRecord;
		this.frequency = frequency;
	}
	
	public void incrementFrequency(int increment) {
		frequency += increment;
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("***\nString: " + string + "\n");
		
		if (leftRecord != null) {
			buffer.append("left node: " + leftRecord.getString() + "\n");
		} else {
			buffer.append("left node is null\n");
		}
		
		if (rightRecord != null) {
			buffer.append("right node: " + rightRecord.getString() + "\n");
		} else {
			buffer.append("right node is null\n");
		}
		
		buffer.append("Frequency: " + frequency);
		
		buffer.append("\n***\n");
		
		return buffer.toString();
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public Record getLeftRecord() {
		return leftRecord;
	}

	public void setLeftRecord(Record leftNode) {
		this.leftRecord = leftNode;
	}

	public Record getRightRecord() {
		return rightRecord;
	}

	public void setRightRecord(Record rightNode) {
		this.rightRecord = rightNode;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	
	
}
