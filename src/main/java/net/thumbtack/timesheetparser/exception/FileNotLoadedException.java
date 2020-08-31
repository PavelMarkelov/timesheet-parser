package net.thumbtack.timesheetparser.exception;

public class FileNotLoadedException extends Exception {
    public FileNotLoadedException(String message) {
        super(message);
    }

    public FileNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
