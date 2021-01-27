package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// Represents an event created by a founder in an organization for the organization volunteers to be assigned to it
public class Event implements Writable {
    private final String eventName;
    private Volunteer eventExecutive;
    private final int eventDuration;
    private final int numVolunteers;
    private final LinkedList<Volunteer> eventVolunteers;

    // constructors

    // EFFECTS: an Event with the given name, executive volunteer, duration, maximum volunteers, and
    // an empty list of Volunteers
    public Event(String name, int eventDuration, int numVolunteers) {
        this.eventDuration = eventDuration;
        this.eventName = name;
        this.numVolunteers = numVolunteers;
        this.eventVolunteers = new LinkedList<>();
    }

    // EFFECTS: an Event with the given name, executive volunteer, duration, maximum volunteers, and a list of
    // volunteers assigned
    public Event(String eventName, Volunteer eventExecutive, int eventDuration, int numVolunteers,
                 LinkedList<Volunteer> eventVolunteers) {
        this.eventDuration = eventDuration;
        this.eventName = eventName;
        this.numVolunteers = numVolunteers;
        this.eventVolunteers = eventVolunteers;
        this.eventExecutive = eventExecutive;
    }

    //getters

    // get name of event
    // EFFECTS: returns the name of the event
    public String getEventName() {
        return eventName;
    }

    // get the executive volunteer responsible for the event
    // EFFECTS: returns the Executive Volunteer Member assigned to the event by the Founder
    public Volunteer getEventExecutive() {
        return eventExecutive;
    }

    // get the duration of the event
    // EFFECTS: returns the number of hours required for the event
    public int getEventDuration() {
        return eventDuration;
    }

    // get the the max number of volunteers assignable to the event
    // EFFECTS: returns the number of Volunteers
    public int getNumVolunteers() {
        return numVolunteers;
    }

    // get the list of all the volunteers assigned o this event
    // EFFECTS: returns a LinkedList of the volunteers
    public LinkedList<Volunteer> getEventVolunteers() {
        return eventVolunteers;
    }

    // Other Methods

    // REQUIRES: the executive isn't already set for this event
    // MODIFIES: this
    // EFFECTS: sets the executive of the event to the given executive.
    public void setEventExecutive(Volunteer eventExecutive) {
        this.eventExecutive = eventExecutive;
        eventExecutive.addEvent();
        eventExecutive.addHoursWorked(eventDuration);
    }

    // REQUIRES: the eventVolunteer is not an executive
    // MODIFIES: this, eventVolunteer
    // EFFECTS: adds the given eventVolunteer to the event and updates the number of hours worked by the eventVolunteer
    public void addVolunteer(Volunteer eventVolunteer) {
        eventVolunteers.add(eventVolunteer);
        eventVolunteer.addHoursWorked(eventDuration);
    }

    // Reader Writer Methods

    // EFFECTS: returns the volunteers in this event as a JSON array
    private JSONArray eventVolunteersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Volunteer volunteer : eventVolunteers) {
            jsonArray.put(volunteer.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("eventName", eventName);
        json.put("eventExecutive", eventExecutive.toJson());
        json.put("eventDuration", eventDuration);
        json.put("eventNumVolunteers", numVolunteers);
        json.put("eventVolunteers", eventVolunteersToJson());
        return json;
    }
}

