package ibd.web.controller;

import java.text.DateFormat;
import java.util.Locale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/index")
public class IndexController {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getIndex() {
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.US);
		java.util.Date today = new java.util.Date();
		String dateOut = dateFormatter.format(today);
		return new ModelAndView("index","dateOut",dateOut);
 
	}
}
