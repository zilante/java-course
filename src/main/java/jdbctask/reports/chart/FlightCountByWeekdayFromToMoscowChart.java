package jdbctask.reports.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FlightCountByWeekdayFromToMoscowChart {
    public static void drawChart(String file1, String file2,
                                 Map<Integer, Integer> flightCountFromByWeekday,
                                 Map<Integer, Integer> flightCountToByWeekday)
            throws IOException {
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();

        for (Map.Entry<Integer, Integer> entry : flightCountFromByWeekday.entrySet()) {
            dataset1.setValue(entry.getValue(), "flight count", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Flight count from Moscow",
                "Weekday",
                "Flight count",
                dataset1,
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartUtils.saveChartAsPNG(new File(file1), barChart, 1000, 1000);


        DefaultCategoryDataset dataset2 = new DefaultCategoryDataset();

        for (Map.Entry<Integer, Integer> entry : flightCountToByWeekday.entrySet()) {
            dataset2.setValue(entry.getValue(), "flight count", entry.getKey());
        }

        barChart = ChartFactory.createBarChart(
                "Flight count to Moscow",
                "Weekday",
                "Flight count",
                dataset2,
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartUtils.saveChartAsPNG(new File(file2), barChart, 1000, 1000);
    }
}
