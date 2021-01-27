package model.exceptions;

public class NotEnoughFundsException extends Exception {

    public NotEnoughFundsException(String organizationName) {
        super("The aren't any applications for " + organizationName + " right now");
    }
}
