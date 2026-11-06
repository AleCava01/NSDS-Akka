package com.exercise5.serverclient.supervisor;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;
import java.time.Duration;

public class SupervisorActor extends AbstractActor{
	
	private static SupervisorStrategy strategy = new OneForOneStrategy(
			1, // Max no of retries
			Duration.ofMinutes(1), // Within what time period
			DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.resume())
			.build());
	
	public SupervisorActor() {
		
	}
	
	@Override
	public SupervisorStrategy supervisorStrategy() {
		return strategy;
	}
	public Receive createReceive() {
		return receiveBuilder()
				.match(Props.class, this::receivedProps)
				.build();
	}
	void receivedProps(Props props) {
		getSender().tell(getContext().actorOf(props), getSelf());
	}
	
	static Props props() {
		return Props.create(SupervisorActor.class);
	}
}
