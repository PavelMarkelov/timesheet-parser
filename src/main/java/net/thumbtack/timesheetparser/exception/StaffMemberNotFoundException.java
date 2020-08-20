package net.thumbtack.timesheetparser.exception;

public class StaffMemberNotFoundException extends RuntimeException {
    public StaffMemberNotFoundException(String message) {
        super(message);
    }

    public StaffMemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
