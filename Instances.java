import java.util.ArrayList;
import java.util.List;

public class Instances {
	List<Attribute> attributes;
	List<Instance>instances;
	
	public Instances () {
		this.attributes = new ArrayList<Attribute>();
		this.instances = new ArrayList<Instance>();
	}
	public Instances (List<Attribute> attributes, List<Instance>instances) {
		this.attributes = attributes;
		this.instances = instances;
	}
	
	public int numInstances() {
		return instances.size();
	}
	
	public int numAttributes() {
		return attributes.size();
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	
	public List<Instance> getInstances() {
		return instances;
	}
	
	public Instance getInstance(int i) {
		return instances.get(i);
	}
	
	public Attribute getAttribute(int i) {
		return attributes.get(i);
	}
}