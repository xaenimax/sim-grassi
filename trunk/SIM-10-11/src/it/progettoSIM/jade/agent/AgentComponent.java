package it.progettoSIM.jade.agent;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AgentComponent extends AgentJade{
	
private boolean politiche;
	
	public AgentComponent(){
		
	}
	
	//funzione che registra il servizio al DF
	public boolean registService() {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setName(this.getLocalName());
		sd.setType("ack");
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
			return true;
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deregister(){
		try { DFService.deregister(this);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	//Funzione che restituisce l'array di tutti gli agenti che forniscono il servizio
	private DFAgentDescription[] getAckAgents(){
		 DFAgentDescription dfd = new DFAgentDescription();
         ServiceDescription sd  = new ServiceDescription();
         sd.setType("ack");
         dfd.addServices(sd);
         SearchConstraints searchAll = new SearchConstraints();
         searchAll.setMaxResults(new Long(-1));
         try {
			return DFService.search(this, dfd, searchAll);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//Funzione che ricerca agenti che offrono il servizio specificato nella stringa
	public AID getService( String service ){
		DFAgentDescription dfd = new DFAgentDescription();
	    ServiceDescription sd = new ServiceDescription();
	    sd.setType( service );
	    dfd.addServices(sd);
	    try {
	    	DFAgentDescription[] result = DFService.search(this, dfd);
	    	if (result.length>0)
	    		return result[0].getName() ;
	    }
	    catch (FIPAException fe) { 
	    	fe.printStackTrace(); 
	    }
	    return null;
	    }
}
