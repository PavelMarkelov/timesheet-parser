package net.thumbtack.timesheetparser.exception;

public class InvalidHeadRowException extends RuntimeException {

    public InvalidHeadRowException(String message) {
        super(message);
    }

    public InvalidHeadRowException(String message, Throwable cause) {
        super(message, cause);
    }
}
