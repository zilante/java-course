package jdbctask.reports.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CancelCountByMonthChart {
    public static void drawChart(String file,
                                 Map<Integer, Integer> cancelCountByMonth)
            throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<Integer, Integer> entry : cancelCountByMonth.entrySet()) {
            dataset.setValue(entry.getValue(), "cancel count", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Cancel count by month",
                "Month",
                "Cancel count",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartUtils.saveChartAsPNG(new File(file), barChart, 1000, 1000);
    }
}
