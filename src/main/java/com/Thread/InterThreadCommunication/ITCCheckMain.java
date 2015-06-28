package com.Thread.InterThreadCommunication;

public class ITCCheckMain {

	public static void main(String[] args) {
		Accoount ac = new Accoount();
		Thread1 t1 = new Thread1(ac);
		Thread2 t2 = new Thread2(ac);
		t1.start();
		t2.start();

	}

}
