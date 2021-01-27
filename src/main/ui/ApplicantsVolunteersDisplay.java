package ui;

import model.Database;
import model.Founder;
import model.Volunteer;
import model.exceptions.NotPromotableException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;

// displays the volunteers or applicants in a menu based on the value of isPromote
public class ApplicantsVolunteersDisplay extends JDialog implements ActionListener {

    Boolean isPromote;
    final JList list;
    JFrame frame;
    JFrame previousFrame;
    JButton cancelButton;
    JButton selectButton;
    JPanel listPane;
    JPanel buttonPane;
    Database database;
    Founder founder;

    // EFFECTS: Displays the applicants or volunteers based on value of isPromote
    public ApplicantsVolunteersDisplay(Database database, Founder founder, boolean isPromote, JFrame prevFrame) {

        this.database = database;
        this.isPromote = isPromote;
        this.frame = new JFrame("Applicants/Volunteer Display");
        this.previousFrame = prevFrame;
        this.founder = founder;

        initializeButtons();
        list = extractSearchList();
        createScrollPane(isPromote);
        createButtonPanel(selectButton);

        Container contentPane = frame.getContentPane();
        contentPane.add(listPane, BorderLayout.CENTER);
        contentPane.add(buttonPane, BorderLayout.PAGE_END);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    // EFFECTS: Creates a search list of the volunteers or applicants.
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

    //EFFECTS: Creates a scroll panel for the search list.
    private void createScrollPane(boolean isPromote) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(250, 80));
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);

        createListPanel(isPromote, scrollPane);
    }

    //EFFECTS: Returns the Json List.
    private JList getJList() {
        final JList list;
        list = new JList(getNames()) {
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

    //EFFECTS: Initialises the buttons.
    private void initializeButtons() {
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);

        selectButton = new JButton("Select");
        selectButton.setActionCommand("Select");
        selectButton.addActionListener(this);
        getRootPane().setDefaultButton(selectButton);
    }


    //EFFECTS: Creates a list panel where the founder can promote the volunteer or the founder can select an applicant.
    private void createListPanel(boolean isPromote, JScrollPane listScroller) {
        listPane = new JPanel();
        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        JLabel label = new JLabel(isPromote ? "Select Volunteer you'd like to promote" :
                "Select Applicant you;d like to accept");
        label.setLabelFor(list);
        listPane.add(label);
        listPane.add(Box.createRigidArea(new Dimension(0, 5)));
        listPane.add(listScroller);
        listPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    //EFFECTS: Creates a button panel.
    private void createButtonPanel(JButton selectButton) {
        buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPane.add(Box.createHorizontalGlue());
        buttonPane.add(cancelButton);
        buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPane.add(selectButton);
    }

    //Handle clicks on the Set and Cancel buttons.
    public void actionPerformed(ActionEvent e) {
        if ("Select".equals(e.getActionCommand())) {
            String value = (String) (list.getSelectedValue());
            frame.setVisible(false);
            if (isPromote) {
                try {
                    promoteVolunteerHelper(value);
                } catch (NotPromotableException notPromotableException) {
                    notPromotableException.printStackTrace();
                    playErrorSound();
                    JOptionPane.showMessageDialog(frame, "The Volunteer doesn't have enough working hours");

                }
            } else {
                database.getFounder(founder.getFounderName()).acceptApplication(database.getVolunteer(value));
                database.getOrganization(founder.getFounderOrganization().getOrganizationName())
                        .addMember(database.getVolunteer(value));
//                database.getOrganization(founder.getFounderOrganization()
//                        .getOrganizationName()).removeApplicant(database.getVolunteer(value));
                new ManageApplicantsMenu(database, founder);
            }
        } else if ("Cancel".equals((e.getActionCommand()))) {
            frame.setVisible(false);
            previousFrame.setVisible(true);
        }

    }

    private void promoteVolunteerHelper(String value) throws NotPromotableException {
        database.getFounder(founder.getFounderName()).promoteVolunteer(database.getVolunteer(value));
        database.getOrganization(founder.getFounderOrganization().getOrganizationName())
               .addMember(database.getVolunteer(value));
        new ManageVolunteersMenu(database, founder);
    }

    //EFFECTS: Returns the names of all volunteers who want to apply for some organisation.
    public String[] getNames() {
        LinkedList<String> volunteers = new LinkedList<>();
        for (Volunteer volunteer : isPromote ? database.getOrganization(founder.getFounderOrganization()
                .getOrganizationName()).getFilteredExecutives(false) : database.getOrganization(founder
                .getFounderOrganization().getOrganizationName()).getOrganizationApplications()) {
            volunteers.add(volunteer.getVolunteerName());
        }
        return volunteers.toArray(new String[]{});
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
