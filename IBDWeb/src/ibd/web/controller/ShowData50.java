package ibd.web.controller;

import ibd.web.beans.Data50;
import ibd.web.classes.IBD50DataRetriever;

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
@RequestMapping("/showData50")
public class ShowData50 {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView getIndex(HttpServletRequest request, HttpServletResponse response) {
		if(!ibd.web.Constants.Constants.jobRunning){
			ibd.web.Constants.Constants.logger.info("Showing Data for IBD50.");
			String selectedDate=request.getParameter("allDates");
			System.out.println(selectedDate);
			Map<String, Object> model = new HashMap<String, Object>();
			List<Data50> data50List = null;
			String currentDate = "";
			if(null != selectedDate && !selectedDate.equalsIgnoreCase("")){
				currentDate = selectedDate;
				data50List = new IBD50DataRetriever().getData50(selectedDate);
			}else{
				currentDate = new IBD50DataRetriever().getTableName();
				data50List = new IBD50DataRetriever().getData50();
			}
			model.put("data50List", data50List);
			model.put("showDate", currentDate);
			List<String> allTables = new IBD50DataRetriever().getAllTables();
			List<String> realDates = new ArrayList<String>();
			for(int i=0;i<allTables.size();i++){
				int index = new IBD50DataRetriever().getLargestDate(allTables);
				realDates.add(allTables.get(index));
				allTables.remove(index);
			}
			List<String> allDates = new ArrayList<String>();
			allDates.add(currentDate);
			for(String tableName: realDates){
				if(!currentDate.equalsIgnoreCase(tableName)){
					allDates.add(tableName);
				}
			}
			model.put("allDates", allDates);
			return new ModelAndView("data50","model",model);
		}else{
			return new ModelAndView("error","model",null);
		}

	}

	private Integer returnMonth(String month){
		if(month.contains("Jan")){
			return 1;
		}else if(month.contains("Feb")){
			return 2;
		}else if(month.contains("Mar")){
			return 3;
		}else if(month.contains("Apr")){
			return 4;
		}else if(month.contains("May")){
			return 5;
		}else if(month.contains("Jun")){
			return 6;
		}else if(month.contains("Jul")){
			return 7;
		}else if(month.contains("Aug")){
			return 8;
		}else if(month.contains("Sep")){
			return 9;
		}else if(month.contains("Oct")){
			return 10;
		}else if(month.contains("Nov")){
			return 11;
		}else{
			return 12;
		}
	}
}
