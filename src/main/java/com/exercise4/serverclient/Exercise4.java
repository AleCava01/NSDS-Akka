package com.exercise4.serverclient;

import java.io.IOException;

import com.counter.decreasingSingleWithBuffer.copy.DataMessage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Exercise4{

	public static void main(String[] args) {
		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef server = sys.actorOf(ServerActor.props(), "server");
		final ActorRef client = sys.actorOf(ClientActor.props(server), "client");

		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.SLEEP, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());

		client.tell(new WhatToSend(WhatToSend.WAKEUP, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.SLEEP, server), ActorRef.noSender());

		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		
		client.tell(new WhatToSend(WhatToSend.WAKEUP, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		client.tell(new WhatToSend(WhatToSend.GENERIC, server), ActorRef.noSender());
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sys.terminate();
	
	}
}