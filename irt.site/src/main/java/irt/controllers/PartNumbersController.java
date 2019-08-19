package irt.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import irt.controllers.components.PartNumberComponent;
import irt.controllers.components.PartNumberForm;
import irt.controllers.components.PartNumberSecondDigitsService;
import irt.controllers.components.PartNumberService;
import irt.controllers.components.interfaces.OptionFor;
import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.FirstDigitsEntity;
import irt.entities.IrtComponentEntity;
import irt.entities.IrtComponentRepository;
import irt.entities.SecondAndThirdDigitEntity;

@Controller
@RequestMapping({"part-numbers", "irt/part-numbers"})
public class PartNumbersController {

	private final Logger logger = LogManager.getLogger();

	@Autowired ApplicationContext applicationContext;
	@Autowired private PartNumberService partNumberService;
	@Autowired private PartNumberSecondDigitsService secondDigitsService;
	@Autowired @Lazy private PartNumberComponent partNumberComponent;

	@ModelAttribute("pnActive")	public String active(){ return "active"; }
	@ModelAttribute("firstList")public List<FirstDigitsEntity> firstList(){ return partNumberComponent.getFirstDiditsList(); }

	@RequestMapping(method=RequestMethod.GET)
	public String getPartNumber(
								PartNumberForm form,
								@CookieValue(required=false) Integer first,
								@CookieValue(required=false) String second,
								@CookieValue(required=false) Long componentId,
								Model model){

		logger.error("\n1) '{}'\n2) '{}'\n3) '{}'\n4) '{}'", form, first, second, componentId);

		PartNumberForm pnf = partNumberComponent.getPartNumberForm();
		if(pnf!=null){
			form.set(pnf);
		}else if(first!=null){
			form.setId(componentId);
			form.setFirst(first);
			form.setSecond(second);
			partNumberService.fillForm(form);
		}

		addAttributes(form, model);

		return "partnumbers";
	}

	@RequestMapping(method=RequestMethod.POST, params="submit_show_part_number")
	public String setPartNumber(
								PartNumberForm form,
								HttpServletResponse response,
								Model model){

//		logger.error(form);
		partNumberService.fillForm(form);
//		logger.error(form);

		addAttributes(form, model);
		addCookies(form, response);

		return "partnumbers";
	}

	@RequestMapping(method=RequestMethod.POST, params="submit-reset")
	public String resetPartNumber(
								PartNumberForm form,
								Model model,
								HttpServletResponse response){

//		logger.error("getPartNumber:\n{}", form);

		form.setId(null);
		form.setFields(null);
		form.setPartNumber(null);
		partNumberService.fillForm(form);
		logger.error("getPartNumber:{}", form);

		addAttributes(form, model);

		return "partnumbers";
	}

	@RequestMapping(method=RequestMethod.POST, params="submit-add")
	public String addPartNumber(
								PartNumberForm form,
								Model model,
								HttpServletResponse response){

		form.setId(null);
		form.setPartNumber(null);
		partNumberService.fillForm(form);

		if(form.getId()!=null)
			model.addAttribute("error", "Component with this manufacture part number already exists");

		else if(partNumberService.isValid(form, model)){
			IrtComponentEntity entity = partNumberService.buildEntity(form);
			partNumberService.fillForm(form, componentRepository.save(entity));
		}

		addAttributes(form, model);
		addCookies(form, response);

		return "partnumbers";
	}

	@RequestMapping(method=RequestMethod.POST, params="submit-update")
	public String updatePartNumber(
								PartNumberForm form,
								Model model,
								HttpServletResponse response){

		if(partNumberService.isValid(form, model)){

			IrtComponentEntity entity = partNumberService.updateEntity(form);
			if(entity==null || entity.getId()==null)
				model.addAttribute("error", "This component can not be updated");

			else {
				PartNumberForm f = new PartNumberForm().set(form).setId(null);
				IrtComponentEntity e = partNumberService.buildEntity(f);

				if(e!=null && e.getId()!=null && !e.getId().equals(entity.getId()))
					model.addAttribute("error", "Component with this manufacture part number already exists");

				else
					partNumberService.fillForm(form, componentRepository.save(entity));
			}
		}

		addAttributes(form, model);
		addCookies(form, response);

		return "partnumbers";
	}

	@Autowired IrtComponentRepository componentRepository;
	@RequestMapping(method=RequestMethod.POST, params="submit-parse")
	public String parsePartNumber( PartNumberForm form, Model model, HttpServletResponse response){

		String pn = form.getPartNumber();
		if(pn==null || pn.isEmpty() || (pn = pn.replaceAll("[-\\s]", "")).isEmpty()){
			model.addAttribute("error", "Tyoe a part number in the text field");

		}else{
			IrtComponentEntity entity = componentRepository.findOneByPartNumber(pn);
			if(entity!=null)
				partNumberService.fillForm(form, entity);
			else{
				model.addAttribute("error", "The partnumber "+ pn +" does not exist");
				partNumberService.fillForm(form, pn);
			}
		}

		addAttributes(form, model);
		addCookies(form, response);
		return "partnumbers";
	}

	private List<SecondAndThirdDigitEntity> secondList(Integer first){ return secondDigitsService.setSecondDigitsEntities( first, partNumberComponent);}

	/**
	 * @param partNumbetForm
	 * @param first - first part number digit 
	 * @param second - second and third part number letters
	 * @param model
	 */
	private void addAttributes( PartNumberForm form, Model model) {

		Integer first = form.getFirst();
		String second = form.getSecond();

		model.addAttribute("secondList", secondList(first));
		options(first, second, model);

		partNumberComponent.setPartNumberForm(form);
		model.addAttribute("title", partNumberComponent.getPageTitle());
	}

	private void options(Integer first, String second, Model model) {

		ValueText[][] options;
		List<ArrayEntity> fields;

		if(first!=null) {
			String beanName = "optionsFor" + first + (second!=null ? second : "");
			OptionFor bean = (OptionFor) applicationContext.getBean(beanName);
			fields = bean.getFields();
			options = bean.getOptions();
		}else{
			fields = null;
			options = null;
		}

		model.addAttribute("fields", fields);
		model.addAttribute("options", options);
	}

	private void addCookies(PartNumberForm partNumbetForm, HttpServletResponse response) {
		response.addCookie(new Cookie("componentId", Optional.ofNullable(partNumbetForm.getId()).map(Object::toString).orElse(null)));
		response.addCookie(new Cookie("first", Optional.ofNullable(partNumbetForm.getFirst()).map(Object::toString).orElse(null)));
		response.addCookie(new Cookie("second", partNumbetForm.getSecond()));
	}
}
