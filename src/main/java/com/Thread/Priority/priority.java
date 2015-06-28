package com.Thread.Priority;

public class priority implements Runnable{
	long click = 0;
	Thread t;
	private volatile boolean running = true;
	
	public priority(int p){
		t= new Thread(this);
		t.setPriority(p);
	}
	public void start(){
		t.start();
	}
	
	public void stop(){
	running= false;
	}
	public void run(){
		while(running){
			click++;
		}
	}

}
