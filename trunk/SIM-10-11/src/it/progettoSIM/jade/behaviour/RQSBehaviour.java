//LA classe descrive il comportamento del componente che rimane in attesa di un ack dall'esterno
package it.progettoSIM.jade.behaviour;

import it.progettoSIM.jade.agent.AgentJade;
import it.progettoSIM.jade.agent.CommandMatch;
import it.progettoSIM.jade.content.Content;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RQSBehaviour extends Behaviour{
	
	private Content cnt;
	
	public RQSBehaviour(Content cnt){
		this.cnt=cnt;
		
	}

	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDRQS );
		MessageTemplate mt = new MessageTemplate(cm);
		
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
