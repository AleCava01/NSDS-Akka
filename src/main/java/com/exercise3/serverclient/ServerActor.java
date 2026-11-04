package com.exercise3.serverclient;

import akka.actor.AbstractActorWithStash;
import akka.actor.Props;
import akka.actor.AbstractActor.Receive;
import akka.actor.AbstractActor;
import java.util.Map;


import java.util.HashMap;

public class ServerActor extends AbstractActor{
	
	Map<String,String> emailByName;
	
	
	public ServerActor() {
		this.emailByName = new HashMap<>();
	}
	static Props props() {
		return Props.create(ServerActor.class);
	}

	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(NewAccount.class, this::onNewAccount)
				.match(NewAccount.class, this::onNewAccount)	
				.build();
	}

	void onNewAccount(NewAccount info) {
		this.emailByName.put(info.name, info.email);
	}
	
	GetAccountResponse onEmailRequest(GetAccountRequest request) {
		String email = this.emailByName.get(request.name);
		return (new GetAccountResponse(email));
	}

}


