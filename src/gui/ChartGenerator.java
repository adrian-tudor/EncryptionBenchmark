package gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.List;

public class ChartGenerator {

    public JPanel createBarChart(List<String[]> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String[] row : results) {
            String algorithm = row[0];
            String operation = row[1];
            double time;
            try {
                time = Double.parseDouble(row[2].replace(" ms", "").trim());
            } catch (NumberFormatException e) {
                time = 0.0; // fallback
            }
            dataset.addValue(time, algorithm, operation);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Performanta Algoritmi",
                "Operatie",
                "Timp (ms)",
                dataset
        );

        return new ChartPanel(chart);
    }
}
