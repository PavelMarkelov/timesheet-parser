package net.thumbtack.timesheetparser.servise_impl.parser;

import java.time.LocalDate;
import java.util.InvalidPropertiesFormatException;
import java.util.OptionalInt;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

public class RowReader {

  private final Row row;

  public RowReader(Row row) {
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

}
