/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ui;

import model.Database;
import model.Founder;
import model.Volunteer;
import ui.utilities.MemberType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

// a display list of all the users in the database
public class ExistingUsersDisplay extends JDialog implements ActionListener {
    private final Boolean isFounder;
    private final JList list;
    JFrame frame;
    JButton cancelButton;
    JPanel listPane;
    JPanel buttonPane;
    Database database;

    // initiates the display panel and shows all the selectable users in the database
    public ExistingUsersDisplay(Database database, String labelText, Boolean isFounder) {
        this.database = database;
        this.isFounder = isFounder;
        this.frame = new JFrame("Database Users Display");
        //Create and initialize the buttons.
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        final JButton selectButton = new JButton("Select");
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        getRootPane().setDefaultButton(selectButton);

        //main part of the dialog
        list = extractSearchList(isFounder, selectButton);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        createListPanel(labelText, listScroller);

        //Lay out the buttons from left to right.
        createButtonPanel(selectButton);

        //Put everything together, using the content pane's BorderLayout.
        Container contentPane = frame.getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    //EFFECTS: Creates a panel of buttons.
    private void createButtonPanel(JButton selectButton) {
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(selectButton);
    }

    //EFFECTS: Creates Panel of lists containing existing users.
    private void createListPanel(String labelText, JScrollPane listScroller) {
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(labelText);
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    //EFFECTS: creates the search list of volunteers or founders
    private JList extractSearchList(Boolean isFounder, JButton selectButton) {
        final JList list;
        list = getJList(isFounder);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        list.setPrototypeCellValue("                      "); //get extra space

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

    //EFFECTS: returns the JList with existing user names in it that can either be a founder or a volunteer
    private JList getJList(Boolean isFounder) {
        final JList list;
        list = new JList(isFounder ? getFounderNames() : getVolunteerNames()) {
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
        if ("Select".equals(e.getActionCommand())) {
            String value = (String) (list.getSelectedValue());
            frame.setVisible(false);
            if (isFounder) {
                Founder founder = database.getFounder(value);
                new FounderMenu(database, founder);
            } else {
                Volunteer volunteer = database.getVolunteer(value);
                new VolunteerMenu(database, volunteer);
            }
        } else if ("Cancel".equals((e.getActionCommand()))) {
            frame.setVisible(false);
            new DatabaseManagerGUI(database, isFounder ? MemberType.FOUNDER : MemberType.VOLUNTEER);
        }

    }

    //EFFECTS : Returns the list of founder names in the database.
    public String[] getFounderNames() {
        LinkedList<String> founders = new LinkedList<>();
        for (Founder founder : database.getFounders()) {
            founders.add(founder.getFounderName());
        }
        return founders.toArray(new String[]{});
    }

    //EFFECTS : Returns the list of volunteer names in the database.
    public String[] getVolunteerNames() {
        LinkedList<String> volunteers = new LinkedList<>();
        for (Volunteer volunteer : database.getVolunteers()) {
            if (!volunteer.getVolunteerName().equals("DefaultVolunteer")) {
                volunteers.add(volunteer.getVolunteerName());
            }
        }
        return volunteers.toArray(new String[]{});
    }
}
