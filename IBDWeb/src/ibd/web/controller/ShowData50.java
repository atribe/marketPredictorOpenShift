package ibd.web.controller;

import ibd.web.beans.Data50;
import ibd.web.classes.IBD50DataRetriever;

import java.util.Date;
import java.util.List;

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
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getIndex() {
		String[] date = (new Date().toString()).split(" ");
		String currentDate = returnMonth(date[1])+"/"+date[2]+"/"+date[5];
		List<Data50> data50List = new IBD50DataRetriever().getData50("3/25/2013", currentDate);
		return new ModelAndView("data50","data50List",data50List);
 
	}
	
	public Integer returnMonth(String month){
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
