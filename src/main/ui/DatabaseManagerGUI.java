package ui;

import model.Database;
import ui.utilities.MemberType;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

// The GUI class with all ui components visible to user with audio/visual components
public class DatabaseManagerGUI extends JFrame implements ActionListener {

    public MemberType memberType;
    private static final String JSON_STORE = "./data/generalDatabase.json";
    private Database database;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    public JFrame frame;

// initializes the database gui and displays the main menu
    public DatabaseManagerGUI(Database database, MemberType memberType) {
        this.memberType = memberType;
        this.database = database;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        displayDatabaseMenu();
        createSound(2);
    }

    // EFFECTS: displays the main menu where the user can choose his/her role and login/signup
    void displayDatabaseMenu() {
        //Create and set up the window.
        frame = new JFrame("Organization Database Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createContentPane());

        //Display the window.
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    // EFFECTS: creates the global menu bar which allows the user to create, load, save database, quit and choose roles
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu mainMenu = new JMenu("Menu");
        JMenu roleMenu = new JMenu("Role");

        JMenuItem createItem = new JMenuItem("Create New Database");
        JMenuItem loadItem = new JMenuItem("Load Database");
        JMenuItem saveItem = new JMenuItem("Save Database");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem readMe = new JMenuItem("Open ReadMe File");

        JRadioButtonMenuItem founderMenuItem = new JRadioButtonMenuItem("Founder");
        JRadioButtonMenuItem volunteerMenuItem = new JRadioButtonMenuItem("Volunteer");

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build first menu in the menu bar.
        createMainMenu(menuBar, mainMenu, createItem, loadItem, saveItem, quitItem, readMe);

        //Build second menu in the menu bar.
        createRoleMenu(menuBar, roleMenu, founderMenuItem, volunteerMenuItem);

        return menuBar;
    }

    // EFFECTS: creates the role switching sub menu
    private void createRoleMenu(JMenuBar menuBar, JMenu roleMenu,
                                JMenuItem founderMenuItem, JMenuItem volunteerMenuItem) {
        menuBar.add(roleMenu);
        roleMenu.addSeparator();
        ButtonGroup group = new ButtonGroup();

        founderMenuItem.setActionCommand("founder");
        founderMenuItem.addActionListener(this::chooseRole);
        founderMenuItem.setSelected(memberType.equals(MemberType.FOUNDER));
        group.add(founderMenuItem);
        roleMenu.add(founderMenuItem);

        volunteerMenuItem.setActionCommand("volunteer");
        volunteerMenuItem.addActionListener(this::chooseRole);
        volunteerMenuItem.setSelected(memberType.equals(MemberType.VOLUNTEER));
        group.add(volunteerMenuItem);
        roleMenu.add(volunteerMenuItem);
    }

    // EFFECTS: helper for menu bar
    private void createMainMenu(JMenuBar menuBar, JMenu mainMenu, JMenuItem createItem, JMenuItem loadItem,
                                JMenuItem saveItem, JMenuItem quitItem, JMenuItem readMe) {

        menuBar.add(mainMenu);
        createItem.setActionCommand("create");
        createItem.addActionListener(this::createAdminListener);
        mainMenu.add(createItem);

        loadItem.setActionCommand("load");
        loadItem.addActionListener(this::createAdminListener);
        mainMenu.add(loadItem);

        saveItem.setActionCommand("save");
        saveItem.addActionListener(this::createAdminListener);
        mainMenu.add(saveItem);

        quitItem.setActionCommand("quit");
        quitItem.addActionListener(this::createAdminListener);
        mainMenu.add(quitItem);

        //a submenu
        mainMenu.addSeparator();

        readMe.setActionCommand("readme");
        readMe.addActionListener(this::createAdminListener);
        mainMenu.add(readMe);
    }

    // EFFECTS: displays the main panel with all visual components
    public JPanel createContentPane() {
        //Create the content-pane-to-be.
        JPanel contentPane = new JPanel();
        JLabel welcomeMessage = new JLabel();
        JButton newUserButton = new JButton();
        JButton existingUserButton = new JButton();

        contentPane.setLayout(new GridLayout(4, 0));
        contentPane.add(configureImage());

        //---- welcomeMessage ----
        welcomeMessage.setText("Welcome to Non Profit Organization Database");
        welcomeMessage.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeMessage);

        //---- newUserButton ----
        newUserButton.setText("Create New User");
        newUserButton.setActionCommand("signup");
        newUserButton.setSize(500, 10);
        newUserButton.addActionListener(this);
        contentPane.add(newUserButton);

        //---- existingUserButton ----
        existingUserButton.setText("Login as Existing User");
        existingUserButton.setActionCommand("login");
        existingUserButton.setSize(500, 30);
        existingUserButton.addActionListener(this);
        contentPane.add(existingUserButton);
        return contentPane;
    }

