package ui;

import model.Database;
import model.Founder;
import ui.utilities.MemberType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class FounderMenu extends JFrame {

    JFrame frame;
    JPanel contentPane = new JPanel();
    JLabel welcomeFounder = new JLabel();
    JLabel founderAge = new JLabel();
    JLabel founderCity = new JLabel();
    JLabel founderOrganization = new JLabel();
    JLabel founderFunds = new JLabel();
    JLabel founderFunction = new JLabel();
    JButton manageEventsButton = new JButton();
    JButton manageVolunteersButton = new JButton();
    JButton manageApplicantsButton = new JButton();
    JButton backButton = new JButton();

    DatabaseManagerGUI dbGui;
    private final Database database;
    private final Founder founder;


    public FounderMenu(Database database, Founder founder) {
        frame = new JFrame("Founder Menu");
        this.dbGui = new DatabaseManagerGUI(database,MemberType.FOUNDER);
        dbGui.frame.setVisible(false);
        this.database = database;
        this.founder = founder;

        displayFounderMenu();
    }

    private void displayFounderMenu() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(dbGui.createMenuBar());
        frame.setContentPane(createContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }



    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        contentPane.setLayout(new GridLayout(10, 0));

        displayPersonalDetails();

        //---- founderFunction ----
        founderFunction.setText("Select Your Founder Function");
        founderFunction.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(founderFunction);

        displayFounderFunctions();

        //---- backButton ----
        backButton.setText("Go Back");
        backButton.setActionCommand("back");
        backButton.setSize(500, 10);
        backButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(backButton);

        return contentPane;
    }

    private void displayFounderFunctions() {
        //---- manageEventsButton ----
        manageEventsButton.setText("Manage Events");
        manageEventsButton.setActionCommand("events");
        manageEventsButton.setSize(500, 10);
        manageEventsButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(manageEventsButton);

        //---- manageVolunteersButton ----
        manageVolunteersButton.setText("Manage Volunteers");
        manageVolunteersButton.setActionCommand("volunteers");
        manageVolunteersButton.setSize(500, 30);
        manageVolunteersButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(manageVolunteersButton);

        //---- manageApplicantsButton ----
        manageApplicantsButton.setText("Manage Applicants");
        manageApplicantsButton.setActionCommand("applicants");
        manageApplicantsButton.setSize(500, 30);
        manageApplicantsButton.addActionListener(this::founderFunctionsListener);
        contentPane.add(manageApplicantsButton);
    }

    private void displayPersonalDetails() {
        //---- welcomeFounder ----
        welcomeFounder.setText("Welcome " + founder.getFounderName() + "!");
        welcomeFounder.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeFounder);

        //---- founderAge ----
        founderAge.setText("Your Age: " + founder.getFounderAge());
        founderAge.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(founderAge);

        //---- founderCity ----
        founderCity.setText("Your City: " + founder.getFounderCity());
        founderCity.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(founderCity);

        //---- founderOrganization ----
        founderOrganization.setText("Your Organization: " + founder.getFounderOrganization().getOrganizationName());
        founderOrganization.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(founderOrganization);

        //---- founderFunds ----
        founderFunds.setText("Funds Left: " + founder.getFounderOrganization().getOrganizationFunds());
        founderFunds.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(founderFunds);
    }

    public void founderFunctionsListener(ActionEvent e) {
        if ("events".equals(e.getActionCommand())) {
            new ManageEventsMenu(database, founder);
        } else if ("volunteers".equals(e.getActionCommand())) {
            new ManageVolunteersMenu(database,founder);
        } else if ("applicants".equals(e.getActionCommand())) {
            new ManageApplicantsMenu(database,founder);
        } else {
            new DatabaseManagerGUI(database, MemberType.FOUNDER);
        }
        frame.setVisible(false);
    }
}
