package main.java.eventbus;

import com.google.common.eventbus.EventBus;


public class EventBusCenter {
    private static EventBus eventBus = new EventBus();

    private EventBusCenter(){}

    public static void register(Object obj) {
        eventBus.register(obj);
    }

    public static void unregister(Object obj) {
        eventBus.unregister(obj);
    }

    public static void post(Object obj) {
        eventBus.post(obj);
    }
}
