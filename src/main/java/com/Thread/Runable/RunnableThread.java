package com.Thread.Runable;

public class RunnableThread implements Runnable {

	public RunnableThread() {
		System.out.println("Default Constructor of Runnable");
	}

	public void run() {
		System.out.println("Runnable Thread run enter");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Runnable Thread run exit");
	}

}
