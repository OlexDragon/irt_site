package irt.entities.builders;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import irt.controllers.components.PartNumberForm;
import irt.entities.FirstDigitsRepository;
import irt.entities.IrtComponentEntity;
import irt.entities.IrtComponentRepository;
import irt.entities.ManufactureRepository;

public class EntityBuilderA implements EntityBuilder{

	protected final Logger logger = LogManager.getLogger(getClass());

	protected @Autowired FirstDigitsRepository firstDigitsRepository;
	protected @Autowired ManufactureRepository manufactureRepository;
	protected @Autowired IrtComponentRepository componentRepository;

	private final String NEXT_TWO_CHARS;

	public EntityBuilderA() {
		this(null);
	}

	protected EntityBuilderA(String secondChars) {
		NEXT_TWO_CHARS = secondChars;
	}

	@Override
	public IrtComponentEntity build(PartNumberForm form) {
		logger.error("build(PartNumberForm): ", form);

		IrtComponentEntity entity;
		if(form!=null){
			entity = Optional.ofNullable(form.getId()).flatMap(componentRepository::findById)
					.orElseGet(
							()->{
								IrtComponentEntity e = new IrtComponentEntity();
								//TODO Fill Entity
								return e;
							});
		}else
			entity = null;

		return entity;
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm, IrtComponentEntity componentEntity) {
		logger.error("fillForm(PartNumberForm, IrtComponentEntity):\n{}\n{} ", partNumberForm, componentEntity);
		
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm) {
		partNumberForm.setPartNumber(Optional.ofNullable(NEXT_TWO_CHARS).map(second->'A' + second).orElse("A"));
		partNumberForm.setFields(new String[3]);
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm, String partNumber) {
		logger.error("\n{}\npartNumber: {} ", partNumberForm, partNumber);
		
	}

	@Override
	public IrtComponentEntity updateEntity(PartNumberForm partNumberForm) {
		logger.error(partNumberForm);
		return null;
	}

	@Override
	public String getMfrId(PartNumberForm partNumberForm) {
		return null;
	}

	@Override
	public String getMfrPN(PartNumberForm partNumberForm) {
		return null;
	}

	@Override
	public String getDesctiption(PartNumberForm partNumberForm) {
		logger.error(partNumberForm);
		return null;
	}
}
