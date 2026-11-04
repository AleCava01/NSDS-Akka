package com.counter.decreasing2;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class CounterActor extends AbstractActor {

	private int counter;

	public CounterActor() {
		this.counter = 0;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(SimpleMessage.class, this::onIncreaseMessage)
								.match(OtherMessage.class, this::onDecreaseMessage).build();
	}

	void onIncreaseMessage(SimpleMessage msg) {
		++counter;
		System.out.println("Counter increased to " + counter);
	}
	void onDecreaseMessage(OtherMessage msg) {
		--counter;
		System.out.println("Received other type of message!");
	}
	static Props props() {
		return Props.create(CounterActor.class);
	}

}
