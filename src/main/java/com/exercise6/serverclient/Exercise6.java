package com.exercise6.serverclient;

import java.io.IOException;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import java.util.Scanner;

public class Exercise6{

	public static void main(String[] args) {
		final ActorSystem sys = ActorSystem.create("System");
		final ActorRef server = sys.actorOf(ServerActor.props(), "server");

		final ActorRef client = sys.actorOf(ClientActor.props(server), "client");
		
		String message;
		Scanner scanner = new Scanner(System.in);
		do {
			System.out.print("Send a request from client to server: ");
			message = scanner.nextLine();
			if(message.equals("exit")) {
				break;
			}
			System.out.println();
			client.tell(new WhatToSend(message, server), ActorRef.noSender());
		}while(true);

		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		sys.terminate();
	
	}
}