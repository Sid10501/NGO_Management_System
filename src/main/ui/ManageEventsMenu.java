package ui;

import model.Database;
import model.Founder;
import ui.utilities.MemberType;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

// the menu that allows the user to select existing ents
public class ManageEventsMenu extends JFrame {

    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel eventFunction = new JLabel();
    JButton existingEventButton = new JButton();
    JButton createEventButton = new JButton();
    JButton backButton = new JButton();

    private final Database database;
    DatabaseManagerGUI dbGui;
    Founder founder;

    public ManageEventsMenu(Database database, Founder founder) {

        frame = new JFrame("Manage Events Menu");
        this.database = database;
        this.dbGui = new DatabaseManagerGUI(database, MemberType.FOUNDER);
        dbGui.frame.setVisible(false);
        this.founder = founder;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(dbGui.createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }


    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        contentPane.setLayout(new GridLayout(4, 0));

        //---- eventFunction ----
        eventFunction.setText("Select Your Founder Function");
        eventFunction.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(eventFunction);

        //---- existingEventButton ----
        existingEventButton.setText("Select An Existing Event");
        existingEventButton.setActionCommand("existing");
        existingEventButton.setSize(500, 10);
        existingEventButton.addActionListener(this::eventFunctionListener);
        contentPane.add(existingEventButton);

        //---- createEventButton ----
        createEventButton.setText("Create New Event");
        createEventButton.setActionCommand("new");
        createEventButton.setSize(500, 30);
        createEventButton.addActionListener(this::eventFunctionListener);
        contentPane.add(createEventButton);

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setActionCommand("back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::eventFunctionListener);
        contentPane.add(backButton);

        return contentPane;
    }

    public void eventFunctionListener(ActionEvent e) {
        if ("existing".equals(e.getActionCommand())) {
            if (!founder.getFounderOrganization().getOrganizationEvents().isEmpty()) {
                new ExistingEventsDisplay(database, founder);
                frame.setVisible(false);
            } else {
                playErrorSound();
                JOptionPane.showMessageDialog(frame, "The Event Database is Empty");
            }
        } else if ("new".equals(e.getActionCommand())) {
            createNewEvent();
        } else {
            new FounderMenu(database, founder);
            frame.setVisible(false);
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

    private void createNewEvent() {
        if (founder.getFounderInvestment() >= 500) {
            new EventCreationMenu(database, founder, frame);
            frame.setVisible(false);
        } else {
            playErrorSound();
            JOptionPane.showMessageDialog(frame, "You don't have sufficient funds");
        }
    }
}
