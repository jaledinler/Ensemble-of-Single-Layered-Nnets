import java.util.ArrayList;
import java.util.List;

public class Attribute {
	//private int numValues;
	private List<String> values;
	private String name;
	
	public Attribute(String name, List<String> values) {
		//this.numValues = numValues;
		this.values = values;
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNumValues() {
		return values.size();
	}
	
	public String getValue(int i) {
		return values.get(i);
	}
	
	public int indexOfValue(String value) {
		int index = -1;
		for (int i = 0; i < values.size(); i++) {
			if (value.equals(values.get(i))) {
				index = i;
				break;
			}
		}
		
		if (index == -1)
			throw new IllegalArgumentException("index of val is -1");
		return index;
	}
}