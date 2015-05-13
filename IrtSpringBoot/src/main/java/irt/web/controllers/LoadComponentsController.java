package irt.web.controllers;

import irt.web.entities.component.repositories.PlacesRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("load")
public class LoadComponentsController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private PlacesRepository placesRepository;
	@RequestMapping("select")
	public String  getPlaces(@RequestParam(required=false) String id, @RequestParam(required=false) String classNames, Model model){
		return getHtmlSelect(id, classNames, placesRepository.findAll(), model);
	}

	public static String getHtmlSelect(String id, String classNames, Object objectToSend, Model model){
		logger.entry(id, classNames, 0);
		model.addAttribute("id", id);
		model.addAttribute("classNames", classNames);
		model.addAttribute("options", objectToSend);
		return "fragments/html-components :: select";
	}
}
