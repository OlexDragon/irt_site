package irt.web.controllers;

import java.util.List;

import irt.web.entities.all.FirstDigitsEntity;
import irt.web.entities.all.repository.FirstDigitsRepository;
import irt.web.entities.all.repository.SecondAndThirdDigitRepository;
import irt.web.entities.component.repositories.PlacesRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
		logger.entry(id, classNames, objectToSend);
		model.addAttribute("id", id);
		model.addAttribute("classNames", classNames);
		model.addAttribute("options", objectToSend);
		return "fragments/html-components :: select";
	}

	List<FirstDigitsEntity> firstDigitsEntities;
	@Autowired
	private FirstDigitsRepository firstDigitsRepository;
	@RequestMapping("first-letters")
	public String  getFirstLetters(@RequestParam(required=false) String id, @RequestParam(required=false) String classNames, Model model){
		return getHtmlSelect(id, classNames, firstDigitsRepository.findAll(), model);
	}

	@Autowired
	private SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	@RequestMapping("secon-and-third-letters")
	public String  getSecondAndThirdLetters(@RequestParam Integer firstLetter, @RequestParam(required=false) String id, @RequestParam(required=false) String classNames, Model model){
		logger.entry(firstLetter, id, classNames);
		return getHtmlSelect(id, classNames, secondAndThirdDigitRepository.findByFirstDigitOrderByDescription(firstLetter, new Sort("description")), model);
	}

	@RequestMapping("part-number-details")
	public String getPartNumberDetails(@RequestParam Integer firstLetter, @RequestParam String secondLetters, Model model){
		logger.entry(firstLetter, secondLetters);

		

		return "fragments/html-components :: select";
	}
}
