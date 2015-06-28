package com.Thread.InterThreadCommunication;

public class Accoount {
	int balance;

	public synchronized void debitAccount(int amount) {
		try {

			System.out.println("Checking balance");
			while ((balance < amount)) {
				System.out.println("Going to wait");
				System.out.println("Going to wait1");
				wait();
				System.out.println("Going to wait2");
			}

			System.out.println("Debeting...");
			balance = balance - amount;

			System.out.println("Balance after debeting : " + balance);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public synchronized void creditAccount(int amount) {
		balance += amount;
		System.out.println("Balance = " + balance + "notify");
		notify();
	}

}
