package ui;

import model.Database;
import model.Event;
import model.Organization;
import model.Volunteer;
import ui.utilities.MemberType;
import model.enums.Status;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

// a menu that displays all the information of the volunteer
public class VolunteerMenu extends JFrame {
    private final Database database;
    DatabaseManagerGUI dbGui;
    Volunteer volunteer;
    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel welcomeVolunteer = new JLabel();
    JLabel volunteerAge = new JLabel();
    JLabel volunteerCity = new JLabel();
    JLabel volunteerOrganization = new JLabel();
    JLabel volunteerRewards = new JLabel();
    JLabel volunteerHours = new JLabel();
    JLabel volunteerPosition = new JLabel();
    JLabel volunteerEvents = new JLabel();

    JButton backButton = new JButton();
    JButton applyButton = new JButton();

    public VolunteerMenu(Database database, Volunteer volunteer) {
        dbGui = new DatabaseManagerGUI(database, MemberType.VOLUNTEER);
        dbGui.frame.setVisible(false);
        frame = new JFrame("Volunteer Menu");
        this.database = database;
        this.volunteer = volunteer;
        displayVolunteerMenu();
    }

    private void displayVolunteerMenu() {
        frame.setJMenuBar(dbGui.createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }


    public JPanel createContentPane() {

        contentPane.setLayout(new GridLayout(10, 0));

        displayPersonalDetails();

        //---- volunteerOrganization ----
        displayVolunteerOrganization();

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setActionCommand("back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::volunteerListener);
        contentPane.add(backButton);

        return contentPane;
    }

    private void displayPersonalDetails() {
        //---- welcomeVolunteer ----
        welcomeVolunteer.setText("Welcome " + volunteer.getVolunteerName() + "!");
        welcomeVolunteer.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeVolunteer);

        //---- volunteerAge ----
        volunteerAge.setText("Your Age: " + volunteer.getVolunteerAge());
        volunteerAge.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerAge);

        //---- volunteerCity ----
        volunteerCity.setText("Your City: " + volunteer.getVolunteerCity());
        volunteerCity.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerCity);

        //---- volunteerRewards ----
        volunteerRewards.setText("Your Earnings: " + volunteer.computeTotalRewards());
        volunteerRewards.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerRewards);

        //---- volunteerPosition ----
        volunteerPosition.setText("Your Position: " + (volunteer.isExec() ? "Executive Volunteer"
                : "General Volunteer"));
        volunteerPosition.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerPosition);

        //---- volunteerHours ----
        volunteerHours.setText("Volunteering Hours Completed: " + volunteer.getVolunteerHoursWorked() + "/"
                + volunteer.getVolunteerMaxHours());
        volunteerHours.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerHours);

        //---- volunteerEvents ----
        volunteerEvents.setText("Your Events: " + getVolunteerEvents(volunteer));
        volunteerEvents.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerEvents);
    }

    private void displayVolunteerOrganization() {
        if (volunteer.getVolunteerStatus() == Status.ACCEPTED) {
            volunteerOrganization.setText("Your Organization: " + getVolunteerOrganization().getOrganizationName());
            volunteerOrganization.setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(volunteerOrganization);

        } else if (volunteer.getVolunteerStatus() == Status.PENDING) {
            volunteerOrganization.setText("Your Application at " + getApplicantOrganization()
                    + " has still not been accepted");
            volunteerOrganization.setHorizontalAlignment(SwingConstants.CENTER);
            contentPane.add(volunteerOrganization);
        } else {
            volunteerOrganization.setText("You Haven't applied to any Organizations");
            volunteerOrganization.setHorizontalAlignment(SwingConstants.CENTER);
            applyButton.setText("Apply to an Organization");
            applyButton.setActionCommand("apply");
            applyButton.setSize(500, 10);
            applyButton.addActionListener(this::volunteerListener);
            contentPane.add(volunteerOrganization);
            contentPane.add(applyButton);
        }
    }


    // EFFECTS: returns the organization of the given volunteer
    private Organization getVolunteerOrganization() {
        for (Organization organization : database.getOrganizations()) {
            for (Volunteer volunteerMember : organization.getOrganizationMembers()) {
                if (volunteerMember.getVolunteerName().equals(volunteer.getVolunteerName())) {
                    return organization;
                }
            }
        }
        return null;
    }

    // EFFECTS: returns the organization of the given volunteer
    private StringBuilder getVolunteerEvents(Volunteer volunteerUser) {
        StringBuilder eventList = new StringBuilder();

        if (volunteerUser.getVolunteerStatus().equals(Status.ACCEPTED)) {
            eventList = new StringBuilder();
            for (Event event : getVolunteerOrganization().getOrganizationEvents()) {
                if (event.getEventVolunteers().contains(volunteerUser)) {
                    eventList.append(event.getEventName());
                }
            }
        }
        return eventList;
    }

    // EFFECTS: returns the name of the applicant's organization
    private String getApplicantOrganization() {
        for (Organization organization : database.getOrganizations()) {
            if (organization.getOrganizationApplications().contains(volunteer)) {
                return organization.getOrganizationName();
            }
        }
        return null;
    }

    public void volunteerListener(ActionEvent e) {
        if ("apply".equals(e.getActionCommand())) {
            if (database.getOrganizations().isEmpty()) {
                playErrorSound();
                JOptionPane.showMessageDialog(frame, "The organization database is empty");
            } else {
                frame.setVisible(false);
                new ExistingOrganizationsDisplay(database, volunteer);
            }
        } else {
            frame.setVisible(false);
            new DatabaseManagerGUI(database, MemberType.VOLUNTEER);
        }
    }

    private void playErrorSound() {
        Clip clip;
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("./assets/errorSound.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception f) {
            System.out.println("Cannot play audio");
        }
    }

}
