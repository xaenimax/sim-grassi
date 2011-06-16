/* Behaviour che gestisce l'arrivo di chiamate di out dagli altri ts
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

public class OutTsBehaviour extends CyclicBehaviour{

	private TupleSpace ts;
	
	public OutTsBehaviour(TupleSpace ts) {
		this.ts = ts;
	}
	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDOUT + AgentJade.CMDTS);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		if (msg != null) {
			try {
				Tuple tuple = (Tuple)msg.getContentObject();
				int sizeTuple = tuple.getSize();
				//Accetta la tupla se ha spazio nella RAM e nel disco(anche potenzialmente)
				int ramTot = ts.getRamTotal(sizeTuple);
				int ramTs = ts.getRamTS(sizeTuple);
				int ramLoad = ts.getRamTotal(0);
				
				int disk = ts.getSizeTS()/1000;
				int freeDisk = ts.getDiskFree();
				int downLoadFree = 100 - ts.getNetLoadDown();
				if(freeDisk < ts.getRangeDisk()) freeDisk = ts.getRangeDisk();//prendo il minimo tra disco libero il vincolo sul disco
				
				int energy = ts.getEnergy();
				
				//migra tupla se la percentuale attuale della ram occupata è maggiore della soglia
				if( ( (ramTot>75  || ( ramTs>30 && ramLoad>60 ) ) ||  ((disk + (sizeTuple/1000))>freeDisk ) ) || downLoadFree < 30 || energy < 25 ){
					ACLMessage reply = msg.createReply();
					reply.addUserDefinedParameter("command", AgentJade.CMDREPLY + AgentJade.CMDOUT + AgentJade.CMDTS);
					String fail = AgentJade.CMDFAIL;
					reply.setContent(fail);
					reply.setPerformative( ACLMessage.INFORM );
					ts.increaseOutReject();
					ts.send(reply);
				} else {
					ACLMessage reply = msg.createReply();
					reply.addUserDefinedParameter("command", AgentJade.CMDREPLY + AgentJade.CMDOUT + AgentJade.CMDTS);
					String ok = AgentJade.CMDOK;
					reply.setContent(ok);
					reply.setPerformative( ACLMessage.INFORM );
					ts.send(reply);
					ts.out(tuple);
					ts.increaseOutEx();
					ts.setAidVectorImported(msg.getSender());
					ts.addBandDown(downLoadFree);
					ts.addSecDown(ts.getSecDown(sizeTuple));
				}
			} catch (UnreadableException e) {
				e.printStackTrace();
			} 
		}
		else block();
		
	}
}
