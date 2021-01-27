
package ui;

import model.Database;
import ui.utilities.MemberType;

public class Main {
    public static void main(String[] args) {
        new DatabaseManagerGUI(new Database("My Database"), MemberType.FOUNDER);
    }
}
