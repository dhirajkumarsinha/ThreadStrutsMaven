package com.Thread.Priority;

public class PriorityMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		priority hi = new priority(Thread.MAX_PRIORITY);
		priority lo = new priority(Thread.MIN_PRIORITY);
		
		lo.start();
		hi.start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lo.stop();
		hi.stop();
		System.out.println("waiting ot child thread to terminate");
		try {
			hi.t.join();
			lo.t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("High thread: "+hi.click);
		System.out.println("Low thread: "+lo.click);
		
		
	}

}
