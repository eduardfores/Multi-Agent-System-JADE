package main;

import java.util.Scanner;

import Util.CreateAgent;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;

//java -cp lib\jade.jar;classes jade.Boot -gui
public class Main {

	public static ContainerController initializeContainer() throws ControllerException{
		//we define the container and we start the userAgent and managerAgent to work with them.
	    Runtime myRuntime = Runtime.instance();

		 // prepare the settings for the platform that we're going to connect to
		 Profile myProfile = new ProfileImpl();
		 myProfile.setParameter(Profile.MAIN_HOST, "localhost");
		 myProfile.setParameter(Profile.MAIN_PORT, "1099");
		 myProfile.setParameter(Profile.CONTAINER_NAME, "practice1");
		 return myRuntime.createAgentContainer(myProfile);
	}
	
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		boolean agentsDefined=false;
		
		boolean finishProcess  = false;
		
		String correct="";
		String userAgentName="";
		String managerAgentName="";
		
		ContainerController practice1Container=null;
		AgentController userAgentController=null;
		AgentController managerAgentController=null;
		
		//first menu to define the names of the agents
		while(!agentsDefined) {
			System.out.println("Enter userAgent name");
			userAgentName = keyboard.next();
			
			System.out.println("Enter managerAgent name");
			managerAgentName = keyboard.next();
			
			System.out.println("The names introduced are:\n\t -userAgent:"+userAgentName+" \n\t -managerAgent:"+managerAgentName);
			
			System.out.println("Are correct? yes(Y) no(N)");
			correct = keyboard.next();
			
			if(correct.toUpperCase().equals("Y")) {
				agentsDefined=true;
				System.out.println("You selected YES (Are correct)");
			}else if(correct.toUpperCase().equals("N")){
				System.out.println("You selected NO (Are not correct)");
				System.out.println("Introduce the names again");
			}else {
				System.out.println("You do not send the correct key (Y or N)");
				System.out.println("----");
				System.out.println("Are correct? yes(Y) no(N)");
				correct = keyboard.next();
				if(correct.toUpperCase().equals("Y")) {
					agentsDefined=true;
				}
			}
		}
		
		 try {
			 //initialize the container and the 2 mainly agents
			 practice1Container=initializeContainer();
			 userAgentController=CreateAgent.createUserAgent(userAgentName, managerAgentName, practice1Container);
			 managerAgentController=CreateAgent.createManagerAgent(managerAgentName, practice1Container);
			 
			 while(!finishProcess) {
				 //initialize the system
				 System.out.println("First of all we have to initialize the which process we want to do.");
				 System.out.println("You must write one of these 2 options: \n - I_configuration1.xml"/* \n - I_configuration2.xml"*/);
				 System.out.print("Write:");
				 String configuration = keyboard.next();
	
				 while(!configuration.equals("I_configuration1.xml") /*&& !configuration.equals("I_configuration2.xml")*/) {
					 System.out.println("you do not write good the configuration name. It must begin by \'I\'");
					 System.out.print("Rewrite the configuration please:");
					 configuration = keyboard.next();
				 }
				 
				 userAgentController.putO2AObject(configuration, AgentController.SYNC);
				 
				 //It is not the best solution to syncronize the userAgent with the main process but is the fastest
				 Thread.sleep(1000);
				 
				//take decicion
				 System.out.println("\nRight now you must to select the decision to do:");
				 System.out.println("Your options are:");
				 
				 if(configuration.equals("I_configuration1.xml")) {
					 System.out.println("- D_requests_tipper.txt");
				 } /*else {
	
					 System.out.println("- D_requests_quality.txt");
				 }*/
				 System.out.print("Write:");
				 
				 String decision = keyboard.next();
	
				 while(!decision.equals("D_requests_tipper.txt") /*&& !decision.equals("D_requests_quality.txt")*/) {
					 System.out.println("you do not write good the configuration name. It must begin by \'D\'");
					 System.out.print("Rewrite the configuration please:");
					 decision = keyboard.next();
				 }
				 
				 userAgentController.putO2AObject(decision, false);
				 
				 //It is not the best solution to syncronize the userAgent with the main process but is the fastest
				 Thread.sleep(1000);
				 
				 System.out.println("Do you want reprocude it again?");
				 System.out.println("Write Yes(Y) or No(N)");
				 System.out.print("Write:");
				 correct = keyboard.next();
				 
				 if(correct.toUpperCase().equals("Y")) {
					System.out.println("We reproduce the process again");
				 }else if(correct.toUpperCase().equals("N")){
					finishProcess=true;
					System.out.println("We stop the process");
				 }else {
					System.out.println("You do not send the correct key (Y or N)");
					System.out.println("----");
					System.out.println("Are correct? yes(Y) no(N)");
					correct = keyboard.next();
					if(correct.toUpperCase().equals("Y")) {
						agentsDefined=true;
					}else {
						System.out.println("We will start again the process");
					}
				 }
			}
			keyboard.close();
			//finish the container
			practice1Container.kill();
		} catch (ControllerException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}
