package net.thumbtack.timesheetparser.exception;

public class StaffMemberNotFoundException extends Exception {
    public StaffMemberNotFoundException(String message) {
        super(message);
    }

    public StaffMemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
