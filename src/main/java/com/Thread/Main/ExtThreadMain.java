package com.Thread.Main;

public class ExtThreadMain {
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		try {
			Thread t = Thread.currentThread();
			System.out.println("Main Thread start");
			ExtThread obj = new ExtThread();
			obj.start();
			System.out.println("\nMainThread rename:"+t+"\nObjThread:"+obj);
			t.sleep(1000);
			System.out.println(" This is the main method ending");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}