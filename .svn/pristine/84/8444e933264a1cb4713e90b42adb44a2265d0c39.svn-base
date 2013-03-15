package ibd.web.controller;

import java.util.HashMap;
import java.util.Map;

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
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView getIndex() {
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("error", "Job is running for now. Please hold!!!");
        return new ModelAndView("index", "model", model);
	}
}
