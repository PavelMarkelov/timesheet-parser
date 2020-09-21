package net.thumbtack.timesheetparser.servise_impl.parser;

import java.time.LocalDate;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;
import java.util.OptionalInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class RowReader {

  private final Row row;
  private RowDataReader rowDataReader = null;

  public RowReader(Row row) {
    this.row = row;
  }

  public RowReader(RowDataReader rowDataReader, Row row) {
    this.rowDataReader = rowDataReader;
    this.row = row;
  }

  public OptionalInt getIndexOfIgnoreCase(final String value) {
    for (final Cell cell : row) {
      if (cell.getCellType().equals(CellType.STRING) && cell.getRichStringCellValue().getString().equals(value)) {
        return OptionalInt.of(cell.getColumnIndex());
      }
    }
    return OptionalInt.empty();
  }

  public String getString(final int index) throws InvalidPropertiesFormatException {
    if (!row.getCell(index).getCellType().equals(CellType.STRING)) {
      throw new InvalidPropertiesFormatException("Invalid type of cell value");
    }
    return row.getCell(index).getRichStringCellValue().getString();
  }

  public LocalDate getDate(final int index) throws InvalidPropertiesFormatException {
    if (!DateUtil.isCellDateFormatted(row.getCell(index))) {
      throw new InvalidPropertiesFormatException("Invalid type of cell value");
    }
    return row.getCell(index).getLocalDateTimeCellValue().toLocalDate();
  }

  public double getDouble(final int index) throws InvalidPropertiesFormatException {
    if (!row.getCell(index).getCellType().equals(CellType.NUMERIC)) {
      throw new InvalidPropertiesFormatException("Invalid type of call value");
    }
    return row.getCell(index).getNumericCellValue();
  }

  public boolean isValidDataInRow() {
    final Optional<Cell> projectNameCellOpt = Optional.ofNullable(row.getCell(rowDataReader.getProjectIndex()));
    final Optional<Cell> developerNameCellOpt = Optional.ofNullable(row.getCell(rowDataReader.getStaffMemberIndex()));
    final Optional<Cell> dateCellOpt = Optional.ofNullable(row.getCell(rowDataReader.getDateIndex()));
    final Optional<Cell> numberOfHoursOpt = Optional.ofNullable(row.getCell(rowDataReader.getNumberOfHoursIndex()));

    final boolean isPresentData = projectNameCellOpt.isPresent() && developerNameCellOpt.isPresent() &&
        dateCellOpt.isPresent() && numberOfHoursOpt.isPresent();

    return isPresentData && projectNameCellOpt.get().getCellType().equals(CellType.STRING) &&
        developerNameCellOpt.get().getCellType().equals(CellType.STRING) &&
        DateUtil.isCellDateFormatted(dateCellOpt.get()) &&
        numberOfHoursOpt.get().getCellType().equals(CellType.NUMERIC);
  }

}
