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

// menu that allows the founder user to accept applicants
public class ManageApplicantsMenu extends JFrame {

    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel applicantFunctions = new JLabel();
    JButton acceptApplicationButton = new JButton();
    JButton backButton = new JButton();

    private final Database database;
    DatabaseManagerGUI dbGui;
    Founder founder;

    // initializes the Menu with all the required option buttons and menu bar
    public ManageApplicantsMenu(Database database, Founder founder) {

        frame = new JFrame("Manage Applicants Menu");
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


    // EFFECTS: creates the main content panel that consists of all the buttons and information
    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        contentPane.setLayout(new GridLayout(6, 0));

        //---- applicantFunctions ----
        applicantFunctions.setText("What would you like to do?");
        applicantFunctions.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(applicantFunctions);

        //---- acceptApplicationButton ----
        acceptApplicationButton.setText("Accept an Applicant");
        acceptApplicationButton.setActionCommand("accept");
        acceptApplicationButton.setSize(500, 30);
        acceptApplicationButton.addActionListener(this::applicantFunctionListener);
        contentPane.add(acceptApplicationButton);

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::applicantFunctionListener);
        contentPane.add(backButton);
        return contentPane;
    }

    // EFFECTS: based on the input, checks if the application database isn't empty and finally displays the
    // ApplicantsVolunteersDisplay menu
    public void applicantFunctionListener(ActionEvent e) {
        if ("accept".equals(e.getActionCommand())) {
            if (!database.getOrganization(founder.getFounderOrganization().getOrganizationName())
                    .getOrganizationApplications().isEmpty()) {
                frame.setVisible(false);
                new ApplicantsVolunteersDisplay(database, founder, false, frame);
            } else {
                Clip clip;
                try {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("./assets/errorSound.wav"));
                    clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    clip.start();
                } catch (Exception f) {
                    System.out.println("Cannot play audio");
                }
                JOptionPane.showMessageDialog(frame, "The Event Database is Empty");
            }
        } else {
            frame.setVisible(false);
            new FounderMenu(database, founder);
        }
    }
}

