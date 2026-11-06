package com.exercise5.serverclient.supervisor;

import akka.actor.ActorRef;

public class ConfigMsg {
	
	private ActorRef server;
	
	public ConfigMsg(ActorRef server) {
		this.server = server;
	}
	public ActorRef getServerRef() {
		return server;
	}

}
