/* Behaviour che gestisce l'arrivo di chiamate di rd dagli altri ts
 * 
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

public class RdTsBehaviour extends CyclicBehaviour {

	private TupleSpace ts;

	public RdTsBehaviour(TupleSpace ts) {
		this.ts = ts;
	}

	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDREAD + AgentJade.CMDTS);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		int upBandFree = 100 - ts.getNetLoadUp();
		if (msg != null) {
			try {
				// Controlla se le poltiche sono attive
				if (ts.politicheAttive()) {
					// Se la banda in upload è minore di un certo valore passa
					// al prossimo TS
					if (upBandFree < 10) {
						ACLMessage reply = msg.createReply();
						reply.addUserDefinedParameter("command",
								AgentJade.CMDREPLY + AgentJade.CMDREAD
										+ AgentJade.CMDTS);
						reply.setContentObject(null);
						reply.setPerformative(ACLMessage.INFORM);
						ts.send(reply);
					} else {
						Tuple template = (Tuple) msg.getContentObject();
						Tuple tuple = (Tuple) ts.rdLocal(template);
						if (tuple != null) {
							this.ts.increaseRdEx();
							int size = tuple.getSize();
							ts.addBandUp(upBandFree);
							ts.addSecUp(ts.getSecUp(size));
						}
						ACLMessage reply = msg.createReply();
						reply.addUserDefinedParameter("command",
								AgentJade.CMDREPLY + AgentJade.CMDREAD
										+ AgentJade.CMDTS);
						reply.setContentObject(tuple);
						reply.setPerformative(ACLMessage.INFORM);
						ts.send(reply);
					}
				} else {
					Tuple template = (Tuple) msg.getContentObject();
					Tuple tuple = (Tuple) ts.rdLocal(template);
					if (tuple != null) {
						this.ts.increaseRdEx();
						int size = tuple.getSize();
						ts.addBandUp(upBandFree);
						ts.addSecUp(ts.getSecUp(size));
					}
					ACLMessage reply = msg.createReply();
					reply.addUserDefinedParameter("command", AgentJade.CMDREPLY
							+ AgentJade.CMDREAD + AgentJade.CMDTS);
					reply.setContentObject(tuple);
					reply.setPerformative(ACLMessage.INFORM);
					ts.send(reply);
				}

			} catch (UnreadableException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			block();

	}

}
