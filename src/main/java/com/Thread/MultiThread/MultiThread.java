package com.Thread.MultiThread;

public class MultiThread implements Runnable {
	String name;
	Thread t;
	MultiThread(String threadName){
		name= threadName;
		t= new Thread(this, name);
		System.out.println("New Thread : "+t);
		
		t.start();
	}
	
	public void run(){
			try {for(int i=5;i>0;i--){
				System.out.println(name+": "+i);
				Thread.sleep(1000);
			}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
			System.out.println(name + "exiting");
	}

}
