package it.progettoSIM.jade.behaviour;

import it.progettoSIM.jade.agent.AgentJade;
import it.progettoSIM.jade.agent.CommandMatch;
import jade.core.behaviours.CyclicBehaviour;

public class PblBehaviour extends CyclicBehaviour {
	
	private String ComponentSerial;
	
	public PblBehaviour(){
		
	}

	@Override
	public void action() {
		CommandMatch cmd=new CommandMatch(AgentJade.CMDPBL);
		
	}
}
