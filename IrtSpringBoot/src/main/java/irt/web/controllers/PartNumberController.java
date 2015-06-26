package irt.web.controllers;

import irt.web.entities.company.CompanyEntity;
import irt.web.entities.company.repositories.CompanyRepository;
import irt.web.entities.component.PlaceEntity;
import irt.web.entities.component.repositories.PlacesRepository;
import irt.web.entities.part_number.repository.SecondAndThirdDigitRepository;
import irt.web.view.ComponentMovementView;
import irt.web.view.ComponentView;
import irt.web.view.beans.ComponentBean;
import irt.web.view.beans.ComponentBean.Status;
import irt.web.view.workers.component.PartNumbers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PartNumberController {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ComponentView componentView;

	@ModelAttribute("menuPartNumber")
	public Boolean  attrSelectedMenu(){
		return true;
	}

	@ModelAttribute("componentView")
	public ComponentView  attrComponentView(){
		return componentView;
	}

	@RequestMapping
	public String partNumber(final ComponentBean componentBean){
		logger.entry(componentBean);

		if(componentBean.getPartNumber()==null){
			componentBean.setPartNumber(componentView.getPartNumber());
		}else if(!PartNumbers.equals(componentBean.getPartNumber(), componentView.getPartNumber())){
			componentView.setComponent(componentBean.getPartNumber());
		}

		componentBean.setStatus(componentView.getComponentEntity()!=null ? Status.SUCCESS : Status.ERROR);
		componentBean.setBom(componentView.hasBOM());
		logger.trace("componentBean: {}", componentBean);

		return "partnumber";
	}

	@RequestMapping("/login_error")
	public String logInError(final ComponentBean componentBean, Model model, @RequestParam(required=false) boolean login){
		logger.entry();

		if(login)
			model.addAttribute("login", true);
		else{
			model.addAttribute("loginEror", true);
			model.addAttribute("msgEror", "Incorrect username or password");
		}

		componentBean.setPartNumber(componentView.getPartNumber());
		componentBean.setStatus(componentView.getComponentEntity()!=null ? Status.SUCCESS : Status.ERROR);
		componentBean.setBom(componentView.hasBOM());
		logger.trace("componentBean: {}", componentBean);

		return "partnumber";
	}

	@Lazy
	@Autowired
	private ComponentMovementView movementView;
	@Autowired
	private PlacesRepository placesRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@RequestMapping("/movement/palce-change")
	public String palceChanged(@RequestParam(required=false) String name, @RequestParam(required=false)Long id, Model model){
		logger.entry(name, id);
		List<CompanyEntity> objectToSend;
		if (name != null) {

			PlaceEntity findOne;
			if(id!=null)
				findOne = placesRepository.findOne(id);
			else
				findOne = null;
			logger.trace(findOne);

			switch (name) {
			case "from":
				movementView.setFrom(id);
				movementView.setFromDetailsTableName(findOne!=null ? findOne.getTableName() : null);
				break;
			case "to":
				movementView.setTo(id);
				movementView.setToDetailsTableName(findOne!=null ? findOne.getTableName() : null);
			}
			objectToSend = companyRepository.findByType(findOne.getPlaceId().shortValue());
		}else
			 objectToSend = null;

		return LoadComponentsController.getHtmlSelect(null, "detail", objectToSend, model);
	}

	@RequestMapping("/movement/details/{tableName}")
	public String getPlaceDetails(@PathVariable String tableName, Model model){
		logger.entry(tableName);

		model.addAttribute("details", companyRepository.findAll());

		return "fragments/movement :: details";
	}
	@Autowired
	private SecondAndThirdDigitRepository secondAndThirdDigitRepository;

	@PreAuthorize("hasRole('PART_NUMBER_EDIT')")
	@RequestMapping("add-part-number")
	public String addPartNumber(ComponentBean componentBean, Model model){

		model.addAttribute("addPartNumber", true);

		return partNumber(componentBean);
	}
}
