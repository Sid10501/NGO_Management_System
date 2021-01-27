//package ui;
//
//import model.*;
//import model.enums.Purpose;
//import model.enums.Status;
//import persistence.JsonReader;
//import persistence.JsonWriter;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.LinkedList;
//import java.util.Scanner;
//
//// Non Profit Organization Database Manager
//public class DatabaseManager {
//    private static final String JSON_STORE = "./data/generalDatabase.json";
//    private final Scanner scanner;
//    private Database database;
//    private final JsonWriter jsonWriter;
//    private final JsonReader jsonReader;
//
//    private enum MemberType {
//        FOUNDER, VOLUNTEER
//    }
//
//
//    // EFFECTS: initiates the database and displays the first menu
//    public DatabaseManager() throws FileNotFoundException {
//        database = new Database("My Database");
//        this.scanner = new Scanner(System.in);
//        jsonWriter = new JsonWriter(JSON_STORE);
//        jsonReader = new JsonReader(JSON_STORE);
//        displayDatabaseMenu();
//    }
//
//    // EFFECTS: displays the menu which asks the role of the user
//    private void displayDatabaseMenu() {
//        System.out.println("Welcome to Non Profit Organization Database");
//        System.out.println("Create New Database (1)");
//        System.out.println("Load Database (2)");
//        System.out.println("Save Database (3)");
//        System.out.println("Quit (4)");
//
//        String line = scanner.nextLine();
//        databaseMenuHelper(line);
//    }
//
//    // EFFECTS: helper in displaying the menu which asks the role of the user
//    private void databaseMenuHelper(String line) {
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                displayMenu();
//                break;
//            case 2:
//                loadDatabase();
//                displayMenu();
//                break;
//            case 3:
//                saveDatabase();
//                displayDatabaseMenu();
//                break;
//            case 4:
//                System.out.println("\nGoodbye!");
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays the menu with asks the role of the user
//    private void displayMenu() {
//        System.out.println(database.getDatabaseName());
//        System.out.println("Select Your Role: ");
//        System.out.println("Founder (press 1)");
//        System.out.println("Volunteer (press 2)");
//        System.out.println("Go to Database Menu (Press 3)");
//        String line = scanner.nextLine();
//        switchMemberMenu(line);
//    }
//
//    // EFFECTS: switches between the roles depending on the input by user
//    private void switchMemberMenu(String line) {
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                displayUserMenu(MemberType.FOUNDER);
//                break;
//            case 2:
//                displayUserMenu(MemberType.VOLUNTEER);
//                break;
//            case 3:
//                displayDatabaseMenu();
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: gets the input command and parses the string input as int and returns it
//    private int getCommand(String line, int command) {
//        try {
//            command = Integer.parseInt(line);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        return command;
//    }
//
//    // EFFECTS: displays a choice between login and sign up
//    private void displayUserMenu(MemberType type) {
//        System.out.println("You're inside " + type + " Menu");
//        System.out.println("Login into Existing User: (press 1)");
//        System.out.println("Create New User: (press 2)");
//        System.out.println("Go Back (press 3)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                displayLoginPage(type);
//                break;
//            case 2:
//                displaySignupPage(type);
//                break;
//            case 3:
//                displayMenu();
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS:  asks the user to input name, age, city and other member type specific fields
//    private void displaySignupPage(MemberType type) {
//        System.out.println("Enter Name: ");
//        String nameInput = scanner.nextLine();
//        System.out.println("Enter Your City: ");
//        String cityInput = scanner.nextLine();
//        System.out.println("Enter Your Age: ");
//        String ageInput = scanner.nextLine();
//        int age = Integer.parseInt(ageInput);
//
//        if (type == MemberType.FOUNDER) {
//            displayFounderSignupPage(nameInput, age, cityInput);
//
//        } else if (type == MemberType.VOLUNTEER) {
//            displayVolunteerSignupPage(nameInput, age, cityInput);
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: asks the user to enter founder specific fields and creates a founder and an organization
//    private void displayFounderSignupPage(String nameInput, int age, String cityInput) {
//        System.out.println("Enter Your Investment: ");
//        String investmentInput = scanner.nextLine();
//        int investment = Integer.parseInt(investmentInput);
//        Founder newFounder = new Founder(nameInput, age, cityInput, investment);
//        database.addFounder(newFounder);
//        System.out.println("Organization Details: ");
//        System.out.println("Enter Name: ");
//        String organizationName = scanner.nextLine();
//        System.out.println("Time Commitment Required: ");
//        String timeInput = scanner.nextLine();
//        int time = Integer.parseInt(timeInput);
//        Purpose inputPurpose = getPurpose();
//        Organization newOrganization = new Organization(organizationName, cityInput,
//                newFounder.getFounderInvestment(), time, inputPurpose);
//        newFounder.setFounderOrganization(newOrganization);
//        database.addOrganization(newOrganization);
//        System.out.println("Founder Creation Complete");
//        displayFounderMenu(newFounder);
//    }
//
//    // EFFECTS: gets the use to choose from an enum of organization purposes
//    private Purpose getPurpose() {
//        System.out.println("Choose Your Purpose: ");
//        System.out.println("HEALTH (press 1)");
//        System.out.println("EDUCATION (press 2)");
//        System.out.println("UNDERPRIVILEGED (press 3)");
//        System.out.println("ANIMALS (press 4)");
//        return setPurpose();
//    }
//
//    // EFFECTS: assigns the purpose based on the input of the user
//    private Purpose setPurpose() {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        Purpose inputPurpose = Purpose.EDUCATION;
//        switch (command) {
//            case 1:
//                inputPurpose = Purpose.HEALTH;
//                break;
//            case 2:
//                inputPurpose = Purpose.EDUCATION;
//                break;
//            case 3:
//                inputPurpose = Purpose.UNDERPRIVILEGED;
//                break;
//            case 4:
//                inputPurpose = Purpose.ANIMALS;
//                break;
//            default:
//                System.out.println("Wrong Command");
//                setPurpose();
//        }
//        return inputPurpose;
//    }
//
//    // EFFECTS: displays the Menu for a founder user and allows the user to choose between managing events,
//    volunteers,
//    // and applicants
//    private void displayFounderMenu(Founder founderUser) {
//        System.out.println("Welcome " + founderUser.getFounderName() + "!");
//        System.out.println("Your Age: " + founderUser.getFounderAge() + ",  Your City: "
//                + founderUser.getFounderCity());
//        System.out.println("Your Organization: " + founderUser.getFounderOrganization().getOrganizationName());
//
//        System.out.println("Founder Functions: ");
//        System.out.println("Manage Events (press 1)");
//        System.out.println("Manage Volunteers (press 2)");
//        System.out.println("Manage Applicants (press 3)");
//        System.out.println("Go Back To Main Menu (press 4)");
//        founderMenuHelper(founderUser);
//    }
//
//    // EFFECTS: helper that takes in the input for the founder menu
//    private void founderMenuHelper(Founder founderUser) {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                manageEventsMenu(founderUser);
//                break;
//            case 2:
//                manageVolunteersMenu(founderUser);
//                break;
//            case 3:
//                manageApplicantsMenu(founderUser);
//                break;
//            case 4:
//                displayMenu();
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: creates a volunteer with the input name, age, a
//    private void displayVolunteerSignupPage(String nameInput, int ageInput, String cityInput) {
//        System.out.println("Enter Your Time Commitment: ");
//        String timeInput = scanner.nextLine();
//        int time = Integer.parseInt(timeInput);
//        Volunteer newVolunteer = new Volunteer(nameInput, ageInput, cityInput, time);
//        database.addVolunteer(newVolunteer);
//        applyForOrganizationsMenu(newVolunteer);
//        displayVolunteerMenu(newVolunteer);
//    }
//
//    // EFFECTS: displays the menu for the volunteer to apply to an organization
//    private void applyForOrganizationsMenu(Volunteer newVolunteer) {
//        System.out.println("Choose Organization to Apply For: ");
//        for (Organization organization : database.getOrganizations()) {
//            System.out.println(organization.getOrganizationName());
//        }
//        System.out.println("Select using Name (press 1)");
//        System.out.println("Filter by Your City (press 2)");
//        System.out.println("Filter by Purpose (press 3)");
//        System.out.println("Filter by Time Commitment (press 4)");
//        System.out.println("Go Back To Database Menu (press 5)");
//        applyHelper(newVolunteer);
//    }
//
//    // EFFECTS: helper in displaying the menu for the volunteer to apply to an organization
//    private void applyHelper(Volunteer newVolunteer) {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switchApplyMenu(newVolunteer, command);
//    }
//
//    // EFFECTS: switch case for the applying menu for a volunteer
//    private void switchApplyMenu(Volunteer newVolunteer, int command) {
//        switch (command) {
//            case 1:
//                applyUsingName(newVolunteer);
//                break;
//            case 2:
//                getOrganizationsByCity(newVolunteer);
//                applyUsingName(newVolunteer);
//                break;
//            case 3:
//                getOrganizationsByPurpose();
//                applyUsingName(newVolunteer);
//                break;
//            case 4:
//                getOrganizationsByTime(newVolunteer);
//                applyUsingName(newVolunteer);
//                break;
//            case 5:
//                displayDatabaseMenu();
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays organizations that have a time commitment requirements less then that of the volunteer
//    private void getOrganizationsByTime(Volunteer newVolunteer) {
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationMinHours() <= (newVolunteer.getVolunteerMaxHours())) {
//                System.out.println(organization.getOrganizationName());
//            }
//        }
//    }
//
//    // EFFECTS: displays organizations that have the same purpose as that of the volunteer
//    private void getOrganizationsByPurpose() {
//        Purpose inputPurpose = getPurpose();
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationPurpose().equals(inputPurpose)) {
//                System.out.println(organization.getOrganizationName());
//            }
//        }
//    }
//
//    // EFFECTS: displays the organizations that are in the same cit as tat of the volunteer
//    private void getOrganizationsByCity(Volunteer newVolunteer) {
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationCity().equals(newVolunteer.getVolunteerCity())) {
//                System.out.println(organization.getOrganizationName());
//            }
//        }
//    }
//
//    // EFFECTS: changes the status of the volunteer, adds the given volunteer to the applications of the applied
//    // organization by checking by the name
//    private void applyUsingName(Volunteer newVolunteer) {
//        System.out.println("Enter Name: ");
//        String organizationName = scanner.nextLine();
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationName().equals(organizationName)) {
//                if (newVolunteer.getVolunteerMaxHours() >= organization.getOrganizationMinHours()) {
//                    newVolunteer.apply(organization);
//                    displayVolunteerMenu(newVolunteer);
//                } else {
//                    System.out.println("Your working hours aren't enough");
//                    applyForOrganizationsMenu(newVolunteer);
//                }
//            }
//        }
//    }
//
//    // EFFECTS: displays the login menu for the volunteer or founder depending on type
//    private void displayLoginPage(MemberType type) {
//        System.out.println("Enter Your User's Name From The Following List: ");
//        if (type == MemberType.FOUNDER) {
//            founderLoginHelper();
//        } else if (type == MemberType.VOLUNTEER) {
//            volunteerLoginHelper();
//        }
//
//    }
//
//    // EFFECTS: helper to the login page for the volunteers
//    private void volunteerLoginHelper() {
//        LinkedList<String> names = new LinkedList<>();
//        for (Volunteer volunteer : database.getVolunteers()) {
//            names.add(volunteer.getVolunteerName());
//            System.out.println(volunteer.getVolunteerName());
//        }
//        String nameInput = scanner.nextLine();
//        if (names.contains(nameInput)) {
//            for (Volunteer volunteer : database.getVolunteers()) {
//                if (volunteer.getVolunteerName().equals(nameInput)) {
//                    displayVolunteerMenu(volunteer);
//                }
//            }
//        } else {
//            retryLogin(MemberType.VOLUNTEER);
//        }
//    }
//
//    // EFFECTS: helper to the login page for the founders
//    private void founderLoginHelper() {
//        LinkedList<String> names = new LinkedList<>();
//        for (Founder founder : database.getFounders()) {
//            names.add(founder.getFounderName());
//            System.out.println(founder.getFounderName());
//        }
//        String nameInput = scanner.nextLine();
//        if (names.contains(nameInput)) {
//            for (Founder founder : database.getFounders()) {
//                if (founder.getFounderName().equals(nameInput)) {
//                    displayFounderMenu(founder);
//                }
//            }
//        } else {
//            retryLogin(MemberType.FOUNDER);
//        }
//    }
//
//    // EFFECTS: displayed when a user that doesn't exist is entered, repeats the process by displaying Login Page
//    private void retryLogin(MemberType type) {
//        System.out.println("The User Entered Doesn't Exist: ");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                displayLoginPage(type);
//                break;
//            case 2:
//                displayUserMenu(type);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays info page for the volunteer and allows the volunteer to apply to organizations
//    private void displayVolunteerMenu(Volunteer volunteerUser) {
//        System.out.println("Welcome " + volunteerUser.getVolunteerName() + "!");
//        System.out.println("Your Age: " + volunteerUser.getVolunteerAge() + ",  Your City: "
//                + volunteerUser.getVolunteerCity());
//        System.out.println("Total Rewards: " + volunteerUser.computeTotalRewards());
//
//        if (volunteerUser.getVolunteerStatus() == Status.ACCEPTED) {
//            getVolunteerOrganization(volunteerUser);
//            System.out.println("Press y to go back");
//            String line1 = scanner.nextLine();
//            if (line1.equals("y")) {
//                displayMenu();
//            }
//        } else if (volunteerUser.getVolunteerStatus() == Status.PENDING) {
//            getApplicantOrganization(volunteerUser);
//        } else {
//            System.out.println("You Haven't applied to any Organizations");
//            applyForOrganizationsMenu(volunteerUser);
//        }
//        String line2 = scanner.nextLine();
//        if (line2.equals("y")) {
//            displayMenu();
//        }
//    }
//
//    // EFFECTS: displays the status of the application for the volunteer
//    private void getApplicantOrganization(Volunteer volunteerUser) {
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationApplications().contains(volunteerUser)) {
//                System.out.println("Your Application at " + organization.getOrganizationName()
//                        + " has still not been accepted");
//            }
//        }
//    }
//
//    // EFFECTS: displays he organization of the given volunteer
//    private void getVolunteerOrganization(Volunteer volunteerUser) {
//        for (Organization organization : database.getOrganizations()) {
//            if (organization.getOrganizationMembers().contains(volunteerUser)) {
//                System.out.println("Your Organization: " + organization.getOrganizationName());
//            }
//        }
//    }
//
//    // EFFECTS: displays the menu for the founder that lets it accept applicant volunteers
//    private void manageApplicantsMenu(Founder founderUser) {
//        System.out.println("Applicant Details:");
//        for (Volunteer volunteerApplicant : founderUser.getFounderOrganization().getOrganizationApplications()) {
//            System.out.println(volunteerApplicant.getVolunteerName());
//        }
//        System.out.println("Applicant Options: ");
//        System.out.println("Accept an Applicant (press 1): ");
//        System.out.println("Go Back (press 2)");
//        manageApplicantsHelper(founderUser);
//    }
//
//    // EFFECTS: helper to the manage applicants menu
//    private void manageApplicantsHelper(Founder founderUser) {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                acceptApplicantVolunteer(founderUser);
//                break;
//            case 2:
//                displayFounderMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: takes in th name of the volunteer that needs to be accepted and in case it exists, accepts the enterd
//    // volunteer and adds it to the list of volunteers in the organization
//    private void acceptApplicantVolunteer(Founder founderUser) {
//        System.out.println("Which Applicant Would You Like To Accept? ");
//        LinkedList<String> applicantNames = new LinkedList<>();
//        for (Volunteer volunteerApplicant : founderUser.getFounderOrganization().getOrganizationApplications()) {
//            applicantNames.add(volunteerApplicant.getVolunteerName());
//        }
//        String applicantName = scanner.nextLine();
//        if (applicantNames.contains(applicantName)) {
//            for (Volunteer applicantVolunteer : founderUser.getFounderOrganization().getOrganizationApplications()) {
//                if (applicantVolunteer.getVolunteerName().equals(applicantName)) {
//                    founderUser.acceptApplication(applicantVolunteer);
//                }
//            }
//        } else {
//            retryAccept(founderUser);
//        }
//    }
//
//    // EFFECT: retries the process of accepting a volunteer in case the one entered doesn't exist in the database
//    private void retryAccept(Founder founderUser) {
//        System.out.println("The Applicant Entered Doesn't Exist ");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                acceptApplicantVolunteer(founderUser);
//                break;
//            case 2:
//                manageApplicantsMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays the volunteers in the organization and allows the founder to add or promote volunteers
//    private void manageVolunteersMenu(Founder founderUser) {
//        System.out.println("Volunteer Details:");
//        for (Volunteer volunteer : founderUser.getFounderOrganization().getOrganizationMembers()) {
//            System.out.println(volunteer.getVolunteerName() + ": Exec? "
//                    + volunteer.isExec());
//        }
//        System.out.println("Volunteer Options: ");
//        System.out.println("Add Volunteers to an Event (press 1): ");
//        System.out.println("Promote Volunteer (press 2)");
//        System.out.println("Go Back (press 3)");
//        manageVolunteerHelper(founderUser);
//    }
//
//    // EFFECTS: helper to the manage volunteer menu
//    private void manageVolunteerHelper(Founder founderUser) {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                getEventByName(founderUser);
//                break;
//            case 2:
//                promoteVolunteer(founderUser);
//                break;
//            case 3:
//                displayFounderMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays all the volunteers in the organization and promotes the one entered in case he/she fulfills
//    // the requirements for the executive position
//    private void promoteVolunteer(Founder founderUser) {
//        System.out.println("Which Volunteer would you like to promote? ");
//        LinkedList<String> volunteerNames = new LinkedList<>();
//        for (Volunteer volunteer : founderUser.getFounderOrganization().getFilteredExecutives(false)) {
//            volunteerNames.add(volunteer.getVolunteerName());
//        }
//        String volunteerLine = scanner.nextLine();
//        if (volunteerNames.contains(volunteerLine)) {
//            for (Volunteer volunteer : founderUser.getFounderOrganization().getFilteredExecutives(false)) {
//                if (volunteer.getVolunteerName().equals(volunteerLine)) {
//                    if (volunteer.getVolunteerHoursWorked() > 5) {
//                        founderUser.promoteVolunteer(volunteer);
//                        manageVolunteersMenu(founderUser);
//                    } else {
//                        System.out.println("Volunteer hasn't worked enough hours (5)");
//                        promoteVolunteer(founderUser);
//                    }
//                }
//            }
//        } else {
//            retryPromote(founderUser);
//        }
//    }
//
//    // EFFECTS: retries getting the volunteer that needs to be promoted in case the one entered doesn't exist
//    private void retryPromote(Founder founderUser) {
//        System.out.println("The Volunteer Entered Doesn't Exist ");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                promoteVolunteer(founderUser);
//                break;
//            case 2:
//                manageVolunteersMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays the menu for the founder and allows him/her to create or select an existing event
//    private void manageEventsMenu(Founder founderUser) {
//        for (Event event : founderUser.getFounderOrganization().getOrganizationEvents()) {
//            System.out.println(event.getEventName());
//        }
//        System.out.println("Event Options:");
//        System.out.println("Select an Existing Event (press 1)");
//        System.out.println("Create New Event (CAD 500) (press 2)");
//        System.out.println("Go Back (press 3)");
//        manageEventsMenuHelper(founderUser);
//    }
//
//    // EFFECTS: helper to the manage events menu
//    private void manageEventsMenuHelper(Founder founderUser) {
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                getEventByName(founderUser);
//                break;
//            case 2:
//                if (founderUser.getFounderOrganization().getOrganizationFunds() >= 100) {
//                    createEventHelper(founderUser);
//                } else {
//                    System.out.println("Not Enough Funds");
//                    manageEventsMenu(founderUser);
//                }
//                break;
//            case 3:
//                displayFounderMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays all the events in the organization and then asks th user to enter the name of the one whose
//    // details need to be shown
//    private void getEventByName(Founder founderUser) {
//        LinkedList<String> eventNames = new LinkedList<>();
//        for (Event event : founderUser.getFounderOrganization().getOrganizationEvents()) {
//            eventNames.add(event.getEventName());
//        }
//        System.out.println("Enter Event Name: ");
//        String eventName = scanner.nextLine();
//        if (eventNames.contains(eventName)) {
//            LinkedList<Event> events = founderUser.getFounderOrganization().getOrganizationEvents();
//            for (Event nextEvent : events) {
//                if (nextEvent.getEventName().equals(eventName)) {
//                    showEventDetails(founderUser, nextEvent);
//                }
//            }
//        } else {
//            retryGetEvent(founderUser);
//        }
//    }
//
//    // EFFECTS: retries getting the event in case the one entered doesn't exist in the organization
//    private void retryGetEvent(Founder founderUser) {
//        System.out.println("The Event Entered Doesn't Exist");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                getEventByName(founderUser);
//                break;
//            case 2:
//                manageEventsMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays all the details of the given event
//    private void showEventDetails(Founder founderUser, Event event) {
//        System.out.println("Event Details: ");
//        System.out.println("Name: " + event.getEventName() + " , Executive Responsible: "
//                + event.getEventExecutive().getVolunteerName() + ", Hours: " + event.getEventDuration());
//        System.out.println("Number of Volunteers Required: " + event.getNumVolunteers()
//                + ", Number of Volunteers Assigned: " + event.getEventVolunteers().size());
//        System.out.println("Assigned Volunteers: ");
//        for (Volunteer volunteer : event.getEventVolunteers()) {
//            System.out.println(volunteer.getVolunteerName());
//        }
//        eventDetailsMenu(founderUser, event);
//    }
//
//    // EFFECTS: displays the details of the given event and allows the founder to add volunteers or go back to menu
//    private void eventDetailsMenu(Founder founderUser, Event event) {
//        System.out.println("Add Volunteers (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                addVolunteer(founderUser, event);
//                break;
//            case 2:
//                manageEventsMenu(founderUser);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: adds the entered volunteer to the given event in case the volunteer fulfills requirements
//    private void addVolunteer(Founder founderUser, Event event) {
//        System.out.println("Which Volunteer would you like to add? ");
//
//        LinkedList<String> volunteerNames = new LinkedList<>();
//        for (Volunteer volunteer : founderUser.getFounderOrganization().getFilteredExecutives(false)) {
//            System.out.println(volunteer.getVolunteerName()
//                    + ", Remaining Hours: "
//                    + volunteer.getVolunteerRemainingHours());
//            volunteerNames.add(volunteer.getVolunteerName());
//        }
//        String volunteerLine = scanner.nextLine();
//        if (volunteerNames.contains(volunteerLine)) {
//            for (Volunteer volunteer : founderUser.getFounderOrganization().getFilteredExecutives(false)) {
//                if (volunteer.getVolunteerName().equals(volunteerLine)) {
//                    if (volunteer.getVolunteerRemainingHours() > event.getEventDuration()) {
//                        founderUser.addVolunteer(volunteer, event);
//                    } else {
//                        System.out.println("Volunteer doesn't have enough time");
//                    }
//                    eventDetailsMenu(founderUser, event);
//                }
//            }
//        } else {
//            retryAdd(founderUser, event);
//        }
//    }
//
//    // EFFECTS: retries adding volunteer to the event in case the one entered doesn't exist
//    private void retryAdd(Founder founderUser, Event event) {
//        System.out.println("The Volunteer Entered Doesn't Exist");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Go Back (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                addVolunteer(founderUser, event);
//                break;
//            case 2:
//                showEventDetails(founderUser, event);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: displays the event creation menu and helps in creating an event from the different fields entered
//    private void createEventHelper(Founder founderUser) {
//        System.out.println("Event Creation Menu: ");
//        System.out.println("Enter Event Name: ");
//        String nameInput = scanner.nextLine();
//        System.out.println("Enter Time Requirement (in hours): ");
//        String timeInput = scanner.nextLine();
//        int time = Integer.parseInt(timeInput);
//        System.out.println("Enter Number of Volunteers: ");
//        String numVolunteersInput = scanner.nextLine();
//        int numVolunteers = Integer.parseInt(numVolunteersInput);
//        Event newEvent = new Event(nameInput, time, numVolunteers);
//        founderUser.createEvent(newEvent);
//        getExecutiveByName(founderUser, newEvent);
//    }
//
//    // EFFECTS: returns a list of all the executives in the organizations and if the entered name is same, sets the
//    // executive to the event
//    private void getExecutiveByName(Founder founderUser, Event event) {
//        LinkedList<String> execNames = new LinkedList<>();
//        for (Volunteer exec : founderUser.getFounderOrganization().getFilteredExecutives(true)) {
//            execNames.add(exec.getVolunteerName());
//        }
//        System.out.println("Enter Name of Executive: ");
//        String execName = scanner.nextLine();
//        if (execNames.contains(execName)) {
//            LinkedList<Volunteer> execs = founderUser.getFounderOrganization().getFilteredExecutives(true);
//            for (Volunteer nextExec : execs) {
//                if (nextExec.getVolunteerName().equals(execName)) {
//                    event.setEventExecutive(nextExec);
//                    nextExec.addEvent();
//                    showEventDetails(founderUser, event);
//                }
//            }
//        } else {
//            retryGetExecutive(founderUser, event);
//        }
//    }
//
//    // EFFECTS: retries the process adding an executive to the event
//    private void retryGetExecutive(Founder founderUser, Event event) {
//        System.out.println("The Executive Entered Doesn't Exist");
//        System.out.println("Try Again (press 1)");
//        System.out.println("Assign Default Executive and Continue (press 2)");
//        String line = scanner.nextLine();
//        int command = 0;
//        command = getCommand(line, command);
//        switch (command) {
//            case 1:
//                getExecutiveByName(founderUser, event);
//                break;
//            case 2:
//                event.setEventExecutive(founderUser.getFounderOrganization().getFilteredExecutives(true).get(0));
//                showEventDetails(founderUser, event);
//                break;
//            default:
//                System.out.println("Wrong Command");
//        }
//    }
//
//    // EFFECTS: saves the database to file
//    private void saveDatabase() {
//        try {
//            for (Founder founder : database.getFounders()) {
//                database.addOrganization(founder.getFounderOrganization());
//            }
//
//            jsonWriter.open();
//            jsonWriter.write(database);
//            jsonWriter.close();
//            System.out.println("Saved " + database.getDatabaseName() + " to " + JSON_STORE);
//        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file: " + JSON_STORE);
//        }
//    }
//
//    // MODIFIES: this
//    // EFFECTS: loads database from file
//    private void loadDatabase() {
//        try {
//            database = jsonReader.read();
//            System.out.println("Loaded " + database.getDatabaseName() + " from " + JSON_STORE);
//        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
//        }
//    }
//}
