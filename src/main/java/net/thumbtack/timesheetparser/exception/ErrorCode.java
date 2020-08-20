package net.thumbtack.timesheetparser.exception;

public enum ErrorCode {

    UNS_FORMAT("This file format if not supported"),
    EMPTY("XLS file is not selected"),
    INV_HD_ROW("Missing columns with \"Staff Member\" or \"Date\" or \"Project\" name"),
    IO_ERR("Something went wrong. Try uploading the file again"),
    FIELD("Field error"),
    STAFF_MEM_NOT_F("Staff member with specified name is not found"),
    FILE_N_LOAD("Excel file not loaded");

    private String errorString;

    ErrorCode(String errorString) {
        this.errorString = errorString;
    }

    public String getErrorString() {
        return errorString;
    }
}
