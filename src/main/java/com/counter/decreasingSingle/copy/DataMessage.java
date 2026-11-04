package com.counter.decreasingSingle.copy;

public class DataMessage {
	
	public static final int DECREASE = 0;
	public static final int INCREASE = 1;

	private int code;
	
	public int getCode() {
		return code;
	}

	public DataMessage(int code) {
		this.code = code;
	}
	
}
