package model;

import model.enums.Purpose;
import model.enums.Status;
import model.exceptions.NotEnoughTimeException;
import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// Represents a Non Profit Organization created by a founder user
public class Organization implements Writable {
    private final String organizationName;
    private final Purpose organizationPurpose;
    private final String organizationCity;
    private final int organizationMinHours;
    private long organizationFund;
    private final LinkedList<Event> organizationEvents;
    private final LinkedList<Volunteer> organizationApplications;
    private final LinkedList<Volunteer> organizationMembers;

    // constructors

    // REQUIRES: organizationName must be unique
    // MODIFIES: this
    // EFFECTS: Sets the organizationName, organizationCity, founder, starting investment, minimum time commitment and
    // the purpose of the organization and adds a defaultExecutive to the list of members
    public Organization(String organizationName, String organizationCity, long investment,
                        int organizationMinHours, Purpose organizationPurpose) {
        this.organizationName = organizationName;
        this.organizationCity = organizationCity;
        this.organizationMembers = new LinkedList<>();
        this.organizationEvents = new LinkedList<>();
        this.organizationFund = investment;
        this.organizationMinHours = organizationMinHours;
        this.organizationPurpose = organizationPurpose;
        this.organizationApplications = new LinkedList<>();

        Volunteer defaultExecutive = new Volunteer("DefaultVolunteer", organizationCity, 25,
                100, 50, 0, true, Status.ACCEPTED);
        this.organizationMembers.add(defaultExecutive);

    }

    // REQUIRES: organizationName must be unique
    // MODIFIES: this
    // EFFECTS: Sets the organizationName, organizationCity, founder, starting investment, minimum time commitment,
    // the purpose, members, events and applications of the organization
    public Organization(String organizationName, String organizationCity, Purpose organizationPurpose,
                        int organizationMinHours, long fund, LinkedList<Volunteer> organizationMembers,
                        LinkedList<Event> organizationEvents, LinkedList<Volunteer> organizationApplications) {
        this.organizationName = organizationName;
        this.organizationCity = organizationCity;
        this.organizationMinHours = organizationMinHours;
        this.organizationPurpose = organizationPurpose;
        this.organizationFund = fund;
        this.organizationMembers = organizationMembers;
        this.organizationEvents = organizationEvents;
        this.organizationApplications = organizationApplications;

    }

    // getters

    // EFFECTS: returns the name of the organization
    public String getOrganizationName() {
        return organizationName;
    }

    // EFFECTS: returns the purpose of the organization
    public Purpose getOrganizationPurpose() {
        return organizationPurpose;
    }

    // EFFECTS: returns the city of the organization taken from the founder
    public String getOrganizationCity() {
        return organizationCity;
    }

    // EFFECTS: returns a list of the volunteers who have applied to the organization of the fonder
    public LinkedList<Volunteer> getOrganizationApplications() {
        return organizationApplications;
    }

    // EFFECTS: returns the list of all the members of the organization
    public LinkedList<Volunteer> getOrganizationMembers() {
        return organizationMembers;
    }

    // EFFECTS: returns a list of all the events created in he organization
    public LinkedList<Event> getOrganizationEvents() {
        return organizationEvents;
    }

    // EFFECTS: returns the total funds left in the organization
    public long getOrganizationFunds() {
        return organizationFund;
    }

    // EFFECTS: returns the minimum time commitment required for joining the organization
    public int getOrganizationMinHours() {
        return organizationMinHours;
    }

    // EFFECTS: returns a list of the executive members or volunteers in the organization
    public LinkedList<Volunteer> getFilteredExecutives(Boolean isExec) {
        LinkedList<Volunteer> execList = new LinkedList<>();
        LinkedList<Volunteer> volunteerList = new LinkedList<>();
        for (Volunteer volunteer : organizationMembers) {
            if (volunteer.isExec()) {
                execList.add(volunteer);
            } else {
                volunteerList.add(volunteer);
            }
        }
        return isExec ? execList : volunteerList;
    }

    // Other Methods

    // REQUIRES: the created event should be unique
    // MODIFIES: this
    // EFFECTS: adds the given event to the list of events in the organization
    public void addEvent(Event event) {
        organizationEvents.add(event);
        this.organizationFund = this.organizationFund - 500;
    }

    // REQUIRES: member must be in the Organization's applicants
    // MODIFIES: this
    // EFFECTS: adds the given volunteer/executive to members.
    public void addMember(Volunteer volunteer) {
        organizationMembers.remove(volunteer);
        organizationMembers.add(volunteer);
        if (organizationApplications.contains(volunteer)) {
            this.organizationApplications.remove(volunteer);
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the given volunteer/executive to members.
    //          throws NotEnoughTimeException if volunteer.volunteerMaxHours < organizationMinHours
    public void addApplicant(Volunteer volunteer) throws NotEnoughTimeException {
        if (volunteer.getVolunteerMaxHours() < organizationMinHours) {
            throw new NotEnoughTimeException(volunteer.getVolunteerName());
        } else {
            organizationApplications.remove(volunteer);
            organizationApplications.add(volunteer);
        }
    }

    // Reader Writer Methods

    // EFFECTS: returns the volunteer members in this organization as a JSON array
    private JSONArray organizationMembersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Volunteer volunteer : organizationMembers) {
            jsonArray.put(volunteer.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns events in this organization as a JSON array
    private JSONArray organizationEventsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Event event : organizationEvents) {
            jsonArray.put(event.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns applications in this organization as a JSON array
    private JSONArray organizationApplicationsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Volunteer applicant : organizationApplications) {
            jsonArray.put(applicant.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("organizationName", organizationName);
        json.put("organizationCity", organizationCity);
        json.put("organizationPurpose", organizationPurpose);
        json.put("organizationMinHours", organizationMinHours);
        json.put("organizationFund", organizationFund);
        json.put("organizationMembers", organizationMembersToJson());
        json.put("organizationEvents", organizationEventsToJson());
        json.put("organizationApplications", organizationApplicationsToJson());
        return json;
    }
}