    // MODIFIES: this
    // EFFECTS: changes the role of the current user
    public void chooseRole(ActionEvent e) {
        createSound(2);
        if ("founder".equals(e.getActionCommand())) {
            memberType = MemberType.FOUNDER;
            frame.setVisible(false);
            new DatabaseManagerGUI(database, MemberType.FOUNDER);
        } else if ("volunteer".equals(e.getActionCommand())) {
            memberType = MemberType.VOLUNTEER;
            frame.setVisible(false);
            new DatabaseManagerGUI(database, MemberType.VOLUNTEER);

        }
    }

    // EFFECTS: configures the image as a JLabel and returns it
    public JLabel configureImage() {
        JLabel logo = new JLabel();
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setIcon(new ImageIcon((new ImageIcon("./assets/LOGO.jpg").getImage()
                .getScaledInstance(1000, 200, Image.SCALE_SMOOTH))));
        return logo;
    }

    // Action listener for buttons on main menu
    public void actionPerformed(ActionEvent e) {
        if ("login".equals(e.getActionCommand())) {
            loginUser();
        } else if ("signup".equals(e.getActionCommand())) {
            signUpUser();
        }
    }

    // EFFECTS: opens FounderCreationMenu or VolunteerCreationMenu and disposes the current frame
    private void signUpUser() {
        if (memberType.equals(MemberType.FOUNDER)) {
            createSound(2);
            new FounderCreationMenu(database, frame);
        } else {
            createSound(2);
            new VolunteerCreationMenu(database, frame);
        }
    }

    // EFFECTS: logs in the user selected or displays error in case no users available
    private void loginUser() {
        if (memberType.equals(MemberType.FOUNDER)) {
            if (database.getFounders().isEmpty()) {
                createSound(3);
                JOptionPane.showMessageDialog(frame, "The Founder Database is empty");
            } else {
                createSound(2);
                new ExistingUsersDisplay(database, "Select Founder", true);
                frame.setVisible(false);
            }
        } else {
            if (database.getVolunteers().isEmpty()) {
                createSound(3);
                JOptionPane.showMessageDialog(frame, "The Volunteer Database is empty");
            } else {
                createSound(1);
                new ExistingUsersDisplay(database, "Select Volunteer", false);
                frame.setVisible(false);

            }
        }
    }

    // EFFECTS: plays a sound based on the input of integer value provided
    public static void createSound(int select) {
        Clip clip;
        String soundPath;
        switch (select) {
            case 1:
                soundPath = "./assets/buttonBeep.wav";
                break;
            case 2:
                soundPath = "./assets/menuBarSound.wav";
                break;
            case 3:
                soundPath = "./assets/errorSound.wav";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + select);
        }
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(soundPath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Cannot play audio");
        }
    }

    // EFFECTS: creates a listener for the menu bar buttons
    public void createAdminListener(ActionEvent e) {
        createSound(1);

        if ("create".equals(e.getActionCommand())) {
            frame.setVisible(false);
            database = new Database("My database");
            displayDatabaseMenu();
        } else if ("load".equals(e.getActionCommand())) {
            int input = JOptionPane.showConfirmDialog(
                    frame, "Are you sure you want to load? All unsaved data will be lost",
                    "Data Loss Alert",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (input == 0) {
                loadDatabase();
            }
        } else if ("save".equals(e.getActionCommand())) {
            saveDatabase();
        } else if ("readme".equals(e.getActionCommand())) {
            displayReadMe();
        } else {
            frame.setVisible(false);
            System.exit(0);
        }
    }

    // EFFECTS: opens the readme file of the project
    private void displayReadMe() {
        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "./README.md");
        try {
            pb.start();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    // EFFECTS: saves the database to file
    private void saveDatabase() {
        try {

            jsonWriter.open();
            jsonWriter.write(database);
            jsonWriter.close();
            System.out.println("Saved " + database.getDatabaseName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads database from file
    private void loadDatabase() {
        try {
            this.database = jsonReader.read();
            System.out.println("Loaded " + database.getDatabaseName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }


}


