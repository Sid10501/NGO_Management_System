package persistence;

import model.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkOrganization(Organization organization1, Organization organization2) {
        assertEquals(organization1.getOrganizationName(), organization2.getOrganizationName());
        assertEquals(organization1.getOrganizationCity(), organization2.getOrganizationCity());
        assertEquals(organization1.getOrganizationPurpose(), organization2.getOrganizationPurpose());
        assertEquals(organization1.getOrganizationMinHours(), organization2.getOrganizationMinHours());
        assertEquals(organization1.getOrganizationFunds(), organization2.getOrganizationFunds());
        checkVolunteers(organization1.getOrganizationMembers(), organization2.getOrganizationMembers());
        checkEvents(organization1.getOrganizationEvents(), organization2.getOrganizationEvents());
        checkVolunteers(organization1.getOrganizationApplications(), organization2.getOrganizationApplications());
    }


    private void checkVolunteers(LinkedList<Volunteer> volunteers1, LinkedList<Volunteer> volunteers2) {
        for(int i = 0; i< volunteers1.size(); i++){
            checkVolunteer(volunteers2.get(i),volunteers1.get(i));
        }
    }

    protected void checkVolunteer(Volunteer volunteer1, Volunteer volunteer2) {
        assertEquals(volunteer1.getVolunteerName(), volunteer2.getVolunteerName());
        assertEquals(volunteer1.getVolunteerAge(), volunteer2.getVolunteerAge());
        assertEquals(volunteer1.getVolunteerCity(), volunteer2.getVolunteerCity());
        assertEquals(volunteer1.getVolunteerNumEvents(), volunteer2.getVolunteerNumEvents());
        assertEquals(volunteer1.getVolunteerHoursWorked(), volunteer2.getVolunteerHoursWorked());
        assertEquals(volunteer1.isExec(), volunteer2.isExec());
        assertEquals(volunteer1.getVolunteerMaxHours(), volunteer2.getVolunteerMaxHours());
        assertEquals(volunteer1.getVolunteerStatus(), volunteer2.getVolunteerStatus());
    }

    protected void checkFounder(Founder founder1, Founder founder2) {
        assertEquals(founder1.getFounderName(), founder2.getFounderName());
        assertEquals(founder1.getFounderAge(), founder2.getFounderAge());
        assertEquals(founder1.getFounderCity(), founder2.getFounderCity());
        assertEquals(founder1.getFounderInvestment(), founder2.getFounderInvestment());
        checkOrganization(founder1.getFounderOrganization(), founder2.getFounderOrganization());
    }

    private void checkEvents(LinkedList<Event> events, LinkedList<Event> organizationEvents) {
        for(int i = 0; i< events.size(); i++){
            checkEvent(organizationEvents.get(i),events.get(i));
        }
    }

    private void checkEvent(Event event1, Event event2){
        assertEquals(event1.getEventName(), event2.getEventName());
        checkVolunteer(event1.getEventExecutive(), event2.getEventExecutive());
        assertEquals(event1.getEventDuration(), event2.getEventDuration());
        assertEquals(event1.getNumVolunteers(), event2.getNumVolunteers());
        checkVolunteers(event1.getEventVolunteers(),event2.getEventVolunteers());
    }
}


//    protected void checkOrganization(Organization organization, String name, String city, Purpose purpose, int minHours,
//                                     long fund, LinkedList<Event> events, LinkedList<Volunteer> members,
//                                     LinkedList<Volunteer> applications) {
//        assertEquals(name, organization.getOrganizationName());
//        assertEquals(city, organization.getOrganizationCity());
//        assertEquals(purpose, organization.getOrganizationPurpose());
//        assertEquals(minHours, organization.getOrganizationMinHours());
//        assertEquals(fund, organization.getOrganizationFunds());
//        checkVolunteers(members, organization.getOrganizationMembers());
//        checkEvents(events, organization.getOrganizationEvents());
//        checkVolunteers(applications, organization.getOrganizationApplications());
//
//    }
//
//    protected void checkFounder(Founder founder, String name, int age, String city, long investment,
//                                Organization organization) {
//        assertEquals(name, founder.getFounderName());
//        assertEquals(age, founder.getFounderAge());
//        assertEquals(city, founder.getFounderCity());
//        assertEquals(investment, founder.getFounderInvestment());
//        checkOrganization(organization, founder.getFounderOrganization());
//    }
//
//    protected void checkVolunteer(Volunteer volunteer, String name, int age, String city, int numEvents,
//                                  int hoursWorked, int maxHours, Status status, boolean isExec) {
//        assertEquals(name, volunteer.getVolunteerName());
//        assertEquals(age, volunteer.getVolunteerAge());
//        assertEquals(city, volunteer.getVolunteerCity());
//        assertEquals(numEvents, volunteer.getVolunteerNumEvents());
//        assertEquals(hoursWorked, volunteer.getVolunteerHoursWorked());
//        assertEquals(isExec, volunteer.isExec());
//        assertEquals(maxHours, volunteer.getVolunteerMaxHours());
//        assertEquals(status, volunteer.getVolunteerStatus());
//    }
