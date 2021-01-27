package model;

import model.enums.Status;
import org.json.JSONObject;
import persistence.Writable;

// Represents a user who can apply to organizations and volunteer at the events
public class Volunteer implements Writable {
    public static final double REWARDS_PER_EVENT = 100;
    public static final double REWARDS_PER_HOUR = 10;
    public static final double REWARDS_PER_EVENT_EXEC = 200;
    public static final double REWARDS_PER_HOUR_EXEC = 20;

    private final String volunteerName;
    private final int volunteerAge;
    private final String volunteerCity;
    private int volunteerHoursWorked;
    private final int volunteerMaxHours;
    private int volunteerNumEvents;
    private boolean isExec;
    private Status volunteerStatus;

    // constructors:

    // REQUIRES: name must be unique
    // MODIFIES: this
    // EFFECTS: Sets the name, age, city, and maxHours of the volunteer
    public Volunteer(String volunteerName, int volunteerAge, String volunteerCity, int volunteerMaxHours) {
        this.volunteerName = volunteerName;
        this.volunteerAge = volunteerAge;
        this.volunteerCity = volunteerCity;
        this.volunteerHoursWorked = 0;
        this.volunteerMaxHours = volunteerMaxHours;
        this.isExec = false;
        this.volunteerNumEvents = 0;
        this.volunteerStatus = Status.NA;
    }

    // REQUIRES: name must be unique
    // MODIFIES: this
    // EFFECTS: Sets the name, age, city, time commitment, number of hours worked, number of events worked, isExec, and
    // status of application for the volunteer
    public Volunteer(String volunteerName, String volunteerCity, int volunteerAge, int volunteerMaxHours,
                     int volunteerHoursWorked, int volunteerNumEvents, boolean isExec, Status volunteerStatus) {
        this.volunteerName = volunteerName;
        this.volunteerAge = volunteerAge;
        this.volunteerCity = volunteerCity;
        this.volunteerHoursWorked = volunteerHoursWorked;
        this.volunteerMaxHours = volunteerMaxHours;
        this.isExec = isExec;
        this.volunteerNumEvents = volunteerNumEvents;
        this.volunteerStatus = volunteerStatus;

    }

    // getters

    // get name of volunteer
    // EFFECTS: returns the name of the volunteer
    public String getVolunteerName() {
        return this.volunteerName;
    }

    // get age of volunteer
    // EFFECTS: returns the age of the volunteer
    public int getVolunteerAge() {
        return this.volunteerAge;
    }

    // get city of volunteer
    // EFFECTS: returns the ciy of the volunteer
    public String getVolunteerCity() {
        return this.volunteerCity;
    }

    // get the number of hours the volunteer has worked in total
    // EFFECTS: returns the hours worked by volunteer in the organization
    public int getVolunteerHoursWorked() {
        return this.volunteerHoursWorked;
    }

    // get the status of the application of the volunteer
    // EFFECTS: returns status of the applicant Volunteer
    public Status getVolunteerStatus() {
        return volunteerStatus;
    }

    // get the max working duration of the volunteer
    // EFFECTS: the number of hours the volunteer can work at max
    public int getVolunteerMaxHours() {
        return this.volunteerMaxHours;
    }

    // get the total number of events
    // EFFECTS: return the number of events assigned to this volunteer
    public int getVolunteerNumEvents() {
        return this.volunteerNumEvents;
    }

    // Other Methods

    // EFFECTS: computes Total Rewards till now
    public double computeTotalRewards() {
        if (this.isExec) {
            return (volunteerNumEvents * REWARDS_PER_EVENT_EXEC + volunteerHoursWorked * REWARDS_PER_HOUR_EXEC);
        } else {
            return (volunteerNumEvents * REWARDS_PER_EVENT + volunteerHoursWorked * REWARDS_PER_HOUR);
        }
    }

    // check if member is an Executive Volunteer
    // EFFECTS: returns true if member is an executive volunteer, false otherwise
    public boolean isExec() {
        return isExec;
    }

    // EFFECTS: adds hours to hoursWorked
    public void addHoursWorked(int hours) {
        volunteerHoursWorked += hours;
    }

    // EFFECTS: returns the remaining number of hours that the volunteer can work
    public int getVolunteerRemainingHours() {
        return this.volunteerMaxHours - this.volunteerHoursWorked;
    }

    // MODIFIES: this (eventsList and numEvents)
    // EFFECTS: adds 1 to numEvents
    public void addEvent() {
        volunteerNumEvents += 1;
    }

    // MODIFIES: this
    // EFFECTS: updates the value of isExec to true in case there is a promotion
    public void promote() {
        this.isExec = true;
    }

    // MODIFIES: this ( status and organization )
    // EFFECTS: changes status of the volunteer applicant to PENDING and adds volunteer to the list of applications in
    // the applied organization
    public void apply() {
        this.volunteerStatus = Status.PENDING;
    }

    // MODIFIES: organization and this
    // EFFECTS: changes the status to ACCEPTED, and adds volunteer to organization's list of members
    public void updateStatus(Organization organization) {
        this.volunteerStatus = Status.ACCEPTED;
        organization.addMember(this);
    }

    // Reader Writer Methods

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("volunteerName", volunteerName);
        json.put("volunteerCity", volunteerCity);
        json.put("volunteerAge", volunteerAge);
        json.put("volunteerHoursWorked", volunteerHoursWorked);
        json.put("volunteerMaxHours", volunteerMaxHours);
        json.put("volunteerNumEvents", volunteerNumEvents);
        json.put("isExec", isExec);
        json.put("volunteerStatus", volunteerStatus);
        return json;
    }
}