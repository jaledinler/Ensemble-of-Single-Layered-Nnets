import java.util.List;

public class Instance{
	int numAttributes;
	List<String> features;
	
	public Instance (int numAttributes,List<String> features) {
		this.numAttributes = numAttributes;
		this.features = features;
		
	}
	
	public String getValue (int attribute) {
		return features.get(attribute);
	}
	
	public int numAttributes () {
		return numAttributes;
	}
	
	public List<String> getFeatures () {
		return features;
	}
}