package model.exceptions;

public class NotPromotableException extends Exception {

    public NotPromotableException(String volunteerName) {
        super("The Volunteer: " + volunteerName + " doesn't have enough working hours");
    }
}
