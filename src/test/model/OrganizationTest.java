package model;

import model.enums.Purpose;
import model.exceptions.NotEnoughTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationTest {
    Organization org1;
    Organization org2;
    Organization org3;
    Organization org4;
    Organization org5;

    Founder founder1;
    Founder founder2;
    Founder founder3;
    Founder founder4;

    Purpose purpose1;
    Purpose purpose2;
    Purpose purpose3;
    Purpose purpose4;

    Volunteer volunteer1;
    Volunteer volunteer2;

    Event event1;
    Event event2;
    LinkedList<Event> events;


    @BeforeEach
    public void setup() {
        founder1 = new Founder("Jacob", 40, "Toronto", 2000);
        founder2 = new Founder("Jack", 45, "Vancouver", 3000);
        founder3 = new Founder("Jason", 38, "Alberta", 4000);
        founder4 = new Founder("Jamie", 35, "Victoria", 2000);

        purpose1 = Purpose.EDUCATION;
        purpose2 = Purpose.ANIMALS;
        purpose3 = Purpose.HEALTH;
        purpose4 = Purpose.UNDERPRIVILEGED;

        volunteer1 = new Volunteer("Amanda", 23, "Queens", 3);
        volunteer2 = new Volunteer("Tessa", 21, "Alberta", 4);

        event1 = new Event("Thanksgiving", 15, 10);
        event2 = new Event("Christmas", 15, 20);
        events = new LinkedList<>();

        org1 = new Organization("Hope", "Toronto", 2000, 1,
                purpose1);
        org2 = new Organization("SaveAnimals", "Vancouver", 3000,
                2, purpose2);
        org3 = new Organization("HealthIsWealth", "Alberta", 4000,
                3, purpose3);
        org4 = new Organization("SaveUs", "Victoria", 2000, 100,
                purpose4);
        org5 = new Organization("New Organization", "City", purpose1,10,
                2500,new LinkedList<>(),new LinkedList<>(),new LinkedList<>());

    }

    @Test
    public void testForGetters() {
        assertEquals("Hope", org1.getOrganizationName());
        assertEquals("SaveAnimals", org2.getOrganizationName());
        assertEquals("HealthIsWealth", org3.getOrganizationName());
        assertEquals("SaveUs", org4.getOrganizationName());
        assertEquals("New Organization", org5.getOrganizationName());

        assertEquals(purpose1, org1.getOrganizationPurpose());
        assertEquals(purpose2, org2.getOrganizationPurpose());
        assertEquals(purpose3, org3.getOrganizationPurpose());
        assertEquals(purpose4, org4.getOrganizationPurpose());
        assertEquals(purpose1, org5.getOrganizationPurpose());

        assertEquals("Toronto", org1.getOrganizationCity());
        assertEquals("Vancouver", org2.getOrganizationCity());
        assertEquals("Alberta", org3.getOrganizationCity());
        assertEquals("Victoria", org4.getOrganizationCity());
        assertEquals("City", org5.getOrganizationCity());

    }

    @Test
    public void testForGetAndAddMember() {
        volunteer2.promote();
        org1.addMember(volunteer1);
        org1.addMember(volunteer2);

        assertTrue(org1.getOrganizationMembers().contains(volunteer1));
        assertTrue(org1.getOrganizationMembers().contains(volunteer2));
        assertFalse(org2.getOrganizationMembers().contains(volunteer1));
    }

    @Test
    public void testForGetAndCreateEvents() {
        org1.addEvent(event1);
        org1.addEvent(event2);
        events.add(event1);
        events.add(event2);

        assertEquals(events, org1.getOrganizationEvents());
        assertEquals(events, org1.getOrganizationEvents());
    }

    @Test
    public void testForGetFunds() {
        assertEquals(2000, org1.getOrganizationFunds());
        assertEquals(3000, org2.getOrganizationFunds());
        assertEquals(4000, org3.getOrganizationFunds());
        assertEquals(2000, org4.getOrganizationFunds());
        assertNotEquals(5500, org1.getOrganizationFunds());
    }

    @Test
    public void testForGetMinHours() {
        assertEquals(1, org1.getOrganizationMinHours());
        assertEquals(2, org2.getOrganizationMinHours());
        assertEquals(3, org3.getOrganizationMinHours());
        assertEquals(100, org4.getOrganizationMinHours());
    }

    @Test
    public void testForGetExecutives() {
        volunteer2.promote();
        org1.addMember(volunteer1);
        org1.addMember(volunteer2);

        assertTrue(volunteer2.isExec());
        assertTrue(org1.getFilteredExecutives(true).contains(volunteer2));
        assertFalse(org1.getFilteredExecutives(true).contains(volunteer1));

    }

    @Test
    public void testForGetVolunteers() {
        org1.addMember(volunteer1);
        org1.addMember(volunteer2);

        assertTrue(org1.getFilteredExecutives(false).contains(volunteer1));
        assertTrue(org1.getFilteredExecutives(false).contains(volunteer2));
        assertFalse(org2.getFilteredExecutives(false).contains(volunteer1));
    }

    @Test
    public void testAddEligibleApplicant() {
        try {
            org1.addApplicant(volunteer1);
        } catch (NotEnoughTimeException e) {
            fail("This should have not thrown an exception.");
        }
        assertTrue(org1.getOrganizationApplications().contains(volunteer1));
    }

    @Test
    public void testAddIneligibleApplicant() {
        try {
            org5.addApplicant(volunteer1);
            fail("This should ave thrown an exception");
        } catch (NotEnoughTimeException e) {
        // do nothing
        }
        assertFalse(org1.getOrganizationApplications().contains(volunteer1));
    }
}