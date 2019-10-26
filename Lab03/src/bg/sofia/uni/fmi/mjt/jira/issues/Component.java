package bg.sofia.uni.fmi.mjt.jira.issues;

public class Component {
	private String name;
	private String shortName;
	
	public Component(String name, String shortName) {
		this.name = name;
		this.shortName = shortName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		Component other = (Component) obj;
		
		return name.equals(other.name) && shortName.equals(other.shortName);
	}
}
