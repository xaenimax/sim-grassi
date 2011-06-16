/* 
 * La classe che implementa il behaviour che risponde
 * alle richieste di rd fatte dal component
 */

package it.progettoSIM.jade.behaviour;

import jade.agent.AgentJade;
import jade.agent.CommandMatch;
import jade.tuplespace.TemplateException;
import jade.tuplespace.Tuple;
import jade.tuplespace.TupleSpace;

import java.io.IOException;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class RdBehaviour extends CyclicBehaviour {

	private TupleSpace ts;

	public RdBehaviour(TupleSpace ts) {
		this.ts = ts;
	}

	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDREAD);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		if (msg != null) {
			try {
				Object template = msg.getContentObject();
				Tuple tuple = (Tuple) ts.rd((Tuple)template);
				if (tuple != null){
					ACLMessage reply = msg.createReply();
					reply.addUserDefinedParameter("command", AgentJade.CMDREPLY + AgentJade.CMDREAD);
					reply.setContentObject(tuple);
					reply.setPerformative( ACLMessage.INFORM );
					ts.send(reply);
				} else {
					ts.getRequests().addRequest(msg.getSender(), AgentJade.CMDREAD, (Tuple)template);
				}
				
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (UnreadableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else
			block();

	}

}
