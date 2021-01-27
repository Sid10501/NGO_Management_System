package model;

import model.enums.Purpose;
import model.exceptions.EventCapacityFullException;
import model.exceptions.NotEnoughFundsException;
import model.exceptions.NotEnoughTimeException;
import model.exceptions.NotPromotableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class FounderTest {
    Founder founder1;
    Founder founder2;
    Founder founder3;

    Organization org1;
    Organization org2;
    Organization org3;
    Organization insufficientFundsOrg;

    Volunteer applicant1;
    Volunteer applicant2;
    Volunteer volunteer1;
    Volunteer volunteer2;

    Event event1;
    Event event2;


    @BeforeEach
    public void setUp() {
        founder1 = new Founder("James", 21, "Toronto", 2500);
        founder2 = new Founder("Alex", 24, "Vancouver", 1400);
        founder3 = new Founder("Allison", 21, "City", 2500, org3);

        org1 = new Organization("Hope", "Toronto", 2000, 1, Purpose.EDUCATION);
        org2 = new Organization("SavePets", "Van", 3000, 2, Purpose.ANIMALS);
        org3 = new Organization("New Organization", "City", Purpose.ANIMALS,
                10, 2500, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());
        insufficientFundsOrg = new Organization("Insufficient Funds Organization", "City", Purpose.ANIMALS,
                10, 400, new LinkedList<>(), new LinkedList<>(), new LinkedList<>());

        applicant1 = new Volunteer("James", 21, "Toronto", 12);
        applicant2 = new Volunteer("Alex", 24, "Vancouver", 24);
        volunteer1 = new Volunteer("Amanda", 23, "Queens", 10);
        volunteer2 = new Volunteer("Tessa", 21, "Alberta", 4);

        event1 = new Event("Thanksgiving", 15, 1);
        event2 = new Event("Christmas", 15, 20);
    }

    @Test
    public void testGetters() {
        assertEquals("James", founder1.getFounderName());
        assertEquals("Alex", founder2.getFounderName());

        assertEquals(21, founder1.getFounderAge());
        assertEquals(24, founder2.getFounderAge());

        assertEquals("Toronto", founder1.getFounderCity());
        assertEquals("Vancouver", founder2.getFounderCity());

        assertEquals(2500, founder1.getFounderInvestment());
        assertEquals(1400, founder2.getFounderInvestment());
    }

    @Test
    public void testSetAndGetOrganization() {
        founder1.setFounderOrganization(org1);
        assertEquals(org1, founder1.getFounderOrganization());
        assertNotEquals(org2, founder1.getFounderOrganization());
    }

    @Test
    public void testAcceptApplications() {
        applicant1.apply();
        try {
            org1.addApplicant(applicant1);
        } catch (NotEnoughTimeException e) {
            fail("This should have not thrown an exception.");

        }
        assertTrue(org1.getOrganizationApplications().contains(applicant1));
        founder1.acceptApplication(applicant1);
        assertTrue(org1.getOrganizationApplications().contains(applicant1));
    }

    @Test
    public void testPromoteVolunteer() {
        volunteer1.addHoursWorked(5);
        assertFalse(volunteer1.isExec());
        try {
            founder1.promoteVolunteer(volunteer1);
        } catch (NotPromotableException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");
        }
        assertTrue(volunteer1.isExec());
    }

    @Test
    public void testPromoteIneligibleVolunteer() {
        assertFalse(volunteer2.isExec());
        try {
            founder1.promoteVolunteer(volunteer2);
            fail("This should have thrown an exception.");
        } catch (NotPromotableException e) {
            // Do nothing
        }
        assertFalse(volunteer2.isExec());
    }

    @Test
    public void testCreateEventEnoughFunds() {
        founder1.setFounderOrganization(org1);
        assertEquals(org1, founder1.getFounderOrganization());
        try {
            founder1.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            e.printStackTrace();
            fail("This should have not thrown an exception.");
        }
        assertTrue(org1.getOrganizationEvents().contains(event1));
    }

    @Test
    public void testCreateEventNotEnoughFunds() {
        founder1.setFounderOrganization(insufficientFundsOrg);
        assertEquals(insufficientFundsOrg, founder1.getFounderOrganization());
        try {
            founder1.createEvent(event1);
            fail("This should have thrown an exception.");
        } catch (NotEnoughFundsException e) {
            // Do nothing
        }
        assertFalse(insufficientFundsOrg.getOrganizationEvents().contains(event1));
    }

    @Test
    public void testAddVolunteer() {
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
        assertTrue(event1.getEventVolunteers().contains(volunteer1));
        assertTrue(event2.getEventVolunteers().contains(volunteer2));
    }

    @Test
    public void testAddVolunteerEventFull() {
        founder1.setFounderOrganization(org1);
        try {
            founder1.createEvent(event1);
        } catch (NotEnoughFundsException e) {
            fail("This should not have thrown an exception.");
        }
        try {
            founder1.addVolunteerToEvent(volunteer1, event1);
            founder1.addVolunteerToEvent(volunteer2, event1);
            fail("This should have thrown an exception.");
        } catch (EventCapacityFullException e) {
            // do nothing
        }
        assertTrue(event1.getEventVolunteers().contains(volunteer1));
        assertFalse(event1.getEventVolunteers().contains(volunteer2));
    }
}
