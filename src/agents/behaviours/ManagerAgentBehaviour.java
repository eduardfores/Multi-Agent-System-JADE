package agents.behaviours;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DTOs.MessageCodes;
import DTOs.SimulationSettings;
import Util.CreateAgent;
import Util.ReadXML;
import Util.SendMessages;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ManagerAgentBehaviour extends CyclicBehaviour {
	
	private enum State {
		INITIALIZE, DECISION
	}
	
	private static final long serialVersionUID = 1L;
	public static final String FILES_PATH = "files\\";
	public static final String FCLS_PATH = "fcl\\";
	public static final String FCL_EXTENSION =".fcl";
	
    private State state;
    private ContainerController container;
    private MessageTemplate performativeFilter = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    private List<AgentController> agentController;
    private SimulationSettings settings;
    
	public ManagerAgentBehaviour(Agent agent, ContainerController container) {
		super(agent);
		this.container=container;
		agentController=new ArrayList<AgentController>();
	}

	public void action() {
		//recive the msg
		ACLMessage msg = myAgent.receive(performativeFilter);
	   	if(msg!=null) {
	   		String[] infoConfig = msg.getContent().split("_");
			
	   		//select the action
			if(infoConfig[0].equals("I")) {
				this.state=State.INITIALIZE;
			}else if(infoConfig[0].equals("D")){
				this.state=State.DECISION;
			}
			
			//execute action
			switch (this.state) {
		        case INITIALIZE:
		        	initializeSystem(infoConfig[1], msg);
		            break;
		        case DECISION:
		        	decideFunction(infoConfig[1]+"_"+infoConfig[2], msg);
		            break;
		        default:
		            block();
		    }
	   	} else {
	   		block();
	   	}
	}

	public void initializeSystem(String configurationFile, ACLMessage msg) {
		//read file xml
   		this.settings = ReadXML.readXML(FILES_PATH+configurationFile);
   		
   		//define the answer to userAgent
   		ACLMessage reply = msg.createReply();
   		reply.setPerformative(ACLMessage.INFORM);
   		//create fuzzy agents
   		String[] fcls = this.settings.getFuzzySettings().split(",");
   		
   		for(int i=0; i<this.settings.getFuzzyagents(); i++) {
   			
   			try {
   				agentController.add(CreateAgent.createFuzzyAgent(this.settings.getApplication(), i,this.container, FCLS_PATH+fcls[i]+FCL_EXTENSION));
			} catch (StaleProxyException e) {
				System.out.println(e.getMessage());
				reply.setContent(MessageCodes.FUZZY_AGENTS_ERROR.toString());
				myAgent.send(reply);
			}
   		}
   		
   		//reply solution
		reply.setContent(MessageCodes.FUZZY_AGENTS_CREATED.toString());
		myAgent.send(reply);
	}

	public void decideFunction(String infoFile, ACLMessage msg) {
		BufferedReader reader= null;
		BufferedReader counter= null;
		try {
			counter = new BufferedReader(new FileReader(FILES_PATH+infoFile));
			//count the lines in the file
			int lines = (int)counter.lines().count();
			//we add the new behaviour in the managerAgent to listen the responses of the FuzzyAgents
			ReciveMessagesFromFuzzyAgentBehaviour reciveMessagesFromFuzzyAgentBehaviour = 
					new ReciveMessagesFromFuzzyAgentBehaviour(this.myAgent, this.settings.getApplication(), this.settings.getFuzzyagents(),lines-1, msg, this.agentController);
			myAgent.addBehaviour(reciveMessagesFromFuzzyAgentBehaviour);
			
			//open file with all data
			reader = new BufferedReader(new FileReader(FILES_PATH+infoFile));
			String line;
			String application;
			int rowNum=1;
			
			application = reader.readLine();
			while((line = reader.readLine()) != null) {
				//send each row to all fuzzy agents
				SendMessages.sendMessagesToFuzzyAgents(myAgent, line, application, this.settings, rowNum);
				rowNum++;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if( reader != null) {
					reader.close();
				}
				if(counter !=null) {
					counter.close();
				}
			} catch (IOException ex) {   
                ex.printStackTrace();  
            }
		}
	}

}
