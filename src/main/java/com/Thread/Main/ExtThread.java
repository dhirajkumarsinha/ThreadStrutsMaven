package com.Thread.Main;

public class ExtThread extends Thread{
	
	public ExtThread() {
		super("Demo Thread");
		System.out.println("Default Constructor of ExtThread");
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run(){		
		try {
			System.out.println("Extended Thread run enter");
			Thread.currentThread().sleep(1005);
			System.out.println("Extended Thread run exit");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}

}
