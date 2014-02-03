package ibd.web.controller;

import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getIndex() {
		/*
		 * DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
		 * java.util.Date today = new java.util.Date();
		 * String dateOut = dateFormatter.format(today);
		 */
		LocalDateTime localDateTime = new LocalDateTime();
		
		/*
		 * index is the page (index.jsp), dateOut is now a variable that is mapped to localDateTime.toString()
		 * It can be called by using ${dateOut} 
		 */
		return new ModelAndView("index","dateOut",localDateTime.toString());

	}
}
