package com.lab.evaluation26;


import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeoutException;

import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;

public class CounterCoordinatorActor extends AbstractActorWithStash {
	private final Props worker0Props;
	private final Props worker1Props;
	private final ActorRef[] workers = new ActorRef[2];
	private scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(50000,SECONDS);

	
	public CounterCoordinatorActor(Props worker0Props, Props worker1Props) {
		this.worker0Props = worker0Props;
		this.worker1Props = worker1Props;
	}

	public static Props props() {
		return props(CounterWorkerActor.props(0), CounterWorkerActor.props(1));
	}

	public static Props props(Props worker0Props, Props worker1Props) {
		return Props.create(CounterCoordinatorActor.class,
				() -> new CounterCoordinatorActor(worker0Props, worker1Props));
	}

	@Override
	public void preStart() {
		// Create the two workers used by this coordinator
		workers[0] = getContext().actorOf(worker0Props, "worker0");
		workers[1] = getContext().actorOf(worker1Props, "worker1");
	}

	@Override
	public Receive createReceive() {
		// TODO: Complete
		return awake();
	}
	
	final Receive sleep() {
		return receiveBuilder()
				.match(RecordMsg.class, this::onRecordMsgWhileSleeping)
				.match(ReportMsg.class, this::onReportRequestWhileSleeping)
				.build();
	}
	
	private void onRecordMsgWhileSleeping(RecordMsg msg) {
		stash();
	}
	private void onReportRequestWhileSleeping(ReportMsg msg) {
		stash();
	}

	final Receive awake() {
		return receiveBuilder()
				.match(RecordMsg.class, this::onRecordMsg)
				.match(ReportMsg.class, this::onReportRequest)
				.build();
	}
	
	private void onRecordMsg(RecordMsg msg) {
		workers[msg.getKey()%2].tell(msg, getSelf());
		System.out.println("[COORDINATOR] Received a request from client, forwarding to worker "+ msg.getKey()%2 + " - "+ workers[msg.getKey()%2]);
	}
	private void onReportRequest(ReportMsg msg) {
		getContext().become(sleep());
		
		// ask pattern for worker 0
		
		ReturnMsg reply_w0 = null;
		ReturnMsg reply_w1 = null;
		
		scala.concurrent.Future<Object> waitingForReplyW0 = ask(workers[0], msg, 500000);
		try {
			reply_w0 = (ReturnMsg) waitingForReplyW0.result(timeout, null);
		} catch (TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}
		
		// ask pattern for worker 1
		scala.concurrent.Future<Object> waitingForReplyW1 = ask(workers[1], msg, 500000);
		try {
			reply_w1 = (ReturnMsg) waitingForReplyW1.result(timeout, null);
		} catch (TimeoutException | InterruptedException e) {
			e.printStackTrace();
		}
		
		ReturnMsg combined_reply = new ReturnMsg(reply_w0.getCount()+reply_w1.getCount(), reply_w0.getSum()+reply_w1.getSum());
		
		getSender().tell(combined_reply, getSelf()); // sends combined result back to client
		
		unstashAll();
		getContext().become(awake());


	}
}
