package Util;

import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class CreateAgent {

	public static AgentController createFuzzyAgent(String name, int num, ContainerController container, String fcl) throws StaleProxyException {
		//send the fcl file for this agent
		String[] args = new String[2];
		args[0]=fcl;
		args[1]=name;
		
		AgentController managerAgentController;
		managerAgentController = container.createNewAgent(name+num, "agents.FuzzyAgent", args);
		managerAgentController.start();
		return managerAgentController;
		
	}
	
	public static AgentController createUserAgent(String userAgentName, String managerName , ContainerController myContainer) throws StaleProxyException {
		//send the managerAgent Name to send the msgs
		String[] args = new String[1];
		args[0]=managerName;
		
		//create the userAgent
		AgentController userAgentController = myContainer.createNewAgent(userAgentName, "agents.UserAgent", args);
		userAgentController.start();
		return userAgentController;
	}
	
	public static AgentController createManagerAgent(String managerAgentName, ContainerController myContainer) throws StaleProxyException {
		//send the container reference to create the fuzzyAgents
		Object[] args = new Object[1];
		args[0]=myContainer;
		
		//create the managerAgent
		AgentController managerAgentController = myContainer.createNewAgent(managerAgentName, "agents.ManagerAgent", args);
		managerAgentController.start();
		return managerAgentController;
	}
}
