package com.aditplanet.utils;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;


public class NotificationService{
	
	
	private static NotificationService notificationService = new NotificationService();

    private HashMap<String, Observable> observables;

    public NotificationService() 
    {
        observables = new HashMap<String, Observable>();
    }

    public static void addObserver(String notification, Observer observer) 
    {
        Observable observable = notificationService.observables.get(notification);
        if (observable==null) 
        {
            observable = new Observable();
            notificationService.observables.put(notification, observable);
        }
        observable.addObserver(observer);
    }

    public static void removeObserver(String notification, Observer observer) 
    {
        Observable observable = notificationService.observables.get(notification);
        if (observable!=null) 
        {         
            observable.deleteObserver(observer);
        }
    }       

    public static void postNotification(String notification, Object object) {
        Observable observable = notificationService.observables.get(notification);
        
        
        if (observable!=null)
        {
//            observable.setChanged();
            observable.notifyObservers(object);
        }
    }
    
//    public void notifyObservers(Object data) 
//    {
//        setChanged();
//        super.notifyObservers(data);
//    }

}
