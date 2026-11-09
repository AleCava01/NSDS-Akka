package com.lab.evaluation26;

import java.util.concurrent.TimeUnit;

import akka.actor.AbstractActor;
import akka.actor.Props;
import scala.concurrent.duration.Duration;


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
		
		ReturnMsg message = new ReturnMsg(count, sum);
		
		if(msg.isReset()) {
			this.count = 0;
			this.sum = 0;
			System.out.println("[WORKER] I resetted.");
		}
		
		// waiting logic: just one second
		int delay = 1;
		getContext().getSystem().scheduler().scheduleOnce(
		        Duration.create(delay, TimeUnit.SECONDS),
		        getSender(),
		        message,
		        getContext().dispatcher(),
		        getSelf()
		    );

		
			
	}
	public void processRecord(RecordMsg msg) {
				// process record
		count++;
		sum += msg.getValue();
	}
}
