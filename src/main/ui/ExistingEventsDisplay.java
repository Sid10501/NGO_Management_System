package ui;

import model.*;
import model.Event;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

// a list display of all the events that exist in the database under the founder passed as parameter
public class ExistingEventsDisplay extends JDialog implements ActionListener {

    private final JList list;
    JFrame frame;
    JPanel listPane;
    JPanel buttonPane;
    JButton cancelButton;
    JButton selectButton;

    Database database;
    Founder founder;
    Event event;

    // initiates the display panel and shows all the selectable events
    public ExistingEventsDisplay(Database database, Founder founder) {

        this.database = database;
        this.founder = founder;
        frame = new JFrame("Event Volunteering Menu");

        //Create and initialize the buttons.
        initiatesButtons();

        //main part of the dialog
        list = extractSearchList();
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);


        createsListPanel(listScroller);

        //Lay out the buttons from left to right.
        createsButtonPanel();

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    //EFFECTS: creates a list panel for the user to select any event.
    private void createsListPanel(JScrollPane listScroller) {
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel("Select Event");
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }


    //EFFECTS: ...
    private JList extractSearchList() {
        final JList list;
        list = getJList();

        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        list.setPrototypeCellValue("longValue"); //get extra space

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

    //EFFECTS: returns the JList with events in it.
    private JList getJList() {
        final JList list;
        list = new JList(getEventNames()) {
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
        return list;
    }

    //EFFECTS: initiates the buttons.
    private void initiatesButtons() {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        selectButton = new JButton("Select");
        selectButton.setActionCommand("select");
        selectButton.addActionListener(this);
        getRootPane().setDefaultButton(selectButton);
    }

    //EFFECTS: Creates a button panel for the user.
    private void createsButtonPanel() {
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(selectButton);
    }

    //EFFECTS: adds and returns the names of the events in the list for founder.
    public String[] getEventNames() {
        LinkedList<String> events = new LinkedList<>();
        for (Event event : founder.getFounderOrganization().getOrganizationEvents()) {
            events.add(event.getEventName());
        }
        return events.toArray(new String[]{});
    }


    //Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if ("select".equals(e.getActionCommand())) {
            String selectedEvent = (String) (list.getSelectedValue());
            event = getEventFromName(selectedEvent);
            frame.setVisible(false);
            new EventDetailsMenu(database, event, founder);
        } else if ("cancel".equals((e.getActionCommand()))) {
            frame.setVisible(false);
            new ManageEventsMenu(database, founder);
        }
    }


    //REQUIRES: !event.contains(selectedEvent)
    //EFFECTS: If the event name is not in the list, add the event to the list
    //         otherwise, return null.
    private Event getEventFromName(String selectedEvent) {
        for (Event event : founder.getFounderOrganization().getOrganizationEvents()) {
            if (event.getEventName().equals(selectedEvent)) {
                return event;
            }
        }
        return null;
    }
}
