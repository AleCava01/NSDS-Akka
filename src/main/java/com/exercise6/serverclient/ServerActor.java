package com.exercise6.serverclient;

import akka.actor.AbstractActorWithStash;
import akka.actor.Props;
import scala.concurrent.duration.Duration;
import java.util.concurrent.TimeUnit;
import akka.actor.ActorRef;

public class ServerActor extends AbstractActorWithStash {
	
	public ServerActor() {
	}
	static Props props() {
		return Props.create(ServerActor.class);
	}

	@Override
	public Receive createReceive() {
		return ready();
		
	}
	
	final Receive busy() {
		return receiveBuilder().match(Request.class, this::onRequestWhileBusy)
				.match(Finished.class, this::onFinished)
				.build();
	}
	
	final Receive ready() {
		return receiveBuilder().match(Request.class, this::onRequestWhileReady)
				.build();
	}
	
	public void onFinished(Finished finished) {
		
		finished.client.tell(
		        new Reply(finished.text),
		        getSelf()
		    );

		getContext().become(ready());
		unstashAll();
	}
	
	public void onRequestWhileBusy(Request request) {
		
		stash();
		
	}
	public void onRequestWhileReady(Request request) {
		String uppercasedText = request.text.toUpperCase();
		ActorRef client = getSender();
		
		getContext().become(busy());
		
		int randomDelay = (int)(Math.random() * 4); // random from 0 to 4 seconds
		getContext().getSystem().scheduler().scheduleOnce(
		        Duration.create(randomDelay, TimeUnit.SECONDS),
		        getSelf(),
		        new Finished(client, uppercasedText),
		        getContext().dispatcher(),
		        getSelf()
		    );

	}

}
