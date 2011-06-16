/*
 * Il behaviour che gestisce le in
 */

package it.progettoSIM.jade.behaviour;

import java.io.IOException;

import jade.agent.AgentJade;
import jade.agent.CommandMatch;
import jade.tuplespace.TemplateException;
import jade.tuplespace.Tuple;
import jade.tuplespace.TupleSpace;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class InBehaviour extends CyclicBehaviour{
	
	private TupleSpace ts;

	public InBehaviour(TupleSpace ts) {
		this.ts = ts;
	}
	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDIN);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		if (msg != null) {
			try {
				Object template = msg.getContentObject();
				Tuple tuple = (Tuple) ts.in((Tuple)template);
				if (tuple != null){
					ACLMessage reply = msg.createReply();
					reply.addUserDefinedParameter("command", AgentJade.CMDREPLY + AgentJade.CMDIN);
					reply.setContentObject(tuple);
					reply.setPerformative( ACLMessage.INFORM );
					ts.send(reply);
				} else {
					ts.getRequests().addRequest(msg.getSender(), AgentJade.CMDIN, (Tuple)template);
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
