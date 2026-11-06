package com.exercise3.serverclient;

import akka.actor.AbstractActor;
import akka.actor.Props;
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
				.match(GetAccountRequest.class, this::onEmailRequest)	
				.build();
	}

	private void onNewAccount(NewAccount info) {
		this.emailByName.put(info.name, info.email);
		
		System.out.println("\n[SERVER] New account added!");
		System.out.println("\n[SERVER] CURRENT ACCOUNTS:");
		emailByName.forEach((name, email) ->
		    System.out.println("[SERVER] " +name + " -> " + email)
		);

	}
	
	private void onEmailRequest(GetAccountRequest request) {
		
		String email = this.emailByName.get(request.name);
		
		GetAccountResponse response = new GetAccountResponse(email);
		
		// Send the response back to the original sender
		getSender().tell(response, getSelf());
	}

}


