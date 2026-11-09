# Evaluation lab - Akka

## Group number: 13

## Group members

- Alessandro Tobia Cavalieri - 10712628
- Anas Hamed Ahmed Hamed - 11116933
- Santiago Arevalo Gomez - 11086763

## Description of message flows

- The main, DistributedCounters.java sends a StartMsg to CounterClientActor (s).
- CounterClientActor sends two types of message to CounterCoordinatorActor:
	- RecordMsg, that contains key and value. -> tell pattern
	- ReportMsg, that contains a flag for resetting workers. -> ask pattern
- CounterCoordinatorActor starts on awake() receiving two type of messages.
	- in awake state, receives a RecordMsg and forward it to the corresponding CounterWorkerActor key%2
	- in awake state, receives a ReportMsg, goes in sleep state and buffers the incoming messages, in the meantime implements the ask pattern toward the two workers, forwarding ReportMsg and combining the replies, then eventually sends back to CounterClientActor. at the end, it unstashAll() and returns awake.
	- in sleep state, it stash()
- CounterWorkersActor, waits 1 seconds delay to emulate processing time.
	- receiving RecordMsg: increase the count and add value to sum
	- receiving ReportMsg: respond to coordinator's ask. Reset the values if the flag is true.
	
	