package net.thumbtack.timesheetparser.dto.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class StaffMemberRequest {

    @NotBlank(message = "Name of staff member can't be empty")
    private String staffMemberName;
    @Min(value = 1, message = "Number of mounts can't be less than 1")
    private int numberOfMonths;
    @Min(value = 0, message = "Number of hours can't be less than 0")
    private int numberOfHours;

    public StaffMemberRequest() {
    }

    public String getStaffMemberName() {
        return staffMemberName;
    }

    public void setStaffMemberName(String staffMemberName) {
        this.staffMemberName = staffMemberName;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public void setNumberOfMonths(int numberOfMonths) {
        this.numberOfMonths = numberOfMonths;
    }

    public int getNumberOfHours() {
        return numberOfHours;
    }

    public void setNumberOfHours(int numberOfHours) {
        this.numberOfHours = numberOfHours;
    }
}
