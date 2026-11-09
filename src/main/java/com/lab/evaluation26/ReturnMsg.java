package com.lab.evaluation26;

public class ReturnMsg extends Msg{
	private int count;
	private int sum;
	
	public ReturnMsg(int count, int sum) {
		this.count = count;
		this.sum = sum;
	}
	public int getCount() {
		return this.count;
	}
	public int getSum() {
		return this.sum;
	}
	
	@Override
	public String toString() {
		return "Count: "+ count + ", Sum: "+ sum;
	}

	

}
