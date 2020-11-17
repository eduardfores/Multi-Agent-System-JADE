package Util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import DTOs.SimulationSettings;

public class ReadXML {
	
	public static SimulationSettings readXML(String pathFile) {
		try {  
			   
	        File file = new File(pathFile);  
	        JAXBContext jaxbContext = JAXBContext.newInstance(SimulationSettings.class);  
	   
	        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
	        SimulationSettings settings= (SimulationSettings) jaxbUnmarshaller.unmarshal(file);  
	          
	        return settings;
	   
	      } catch (JAXBException e) {  
	        System.out.println(e.getMessage()); 
	      }  
		return null;
	}

}
