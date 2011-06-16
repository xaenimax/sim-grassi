/* La classe che rappresenta il behaviour che aspetta la chiamata out dall'applicazione
 * 
 */

package it.progettoSIM.jade.behaviour;

import jade.agent.AgentJade;
import jade.agent.CommandMatch;
import jade.tuplespace.Tuple;
import jade.tuplespace.TupleSpace;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


public class OutBehaviour extends CyclicBehaviour{
	
	private TupleSpace ts;

	public OutBehaviour(TupleSpace ts) {
		this.ts = ts;
	}

	@Override
	public void action() {
		CommandMatch cm = new CommandMatch(AgentJade.CMDOUT);
		MessageTemplate mt = new MessageTemplate(cm);
		ACLMessage msg = ts.receive(mt);
		if (msg != null) {
			Tuple tuple;
			try {
				tuple = (Tuple)msg.getContentObject();
				int sizeTuple = tuple.getSize();
				//inizio politica se attiva
				if(ts.politicheAttive()){
					int ramTot = ts.getRamTotal(sizeTuple);
					int ramTs = ts.getRamTS(sizeTuple);
					int ramLoad = ts.getRamTotal(0);
					int disk = ts.getSizeTS()/1000;//è in MB size è in KB
					int freeDisk = ts.getDiskFree();
					if(freeDisk > ts.getRangeDisk()) freeDisk = ts.getRangeDisk();//prendo il minimo tra disco libero il vincolo sul disco
					int upLoadFree = 100 - ts.getNetLoadUp();
					//migra tupla se la percentuale attuale della ram occupata è maggiore della soglia
					if( (( ramTot>73  || ( ramTs>30 && ramLoad>60 )  ) ||  ((disk + (sizeTuple/1000))>freeDisk )) && upLoadFree > 5){
						AID aid = ts.sendOut(tuple);
						if(aid == null){
							ts.increaseOutR();
							ts.out(tuple);
						} else {
							this.ts.increaseOut();
							ts.setAidVectorExported(aid);
							ts.addBandUp(upLoadFree);
							ts.addSecUp(ts.getSecUp(sizeTuple));
						}
						
					}else ts.out(tuple);
				}else ts.out(tuple);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
