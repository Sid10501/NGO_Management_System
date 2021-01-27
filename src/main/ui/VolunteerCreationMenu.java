package ui;

import model.Database;
import model.Volunteer;
import ui.utilities.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VolunteerCreationMenu extends JFrame implements ActionListener {

    private final Database database;
    JFrame frame;
    JFrame previousFrame;
    JTextField volunteerName;
    JFormattedTextField volunteerAge;
    JTextField volunteerCity;
    JFormattedTextField volunteerMaxHours;
    Volunteer volunteerUser;
    boolean creationComplete = false;
    JLabel founderDetails;



    public VolunteerCreationMenu(Database database, JFrame previousFrame) {
        this.database = database;

        JPanel volunteerFields = new JPanel() {
            //Don't allow us to stretch vertically.
            public Dimension getMaximumSize() {
                Dimension pref = getPreferredSize();
                return new Dimension(Integer.MAX_VALUE, pref.height);
            }
        };

        founderDetails = new JLabel("Enter Your Information:");
        volunteerFields.setLayout(new BoxLayout(volunteerFields, BoxLayout.PAGE_AXIS));
        volunteerFields.add(founderDetails);
        volunteerFields.add(createEntryFields());
        volunteerFields.add(createButtons());

        this.previousFrame = previousFrame;
        frame = new JFrame("Volunteer Creation Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add contents to the window.
        frame.add(volunteerFields);

        //Display the window.
        frame.setSize(1000, 600);
        frame.pack();
        frame.setVisible(true);
    }

    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton button = new JButton("Create Volunteer");
        button.setActionCommand("create");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear Fields");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            creationComplete = false;
            volunteerName.setText("");
            volunteerAge.setValue("");
            volunteerCity.setText("");
            volunteerMaxHours.setValue("");
        } else {
            creationComplete = true;
            createVolunteer();
        }
    }

    protected void createVolunteer() {
        if (volunteerName.getText().equals("") || volunteerAge.getText().equals("")
                || volunteerCity.getText().equals("") || volunteerMaxHours.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "One or more fields are left empty");
        } else {
            volunteerUser = new Volunteer(volunteerName.getText(), Integer.parseInt(volunteerAge.getText()),
                    volunteerCity.getText(), Integer.parseInt(volunteerMaxHours.getText()));
            database.addVolunteer(volunteerUser);
            this.previousFrame.dispose();
            frame.setVisible(false);
            new VolunteerMenu(database, volunteerUser);
        }
    }

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {"Name: ", "Age: ", "City: ", "Max Time Commitment: "};

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        createTextFields(fields, fieldNum);

        //Associate label/field pairs, add everything, and lays it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i]);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf = (JTextField) fields[i];
            tf.addActionListener(this);
        }
        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, 10, 10, 10, 10);
        return panel;
    }

    private void createTextFields(JComponent[] fields, int fieldNum) {
        volunteerName = new JTextField();
        volunteerName.setColumns(20);
        fields[fieldNum++] = volunteerName;

        volunteerAge = new JFormattedTextField();
        fields[fieldNum++] = volunteerAge;

        volunteerCity = new JTextField();
        volunteerCity.setColumns(20);
        fields[fieldNum++] = volunteerCity;

        volunteerMaxHours = new JFormattedTextField();
        fields[fieldNum] = volunteerMaxHours;
    }
}
