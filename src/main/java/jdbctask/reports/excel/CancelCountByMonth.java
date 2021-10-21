package jdbctask.reports.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class CancelCountByMonth {
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
                                 Map<Integer, Integer> cancelCountByMonth)
            throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("cancel-count-by-month");

        Row attributeNamesRow = sheet.createRow(0);

        CellStyle cellStyle = book.createCellStyle();
        Font headerFont = book.createFont();
        setHeaderCellStyle(cellStyle, headerFont);

        Cell name1 = attributeNamesRow.createCell(0);
        name1.setCellStyle(cellStyle);
        name1.setCellValue("Month");

        Cell name2 = attributeNamesRow.createCell(1);
        name2.setCellStyle(cellStyle);
        name2.setCellValue("Cancel count");

        int rowIndex = 1;
        for (Map.Entry<Integer, Integer> entry : cancelCountByMonth.entrySet()) {
            Row row = sheet.createRow(rowIndex);
            Cell month = row.createCell(0);
            Cell cancelCount = row.createCell(1);

            month.setCellValue(entry.getKey());
            cancelCount.setCellValue(entry.getValue());

            rowIndex++;
        }

        // Меняем размер столбца
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }
}
