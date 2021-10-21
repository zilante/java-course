package jdbctask.reports.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class FlightCountByWeekdayFromToMoscow {
    private void setHeaderCellStyle(CellStyle cellStyle, Font headerFont) {
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

        /* adding heading style */
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);

        headerFont.setFontHeightInPoints((short) 12);
        cellStyle.setFont(headerFont);
    }

    public void writeInExcelFile(String file,
                                 Map<Integer, Integer> flightCountsFrom,
                                 Map<Integer, Integer> flightCountsTo) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("flight-count-from-to-moscow");

        Row attributeNamesRow = sheet.createRow(0);

        CellStyle cellStyle = book.createCellStyle();
        Font headerFont = book.createFont();
        setHeaderCellStyle(cellStyle, headerFont);

        Cell name1 = attributeNamesRow.createCell(0);
        name1.setCellStyle(cellStyle);
        name1.setCellValue("Weekday");

        Cell name2 = attributeNamesRow.createCell(1);
        name2.setCellStyle(cellStyle);
        name2.setCellValue("Flight count from");

        Cell name3 = attributeNamesRow.createCell(2);
        name3.setCellStyle(cellStyle);
        name3.setCellValue("Flight count to");

        int weekDayCount = 7;
        for (int i = 1; i <= weekDayCount; ++i) {
            Row row = sheet.createRow(i + 1);
            Cell weekDay = row.createCell(0);
            Cell flightCountFrom = row.createCell(1);
            Cell flightCountTo = row.createCell(2);

            weekDay.setCellValue(i);
            flightCountTo.setCellValue(flightCountsTo.get(i));
            flightCountFrom.setCellValue(flightCountsFrom.get(i));
        }

        // Меняем размер столбца
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }
}
