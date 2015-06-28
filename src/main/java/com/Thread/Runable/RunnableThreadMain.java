package com.Thread.Runable;

public class RunnableThreadMain {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		
		
		try {
			Thread currentThread = Thread.currentThread();
			System.out.println("Main Thread start");
			RunnableThread obj = new RunnableThread();
			Thread t = new Thread(obj);// Runnable thread method for whose run method to be executed
			t.start();
			//System.out.println("\nMainThread:"+t+"\nObjThread:"+obj);
			currentThread.sleep(1000);
			//currentThread.notify();
			System.out.println(" This is the main method ending");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("main thread exiting");
	}
}