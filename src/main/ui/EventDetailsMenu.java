package ui;

import model.*;
import model.Event;
import ui.utilities.MemberType;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

// Event Details menu that provides all event details and allows the founder to add volunteers
public class EventDetailsMenu extends JFrame {

    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel eventName = new JLabel();
    JLabel eventExecutive = new JLabel();
    JLabel eventTimeCommitment = new JLabel();
    JLabel eventAvailability = new JLabel();
    JLabel eventVolunteers = new JLabel();

    JButton addVolunteersButton = new JButton();
    JButton backButton = new JButton();

    private final Database database;
    Founder founder;
    Event event;
    DatabaseManagerGUI dbGui;

    // MODIFIES: this
    // EFFECTS: creates and displays an event details menu
    public EventDetailsMenu(Database database, Event event, Founder founder) {

        frame = new JFrame("Event Details Menu");
        this.database = database;
        this.founder = founder;
        this.event = event;
        dbGui = new DatabaseManagerGUI(database, MemberType.FOUNDER);
        dbGui.frame.setVisible(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    // EFFECTS: creates a menu bar for the event details menu
    public JMenuBar createMenuBar() {
        return dbGui.createMenuBar();
    }

    // EFFECTS: creates the main panel that consists of all the labels and buttons
    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        contentPane.setLayout(new GridLayout(7, 0));
        displayEventDetails();

        //---- addVolunteersButton ----
        addVolunteersButton.setText("Add Volunteers");
        addVolunteersButton.setActionCommand("add");
        addVolunteersButton.setSize(500, 10);
        addVolunteersButton.addActionListener(this::eventFunctionListener);
        contentPane.add(addVolunteersButton);

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setActionCommand("back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::eventFunctionListener);
        contentPane.add(backButton);

        return contentPane;
    }

    // EFFECTS: displays all the JLabels that consist of the event details
    private void displayEventDetails() {
        //---- eventName ----
        eventName.setText(event.getEventName() + " Details:");
        eventName.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventName);

        //---- eventExecutive ----
        eventExecutive.setText("Executive Responsible" + event.getEventExecutive().getVolunteerName());
        eventExecutive.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventExecutive);

        //---- eventTimeCommitment ----
        eventTimeCommitment.setText("Time Commitment: " + event.getEventDuration());
        eventTimeCommitment.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventTimeCommitment);

        //---- eventAvailability ----
        eventAvailability.setText("Volunteer Capacity Filled: " + event.getEventVolunteers().size() + "/"
                + event.getNumVolunteers());
        eventAvailability.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventAvailability);

        //---- eventVolunteers ----
        StringBuilder volunteers = new StringBuilder();
        for (Volunteer volunteer : event.getEventVolunteers()) {
            volunteers.append(volunteer.getVolunteerName());
        }
        eventVolunteers.setText("Current Volunteers: " + volunteers);
        eventVolunteers.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventVolunteers);
    }

    // EFFECTS: an action listener for the buttons which adds a new volunteer to the event
    public void eventFunctionListener(ActionEvent e) {
        if ("add".equals(e.getActionCommand())) {
            if (founder.getFounderOrganization().getFilteredExecutives(false).isEmpty()) {
                Clip clip;
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("./assets/errorSound.wav"));
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } catch (Exception f) {
                    System.out.println("Cannot play audio");
                }
                JOptionPane.showMessageDialog(frame, "The General Volunteer Database is Empty");
            } else {
                new ExistingVolunteersDisplay(database, event, founder, false);
            }
        } else {
            new ManageEventsMenu(database, founder);
        }
        frame.setVisible(false);
    }
}
