package agents.behaviours;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class FuzzyAgentBehaviour extends CyclicBehaviour {
    
	private static final long serialVersionUID = -8659012849908160858L;
	private static final String TIP_APPLICATION="tipper";
	private static final String QUALITY_APPLICATION="qualityservice";
	
	private FIS fis;
	private String application;
    private MessageTemplate performativeFilter = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
    
	public FuzzyAgentBehaviour(Agent agent, String fcl, String application) {
		super(agent);
		this.application = application;
		this.fis=FIS.load(fcl,true);
		if( fis == null ) { 
            System.err.println("Can't load file: '" 
                                   + fcl + "'");
            return;
        }
	}

	public void action() {
		ACLMessage msg = myAgent.receive(performativeFilter);
		if(msg != null) {
			String[] vars = msg.getContent().split(",");
			Variable result = null;
			
	        if(this.application.equals(TIP_APPLICATION)) {
	        	// Show 
		        FunctionBlock functionBlock = fis.getFunctionBlock(this.application);
	        	// Set inputs
		        fis.setVariable("service", Double.parseDouble(vars[1]));
		        fis.setVariable("food", Double.parseDouble(vars[2]));
		        
		        // Evaluate
		        fis.evaluate();

		        // Show output variable's chart
		        result = functionBlock.getVariable("tip");
		        
	        } else if(this.application.equals(QUALITY_APPLICATION)) {
	        	
	        	// Show 
		        FunctionBlock functionBlock = fis.getFunctionBlock("MamdaniQoSFewRules");
		        
		        fis.setVariable("commitment", Double.parseDouble(vars[1]));
		        fis.setVariable("clarity", Double.parseDouble(vars[2]));
		        fis.setVariable("influence", Double.parseDouble(vars[3]));
		        
		        // Evaluate
		        fis.evaluate();
		        
		        result = functionBlock.getVariable("service_quality");
	        }
	        
	        
	        // Print ruleSet
	        ACLMessage reply = msg.createReply();
	        reply.setPerformative(ACLMessage.INFORM);
	        reply.setContent(vars[0]+"_"+result.getLatestDefuzzifiedValue());
	        myAgent.send(reply);
	        
		} else {
			block();
		}
       
	}


}
