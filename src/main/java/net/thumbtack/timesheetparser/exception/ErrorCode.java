package net.thumbtack.timesheetparser.exception;

public enum ErrorCode {

    UNS_FORMAT("This file format if not supported"),
    EMPTY("XLS file is not selected"),
    INV_HD_ROW("Missing columns with \"Staff Member\" or \"Date\" or \"Project\" name");

    private String errorString;

    ErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
