package DTOs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement( name = "SimulationSettings" )
public class SimulationSettings {

	@XmlElement 
	protected String title;
	@XmlElement 
	protected String application;
	@XmlElement 
	protected int fuzzyagents;
	@XmlElement 
	protected String fuzzySettings;
	@XmlElement 
	protected String aggregation;
	
	public SimulationSettings(String title, String application, int fuzzyagents, String fuzzySettings,
			String aggregation) {
		super();
		this.title = title;
		this.application = application;
		this.fuzzyagents = fuzzyagents;
		this.fuzzySettings = fuzzySettings;
		this.aggregation = aggregation;
	}

	
	public SimulationSettings() {}

 
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	 
	public String getApplication() {
		return application;
	}
	
	public void setApplication(String application) {
		this.application = application;
	}
	
	public int getFuzzyagents() {
		return fuzzyagents;
	}
	
	public void setFuzzyagents(int fuzzyagents) {
		this.fuzzyagents = fuzzyagents;
	}
	
	public String getFuzzySettings() {
		return fuzzySettings;
	}
	
	public void setFuzzySettings(String fuzzySettings) {
		this.fuzzySettings = fuzzySettings;
	}
	
	public String getAggregation() {
		return aggregation;
	}
	
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}


	@Override
	public String toString() {
		return "SimulationSettings [title=" + title + ", application=" + application + ", fuzzyagents=" + fuzzyagents
				+ ", fuzzySettings=" + fuzzySettings + ", aggregation=" + aggregation + "]";
	}
	
	
}
