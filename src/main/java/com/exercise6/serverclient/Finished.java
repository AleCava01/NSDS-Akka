package com.exercise6.serverclient;
import akka.actor.ActorRef;

public class Finished {
	
	public final ActorRef client;
	
	public final String text;

	
	public Finished(ActorRef client, String text) {
		this.client = client;
		this.text = text;
	}
}
