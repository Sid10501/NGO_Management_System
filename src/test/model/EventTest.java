package model;

import model.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {
    Event event1;
    Event event2;
    Event event3;
    Volunteer volunteer1;
    Volunteer volunteer2;
    Volunteer volunteer3;
    Volunteer defaultExecutive;
    LinkedList<Volunteer> volunteers;

    @BeforeEach
    public void setup() {
        volunteers = new LinkedList<>();
        volunteer1 = new Volunteer("Evans", 20, "Vancouver", 3);
        volunteer2 = new Volunteer("Harry", 24, "Toronto", 2);
        volunteer3 = new Volunteer("Emma", 22, "Alberta", 4);
        defaultExecutive = new Volunteer("DefaultVolunteer", "City", 25,
                100, 50, 0, true, Status.ACCEPTED);

        event1 = new Event("Thanksgiving", 15, 10);
        event2 = new Event("Christmas", 15, 20);
        event3 = new Event("Event Name", defaultExecutive, 4, 5, new LinkedList<>());
    }

    @Test
    public void testForGetName() {
        assertEquals("Thanksgiving", event1.getEventName());
        assertEquals("Christmas", event2.getEventName());
    }

    @Test
    public void testForGetDuration() {
        assertEquals(15, event1.getEventDuration());
        assertEquals(15, event2.getEventDuration());
    }

    @Test
    public void testForGetNumberOfVolunteers() {
        assertEquals(10, event1.getNumVolunteers());
        assertEquals(20, event2.getNumVolunteers());
    }

    @Test
    public void testForGetVolunteers() {
        event1.addVolunteer(volunteer1);
        event1.addVolunteer(volunteer2);

        assertTrue(event1.getEventVolunteers().contains(volunteer1));
        assertTrue(event1.getEventVolunteers().contains(volunteer2));
        assertTrue(event2.getEventVolunteers().isEmpty());
    }

    @Test
    public void testForGetAndSetExecutive() {
        event1.setEventExecutive(volunteer3);
        assertEquals(volunteer3, event1.getEventExecutive());
        assertNotEquals(volunteer2, event2.getEventExecutive());
    }
}