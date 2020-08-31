package net.thumbtack.timesheetparser.exception;

public class InvalidHeadRowException extends Exception {

    public InvalidHeadRowException(String message) {
        super(message);
    }

    public InvalidHeadRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
