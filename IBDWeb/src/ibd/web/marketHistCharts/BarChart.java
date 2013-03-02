/* -------------------------
 * BarChart.java
 * -------------------------
 * (C) Copyright 2005-2009, by Object Refinery Limited.
 *
 */

package ibd.web.marketHistCharts;

import ibd.web.classes.Output;
import ibd.web.classes.VarDow;
import ibd.web.classes.VarNasdaq;
import ibd.web.classes.VarSP500;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.SortOrder;

/**
 * A simple demonstration application showing how to create a layered bar chart.
 */
public class BarChart{// extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
//    public BarChart(String title) {
//        super(title);
//        JPanel chartPanel = createDemoPanel();
//        chartPanel.setPreferredSize(new Dimension(500, 270));
//        setContentPane(chartPanel);
//    }

    /**
     * Returns a sample dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset(int time) {
	
	Output outputSP500 = VarSP500.currentSP500;
	Output outputDow = VarDow.currentDow;
	Output outputNasdaq = VarNasdaq.currentNasdaq;


	// row keys...
        String category1 = "SP500";
        String category2 = "Dow";
        String category3 = "Nasdaq";

        // column keys...
        String series1 = "Index %Gain/Loss";
        String series2 = "MarketPredictor %Gain/Loss";

        // create the dataset...
	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	int i=0;
	switch (time) {
	    case 5:
		i = 0;
		break;
	    case 10:
		i = 1;
		break;
	    case 15:
		i = 2;
		break;
	    case 20:
		i = 3;
		break;
	    case 30:
		i = 4;
		break;
	    case 40:
		i = 5;
		break;
	    case 50:
		i = 6;
		break;
	}
	dataset.addValue(outputSP500.histReturns[i][0], series1, category1);
	dataset.addValue(outputDow.histReturns[i][0], series1, category2);
	dataset.addValue(outputNasdaq.histReturns[i][0], series1, category3);

        dataset.addValue(outputSP500.histReturns[i][1], series2, category1);
        dataset.addValue(outputDow.histReturns[i][1], series2, category2);
        dataset.addValue(outputNasdaq.histReturns[i][1], series2, category3);

        return dataset;

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(CategoryDataset dataset,int time) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            time+" Year Comparisons (%)",  // chart title
            "Market Index",                  // domain axis label
            "% Gain/Loss",                     // range axis label
            dataset,                     // data
            PlotOrientation.VERTICAL,    // orientation
            true,                        // include legend
            true,                        // tooltips?
            true                        // URLs?
        );


        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangePannable(true);
        plot.setRangeZeroBaselineVisible(true);

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

//        LogAxis yAxis = new LogAxis("Y");
//        plot.setRangeAxis(yAxis);

        // disable bar outlines...
        LayeredBarRenderer renderer = new LayeredBarRenderer();
        renderer.setDrawBarOutline(false);
        plot.setRenderer(renderer);

        // for this renderer, we need to draw the first series last...
        plot.setRowRenderingOrder(SortOrder.DESCENDING);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.red, 0.0f,
                0.0f, new Color(0, 0, 64));
        GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green, 0.0f,
                0.0f, new Color(0, 64, 0));
        GradientPaint gp2 = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f,
                0.0f, new Color(64, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        return chart;
    }

    /**
     * Returns a chart object with a given dataset
     * @param time
     * @return
     */
    public static JFreeChart returnChart(int time){
	CategoryDataset dataset = BarChart.createDataset(time);
	JFreeChart chart = BarChart.createChart(dataset,time);
	return chart;
    }
}
