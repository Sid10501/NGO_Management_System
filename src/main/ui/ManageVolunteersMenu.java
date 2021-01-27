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

public class ManageVolunteersMenu extends JFrame {

    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel welcomeMessage = new JLabel();
    JLabel volunteerManagementFunctions = new JLabel();
    JButton promoteVolunteerButton = new JButton();
    JButton backButton = new JButton();
    DatabaseManagerGUI dbGui;
    private final Database database;
    Founder founder;

    public ManageVolunteersMenu(Database database, Founder founder) {
        dbGui = new DatabaseManagerGUI(database, MemberType.FOUNDER);
        dbGui.frame.setVisible(false);
        frame = new JFrame("Manage Volunteers Menu");
        this.founder = founder;
        this.database = database;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(dbGui.createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        contentPane.setLayout(new GridLayout(6, 0));

        //---- founderDetails ----
        welcomeMessage.setText("Welcome to Manage Volunteers Menu!");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeMessage);

        //---- volunteerManagementFunctions ----
        volunteerManagementFunctions.setText("Select Your Task");
        volunteerManagementFunctions.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(volunteerManagementFunctions);

        //---- promoteVolunteerButton ----
        promoteVolunteerButton.setText("Promote Volunteer");
        promoteVolunteerButton.setActionCommand("promote");
        promoteVolunteerButton.setSize(500, 30);
        promoteVolunteerButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(promoteVolunteerButton);

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setActionCommand("back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(backButton);

        return contentPane;
    }

    public void founderFunctionsListener(ActionEvent e) {
        if ("promote".equals(e.getActionCommand())) {
            if (founder.getFounderOrganization().getFilteredExecutives(false).isEmpty()) {
                playErrorSound();
                JOptionPane.showMessageDialog(frame, "The Database doesn't have any Promotable Volunteers");
            } else {
                new ApplicantsVolunteersDisplay(database, founder, true, frame);
                frame.setVisible(false);
            }
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

}

