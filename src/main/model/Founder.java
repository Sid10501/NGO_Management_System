package model;

import com.sun.org.apache.xpath.internal.operations.Or;
import model.enums.Purpose;
import model.exceptions.EventCapacityFullException;
import model.exceptions.NotEnoughFundsException;
import model.exceptions.NotPromotableException;
import persistence.Writable;
import org.json.JSONObject;

import java.util.LinkedList;

// Represents a user who can create an organization, accept applications, create events, add volunteers to events,
// assign executives and promote volunteers
public class Founder implements Writable {
    private final String founderName;
    private final int founderAge;
    private final String founderCity;
    private Organization founderOrganization;
    private final long founderInvestment;

    // constructors

    // REQUIRES: name must be unique
    // MODIFIES: this
    // EFFECTS: A Founder with the given name, age, city and investment
    public Founder(String name, int age, String city, int founderInvestment) {
        this.founderName = name;
        this.founderAge = age;
        this.founderCity = city;
        this.founderInvestment = founderInvestment;
        this.founderOrganization = new Organization("New Organization", "City", Purpose.ANIMALS, 10,
                2500, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
    }

    // REQUIRES: name must be unique
    // MODIFIES: this
    // EFFECTS: A Founder with the given founderName, founderAge, founderCity, founderInvestment and founderOrganization
    public Founder(String founderName, int founderAge, String founderCity, int founderInvestment,
                   Organization founderOrganization) {
        this.founderName = founderName;
        this.founderAge = founderAge;
        this.founderCity = founderCity;
        this.founderInvestment = founderInvestment;
        this.founderOrganization = founderOrganization;
    }

    // getters

    // get name of founder
    // EFFECTS: returns the name of the founder
    public String getFounderName() {
        return this.founderName;
    }

    // get age of founder
    // EFFECTS: returns the age of the founder
    public int getFounderAge() {
        return this.founderAge;
    }

    // get city of founder
    // EFFECTS: returns the ciy of the founder
    public String getFounderCity() {
        return this.founderCity;
    }

    // get the organization of the founder
    // EFFECTS: returns the organization the volunteer is part of
    public Organization getFounderOrganization() {
        return founderOrganization;
    }

    // get the investment of the founder
    // EFFECTS: returns the investment of the founder which is assigned to the funds of the organization
    public long getFounderInvestment() {
        return this.founderInvestment;
    }

    // Other Methods

    // REQUIRES: the organization field name should be unique
    // MODIFIES: this and organization
    // EFFECTS: creates a new organization with the given name, city, minimum hours, purpose and assigns it to
    // the organization field of the founder, it also returns the organization
    public void setFounderOrganization(Organization founderOrganization) {
        this.founderOrganization = founderOrganization;
    }

    // MODIFIES: volunteer
    // EFFECTS: accepts the applicant and changes the status of the volunteer to ACCEPTED.
    public void acceptApplication(Volunteer volunteer) {
        volunteer.updateStatus(founderOrganization);
        this.founderOrganization.getOrganizationApplications().remove(volunteer);
    }

    // MODIFIES: volunteer and founderOrganization
    // EFFECTS: promotes the input volunteer to Executive Volunteer
    //          throws NotPromotableException if volunteer's working hours < 5
    public void promoteVolunteer(Volunteer volunteer) throws NotPromotableException {
        if (volunteer.getVolunteerHoursWorked() >= 5) {
            volunteer.promote();
        } else {
            throw new NotPromotableException(volunteer.getVolunteerName());
        }
    }

    // MODIFIES: this and event
    // EFFECTS: takes the given event and passes it as a parameter to a method of the organization of the founder which
    //          adds the event to the list of events in the founder's organization
    //          throws NotEnoughFundsException if remaining funds < 500
    public void createEvent(Event newEvent) throws NotEnoughFundsException {
        if (getFounderOrganization().getOrganizationFunds() >= 500) {
            founderOrganization.addEvent(newEvent);
        } else {
            throw new NotEnoughFundsException(founderOrganization.getOrganizationName());
        }
    }

    // MODIFIES: event and volunteer
    // EFFECTS: adds given volunteer to event team, also adds the event to the volunteer's set of events
    //          throws EventCapacityFullException if number of eventVolunteers.size() == numVolunteers
    public void addVolunteerToEvent(Volunteer volunteer, Event event) throws EventCapacityFullException {
        if (event.getEventVolunteers().size() != event.getNumVolunteers()) {
            event.addVolunteer(volunteer);
            volunteer.addEvent();
        } else {
            throw new EventCapacityFullException(event.getEventName());
        }
    }
    // Reader Writer Methods

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("founderName", founderName);
        json.put("founderCity", founderCity);
        json.put("founderAge", founderAge);
        json.put("founderInvestment", founderInvestment);
        json.put("founderOrganization", founderOrganization.toJson());
        return json;
    }
}