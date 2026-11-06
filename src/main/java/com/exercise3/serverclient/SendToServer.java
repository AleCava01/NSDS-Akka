
package com.exercise3.serverclient;
import java.util.Optional;

public class SendToServer {
	
	public static final int ASK_CREATE_ACCOUNT = 0;
	public static final int ASK_EMAIL = 1;
	
	public int type;
	public Optional<String> email;
	public String name;
	
	public SendToServer(int type, String email, String name) {
		this.type = type;
		this.email = Optional.ofNullable(email);
		this.name = name;
	}
	

}

