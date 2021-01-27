package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

// Represents the basic structure of a Database that has a name, and consists of a list of all organizations, founders
// as well as the volunteers
public class Database implements Writable {
    private final String databaseName;
    private final LinkedList<Organization> organizations;
    private final LinkedList<Founder> founders;
    private final LinkedList<Volunteer> volunteers;

    // constructor

    // REQUIRES: name must be unique
    // MODIFIES: this
    // EFFECTS: A Database with the given name, an empty list of organizations, founders, and volunteers
    public Database(String name) {
        this.databaseName = name;
        this.organizations = new LinkedList<>();
        this.founders = new LinkedList<>();
        this.volunteers = new LinkedList<>();
    }
//test
    // getters

    //EFFECTS: returns the databaseName of the database
    public String getDatabaseName() {
        return databaseName;
    }

    // EFFECTS: returns the list of all organizations in the database
    public LinkedList<Organization> getOrganizations() {
        return organizations;
    }

    // EFFECTS: returns the list of all the founders in the database
    public LinkedList<Founder> getFounders() {
        return founders;
    }

    // EFFECTS: returns the list of all the volunteers in the database
    public LinkedList<Volunteer> getVolunteers() {
        return volunteers;
    }

    // EFFECTS: returns the number of organizations in the database's list of organizations
    public int numOrganizations() {
        return organizations.size();
    }

    // EFFECTS: returns the number of founders in the database's list of founders
    public int numFounders() {
        return founders.size();
    }

    // EFFECTS: returns the number of volunteers in the database's list of volunteers
    public int numVolunteers() {
        return volunteers.size();
    }

    // Other Methods

    // EFFECTS: adds the given Volunteer to the database's list of volunteers
    public void addVolunteer(Volunteer newVolunteer) {
        volunteers.add(newVolunteer);
    }

    // EFFECTS: adds the given Founder to the database's list of founders
    public void addFounder(Founder newFounder) {
        founders.add(newFounder);
    }

    // EFFECTS: adds the given Organization to the database's list of organizations
    public void addOrganization(Organization newOrganization) {
        organizations.add(newOrganization);
    }

    public Volunteer getVolunteer(String volunteerName) {
        for (Volunteer volunteer : getVolunteers()) {
            if (volunteer.getVolunteerName().equals(volunteerName)) {
                return volunteer;
            }
        }
        return null;
    }

    public Founder getFounder(String founderName) {
        for (Founder founder : getFounders()) {
            if (founder.getFounderName().equals(founderName.trim())) {
                return founder;
            }
        }
        return null;
    }

    public Organization getOrganization(String organizationName) {
        for (Organization organization : getOrganizations()) {
            if (organization.getOrganizationName().equals(organizationName)) {
                return organization;
            }
        }
        return null;
    }


//    public Organization getFounderOrganization(Founder founder) {
//        for (Organization organization : getOrganizations()) {
//            if (organization.getOrganizationName().equals(founder.getFounderOrganization())) {
//                return organization;
//            }
//        }
//        return null;
//    }
//
//    public Organization getVolunteerOrganization(Volunteer volunteer) {
//        for (Organization organization : getOrganizations()) {
//            if (organization.getOrganizationMembers().contains(volunteer)) {
//                return organization;
//            }
//        }
//        System.out.println("no");
//        return null;
//    }



    // Reader Writer Methods

    // EFFECTS: returns founders in this database as a JSON array
    private JSONArray foundersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Founder founder : founders) {
            jsonArray.put(founder.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns founders in this database as a JSON array
    private JSONArray volunteersToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Volunteer volunteer : volunteers) {
            jsonArray.put(volunteer.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns founders in this database as a JSON array
    private JSONArray organizationsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Organization organization : organizations) {
            jsonArray.put(organization.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("databaseName", databaseName);
        json.put("founders", foundersToJson());
        json.put("volunteers", volunteersToJson());
        json.put("organizations", organizationsToJson());
        return json;
    }
}