package ibd.web.controller;

import ibd.web.Resource.LoadProperties;
import ibd.web.classes.IBD50DataRetriever;
import ibd.web.classes.MarketDB;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		Map<String, Object> model = new HashMap<String, Object>();
		String currentDate = new IBD50DataRetriever().getTableName();
		model.put("showDate",currentDate);
		List<String> allTables = new IBD50DataRetriever().getAllIndices(currentDate);
		model.put("allTables",allTables);
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
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(c!=null) {
				try {
					c.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return tableNames;
	}
}
