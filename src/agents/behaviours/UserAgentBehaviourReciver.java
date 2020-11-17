package agents.behaviours;

import DTOs.MessageCodes;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UserAgentBehaviourReciver extends CyclicBehaviour {
    
	private static final long serialVersionUID = 1288719781997466055L;
	private static final String  FILE_RESULTS = "_results.txt";
	private static final String  RESULTS_PATH= "results\\";
	
	private MessageTemplate performativeFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
	
	public UserAgentBehaviourReciver(Agent agent) {
		super(agent);
	}

	public void action() {
		ACLMessage reply = myAgent.receive(performativeFilter);
		
		if(reply != null){
			String[] replySplited = reply.getContent().split("-");
			if(replySplited[0].equals(MessageCodes.FUZZY_AGENTS_CREATED.toString())) {
				System.out.println("The fuzzy Agents are created");
			}else if(replySplited[0].equals(MessageCodes.FUZZY_AGENTS_ERROR.toString())) {
				System.out.println("The fuzzy Agents have problems in the creation");
			}else if(replySplited[0].equals(MessageCodes.RESULTS_SAVED_ERROR.toString())) {
				System.out.println("The store of results in managerAgent have problems in the creation");
			}else if(replySplited[0].equals(MessageCodes.RESULTS_SAVED.toString())) {
				System.out.println("The results are in the file: "+RESULTS_PATH+replySplited[1]+FILE_RESULTS);
			}
		} else {
			block();
		}
		
	}
}
