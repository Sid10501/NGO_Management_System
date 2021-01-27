package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.stream.Stream;

import model.enums.Purpose;
import model.enums.Status;
import org.json.*;

// Represents a reader that reads database from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads database from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Database read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDatabase(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }

    // EFFECTS: parses database from JSON object and returns it
    private Database parseDatabase(JSONObject jsonObject) {
        String name = jsonObject.getString("databaseName");
        Database database = new Database(name);
        addOrganizations(database, jsonObject);
        addFounders(database, jsonObject);
        addVolunteers(database, jsonObject);
        return database;
    }

    // EFFECTS: parses founder from JSON object and returns it
    private Founder parseFounder(JSONObject jsonObject) {
        String founderName = jsonObject.getString("founderName");
        String founderCity = jsonObject.getString("founderCity");
        int founderAge = jsonObject.getInt("founderAge");
        int founderInvestment = jsonObject.getInt("founderInvestment");

        Organization founderOrganization = parseOrganization(jsonObject.getJSONObject("founderOrganization"));
        return new Founder(founderName, founderAge, founderCity, founderInvestment, founderOrganization);
    }

    // EFFECTS: parses applications from JSON Array and returns a linked list of applications
    private LinkedList<Volunteer> parseApplications(JSONArray founderApplications) {
        LinkedList<Volunteer> applications = new LinkedList<>();
        for (Object json : founderApplications) {
            applications.add(parseVolunteer((JSONObject) json));
        }
        return applications;
    }

    // EFFECTS: parses organization from JSON object and returns it
    private Organization parseOrganization(JSONObject jsonObject) {
        String organizationName = jsonObject.getString("organizationName");
        String organizationCity = jsonObject.getString("organizationCity");
        Purpose organizationPurpose = Purpose.valueOf(jsonObject.getString("organizationPurpose"));
        int organizationMinHours = jsonObject.getInt("organizationMinHours");
        int organizationFund = jsonObject.getInt("organizationFund");

        LinkedList<Volunteer> organizationApplications =
                parseApplications(jsonObject.getJSONArray("organizationApplications"));
        LinkedList<Volunteer> organizationMembers = parseVolunteers(jsonObject.getJSONArray("organizationMembers"));
        LinkedList<Event> organizationEvents = parseEvents(jsonObject.getJSONArray("organizationEvents"));
        return new Organization(organizationName, organizationCity, organizationPurpose, organizationMinHours,
                organizationFund, organizationMembers, organizationEvents, organizationApplications);
    }

    // EFFECTS: parses volunteers from JSON Array and returns a linked list of volunteers
    private LinkedList<Volunteer> parseVolunteers(JSONArray organizationMembers) {
        LinkedList<Volunteer> volunteers = new LinkedList<>();
        for (Object json : organizationMembers) {
            volunteers.add(parseVolunteer((JSONObject) json));
        }
        return volunteers;
    }

    // EFFECTS: parses volunteer from JSON object and returns it
    private Volunteer parseVolunteer(JSONObject jsonObject) {
        String volunteerName = jsonObject.getString("volunteerName");
        String volunteerCity = jsonObject.getString("volunteerCity");
        int volunteerAge = jsonObject.getInt("volunteerAge");
        int volunteerHoursWorked = jsonObject.getInt("volunteerHoursWorked");
        int volunteerMaxHours = jsonObject.getInt("volunteerMaxHours");
        int volunteerNumEvents = jsonObject.getInt("volunteerNumEvents");
        boolean isExec = jsonObject.getBoolean("isExec");
        Status volunteerStatus = Status.valueOf(jsonObject.getString("volunteerStatus"));

        return new Volunteer(volunteerName, volunteerCity, volunteerAge, volunteerMaxHours, volunteerHoursWorked,
                volunteerNumEvents, isExec, volunteerStatus);
    }

    // EFFECTS: parses events from JSON Array and returns a linked list of events
    private LinkedList<Event> parseEvents(JSONArray organizationEvents) {
        LinkedList<Event> events = new LinkedList<>();
        for (Object json : organizationEvents) {
            events.add(parseEvent((JSONObject) json));
        }
        return events;
    }

    private Event parseEvent(JSONObject jsonObject) {
        String eventName = jsonObject.getString("eventName");
        Volunteer eventExecutive = parseVolunteer(jsonObject.getJSONObject("eventExecutive"));
        int eventDuration = jsonObject.getInt("eventDuration");
        int eventNumVolunteers = jsonObject.getInt("eventNumVolunteers");
        LinkedList<Volunteer> eventVolunteers = parseVolunteers(jsonObject.getJSONArray("eventVolunteers"));
        return new Event(eventName, eventExecutive, eventDuration, eventNumVolunteers, eventVolunteers);
    }

    // MODIFIES: database
    // EFFECTS: parses founders from JSON object and adds them to database
    private void addFounders(Database database, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("founders");
        for (Object json : jsonArray) {
            JSONObject nextFounder = (JSONObject) json;
            addFounder(database, nextFounder);
        }
    }

    // MODIFIES: database
    // EFFECTS: parses founder from JSON object and adds it to database
    private void addFounder(Database database, JSONObject jsonObject) {
        Founder founder = parseFounder(jsonObject);
        database.addFounder(founder);
    }

    // MODIFIES: database
    // EFFECTS: parses volunteers from JSON object and adds them to database
    private void addVolunteers(Database database, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("volunteers");
        for (Object json : jsonArray) {
            JSONObject nextVolunteer = (JSONObject) json;
            addVolunteer(database, nextVolunteer);
        }
    }

    // MODIFIES: database
    // EFFECTS: parses volunteer from JSON object and adds it to database
    private void addVolunteer(Database database, JSONObject jsonObject) {
        Volunteer volunteer = parseVolunteer(jsonObject);
        database.addVolunteer(volunteer);
    }

    // MODIFIES: database
    // EFFECTS: parses organizations from JSON object and adds them to database
    private void addOrganizations(Database database, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("organizations");
        for (Object json : jsonArray) {
            JSONObject nextOrganization = (JSONObject) json;
            addOrganization(database, nextOrganization);
        }
    }

    // MODIFIES: database
    // EFFECTS: parses organization from JSON object and adds it to database
    private void addOrganization(Database database, JSONObject jsonObject) {
        Organization organization = parseOrganization(jsonObject);
        database.addOrganization(organization);
    }
}
