package agents;

import agents.behaviours.ManagerAgentBehaviour;
import jade.core.Agent;
import jade.wrapper.ContainerController;

public class ManagerAgent extends Agent{
	private static final long serialVersionUID = 1524408279599538267L;

	protected void setup() {
		
		final Object[] args = getArguments();
		
		if(args.length==0){
			System.err.println("Error while creating the user agent. There are not arguments");
			System.exit(-1);
		}
		
		ManagerAgentBehaviour managerBehaviour = new  ManagerAgentBehaviour(this, (ContainerController) args[0]);
		addBehaviour(managerBehaviour);
	}
}
