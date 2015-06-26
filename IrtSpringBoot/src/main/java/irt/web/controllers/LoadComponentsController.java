package irt.web.controllers;

import irt.web.data.Numbers;
import irt.web.entities.all.ArrayEntity;
import irt.web.entities.all.repository.ArrayEntityRepository;
import irt.web.entities.component.repositories.ComponentsRepository;
import irt.web.entities.component.repositories.ManufactureRepository;
import irt.web.entities.component.repositories.PlacesRepository;
import irt.web.entities.part_number.FirstDigitsEntity;
import irt.web.entities.part_number.HtmOptionEntityPK;
import irt.web.entities.part_number.HtmlOptionEntity;
import irt.web.entities.part_number.PartNumberDetailsPK;
import irt.web.entities.part_number.PartNumberDetailsView;
import irt.web.entities.part_number.SecondAndThirdDigitEntity;
import irt.web.entities.part_number.SecondAndThirdDigitPK;
import irt.web.entities.part_number.repository.FirstDigitsRepository;
import irt.web.entities.part_number.repository.HtmlOptionEntityRepository;
import irt.web.entities.part_number.repository.HtmlOptionsViewRepository;
import irt.web.entities.part_number.repository.PartNumberDetailsViewRepository;
import irt.web.entities.part_number.repository.SecondAndThirdDigitRepository;
import irt.web.workers.beans.interfaces.ValueText;

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
import org.springframework.web.bind.annotation.ResponseBody;

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

	@Autowired
	private FirstDigitsRepository firstDigitsRepository;

	@RequestMapping("first-digit")
	@ResponseBody
	public FirstDigitsEntity  getFirsDigit(@RequestParam Integer id){
		return firstDigitsRepository.findOne(id);
	}

	@RequestMapping("first-letter")
	public String  getFirstLetters(@RequestParam(required=false) String id, @RequestParam(required=false) String classNames, Model model){
		return getHtmlSelect(id, classNames, firstDigitsRepository.findAll(), model);
	}

	@Autowired
	private SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	@RequestMapping("secon-and-third-letters")
	public String  getSecondAndThirdLetters(@RequestParam Integer firstLetter, @RequestParam(required=false) String id, @RequestParam(required=false) String classNames, Model model){
		logger.entry(firstLetter, id, classNames);
		return getHtmlSelect(id, classNames, secondAndThirdDigitRepository.findByKeyIdFirstDigitOrderByDescriptionAsc(firstLetter), model);
	}

	@RequestMapping("part-number-details")
	public String getPartNumberDetails(@RequestParam Integer firstLetter, @RequestParam String secondLetters, Model model){
		logger.entry(firstLetter, secondLetters);

		SecondAndThirdDigitEntity secondAndThirdDigitEntity = secondAndThirdDigitRepository.findOne(new SecondAndThirdDigitPK(secondLetters, firstLetter));
		if(secondAndThirdDigitEntity == null || secondAndThirdDigitEntity.getHasArrayEntity()==null){
			logger.error("\n\tHave to add relation to  'ClassIdHasArrayEntity' with  firstLetterId='{}' and secondLetters='{}'\n\tsecondAndThirdDigitEntity={}", firstLetter, secondLetters, secondAndThirdDigitEntity);
			return null;
		}

		model.addAttribute("classId", secondAndThirdDigitEntity.getHasArrayEntity().getClassId());
		model.addAttribute("arrayEntities", getHtmlFieldsArray(secondAndThirdDigitEntity));

		return "fragments/add-part-number :: pn-details";
	}

	private List<ArrayEntity> getHtmlFieldsArray(
			SecondAndThirdDigitEntity secondAndThirdDigitEntity) {
		List<ArrayEntity> arrayEntities = secondAndThirdDigitEntity.getHasArrayEntity().getArrayEntityList();
		for(ArrayEntity ae:arrayEntities){
			String text = ae.getKey().getId();
			int i = text.indexOf("<");
			if(i>=0) ae.getKey().setId(text.substring(text.indexOf(">")+1).trim());
		}
		Collections.sort(arrayEntities, new Comparator<ArrayEntity>() {
			@Override
			public int compare(ArrayEntity o1, ArrayEntity o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});
		return arrayEntities;
	}

	@Autowired
	private HtmlOptionsViewRepository htmlOptionsViewRepository;
	@Autowired
	private HtmlOptionEntityRepository htmlOptionEntityRepository;
	@Autowired
	private ManufactureRepository manufactureRepository;
	@RequestMapping("pn-options")
	public String getHtmlOptionsView(@RequestParam Integer classId, @RequestParam Short arraySequence, @RequestParam String title, Model model){
		logger.entry(classId, arraySequence);

		List<? extends ValueText> valueTexts = htmlOptionsViewRepository.findByClassIdAndArraySequence(classId, arraySequence, new Sort("description"));

		if(valueTexts.isEmpty()) {
			HtmlOptionEntity htmlOptionEntity = htmlOptionEntityRepository.findOne(new HtmOptionEntityPK(classId, arraySequence));
			switch(htmlOptionEntity.getArrayName()){
			case "mfr":
				valueTexts = manufactureRepository.findAll(new Sort("name"));
			}
		}

		logger.trace("\n\t{}", valueTexts);

		model.addAttribute("classId", classId);
		model.addAttribute("title", title);
		model.addAttribute("options", valueTexts);

		 return "fragments/html-components :: options";
	}

	@Autowired private ArrayEntityRepository arrayEntityRepository;
	@Autowired private PartNumberDetailsViewRepository partNumberDetailsViewRepository;
	@Autowired private ComponentsRepository componentsRepository;

	@RequestMapping("part-number")
	@ResponseBody
	public String getPartNumber(
			@RequestParam Integer sequence,
			@RequestParam String detailId,
			@RequestParam String pn){

		logger.entry(sequence, detailId, pn);

		if(pn!=null && pn.length()>=3){
			PartNumberDetailsView partNumberDetailsView;
			if(detailId==null){

				partNumberDetailsView = partNumberDetailsViewRepository.findFirstByKeyFirstThreeCharsAndKeySequence(pn.substring(0, 3), sequence);
				Integer size = partNumberDetailsView.getSize();

				if(size!=null)
					detailId = new String(new char[size]);
				else
					partNumberDetailsView = null;;

			}else
				partNumberDetailsView = partNumberDetailsViewRepository.findOne(new PartNumberDetailsPK(pn.substring(0, 3), sequence, detailId));
			logger.trace("\n\t{}", partNumberDetailsView);

			if(partNumberDetailsView!=null){
				Integer position1 = partNumberDetailsView.getPosition();
				Integer size = partNumberDetailsView.getSize();
				pn = getPartNumber(pn, detailId, position1, size);
			}
		}
		return pn;
	}

	private String getPartNumber(String pn, String detailId, Integer position1,
			Integer size) {
		if(position1!=null){
			int position2 = position1 + size;

			pn = pn.replaceAll("-", "");
			pn = String.format("%1$-18s", pn);
			pn = pn.substring(0, position1) + detailId + pn.substring(position2);
			pn = pn.trim().replaceAll(" ", "_");
			pn = componentsRepository.partNumberWithDashes(pn);
			logger.trace("'{}'", pn);
		}
		return pn;
	}
	@RequestMapping("part-number/input")
	@ResponseBody
	public String getPartNumberFromInputField(
			@RequestParam Integer sequence,
			@RequestParam String detailId,
			@RequestParam String pn,
			@RequestParam boolean seqN){

		logger.entry(sequence, detailId, pn, seqN);

		PartNumberDetailsView partNumberDetailsView = partNumberDetailsViewRepository.findFirstByKeyFirstThreeCharsAndKeySequence(pn.substring(0, 3), sequence);
		logger.trace(partNumberDetailsView);

		if(partNumberDetailsView!=null){
			Integer size = partNumberDetailsView.getSize();
			detailId = Numbers.numberToExponential(detailId, size);
			pn = getPartNumber(pn, detailId, partNumberDetailsView.getPosition(), size);
		}

		return pn;
	}
}
