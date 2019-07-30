package irt.controllers.components;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import irt.controllers.components.validators.Validator;
import irt.entities.FirstDigitsRepository;
import irt.entities.IrtComponentEntity;
import irt.entities.IrtComponentRepository;
import irt.entities.builders.EntityBuilder;

@Service
public class PartNumberService {

	private final Logger logger = LogManager.getLogger();

	@Autowired ApplicationContext applicationContext;
	@Autowired IrtComponentRepository componentRepository;

	public IrtComponentEntity buildEntity(PartNumberForm partNumberForm) {

		if(partNumberForm==null)
			return null;

		IrtComponentEntity buildEntity = getBuilder(partNumberForm).build(partNumberForm);

		String pn = partNumberForm.getPartNumber();
		if(buildEntity!=null){
			IrtComponentEntity entity;

			if(buildEntity.getId()!=null)
				entity = componentRepository.findById(buildEntity.getId()).get();

			else if(buildEntity.getManufPartNumber()!=null && !buildEntity.getManufPartNumber().isEmpty())
				entity = componentRepository.findOneByManufPartNumber(buildEntity.getManufPartNumber());

			else if(pn==null || (pn = pn.replaceAll("-", "")).contains("_"))
				entity = null;

			else
				entity = componentRepository.findOneByPartNumber(pn);

				if(entity!=null)
				buildEntity = entity;
		}

		return buildEntity;
	}

	public IrtComponentEntity updateEntity(PartNumberForm partNumberForm) {

		return getBuilder(partNumberForm).updateEntity(partNumberForm);
	}

	public void fillForm(PartNumberForm partNumberForm) {

		IrtComponentEntity entity = findEntityById(partNumberForm);
		logger.trace("findEntityById:{}", entity);

		if(entity==null){
			entity =  findByMfrPN(partNumberForm);
			logger.trace("findByMfrPN:{}", entity);
		}

		if(entity==null){
			entity =  findEntityByPN(partNumberForm);
			logger.trace("findEntityByPN:{}", entity);
		}

		if(entity!=null)
			fillForm(partNumberForm, entity);

		else{
			getBuilder(partNumberForm).fillForm(partNumberForm);
		}
	}

	public void fillForm(PartNumberForm partNumberForm, IrtComponentEntity entity) {

		if(partNumberForm!=null && entity!=null){
			String pn = entity.getPartNumber();
			if(pn!=null && !pn.isEmpty() && pn.length()>=3){
				String ext = pn.substring(0, 3);
				EntityBuilder entityBuilder = (EntityBuilder) applicationContext.getBean("entityBuilder" + ext);
				entityBuilder.fillForm(partNumberForm, entity);
			}
		}
	}

	@Autowired @Lazy FirstDigitsRepository firstDigitsRepository;
	public void fillForm(PartNumberForm partNumberForm, String partNumber) {

		if(Character.isDigit(partNumber.charAt(0))){
			partNumberForm.setFirst(firstDigitsRepository.findOneByPartNumbetFirstChar(partNumber.charAt(0)).getId());

			if(partNumber.length()>=3)
				partNumberForm.setSecond(partNumber.substring(1, 3));

			EntityBuilder entityBuilder = getBuilder(partNumberForm);
			entityBuilder.fillForm(partNumberForm, partNumber);
		}
		
	}

	private IrtComponentEntity findByMfrPN(PartNumberForm partNumberForm) {
		EntityBuilder entityBuilder = getBuilder(partNumberForm);

		IrtComponentEntity entity;
		String mfrPN = entityBuilder.getMfrPN(partNumberForm);
		if(mfrPN!=null && !(mfrPN).isEmpty())
			entity = componentRepository.findOneByManufPartNumber(mfrPN);
		else
			entity = null;

		return entity;
	}

	public boolean isValid(PartNumberForm partNumberForm, Model model) {

		Validator validator = getValidator(partNumberForm);

		String errorMessage = validator.validatre(partNumberForm);
		if(errorMessage!=null)
			model.addAttribute("error", errorMessage);

		return errorMessage==null;
	}

	private IrtComponentEntity	findEntityByPN	(PartNumberForm partNumberForm) { String pn = partNumberForm.getPartNumber(); return pn!=null && !(pn = pn.replaceAll("[-\\s]", "")).isEmpty() ? componentRepository.findOneByPartNumber(pn) : null; }
	private IrtComponentEntity	findEntityById	(PartNumberForm partNumberForm) { return partNumberForm.getId()!=null ? componentRepository.findById(partNumberForm.getId()).get() : null; }
	private Validator			getValidator	(PartNumberForm partNumberForm) { return (Validator) applicationContext.getBean("validator" + getExt(partNumberForm)); }
	private EntityBuilder		getBuilder		(PartNumberForm partNumberForm) { return (EntityBuilder) applicationContext.getBean("entityBuilder" + getExt(partNumberForm)); }
//	private EntityBuilder		getBuilder		(String firstThreeLetters) { return (EntityBuilder) applicationContext.getBean("entityBuilder" + firstThreeLetters); }

	private String getExt(PartNumberForm partNumberForm) {
		Integer f = partNumberForm.getFirst();
		String s = partNumberForm.getSecond();
		String ext = (f!=null ? f : "") + (s!=null ? s : "");
		return ext;
	}

	public String getMfrPN(PartNumberForm form) {
		return getBuilder(form).getMfrPN(form);
	}
}
