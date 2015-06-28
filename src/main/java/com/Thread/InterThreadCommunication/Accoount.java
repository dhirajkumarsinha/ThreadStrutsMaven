package com.Thread.InterThreadCommunication;

public class Accoount {
	int balance;

	public synchronized void debitAccount(int amount) {

		System.out.println("Checking balance");
		while ((balance < amount)) {
			System.out.println("Going to wait");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Debeting...");
		balance = balance - amount;

		System.out.println("Balance after debeting : " + balance);

	}

	public synchronized void creditAccount(int amount) {
		balance+=amount;
		System.out.println("Balance = "+balance+"notify");
		notify();
	}

}
