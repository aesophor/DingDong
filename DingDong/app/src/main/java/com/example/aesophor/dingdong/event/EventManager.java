package com.example.aesophor.dingdong.event;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class EventManager {

    private static EventManager eventManager;

    private final Map<EventType, List<EventListener>> listeners;

    private EventManager() {
        listeners = new HashMap<>();

        // List of handlers for events of user login/logout.
        listeners.put(EventType.LOGIN, new ArrayList<EventListener>());
        listeners.put(EventType.LOGOUT, new ArrayList<EventListener>());

        // List of handlers for events of new messages arrival.
        listeners.put(EventType.NEW_MESSAGE, new ArrayList<EventListener>());
    }

    public static EventManager getInstance() {
        if (eventManager == null) {
            eventManager = new EventManager();
        }

        return eventManager;
    }


    /**
     * Adds a EventHandler which subscribes to a specific type of Event.
     * @param eventType the type of the Event which the handler will handle.
     * @param handler the EventHandler to be added.
     */
    public void addEventHandler(EventType eventType, EventListener handler) {
        listeners.get(eventType).add(handler);
    }

    /**
     * Clears all EventHandlers recorded in the handlers HashMap.
     * This should be called upon user logging out.
     */
    public void clearEventHandlers() {
        for (List<EventListener> handlerList : listeners.values()) {
            handlerList.clear();
        }
    }

    /**
     * Fires the specified Events to all of its EventHandlers.
     * @param event the Event to fire.
     */
    public void fireEvent(Event event) {
        EventType eventType = event.getEventType();

        for (EventListener listener : listeners.get(eventType)) {
            listener.handle(event);
        }
    }

}