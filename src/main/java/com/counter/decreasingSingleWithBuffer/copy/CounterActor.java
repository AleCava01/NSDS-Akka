package com.counter.decreasingSingleWithBuffer.copy;


import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithStash;
//import akka.actor.typed.javadsl.StashBuffer;


import akka.actor.Props;

public class CounterActor extends AbstractActorWithStash {
	


	private int counter;

	public CounterActor() {
		this.counter = 0;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder().match(DataMessage.class, this::onMessage).build();
	}

	void onMessage(DataMessage msg) {
		if (msg.getCode() == DataMessage.DECREASE) {
			
			if(counter-1<0) {
				// stash
				//buffer.stash(DataMessage);
				System.out.println("im stashing :)");
				stash();
				
			}
			else{
				System.out.println("I am decreasing the counter, that now is: " + (--counter));
			}		
			
		} else if (msg.getCode() == DataMessage.INCREASE) {
			System.out.println("I am increasing the counter, that now is: " + (++counter));
			if(counter>10) {
				unstashAll();
			}
			
			
		}
	}

	static Props props() {
		return Props.create(CounterActor.class);
	}

}
