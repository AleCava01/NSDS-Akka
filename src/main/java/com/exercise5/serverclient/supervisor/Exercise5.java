package com.exercise5.serverclient.supervisor;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;


import java.util.concurrent.TimeoutException;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


public class Exercise5 {
	public static void main(String[] args) {
		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef client = sys.actorOf(ClientActor.props(), "client");
		scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);

		// Asking the supervisor to create the server
		
		final ActorRef supervisor = sys.actorOf(SupervisorActor.props(), "supervisor");
		scala.concurrent.Future<Object> waitingForServer = ask(supervisor, Props.create(ServerActor.class), 5000);
		ActorRef server = null;
		try {
			server = (ActorRef) waitingForServer.result(timeout,null);
		} catch (TimeoutException | InterruptedException e){
			e.printStackTrace();
		}
		
		client.tell(new ConfigMsg(server), ActorRef.noSender());
		
		
		client.tell(new PutMsg("Alessandro","alessandro@gmail.com"), ActorRef.noSender());
		client.tell(new PutMsg("Santiago","santiago@gmail.com"), ActorRef.noSender());
		client.tell(new PutMsg("Anas","anas@gmail.com"), ActorRef.noSender());
		
		
		client.tell(new GetMsg("Alessandro"), ActorRef.noSender());
		
		client.tell(new PutMsg("Fail!","..."), ActorRef.noSender());
		
		// Should work if correctly resumed
		client.tell(new GetMsg("Alessandro"), ActorRef.noSender());

		
		sys.terminate();
	
	}

}
