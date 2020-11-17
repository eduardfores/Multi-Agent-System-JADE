package Util;

import DTOs.SimulationSettings;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class SendMessages {
	
	public static void sendMessagesToFuzzyAgents(Agent myAgent, String info, String application, SimulationSettings settings, int rowNum) {
		
		final ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.setSender(myAgent.getAID());
		
		//add all recivers in of the msg
		for(int i=0; i<settings.getFuzzyagents(); i++) {
   			String name = application+i;
			msg.addReceiver(new AID(name, AID.ISLOCALNAME));
		}
		
		msg.setContent(rowNum+","+info);
		myAgent.send(msg); //send messages
	}
}
