package net.thumbtack.timesheetparser.servise_impl.parser;

import java.util.InvalidPropertiesFormatException;
import net.thumbtack.timesheetparser.exception.InvalidHeadRowException;
import org.apache.poi.ss.usermodel.Row;

public class RowDataReader {

  private static String PROJECT = "Project";
  private static String STAFF_MEMBER = "Staff Member";
  private static String DATE = "Date";
  private static String INPUT = "Input";

  private final int projectIndex;
  private final int staffMemberIndex;
  private final int dateIndex;
  private final int numberOfHours;

  public RowDataReader(int projectIndex, int staffMemberIndex, int dateIndex, int numberOfHours) {
    this.projectIndex = projectIndex;
    this.staffMemberIndex = staffMemberIndex;
    this.dateIndex = dateIndex;
    this.numberOfHours = numberOfHours;
  }

  public static RowDataReader createFromHeader(final Row row) throws InvalidHeadRowException {
    final RowReader rowReader = new RowReader(row);
    final int projectIndex = rowReader.getIndexOfIgnoreCase(PROJECT).orElse(-1);
    final int staffMemberIndex = rowReader.getIndexOfIgnoreCase(STAFF_MEMBER).orElse(-1);
    final int dateIndex = rowReader.getIndexOfIgnoreCase(DATE).orElse(-1);
    final int numberOfHours = rowReader.getIndexOfIgnoreCase(INPUT).orElse(-1);

    if (projectIndex == -1 || staffMemberIndex == -1 || dateIndex == -1 || numberOfHours == -1) {
      throw new InvalidHeadRowException("");
    }

    return new RowDataReader(projectIndex, staffMemberIndex, dateIndex, numberOfHours);
  }

  public RowData readData(final Row row) throws InvalidPropertiesFormatException {
    final RowReader reader = new RowReader(row);
    return new RowData(
        reader.getString(projectIndex),
        reader.getString(staffMemberIndex),
        reader.getDate(dateIndex),
        reader.getDouble(numberOfHours)
    );
  }


}
