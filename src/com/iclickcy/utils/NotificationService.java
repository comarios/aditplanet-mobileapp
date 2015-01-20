package com.iclickcy.utils;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class NotificationService extends Observable {

	private static NotificationService notificationService = null;
	private static HashMap<String, Observable> observables;

	protected NotificationService() {
		// Exists only to defeat instantiation.
	}

	public static NotificationService getInstance() {
		if (notificationService == null) {
			notificationService = new NotificationService();
			observables = new HashMap<String, Observable>();
		}
		return notificationService;
	}

	public void addObserver(String notification, Observer observer) {
		Observable observable = NotificationService.observables
				.get(notification);
		
		if (observable == null) {
			observable = new Observable();
			observable.addObserver(observer);
			NotificationService.observables.put(notification, observable);
			System.out.println("observer is null, add it");
		}
		observable.addObserver(observer);
	}

	public void removeObserver(String notification, Observer observer) {
		Observable observable = NotificationService.observables
				.get(notification);
		if (observable != null) {
			observable.deleteObserver(observer);
		}
	}

	public void postNotification(String notification, Object object) {
		Observable observable = NotificationService.observables
				.get(notification);

		if (observable != null) {

			
			//setChanged();
			//notifyObservers();
			System.out.println("post notification");
			System.out.println("hasChanged(): " + hasChanged());
			setChanged();
			notifyObservers(object);
			
		}
	}

	// public void notifyObservers(Object data)
	// {
	// setChanged();
	// super.notifyObservers(data);
	// }

}
