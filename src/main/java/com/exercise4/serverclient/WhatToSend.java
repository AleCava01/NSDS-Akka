package com.exercise4.serverclient;

import akka.actor.ActorRef;


public class WhatToSend {
	
	public static final int SLEEP = 0;
	public static final int WAKEUP = 1;
	public static final int GENERIC = 2;
	
	public String text;
	
	public int type;
	public ActorRef server;
	
	public WhatToSend(int type, ActorRef server) {
		this.type = type;
		this.server = server;
	}
}
