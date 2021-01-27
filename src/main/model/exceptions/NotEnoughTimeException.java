package model.exceptions;

public class NotEnoughTimeException extends Exception {

    public NotEnoughTimeException(String volunteerName) {
        super("The volunteer: " + volunteerName + " doesn't have enough time commitment");
    }
}
