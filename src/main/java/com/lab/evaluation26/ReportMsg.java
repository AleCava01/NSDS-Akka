package com.lab.evaluation26;

import akka.actor.ActorRef;

public final class ReportMsg extends Msg {
    private final boolean reset;
    private ActorRef sender;

    public ReportMsg(boolean reset) { this.reset = reset; }
    public ReportMsg(boolean reset, ActorRef sender) { this.reset = reset; this.sender = sender;}

    public boolean isReset() { return reset; }
    
    public ActorRef getSender() {return sender;}

    @Override
    public String toString() { return "ReportMsg(reset=" + reset + ")"; }
}
