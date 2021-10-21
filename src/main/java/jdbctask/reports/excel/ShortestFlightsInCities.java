package jdbctask.reports.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ShortestFlightsInCities {
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
                                 List<List> shortestFlightsInCities) throws IOException {
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("shortest-flights-in-cities");

        Row attributeNamesRow = sheet.createRow(0);

        CellStyle cellStyle = book.createCellStyle();
        Font headerFont = book.createFont();
        setHeaderCellStyle(cellStyle, headerFont);

        Cell name1 = attributeNamesRow.createCell(0);
        name1.setCellStyle(cellStyle);
        name1.setCellValue("Departure city");

        Cell name2 = attributeNamesRow.createCell(1);
        name2.setCellStyle(cellStyle);
        name2.setCellValue("Arrival city");

        Cell name3 = attributeNamesRow.createCell(2);
        name3.setCellStyle(cellStyle);
        name3.setCellValue("Average flight duration");

        for (int i = 0; i < shortestFlightsInCities.get(0).size(); ++i) {
            Row row = sheet.createRow(i + 1);
            Cell departureCityCell = row.createCell(0);
            Cell arrivalCityCell = row.createCell(1);
            Cell averageFlightDurationCell = row.createCell(2);

            String departureCity = shortestFlightsInCities.get(0).get(i).toString();
            String arrivalCity = shortestFlightsInCities.get(1).get(i).toString();
            Double averageFlightDuration =
                    Double.parseDouble(shortestFlightsInCities.get(2).get(i).toString());

            departureCityCell.setCellValue(departureCity);
            arrivalCityCell.setCellValue(arrivalCity);
            averageFlightDurationCell.setCellValue(averageFlightDuration);
        }

        // Меняем размер столбца
        sheet.autoSizeColumn(1);

        // Записываем всё в файл
        book.write(new FileOutputStream(file));
        book.close();
    }
}
