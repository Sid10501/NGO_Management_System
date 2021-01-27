package ui;

import model.Database;
import model.Organization;
import model.Volunteer;
import model.exceptions.NotEnoughTimeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

// a list display of all the organizations that exist in the database
public class ExistingOrganizationsDisplay extends JDialog implements ActionListener {

    JList list;
    JFrame frame;
    JPanel listPane;
    JPanel buttonPane;
    JButton cancelButton;
    JButton selectButton;

    Database database;
    Volunteer volunteer;

    // initiates the display panel and shows all the selectable organizations
    public ExistingOrganizationsDisplay(Database database, Volunteer volunteer) {

        this.database = database;
        this.volunteer = volunteer;
        frame = new JFrame("Organizations Display Menu");

        //Create and initialize the buttons.
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        selectButton = new JButton("Select");
        selectButton.setActionCommand("select");
        selectButton.addActionListener(this);
        getRootPane().setDefaultButton(selectButton);

        //main part of the dialog
        extractSearchList();
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);


        createListPanel(scrollPane);

        //Lay out the buttons from left to right.
        createButtonPanel();

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    //EFFECTS: Creates a panel of buttons.
    private void createButtonPanel() {
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(selectButton);
    }

    //EFFECTS: Creates list of existing organisations.
    private void createListPanel(JScrollPane scrollPane) {
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Select Organization");
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(scrollPane);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    //EFFECTS: ...
    private void extractSearchList() {
        getJList();

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);


        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectButton.doClick(); //emulate button click
                }
            }
        });
    }

    //EFFECTS: gets the JList with organization names in it.
    private void getJList() {
        list = new JList(getOrganizationNames()) {
            //Subclass JList to workaround bug 4832765, which can cause the
            //scroll pane to not let the user easily scroll up to the beginning
            //of the list.  An alternative would be to set the unitIncrement
            //of the JScrollBar to a fixed value. You wouldn't get the nice
            //aligned scrolling, but it should work.
            public int getScrollableUnitIncrement(Rectangle visibleRect,
                                                  int orientation,
                                                  int direction) {
                int row;
                if (orientation == SwingConstants.VERTICAL && direction < 0 && (row = getFirstVisibleIndex()) != -1) {
                    Rectangle r = getCellBounds(row, row);
                    if ((r.y == visibleRect.y) && (row != 0)) {
                        Point loc = r.getLocation();
                        loc.y--;
                        int prevIndex = locationToIndex(loc);
                        Rectangle prevR = getCellBounds(prevIndex, prevIndex);

                        if (prevR == null || prevR.y >= r.y) {
                            return 0;
                        }
                        return prevR.height;
                    }
                }
                return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
            }
        };
    }

    //EFFECTS: adds and returns the names of the organizations that the founder created.
    public String[] getOrganizationNames() {
        LinkedList<String> organizations = new LinkedList<>();
        for (Organization organization : database.getOrganizations()) {
            organizations.add(organization.getOrganizationName());
        }
        return organizations.toArray(new String[]{});
    }


    //Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if ("select".equals(e.getActionCommand())) {
            String value = (String) (list.getSelectedValue());
            try {
                applyForOrganization(value);
            } catch (NotEnoughTimeException notEnoughTimeException) {
                notEnoughTimeException.printStackTrace();
                JOptionPane.showMessageDialog(frame, "You don't have enough time commitment");
            }
        } else if ("cancel".equals((e.getActionCommand()))) {
            frame.setVisible(false);
            new VolunteerMenu(database, volunteer);
        }
    }

    private void applyForOrganization(String value) throws NotEnoughTimeException {
        database.getOrganization(value).addApplicant(database.getVolunteer(volunteer.getVolunteerName()));
        database.getOrganization(value).addApplicant(volunteer);
        database.getVolunteer(volunteer.getVolunteerName()).apply();
        volunteer.apply();

        frame.setVisible(false);
        new VolunteerMenu(database, database.getVolunteer(volunteer.getVolunteerName()));
    }
}
