package ibd.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class ErrorController {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = {RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getIndex() {
		return new ModelAndView("error");
	}
}
