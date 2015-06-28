package com.Thread.InterThreadCommunication;

public class Thread2 extends Thread {

	Accoount ac;

	public Thread2(Accoount ac) {
		this.ac = ac;
	}

	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println("Rs 100 credited");
			ac.creditAccount(100);
		}
	}

}
