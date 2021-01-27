package ui;

import model.Database;
import model.Event;
import model.Founder;
import ui.utilities.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventCreationMenu extends JFrame implements ActionListener {

    private final Database database;
    private final Founder founder;

    private final JFrame frame;
    JFrame previousFrame;
    JLabel founderDetails = new JLabel("Enter Event Details:");
    JTextField nameField;
    JFormattedTextField timeCommitmentField;
    JFormattedTextField numVolunteersField;
    boolean creationComplete = false;
    static final int GAP = 10;

    // initiates the database, and founder, and displays the event creation menu
    public EventCreationMenu(Database database, Founder founder, JFrame prevFrame) {
        this.database = database;
        this.founder = founder;
        this.previousFrame = prevFrame;

        JPanel founderFields = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE,
                        pref.height);
            }
        };

        founderFields.setLayout(new BoxLayout(founderFields, BoxLayout.PAGE_AXIS));
        founderFields.add(founderDetails);
        founderFields.add(createEntryFields());
        founderFields.add(createButtons());

        this.frame = new JFrame("Event Creation Menu");
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add contents to the window.
        this.frame.add(founderFields);

        //Display the window.
        this.frame.setSize(1000, 600);
        this.frame.pack();
        this.frame.setVisible(true);
    }

    // EFFECTS: creates the button panel for the menu
    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton button = new JButton("Create Event");
        button.setActionCommand("create");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear Fields");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);
        return panel;
    }

    // Called when the user clicks the button or presses Enter in a text field.
    // EFFECTS: creates an event or clears all the fields
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            creationComplete = false;
            nameField.setText("");
            timeCommitmentField.setValue("");
            numVolunteersField.setValue("");
        } else {
            creationComplete = true;
            createEvent();
        }
    }

    // EFFECTS: helper for creating an event
    protected void createEvent() {
        if (creationComplete) {
            Event newEvent = new Event(nameField.getText(), Integer.parseInt(timeCommitmentField.getText()),
                    Integer.parseInt(numVolunteersField.getText()));
            this.frame.setVisible(false);
            this.previousFrame.dispose();
            new ExistingVolunteersDisplay(database, newEvent, founder, true);
        }
    }


    // EFFECTS: creates the text entry fields for the event information
    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {"Event Name: ", "Time Commitment Required: ", "Number of Volunteers: "};
        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        nameField = new JTextField();
        nameField.setColumns(20);
        fields[fieldNum++] = nameField;

        timeCommitmentField = new JFormattedTextField();
        fields[fieldNum++] = timeCommitmentField;

        numVolunteersField = new JFormattedTextField();
        fields[fieldNum] = numVolunteersField;

        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i]);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf;
            tf = (JTextField) fields[i];
            tf.addActionListener(this);
        }

        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, GAP, GAP, GAP, GAP / 2);
        return panel;
    }


}
