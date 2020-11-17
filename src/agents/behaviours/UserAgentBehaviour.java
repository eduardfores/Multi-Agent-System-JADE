package agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class UserAgentBehaviour extends CyclicBehaviour {
    
	private static final long serialVersionUID = -596443178110179899L;
	private String managerName;
	
	public UserAgentBehaviour(Agent agent, String managerName) {
		super(agent);
		this.managerName=managerName;
	}

	public void action() {
		String configuration = (String) myAgent.getO2AObject();
		
		if(configuration != null){
			System.out.println("You selected the configuration: "+configuration);
			
			final ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setSender(this.myAgent.getAID());
			msg.addReceiver(new AID(this.managerName, AID.ISLOCALNAME));  

			msg.setContent(configuration);
			this.myAgent.send(msg);			
		}else {
			block();
		}
		
	}
}
