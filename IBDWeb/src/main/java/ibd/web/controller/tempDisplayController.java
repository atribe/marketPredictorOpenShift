package ibd.web.controller;

import ibd.web.DAO.IndexAnalysisRowDAO;
import ibd.web.DataObjects.IndexAnalysisRow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tempDisplay")
public class tempDisplayController {

@Autowired private IndexAnalysisRowDAO indexAnalyaisRowDAO;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<IndexAnalysisRow> analysisRows = indexAnalyaisRowDAO.findAll();
		model.addAttribute("analysisRows", analysisRows);
		return "index";
	}
}
