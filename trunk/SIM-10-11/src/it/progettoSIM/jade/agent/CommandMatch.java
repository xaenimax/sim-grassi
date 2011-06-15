/* La classe che gestisce il match dei messaggi
 * scambiati tra gli agenti
 */

package it.progettoSIM.jade.agent;
import jade.lang.acl.*;

public class CommandMatch implements MessageTemplate.MatchExpression{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String command;
	
	public CommandMatch(String command){
		this.command = command;
	}

	@Override
	public boolean match(ACLMessage msg) {
		if(msg.getUserDefinedParameter("command") == null) return false;
		if ( command.compareTo(msg.getUserDefinedParameter("command"))== 0) return true;
		return false;
	}
	
	
}
