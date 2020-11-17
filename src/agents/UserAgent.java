package agents;

import agents.behaviours.UserAgentBehaviour;
import agents.behaviours.UserAgentBehaviourReciver;
import jade.core.Agent;

public class UserAgent extends Agent{

	private static final long serialVersionUID = 1L;
	
	public void setup() {
		
		final Object[] args = getArguments();
		if(args.length==0){
			System.err.println("Error while creating the user agent. There are not arguments");
			System.exit(-1);
		}
			
		setEnabledO2ACommunication(true, 0);
		
		UserAgentBehaviour userBehaviour = new  UserAgentBehaviour(this, (String) args[0]);
		UserAgentBehaviourReciver userBehaviourReciver = new  UserAgentBehaviourReciver(this);
		addBehaviour(userBehaviourReciver);
		addBehaviour(userBehaviour);
	}
}
