package com.exercise5.serverclient.supervisor;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeoutException;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class ClientActor extends AbstractActor {
	
	private ActorRef server;
	private scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5,SECONDS);
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(ReplyMsg.class, this::processReply)
				.match(ConfigMsg.class, this::configure)
				.match(PutMsg.class, this::putEntry)
				.match(GetMsg.class, this::query)
				.build();
	}
	
	static Props props() {
		return Props.create(ClientActor.class);
	}
	
	void processReply(ReplyMsg msg) {
		System.out.println("[CLIENT] Unsolicited reply, this should not happen!");
	}
	
	
	void putEntry(PutMsg msg) {
		if(server != null) {
			System.out.println("[CLIENT] Sending new entry "+ msg.getName() + " - "+ msg.getEmail());
			server.tell(msg, self());
		}
		else {
			System.out.println("[CLIENT] Error: missing server address!");
		}
	}
	
	
	void query(GetMsg msg) {
		if(server != null) {
			System.out.println("[CLIENT] Issuing query for "+ msg.getName());
			
			// Ask pattern
			scala.concurrent.Future<Object> waitingForReply = ask(server, msg, 5000);
			try {
				ReplyMsg reply = (ReplyMsg) waitingForReply.result(timeout, null);
				if(reply.getEmail()!=null) {
					System.out.println("[CLIENT] Received reply, email is "+reply.getEmail());
				}
				else {
					System.out.println("[CLIENT] Received reply, no email found!");
				}
			} catch (TimeoutException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("[CLIENT] Error: missing server address!");
		}
	}
	
	
	void configure(ConfigMsg msg) {
		System.out.println("[CLIENT] New server set up: "+ msg.getServerRef());
		this.server = msg.getServerRef();
	}


	

}
