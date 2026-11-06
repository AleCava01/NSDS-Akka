package com.exercise3.serverclient;

import akka.pattern.Patterns;
import scala.compat.java8.FutureConverters;

import akka.actor.Props;


import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;


public class ClientActor extends AbstractActor{
	
	private final ActorRef server;
	
	public ClientActor(ActorRef server) {
		this.server = server;
	}
	
	static Props props(ActorRef server) {
	    return Props.create(ClientActor.class, () -> new ClientActor(server));
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SendToServer.class, this::onSendToServer)
				.build();
	}
	
	private void onSendToServer(SendToServer whatToAsk) {
		if(whatToAsk.type == SendToServer.ASK_CREATE_ACCOUNT) {
			
			server.tell(
					new NewAccount(
							whatToAsk.email.orElse(null),
							whatToAsk.name
							),
					getSelf()
					);
			
		}
		else if(whatToAsk.type == SendToServer.ASK_EMAIL) {
			// ask pattern
			GetAccountRequest req = new GetAccountRequest(whatToAsk.name);
			CompletionStage<Object> future = FutureConverters.toJava(
			        Patterns.ask(server, req , 1000)
			);			
			future.thenAccept(response -> {
				GetAccountResponse msg = (GetAccountResponse) response;
				System.out.println("\n[CLIENT] Received email: "+msg.email);
			}).exceptionally(error -> {
		        System.err.println("\n[CLIENT] Request failed: " + error.getMessage());
		        return null;
		    });
		}
	}
}
