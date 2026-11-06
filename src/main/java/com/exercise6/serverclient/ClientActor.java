package com.exercise6.serverclient;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.AbstractActor.Receive;
import akka.pattern.Patterns;
import scala.compat.java8.FutureConverters;
import scala.concurrent.duration.Duration;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


public class ClientActor extends AbstractActor{
	
	private final ActorRef server;
	
	public ClientActor(ActorRef server) {
		this.server = server;
	}
	public static Props props(ActorRef server) {
        return Props.create(ClientActor.class,
                () -> new ClientActor(server));
    }

	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(WhatToSend.class, this::onMainRequest)
				.build();
	}
	public void onMainRequest(WhatToSend whatToSend) {
		long start = System.currentTimeMillis();

	    CompletionStage<Object> future = FutureConverters.toJava(
	        Patterns.ask(server, new Request(whatToSend.text), 40000)
	    );

	    future.thenAccept(response -> {
	        Reply reply = (Reply) response;
	        System.out.println("Received: " + reply.response + " after: "+ (System.currentTimeMillis()-start) + " s");
	    }).exceptionally(error -> {
	        System.err.println("Request failed: " + error.getMessage());
	        return null;
	    });
	}
}
