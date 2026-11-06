package com.exercise6.serverclient;

import akka.actor.ActorRef;


public class WhatToSend {
	
	public String text;
	public ActorRef server;
	
	public WhatToSend(String text, ActorRef server) {
		this.text = text;
		this.server = server;
	}
}
