package ibd.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page")
public class TestController {
	/**
	 * 
	 * @return ModelAndView
	 */
	@RequestMapping(method = {RequestMethod.GET})
	public ModelAndView getIndex() {
        return new ModelAndView("page");
	}
}
