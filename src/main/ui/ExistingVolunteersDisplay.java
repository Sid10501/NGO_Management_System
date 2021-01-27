package ui;

import model.Database;
import model.Event;
import model.Founder;
import model.Volunteer;
import model.exceptions.EventCapacityFullException;
import model.exceptions.NotEnoughFundsException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

// a display list of all the volunteers in the founder's organization in the database
public class ExistingVolunteersDisplay extends JDialog implements ActionListener {
    JFrame frame;
    JPanel volunteersListPane;
    JPanel buttonPane;
    JButton cancelButton;
    JButton selectButton;
    final JList list;
    Database database;
    Founder founder;
    model.Event event;
    Volunteer volunteer;
    Boolean isExec;

    // initiates the display panel and shows all the selectable volunteers in the organization
    public ExistingVolunteersDisplay(Database database, Event event, Founder founder, Boolean isExec) {

        this.database = database;
        this.founder = founder;
        this.event = event;
        this.isExec = isExec;
        frame = new JFrame("Volunteer Selection Menu");

        //Create and initialize the buttons.
        initializeButtons();

        //main part of the dialog
        list = extractSearchList();
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        createListPanel(listScroller);
        createButtonPanel();

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(volunteersListPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    private void initializeButtons() {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        selectButton = new JButton("Select");
        selectButton.setActionCommand("select");
        selectButton.addActionListener(this);
        getRootPane().setDefaultButton(selectButton);
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

    //EFFECTS: Creates Panel of lists containing existing volunteers.
    private void createListPanel(JScrollPane listScroller) {
        volunteersListPane = new JPanel();
        volunteersListPane.setLayout(new BoxLayout(volunteersListPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Select Volunteer");
        label.setLabelFor(list);
        volunteersListPane.add(label);
        volunteersListPane.add(Box.createRigidArea(new Dimension(0, 5)));
        volunteersListPane.add(listScroller);
        volunteersListPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    //EFFECTS: ...
    private JList extractSearchList() {
        final JList list;
        list = getJList();
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setPrototypeCellValue("                "); //get extra space
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(-1);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    selectButton.doClick(); //emulate button click
                }
            }
        });
        return list;
    }

    //EFFECTS: returns the JList with volunteer names.
    private JList getJList() {
        final JList list;
        list = new JList(getVolunteerNames()) {
            public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
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
        return list;
    }

    //Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if ("select".equals(e.getActionCommand())) {
            String selectedVolunteer = (String) (list.getSelectedValue());
            this.volunteer = getVolunteerFromName(selectedVolunteer);
            if (isExec) {
                try {
                    createNewEvent();
                } catch (NotEnoughFundsException notEnoughFundsException) {
                    notEnoughFundsException.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "You don't have enough funds to create an event");
                }
            } else {
                try {
                    addVolunteersToEvent(selectedVolunteer);
                } catch (EventCapacityFullException eventCapacityFullException) {
                    eventCapacityFullException.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "The event is already full");

                }
            }
        } else if ("cancel".equals((e.getActionCommand()))) {
            frame.setVisible(false);
            new EventDetailsMenu(database, event, founder);
        }
    }

    //EFFECTS: if getVolunteerRemainingHours > getEventDuration && !event.contains(volunteer),
    //         assign the volunteer to event, return null otherwise.
    private void addVolunteersToEvent(String selectedVolunteer) throws EventCapacityFullException {
        if (volunteer.getVolunteerRemainingHours() > event.getEventDuration()
                && !event.getEventVolunteers().contains(volunteer)) {
            founder.addVolunteerToEvent(database.getVolunteer(selectedVolunteer),
                    getEventFromName(event.getEventName()));
            frame.setVisible(false);
            new EventDetailsMenu(database, event, founder);
        } else {
            JOptionPane.showMessageDialog(null, "Not Enough Hours");
        }
    }


    //REQUIRES: !event.contains(selectedEvent)
    //EFFECTS: If the event name is not in the list, add and return the event to the list
    //         otherwise, return null.
    public Event getEventFromName(String selectedEvent) {
        for (Event event : database.getFounder(founder.getFounderName()).getFounderOrganization()
                .getOrganizationEvents()) {
            if (event.getEventName().equals(selectedEvent)) {
                return event;
            }
        }
        return null;
    }


    //EFFECTS: Creates a new Event
    private void createNewEvent() throws NotEnoughFundsException {
        event.setEventExecutive(volunteer);
        volunteer.addEvent();
        database.getOrganization(founder.getFounderOrganization().getOrganizationName()).addEvent(event);
        database.getFounder(founder.getFounderName()).createEvent(event);
        frame.setVisible(false);
        new EventDetailsMenu(database, event, founder);
    }

    //REQUIRES: !volunteer.contains(selectedVolunteer)
    //EFFECTS: If the volunteer name is not in the list, add and return the volunteer to the list
    //         otherwise, return null.
    private Volunteer getVolunteerFromName(String selectedVolunteer) {
        for (Volunteer volunteer : founder.getFounderOrganization().getOrganizationMembers()) {
            if (volunteer.getVolunteerName().equals(selectedVolunteer)) {
                return volunteer;
            }
        }
        return founder.getFounderOrganization().getOrganizationMembers().get(0);
    }

    //EFFECTS: Returns the names of the volunteers in the list.
    public String[] getVolunteerNames() {
        LinkedList<String> volunteers = new LinkedList<>();
        for (Volunteer volunteer : founder.getFounderOrganization().getFilteredExecutives(isExec)) {
            if (!event.getEventVolunteers().contains(volunteer)) {
                volunteers.add(volunteer.getVolunteerName());
            }
        }
        return volunteers.toArray(new String[]{});
    }


}
