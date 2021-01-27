package persistence;

import model.*;
import model.enums.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {


    @Test
    void testWriterInvalidFile() {
        try {
            Database database = new Database("My Database");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyDatabase() {
        try {
            Database database = new Database("My Database");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDatabase.json");
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDatabase.json");
            database = reader.read();
            assertEquals("My Database", database.getDatabaseName());
            assertEquals(0, database.numFounders());
            assertEquals(0, database.numVolunteers());
            assertEquals(0, database.numOrganizations());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
    @Test
    void testWriterNoVolunteers() {
        try {
            Database database = new Database("My Database");
            assertEquals("My Database", database.getDatabaseName());
            assertEquals(0, database.numOrganizations());
            assertEquals(0, database.numFounders());
            assertEquals(0, database.numVolunteers());

            Volunteer defaultExecutive = new Volunteer("DefaultVolunteer", "City", 25,
                    100, 50, 0, true, Status.ACCEPTED);
            LinkedList<Volunteer> defaultVolunteers = new LinkedList<>();
            defaultVolunteers.add(defaultExecutive);
            LinkedList<Volunteer> emptyEventVolunteers = new LinkedList<>();

            LinkedList<Volunteer> emptyApplicationsSet = new LinkedList<>();

            Event event1 = new Event("Thanksgiving", defaultExecutive, 15, 10, emptyEventVolunteers);
            LinkedList<Event> organizationEvents = new LinkedList<>();
            organizationEvents.add(event1);

            Organization organization = new Organization("Hope", "Vancouver", Purpose.EDUCATION,
                    10, 2500, defaultVolunteers, organizationEvents, emptyApplicationsSet);
            Founder founder = new Founder("Sidharth", 19, "Vancouver", 2500,
                    organization);

            database.addOrganization(organization);
            database.addFounder(founder);
            database.addVolunteer(defaultExecutive);

            JsonWriter writer = new JsonWriter("./data/testWriterNoVolunteerDatabase.json");
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterNoVolunteerDatabase.json");
            database = reader.read();

            assertEquals(1, database.numOrganizations());
            assertEquals(1, database.numFounders());
            assertEquals(1, database.numVolunteers());
            checkOrganization(database.getOrganizations().get(0), organization);
            checkFounder(database.getFounders().get(0), founder);
            checkVolunteer(database.getVolunteers().get(0),defaultExecutive);

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderOnlyVolunteers() {
        try {
            Database database = new Database("My Database");
            assertEquals("My Database", database.getDatabaseName());
            assertEquals(0, database.numOrganizations());
            assertEquals(0, database.numFounders());
            assertEquals(0, database.numVolunteers());


            Volunteer volunteer1 = new Volunteer("Billy", "Egypt", 24,
                    24, 0, 0, false, Status.NA);

            Volunteer volunteer2 = new Volunteer("Charlie", "Vancouver", 18,
                    35, 0, 0, false, Status.NA);

            database.addVolunteer(volunteer1);
            database.addVolunteer(volunteer2);

            JsonWriter writer = new JsonWriter("./data/testWriterOnlyVolunteersDatabase.json");
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterOnlyVolunteersDatabase.json");
            database = reader.read();

            assertEquals(0, database.numOrganizations());
            assertEquals(0, database.numFounders());
            assertEquals(2, database.numVolunteers());

            checkVolunteer(database.getVolunteers().get(0), volunteer1);
            checkVolunteer(database.getVolunteers().get(1), volunteer2);
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
    @Test
    void testWriterGeneralDatabase() {
        try {
            Database database = new Database("My Database");
            assertEquals("My Database", database.getDatabaseName());
            assertEquals(0, database.numOrganizations());
            assertEquals(0, database.numFounders());
            assertEquals(0, database.numVolunteers());

            Volunteer defaultExecutive = new Volunteer("DefaultVolunteer", "City", 25,
                    100, 50, 0, true, Status.ACCEPTED);
            Volunteer volunteer1 = new Volunteer("Asher", "Vancouver", 21,
                    24, 0, 0, false, Status.NA);
            Volunteer volunteer2 = new Volunteer("James", "Vancouver", 19,
                    35, 0, 0, false, Status.PENDING);
            Volunteer volunteer3 = new Volunteer("Frank", "Alberta", 24,
                    45, 2, 1, false, Status.ACCEPTED);
            Volunteer volunteer4 = new Volunteer("Stacy", "Paris", 25,
                    32, 5, 1, false, Status.ACCEPTED);
            Volunteer volunteer5 = new Volunteer("Gavin", "Paris", 28,
                    25, 13, 2, false, Status.ACCEPTED);

            LinkedList<Volunteer> organization1Volunteers = new LinkedList<>();
            organization1Volunteers.add(defaultExecutive);

            LinkedList<Volunteer> organization2Volunteers = new LinkedList<>();
            organization2Volunteers.add(defaultExecutive);
            organization2Volunteers.add(volunteer3);

            LinkedList<Volunteer> organization3Volunteers = new LinkedList<>();
            organization3Volunteers.add(defaultExecutive);
            organization3Volunteers.add(volunteer4);
            organization3Volunteers.add(volunteer5);

            LinkedList<Volunteer> event1Volunteers = new LinkedList<>();
            event1Volunteers.add(volunteer3);

            LinkedList<Volunteer> event2Volunteers = new LinkedList<>();
            event2Volunteers.add(volunteer4);

            LinkedList<Volunteer> event3Volunteers = new LinkedList<>();
            event3Volunteers.add(volunteer4);
            event3Volunteers.add(volunteer5);

            LinkedList<Volunteer> organization1Applicants = new LinkedList<>();
            organization1Applicants.add(volunteer2);

            LinkedList<Volunteer> emptyApplicationsSet = new LinkedList<>();


            Event event1 = new Event("Thanksgiving", defaultExecutive, 2, 5, event1Volunteers);
            Event event2 = new Event("Christmas", defaultExecutive, 5, 8, event2Volunteers);
            Event event3 = new Event("Diwali", defaultExecutive, 8, 4, event3Volunteers);

            LinkedList<Event> emptyEventsList = new LinkedList<>();
            LinkedList<Event> organization2Events = new LinkedList<>();
            LinkedList<Event> organization3Events = new LinkedList<>();

            organization2Events.add(event1);
            organization3Events.add(event2);
            organization3Events.add(event3);


            Organization organization1 = new Organization("Hope", "Vancouver", Purpose.EDUCATION,
                    10, 2500, organization1Volunteers, emptyEventsList, organization1Applicants);
            Organization organization2 = new Organization("Healthy Life", "Alberta", Purpose.HEALTH,
                    6, 3200, organization2Volunteers, organization2Events, emptyApplicationsSet);
            Organization organization3 = new Organization("Help Animals", "Paris", Purpose.ANIMALS,
                    14, 4700, organization3Volunteers, organization3Events, emptyApplicationsSet);


            Founder founder1 = new Founder("Jill", 21, "Vancouver", 2500, organization1);
            Founder founder2 = new Founder("Ruby", 27, "Alberta", 3200, organization2);
            Founder founder3 = new Founder("Alex", 26, "Paris", 4700, organization3);

            database.addOrganization(organization1);
            database.addOrganization(organization2);
            database.addOrganization(organization3);
            database.addFounder(founder1);
            database.addFounder(founder2);
            database.addFounder(founder3);
            database.addVolunteer(defaultExecutive);
            database.addVolunteer(volunteer1);
            database.addVolunteer(volunteer2);
            database.addVolunteer(volunteer3);
            database.addVolunteer(volunteer4);
            database.addVolunteer(volunteer5);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDatabase.json");
            writer.open();
            writer.write(database);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDatabase.json");
            database = reader.read();

            assertEquals(3, database.numOrganizations());
            assertEquals(3, database.numFounders());
            assertEquals(6, database.numVolunteers());
            checkOrganization(database.getOrganizations().get(0), organization1);
            checkOrganization(database.getOrganizations().get(1), organization2);
            checkOrganization(database.getOrganizations().get(2), organization3);

            checkFounder(database.getFounders().get(0), founder1);
            checkFounder(database.getFounders().get(1), founder2);
            checkFounder(database.getFounders().get(2), founder3);

            checkVolunteer(database.getVolunteers().get(0), defaultExecutive);
            checkVolunteer(database.getVolunteers().get(1), volunteer1);
            checkVolunteer(database.getVolunteers().get(2), volunteer2);
            checkVolunteer(database.getVolunteers().get(3), volunteer3);
            checkVolunteer(database.getVolunteers().get(4), volunteer4);
            checkVolunteer(database.getVolunteers().get(5), volunteer5);

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}