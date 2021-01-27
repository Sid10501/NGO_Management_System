package ui;

import model.Database;
import model.Founder;
import model.Organization;
import model.enums.Purpose;
import ui.utilities.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// a field entry class that allows the user to input all the founder information for a user creation
public class FounderCreationMenu extends JFrame implements ActionListener {

    private final JFrame frame;
    private final Database database;

    JTextField nameField;
    JFormattedTextField ageField;
    JTextField cityField;
    JFormattedTextField investmentField;
    JTextField organizationNameField;
    JSpinner organizationPurposeSpinner;
    JFormattedTextField organizationTimeCommitment;
    JFrame previousFrame;
    JLabel founderDetails = new JLabel("Enter Your Information:");

    static final int GAP = 10;

    // initiates the menu along with all the fields required for founder creation
    public FounderCreationMenu(Database database, JFrame f) {
        this.database = database;
        this.previousFrame = f;

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

        frame = new JFrame("Founder Creation Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Add contents to the window.
        frame.add(founderFields);

        //Display the window.
        frame.setSize(1000, 600);
        frame.pack();
        frame.setVisible(true);
    }

    //EFFECTS: creates the buttons.
    protected JComponent createButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton button = new JButton("Create Founder");
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("Clear Fields");
        button.addActionListener(this);
        button.setActionCommand("clear");
        panel.add(button);
        return panel;
    }

    /**
     * Called when the user clicks the button or presses
     * Enter in a text field.
     */
    public void actionPerformed(ActionEvent e) {
        if ("clear".equals(e.getActionCommand())) {
            nameField.setText("");
            ageField.setValue("");
            cityField.setText("");
            investmentField.setValue("");
            organizationNameField.setText("");
            organizationTimeCommitment.setValue("");
        } else {
            createFounder();
        }
    }

    //EFFECTS: If neither of the fields are null, create a founder.
    protected void createFounder() {
        if (nameField.getText().equals("") || ageField.getText().equals("") || cityField.getText().equals("")
                || investmentField.getText().equals("") || organizationNameField.getText().equals("")
                || organizationNameField.getText().equals("")) {

            JOptionPane.showMessageDialog(frame, "One or more fields are left empty");
        } else {
            Purpose organizationPurpose = setPurpose();
            Organization newOrganization = new Organization(organizationNameField.getText(), cityField.getText(),
                    Integer.parseInt(investmentField.getText()), Integer.parseInt(organizationTimeCommitment.getText()),
                    organizationPurpose);
            Founder founderUser = new Founder(nameField.getText(), Integer.parseInt(ageField.getText()),
                    cityField.getText(), Integer.parseInt(investmentField.getText()), newOrganization);
            database.addFounder(founderUser);
            database.addOrganization(newOrganization);
            database.addVolunteer(founderUser.getFounderOrganization().getOrganizationMembers().get(0));
            frame.setVisible(false);
            this.previousFrame.dispose();
            new FounderMenu(database, founderUser);
        }
    }

    //EFFECTS: Sets the purpose of the organization.
    private Purpose setPurpose() {
        String command = (String) organizationPurposeSpinner.getValue();
        Purpose inputPurpose = Purpose.EDUCATION;
        switch (command) {
            case "EDUCATION":
                inputPurpose = Purpose.EDUCATION;
                break;
            case "UNDERPRIVILEGED":
                inputPurpose = Purpose.UNDERPRIVILEGED;
                break;
            case "HEALTH":
                inputPurpose = Purpose.HEALTH;
                break;
            case "ANIMALS":
                inputPurpose = Purpose.ANIMALS;
                break;
        }
        return inputPurpose;
    }

    protected JComponent createEntryFields() {
        JPanel panel = new JPanel(new SpringLayout());

        String[] labelStrings = {"Name: ", "Age: ", "City: ", "Investment: ", "Organization Name",
                "Organization Purpose", "Required Time Commitment"};

        JLabel[] labels = new JLabel[labelStrings.length];
        JComponent[] fields = new JComponent[labelStrings.length];
        int fieldNum = 0;

        //Create the text field and set it up.
        createFields(fields, fieldNum);

        //Associate label/field pairs, add everything,
        //and lay it out.
        for (int i = 0; i < labelStrings.length; i++) {
            labels[i] = new JLabel(labelStrings[i]);
            labels[i].setLabelFor(fields[i]);
            panel.add(labels[i]);
            panel.add(fields[i]);

            //Add listeners to each field.
            JTextField tf;
            if (fields[i] instanceof JSpinner) {
                tf = getTextField((JSpinner) fields[i]);
            } else {
                tf = (JTextField) fields[i];
            }
            tf.addActionListener(this);
        }

        SpringUtilities.makeCompactGrid(panel, labelStrings.length, 2, GAP, GAP, GAP, GAP / 2);
        return panel;
    }

    private void createFields(JComponent[] fields, int fieldNum) {
        nameField = new JTextField();
        nameField.setColumns(20);
        fields[fieldNum++] = nameField;

        ageField = new JFormattedTextField();
        fields[fieldNum++] = ageField;

        cityField = new JTextField();
        cityField.setColumns(20);
        fields[fieldNum++] = cityField;

        investmentField = new JFormattedTextField();
        fields[fieldNum++] = investmentField;

        organizationNameField = new JTextField();
        organizationNameField.setColumns(20);
        fields[fieldNum++] = organizationNameField;

        String[] stateStrings = getStateStrings();
        organizationPurposeSpinner = new JSpinner(new SpinnerListModel(stateStrings));
        fields[fieldNum++] = organizationPurposeSpinner;

        organizationTimeCommitment = new JFormattedTextField();
        fields[fieldNum] = organizationTimeCommitment;
    }

    //EFFECTS: Gets the state/purpose of organization
    public String[] getStateStrings() {
        return new String[]{
                String.valueOf(Purpose.ANIMALS),
                String.valueOf(Purpose.EDUCATION),
                String.valueOf(Purpose.HEALTH),
                String.valueOf(Purpose.UNDERPRIVILEGED)
        };
    }

    public JFormattedTextField getTextField(JSpinner spinner) {
        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        } else {
            System.err.println("Unexpected editor type: " + spinner.getEditor().getClass()
                    + " isn't a descendant of DefaultEditor");
            return null;
        }
    }

}
