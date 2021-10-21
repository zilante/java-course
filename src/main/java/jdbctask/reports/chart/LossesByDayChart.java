package jdbctask.reports.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class LossesByDayChart {
    public static void drawChart(String file, Map<String, Double> lossesByDay)
            throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : lossesByDay.entrySet()) {
            dataset.setValue(entry.getValue(), "loss", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Losses by day",
                "Day",
                "Loss",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        ChartUtils.saveChartAsPNG(new File(file), barChart, 1000, 1000);
    }
}