package com.exercise3.serverclient;

import java.io.IOException;

import com.exercise3.serverclient.ClientActor;
import com.exercise3.serverclient.ServerActor;
import com.exercise3.serverclient.SendToServer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Exercise3 {
	public static void main(String[] args) {
		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef server = sys.actorOf(ServerActor.props(), "server");
		final ActorRef client = sys.actorOf(ClientActor.props(server), "client");

		SendToServer first_account = new SendToServer(SendToServer.ASK_CREATE_ACCOUNT, 
														"alessandro@gmail.com", 
														"Alessandro");
		SendToServer santiago_account = new SendToServer(SendToServer.ASK_CREATE_ACCOUNT, 
														"santiago@gmail.com", 
														"Santiago");
		SendToServer anas_account = new SendToServer(SendToServer.ASK_CREATE_ACCOUNT, 
				"anas@gmail.com", 
				"Anas");
		SendToServer ask_account = new SendToServer(SendToServer.ASK_EMAIL,
									"",
									"Alessandro");
		
		client.tell(first_account, server);
		client.tell(santiago_account, server);
		client.tell(anas_account, server);
		client.tell(ask_account, server);

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sys.terminate();
	
	}

}
