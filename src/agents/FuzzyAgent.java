package agents;

import agents.behaviours.FuzzyAgentBehaviour;
import jade.core.Agent;

public class FuzzyAgent extends Agent{

	private static final long serialVersionUID = -6518140430784683918L;

	protected void setup() {
		
		final Object[] args = getArguments();
		
		if(args.length==0){
			System.err.println("Error while creating the user agent. There are not arguments");
			System.exit(-1);
		}
		//arg[0]= fcl - arg[1]=name
		FuzzyAgentBehaviour fuzzyBehaviour = new  FuzzyAgentBehaviour(this, (String) args[0], (String) args[1]);
		addBehaviour(fuzzyBehaviour);
	}
}
