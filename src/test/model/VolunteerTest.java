package model;


import model.enums.Purpose;
import model.enums.Status;
import model.exceptions.EventCapacityFullException;
import model.exceptions.NotEnoughFundsException;
import model.exceptions.NotEnoughTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class VolunteerTest{

    Volunteer volunteer1;
    Volunteer volunteer2;

    Organization org1;
    Organization org2;
    Organization org3;

    Founder founder1;
    Founder founder2;
    Founder founder3;

    Event event1;
    Event event2;
    Event event3;
    LinkedList<Event> events;


    @BeforeEach
    public void setup() {
        volunteer1 = new Volunteer("Amanda", 23, "Queens", 40);
        volunteer2 = new Volunteer("Tessa", 21, "Alberta", 36);

        founder1 = new Founder("Jacob", 40, "Toronto", 2000);
        founder2 = new Founder("Jack", 45, "Vancouver", 3000);
        founder3 = new Founder("Jason", 38, "Alberta", 4000);

        event1 = new Event("Thanksgiving", 15, 2);
        event2 = new Event("Christmas", 5, 20);
        event3 = new Event("Diwali", 2, 4);
        events = new LinkedList<>();

        org1 = new Organization("Hope", "Toronto", 1000, 1,
                Purpose.HEALTH);
        org2 = new Organization("SaveAnimals", "Vancouver", 3000,
                2, Purpose.ANIMALS);
        org3 = new Organization("HealthIsWealth", "Alberta", 4000,
                3, Purpose.EDUCATION);
    }

    @Test
    public void testForGetters() {
        assertEquals("Amanda", volunteer1.getVolunteerName());
        assertEquals("Tessa", volunteer2.getVolunteerName());
        assertEquals(23, volunteer1.getVolunteerAge());
        assertEquals(21, volunteer2.getVolunteerAge());
        assertEquals("Queens", volunteer1.getVolunteerCity());
        assertEquals("Alberta", volunteer2.getVolunteerCity());
    }


    @Test
    public void testForGetHoursWorked() {
        founder1.setFounderOrganization(org1);
        try {
            founder1.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        try {
            founder1.createEvent(event2);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        try {
            founder1.addVolunteerToEvent(volunteer1,event1);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        try {
            founder1.addVolunteerToEvent(volunteer2,event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        assertTrue(event1.getEventVolunteers().contains(volunteer1));
        assertTrue(event2.getEventVolunteers().contains(volunteer2));
        assertEquals(event1.getEventDuration(), volunteer1.getVolunteerHoursWorked());
        assertEquals(event2.getEventDuration(), volunteer2.getVolunteerHoursWorked());
    }

    @Test
    public void testForGetStatus() {
        founder1.setFounderOrganization(org1);
        assertEquals(Status.NA, volunteer1.getVolunteerStatus());
        volunteer1.apply();
        assertEquals(Status.PENDING, volunteer1.getVolunteerStatus());
        try {
            founder1.getFounderOrganization().addApplicant(volunteer1);
        } catch (NotEnoughTimeException e) {
            fail("This should have not thrown an exception.");
            e.printStackTrace();
        }
        assertTrue(founder1.getFounderOrganization().getOrganizationApplications().contains(volunteer1));
        founder1.acceptApplication(volunteer1);
        assertFalse(founder1.getFounderOrganization().getOrganizationApplications().contains(volunteer1));
        assertEquals(Status.ACCEPTED, volunteer1.getVolunteerStatus());

    }

    @Test
    public void testForGetMaxHours() {
        assertEquals(40, volunteer1.getVolunteerMaxHours());
        assertEquals(36, volunteer2.getVolunteerMaxHours());
    }

    @Test
    public void testForGetNumEvents() {
        founder1.setFounderOrganization(org1);
        try {
            founder1.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        try {
            founder1.createEvent(event2);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        assertEquals(0, volunteer1.getVolunteerNumEvents());
        volunteer2.apply();
        assertEquals(0, volunteer2.getVolunteerNumEvents());
        founder1.acceptApplication(volunteer2);
        assertEquals(Status.ACCEPTED, volunteer2.getVolunteerStatus());
        assertEquals(0, volunteer2.getVolunteerNumEvents());
        try {
            founder1.addVolunteerToEvent(volunteer2,event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        assertEquals(1, volunteer2.getVolunteerNumEvents());
    }


    @Test
    public void testForTotalRewards() {
        founder1.setFounderOrganization(org1);
        try {
            founder1.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        try {
            founder1.createEvent(event2);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");
        }
        volunteer1.apply();
        volunteer2.apply();
        founder1.acceptApplication(volunteer1);
        founder1.acceptApplication(volunteer2);
        volunteer2.promote();
        assertTrue(volunteer2.isExec());
        assertFalse(volunteer1.isExec());
        assertTrue(org1.getFilteredExecutives(true).contains(volunteer2));
        assertFalse(org1.getFilteredExecutives(true).contains(volunteer1));
        try {
            founder1.addVolunteerToEvent(volunteer1, event1);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        try {
            founder1.addVolunteerToEvent(volunteer2, event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        try {
            founder1.addVolunteerToEvent(volunteer1, event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        assertEquals(1, volunteer2.getVolunteerNumEvents());
        assertEquals(2, volunteer1.getVolunteerNumEvents());
        assertEquals(20*10+2*100, volunteer1.computeTotalRewards());
        assertEquals(5*20+200, volunteer2.computeTotalRewards());
    }

    @Test
    public void testForIsExecutive() {
        founder1.setFounderOrganization(org1);

        volunteer1.apply();
        volunteer2.apply();
        founder1.acceptApplication(volunteer1);
        founder1.acceptApplication(volunteer2);

        volunteer2.promote();
        assertTrue(volunteer2.isExec());
        assertFalse(volunteer1.isExec());
    }

    @Test
    public void testForRemainingHours() {
        founder3.setFounderOrganization(org3);
        try {
            founder3.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");

        }
        try {
            founder3.createEvent(event2);
        } catch (NotEnoughFundsException e) {
            fail("This should have not thrown an exception.");

        }
        volunteer1.apply();
        volunteer2.apply();
        founder3.acceptApplication(volunteer1);
        founder3.acceptApplication(volunteer2);
        assertEquals(40, volunteer1.getVolunteerRemainingHours());
        assertEquals(36,volunteer2.getVolunteerRemainingHours());
        try {
            founder3.addVolunteerToEvent(volunteer1, event1);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        try {
            founder3.addVolunteerToEvent(volunteer2,event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        assertEquals(40-15, volunteer1.getVolunteerRemainingHours());
        assertEquals(36-5,volunteer2.getVolunteerRemainingHours());
        try {
            founder3.addVolunteerToEvent(volunteer1, event2);
        } catch (EventCapacityFullException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");

        }
        assertEquals(40-15-5, volunteer1.getVolunteerRemainingHours());
        assertEquals(36-5, volunteer2.getVolunteerRemainingHours());
    }
}