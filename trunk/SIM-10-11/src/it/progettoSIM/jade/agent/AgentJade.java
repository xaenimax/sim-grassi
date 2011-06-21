/* La classe che rappresenta gli agenti JADE
 * 
 */
package it.progettoSIM.jade.agent;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

abstract public class AgentJade extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Campi statici relativi ai vari comandi
	public final static String CMDREAD = "rd";
	public final static String CMDIN = "in";
	public final static String CMDREPLY = "reply";
	public final static String CMDTS = "ts";
	public final static String CMDOUT = "out";
	public final static String CMDDEL = "del";
	public final static String CMDFAIL = "fail";
	public final static String CMDOK = "ok";
	//--------------
	public final static String CMDRQS = "request";
	public final static String CMDACK = "ack";
	
	public AgentJade(){
	}
	
	// Funzione che formatta l'ACLMessage in maniera corretta
	public static ACLMessage createCommandMessage(String cmd) {
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addUserDefinedParameter("command", cmd);
		return msg;
	}
}
