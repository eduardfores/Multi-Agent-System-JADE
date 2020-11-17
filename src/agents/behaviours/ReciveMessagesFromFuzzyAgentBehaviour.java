package agents.behaviours;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTOs.MessageCodes;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class ReciveMessagesFromFuzzyAgentBehaviour extends CyclicBehaviour {
    
	private static final long serialVersionUID = 3574872818749675286L;
	private static final String  FILE_RESULTS = "_results.txt";
	private static final String  RESULTS_PATH= "results\\";
	
	private String application;
    private MessageTemplate performativeFilter = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
    private int numFuzzyAgents;
    private int totalNumTests;
    private int testCounter;
    private ACLMessage msg;
    private Map<Integer, Double> results;
    private List<AgentController> agentController;
    
	public ReciveMessagesFromFuzzyAgentBehaviour(Agent agent, String application, int numFuzzyAgents, 
			int numTests, ACLMessage msg, List<AgentController> agentController) {
		super(agent);
		this.application = application;
		this.numFuzzyAgents = numFuzzyAgents;
		this.totalNumTests = numFuzzyAgents*numTests;
		this.msg=msg;
		this.testCounter=0;
		this.results = new HashMap<Integer, Double>();
		this.agentController = agentController;
	}

	public void action() {
		ACLMessage msg = myAgent.receive(performativeFilter);
		if(msg != null) {

			String[] reply=msg.getContent().split("_");
			
			if(this.results.get(Integer.parseInt(reply[0])) != null) {
				this.results.put(Integer.parseInt(reply[0]), this.results.get(Integer.parseInt(reply[0]))+Double.parseDouble(reply[1]));
			}else {
				this.results.put(Integer.parseInt(reply[0]), Double.parseDouble(reply[1]));
			}
			
			this.testCounter++;

			//when all tests are save we will create the result file
			if(this.testCounter==this.totalNumTests) {
				this.writeFile(this.msg);
			}
			
		} else {
			block();
		}
       
	}

	public void writeFile(ACLMessage msg) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		ACLMessage replyMsg = msg.createReply();
		replyMsg.setPerformative(ACLMessage.INFORM);
		
		try {
			fw = new FileWriter(RESULTS_PATH+this.application+FILE_RESULTS);
            bw = new BufferedWriter(fw);
            
            for (Map.Entry<Integer, Double> entry : this.results.entrySet()) {
                double average=(entry.getValue()/this.numFuzzyAgents);
                bw.write("Result line "+entry.getKey()+": "+average+"\n");
            }

       	} catch (IOException e) {
            System.err.format("IOException: %s%n", e);
			replyMsg.setContent(MessageCodes.RESULTS_SAVED_ERROR.toString());
			myAgent.send(replyMsg);
        } finally {
        	
        	
			replyMsg.setContent(MessageCodes.RESULTS_SAVED.toString()+"-"+this.application);
			myAgent.send(replyMsg);
			
			//kill fuzzy agents to create new agents
			this.agentController.forEach(agent ->{
				try {
					agent.kill();
				} catch (StaleProxyException e) {
					System.out.println(e.getMessage());
				}
			});
			
			this.agentController.clear();
			this.myAgent.removeBehaviour(this);
			
            try {
            	if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {
                System.err.format("IOException: %s%n", ex);
            }
        }
		
	}

}
