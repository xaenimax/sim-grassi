/*
 * Il behaviour che cancella la richiesta da quelle pendenti 
 */

package it.progettoSIM.jade.behaviour;

import it.progettoSIM.jade.agent.AgentJade;
import it.progettoSIM.jade.agent.CommandMatch;
import it.progettoSIM.jade.tuplespace.TupleSpace;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class DelBehaviour extends CyclicBehaviour{
	
	private TupleSpace ts;

	public DelBehaviour(TupleSpace ts) {
		this.ts = ts;
	}
	
	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDDEL);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		if (msg != null) {
			ts.getRequests().deleteRequest(msg.getSender());
			System.out.println("Richiesta cancellata");
		}else{
			block();
		}
		
	}
	
}
