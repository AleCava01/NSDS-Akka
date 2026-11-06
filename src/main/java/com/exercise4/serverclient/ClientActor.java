package com.exercise4.serverclient;

import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.AbstractActor.Receive;
import akka.pattern.Patterns;
import scala.compat.java8.FutureConverters;


public class ClientActor extends AbstractActor{
	
	private final ActorRef server;
	public int counter;
	
	public ClientActor(ActorRef server) {
		this.server = server;
		this.counter = 0;
	}
	
	static Props props(ActorRef server) {
	    return Props.create(ClientActor.class, () -> new ClientActor(server));
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder().match(WhatToSend.class, this::onMainRequest)
				.build();
	}
	public void onMainRequest(WhatToSend request) {
		
		if(request.type == request.SLEEP) {
			server.tell(new SleepMessage(), getSelf());
		}
		else if(request.type == request.WAKEUP) {
			server.tell(new WakeupMessage(), getSelf());
		}
		else {
			// ask pattern
			GenericMessage toSend = new GenericMessage();

			toSend.number = counter;
			counter++;

			CompletionStage<Object> future = FutureConverters.toJava(
			        Patterns.ask(server, toSend, 1000)
			);			
			future.thenAccept(response -> {
				GenericMessage msg = (GenericMessage) response;
				System.out.println("Received message n."+msg.number);
			}).exceptionally(error -> {
		        System.err.println("Request failed: " + error.getMessage());
		        return null;
		    });
					
		}
	}

}
