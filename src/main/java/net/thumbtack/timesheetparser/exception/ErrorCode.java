package net.thumbtack.timesheetparser.exception;

public enum ErrorCode {

    UNS_FORMAT("This file format if not supported"),
    EMPTY("XLS file is not selected");

    private String errorString;

    ErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
