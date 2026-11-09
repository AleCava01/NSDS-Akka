package com.lab.evaluation26;

import akka.actor.AbstractActor;
import akka.actor.Props;


public class CounterWorkerActor extends AbstractActor {
	private final int workerId;
	
	private int count;
	private int sum;

	public CounterWorkerActor(int workerId) {
		this.workerId = workerId;
		this.count = 0;
		this.sum=0;
	}

	public static Props props(int workerId) {
		return Props.create(CounterWorkerActor.class, () -> new CounterWorkerActor(workerId));
	}

	@Override
	public Receive createReceive() {
		// TODO: Complete
		
		return receiveBuilder()
				.match(RecordMsg.class, this::processRecord)
				.match(ReportMsg.class, this::processReport)
				.build();
	}
	public void processReport(ReportMsg msg) {
		// sends the report back
		System.out.println("[WORKER + " + workerId + " - "+ getSelf() +"] Received a report request!");
		
		getSender().tell(new ReturnMsg(count, sum), getSelf());
		
		if(msg.isReset()) {
			this.count = 0;
			this.sum = 0;
			System.out.println("[WORKER] I resetted.");
		}
		
		
	}
	public void processRecord(RecordMsg msg) {
				// process record
		count++;
		sum += msg.getValue();
	}
}
