package com.Thread.MultiThread;

public class MultiThreadMain {
	public static void main(String[] args) {
		
		
		try {
			new MultiThread("one");
			new MultiThread("two");
			new MultiThread("three");
			
			Thread.sleep(1000); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("main thread exiting");
	}
}