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
     * Adds a EventListener which subscribes to a specific type of Event.
     * @param eventType the type of the Event which the listener will handle.
     * @param listener the EventListener to be added.
     */
    public void addEventListener(EventType eventType, EventListener listener) {
        listeners.get(eventType).add(listener);
    }

    /**
     * Clears all EventListeners recorded in the listeners HashMap.
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