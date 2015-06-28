package com.Thread.InterThreadCommunication;

public class Thread1 extends Thread{
	
	Accoount ac;
	
	public Thread1(Accoount ac) {
		this.ac = ac;
	}
	
	@Override
	public void run() {
		ac.debitAccount(300);
	}

}
