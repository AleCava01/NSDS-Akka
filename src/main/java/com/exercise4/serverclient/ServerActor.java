package com.exercise4.serverclient;

import akka.actor.AbstractActorWithStash;
import akka.actor.Props;

public class ServerActor extends AbstractActorWithStash {
	
	public ServerActor() {
	}
	static Props props() {
		return Props.create(ServerActor.class);
	}

	@Override
	public Receive createReceive() {
		
		return waking();
	}
	
	final Receive sleeping() {
		return receiveBuilder().match(SleepMessage.class, this::onSleepMessageWhileSleeping)
				.match(WakeupMessage.class, this::onWakeupMessageWhileSleeping)	
				.match(GenericMessage.class, this::onGenericMessageWhileSleeping)	
				.build();
	}
	
	final Receive waking() {
		return receiveBuilder().match(SleepMessage.class, this::onSleepMessageWhileWaking)
				.match(WakeupMessage.class, this::onWakeupMessageWhileWaking)	
				.match(GenericMessage.class, this::onGenericMessageWhileWaking)	
				.build();

	}
	
	public void onSleepMessageWhileWaking(SleepMessage msg) {
		// Change to sleep behavior
		getContext().become(sleeping());
		System.out.println("Now sleeping");
		
	}
	public void onWakeupMessageWhileWaking(WakeupMessage msg) {
		// do nothing
		System.out.println("I was already woke");

	}
	public void onGenericMessageWhileWaking(GenericMessage msg) {
		// sends back
		getSender().tell(msg, getSelf());
	}
	
	public void onSleepMessageWhileSleeping(SleepMessage msg) {
		// do nothing
		System.out.println("zzZ");
	}
	public void onWakeupMessageWhileSleeping(WakeupMessage msg) {
		getContext().become(waking());
		unstashAll();
		System.out.println("I woke up!");
	}
	public void onGenericMessageWhileSleeping(GenericMessage msg) {
		stash();
		System.out.println("zzZ, stash");
	}

}
