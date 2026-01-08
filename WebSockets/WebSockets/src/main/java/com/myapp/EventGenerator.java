package com.myapp;

import java.util.HashSet;
import java.util.Set;

public class EventGenerator extends Thread {

	private static Set<Observer> observers = new HashSet<>();

	public static synchronized void registry(Observer observer) {
		observers.add(observer);
	}

	public static synchronized void unregistry(Observer observer) {
		observers.remove(observer);
	}

	private static synchronized void broadcast() {
		Event event = Event.buildNextRandomically();
		for (Observer observer : observers) {
			observer.notify(event);
		}
	}

	@Override
	public void run() {
		while (!isInterrupted()) {
			try {
				Thread.sleep(5000);

				broadcast();

			} catch (InterruptedException e) {
				interrupt();
				e.printStackTrace();
			}
		}
	}

}
