package irt.web.controllers;

import irt.web.entities.all.ArrayEntity;
import irt.web.entities.all.FirstDigitsEntity;
import irt.web.entities.all.SecondAndThirdDigitEntity;
import irt.web.entities.all.SecondAndThirdDigitEntityPK;
import irt.web.entities.all.repository.FirstDigitsRepository;
import irt.web.entities.all.repository.SecondAndThirdDigitRepository;
import irt.web.entities.component.repositories.PlacesRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

		SecondAndThirdDigitEntity secondAndThirdDigitEntity = secondAndThirdDigitRepository.findOne(new SecondAndThirdDigitEntityPK(secondLetters, firstLetter));
		if(secondAndThirdDigitEntity == null || secondAndThirdDigitEntity.getClassIdHasArrayEntity()==null){
			logger.error("\n\tHave to add relation to  'ClassIdHasArrayEntity' with  firstLetterId='{}' and secondLetters='{}'\n\tsecondAndThirdDigitEntity={}", firstLetter, secondLetters, secondAndThirdDigitEntity);
			return null;
		}

		model.addAttribute("arrayEntities", getHtmlFieldsArray(secondAndThirdDigitEntity));

		return "fragments/add-part-number :: pn-details";
	}

	private List<ArrayEntity> getHtmlFieldsArray(
			SecondAndThirdDigitEntity secondAndThirdDigitEntity) {
		List<ArrayEntity> arrayEntities = secondAndThirdDigitEntity.getClassIdHasArrayEntity().getArrayEntityList();
		for(ArrayEntity ae:arrayEntities){
			String text = ae.getArrayEntityPK().getId();
			int i = text.indexOf("<");
			if(i>=0) ae.getArrayEntityPK().setId(text.substring(text.indexOf(">")+1));
		}
		Collections.sort(arrayEntities, new Comparator<ArrayEntity>() {
			@Override
			public int compare(ArrayEntity o1, ArrayEntity o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});
		return arrayEntities;
	}
}
