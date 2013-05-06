package ibd.web.controller;

import ibd.web.Resource.LoadProperties;
import ibd.web.beans.StockData;
import ibd.web.classes.MarketDB;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.OHLCDataset;
import org.jfree.data.xy.XYDataset;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/showDataIndices50")
public class ShowDataIndices50 {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getIndex(HttpServletRequest request, HttpServletResponse response) {
		String selectedDate=(String)request.getParameter("allDates");
		List<String> tables = getAllTables(selectedDate);
        String chartText = null;
		if(null != selectedDate && !"".equalsIgnoreCase(selectedDate.trim())){
			chartText = selectedDate.trim();
		}else{
			chartText = tables.get(0);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("chartText", chartText);
		model.put("showDate",new Date().toString());
		model.put("allTables",tables);
		return new ModelAndView("allCharts","model",model);
	}
	
	public void removeFromDirectory(String directoryName){
		File[] files = finder(LoadProperties.serverPath);
		for(File file: files){ 
			file.delete();
		}
	}
	
	public File[] finder(String dirName){
    	File dir = new File(dirName);

		if (!dir.exists())
		  {
		    System.out.println("creating directory: " + dir.toString());
		    boolean result = dir.mkdir();  
		    if(result){    
		       System.out.println("DIR created");  
		     }

		  }
    	return dir.listFiles(new FilenameFilter() { 
    	         public boolean accept(File dir, String filename)
    	              { return filename.endsWith(".png"); }
    	} );

    }
	
	private static List<String> getAllTables(String selectedVal){
		Connection c = MarketDB.getConnectionIBD50PricesVolumes();
		List<String> tableNames = new ArrayList<String>();
		if(null != selectedVal && !"".equalsIgnoreCase(selectedVal.trim())){
			tableNames.add(selectedVal);
		}
		DatabaseMetaData md = null;
		try {
			md = c.getMetaData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    ResultSet rs = null;
		try {
			rs = md.getTables(null, null, "%", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    try {
			while (rs.next()) {
			  String name = rs.getString(3);
			  name = name.substring(1);
			  if(!tableNames.contains(name)){
				  tableNames.add(name);
			  }
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs!=null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if(c!=null)
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	    return tableNames;
	}
}
