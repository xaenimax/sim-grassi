/* Questa è la classe che rappresenta il componente generico per la pubblicazione e sottoscrizione dei servizi
 */

package it.progettoSIM.jade.agent;

import it.progettoSIM.jade.content.Content;

import java.io.IOException;

import jade.tuplespace.Node;
import jade.tuplespace.Tuple;
import jade.tuplespace.TupleSpace;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

abstract public class AgentSimulator extends AgentJade{
	
	private static int countApp = 1;
	private AID tsAID;
	
	public AgentSimulator(){
		this.setupAppAgent();
		
	}
	public void setup(){
		
	}
	
	//La funzione che crea e fa partire l'agente dell'applicazione
	private void setupAppAgent(){
		ContainerController ac = null;
		Runtime run = Runtime.instance();
		Profile p = new ProfileImpl();
		ac = (ContainerController)run.createAgentContainer(p);
		try {
			AgentController a = ac.acceptNewAgent("Application" + countApp, this);
			countApp++;
			a.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
	}
	
	//La funzione invia un messaggio di disponibilità allo sblocco del componente successivo
	public boolean publish(Content cnt){
		ACLMessage msg=AgentJade.createCommandMessage(CMDPBL);
		msg.addReceiver(this.tsAID);//aggiunto slot receiver
		try {
			msg.setContentObject(cnt);
			this.send(msg);
			return true;
		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	//La funzione che chiama la rd sul tuplespace e si blocca in attesa di risposta
	public Object rd(Tuple template, long millis){
		return this.sendCommand(template, millis, AgentJade.CMDREAD);
	}
	
	//La funzione che chiamala in sul tuplespace e si blocca in attesa di risposta
	public Object in(Tuple template, long millis){
		return this.sendCommand(template, millis, AgentJade.CMDIN);
	}
	
	//La funzione che esegue un comando di rd o in e si blocca in attesa di risposta
	public Object sendCommand(Tuple template, long millis, String cmd){
		ACLMessage msg = AgentJade.createCommandMessage(cmd);
		CommandMatch cm = new CommandMatch(AgentJade.CMDREPLY + cmd);
		MessageTemplate mt = new MessageTemplate(cm);
		msg.addReceiver(this.tsAID);
		try {
			msg.setContentObject(template);
			this.send(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ACLMessage reply = this.blockingReceive(mt, millis);
		try {
			if(reply != null){
				return (Tuple)reply.getContentObject();
			} else {
				ACLMessage msgDel = AgentJade.createCommandMessage(AgentJade.CMDDEL);
				msgDel.addReceiver(tsAID);
				this.send(msgDel);
				return null;
			}
		} catch (UnreadableException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//La funzione che permette la registrazione su un nodo
	public boolean register(Node node){
		tsAID = node.getTsAID();
		return true;
	}
}
