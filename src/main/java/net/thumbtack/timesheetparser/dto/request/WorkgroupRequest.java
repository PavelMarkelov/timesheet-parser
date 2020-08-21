package net.thumbtack.timesheetparser.dto.request;

import javax.validation.constraints.Min;
import java.time.LocalDate;

public class WorkgroupRequest {

    @Min(message = "Staff member id can't less that 1", value = 1)
    private int staffMemberId;
    @Min(message = "Project id can't less that 1", value = 1)
    private int projectId;

    public WorkgroupRequest() {
    }

    public WorkgroupRequest(int staffMemberId, int projectId, LocalDate start, LocalDate end) {
        this.staffMemberId = staffMemberId;
        this.projectId = projectId;
    }

    public int getStaffMemberId() {
        return staffMemberId;
    }

    public void setStaffMemberId(int staffMemberId) {
        this.staffMemberId = staffMemberId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
