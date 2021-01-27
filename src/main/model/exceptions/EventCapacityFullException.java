package model.exceptions;

public class EventCapacityFullException extends Exception {

    public EventCapacityFullException(String eventName) {
        super("The Event: " + eventName + " is full");
    }
}
