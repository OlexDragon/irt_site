package irt.controllers.components.options;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.OptionFor;
import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.ArrayEntityRepository;
import irt.entities.ArrayNameEntity;
import irt.entities.ClassIdHasArrayEntity;
import irt.entities.FirstDigitsHasArrayNamesEntityRepository;
import irt.entities.SecondAndThirdDigitPK;
import irt.entities.SecondAndThirdDigitRepository;

@Component
@Scope("session")
public class OptionsForA implements OptionFor{

	protected final Logger logger = LogManager.getLogger(getClass().getName());

	@Autowired @Lazy protected FirstDigitsHasArrayNamesEntityRepository firstDigitsHasArrayNamesEntityRepository;
	@Autowired @Lazy protected SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	@Autowired @Lazy protected ArrayEntityRepository arrayEntityRepository;

	private final Integer FIRST_ID = 2;
	private final int LENGTH = 3;
	private final String SECOND_ID;

	protected ValueText[][]		options;			  public ValueText[][] getOptions()		{ return options; }
	protected List<ArrayEntity>	fields; 	@Override public List<ArrayEntity> getFields()	{ return fields; }


	public OptionsForA() { this(null); }
	public OptionsForA(String secondId) {

		SECOND_ID = secondId;
	}

	@PostConstruct
	private void fillProperties(){

		setFields();
		setFieldOptions();
	}

	@Transactional
	protected void setFields() {

		options = new ValueText[LENGTH][0];

		if(SECOND_ID==null){
			firstDigitsHasArrayNamesEntityRepository.findById(FIRST_ID)
			.ifPresent(entity->fields = entity.getArrayNameEntity().getArrayEntities());
		}else{
			final SecondAndThirdDigitPK id = new SecondAndThirdDigitPK(SECOND_ID, FIRST_ID);
//			logger.error(id);
			secondAndThirdDigitRepository.findById(id)
			.ifPresent(
					entity->{
						logger.error("setFields() \n{}", entity);
						fields = entity.getHasArrayEntity()
								.flatMap(ClassIdHasArrayEntity::getArrayNameEntity)
								.map(ArrayNameEntity::getArrayEntities)
								.orElseGet(
										()->{
											logger.warn("setFields(): Database doesn't have ArrayNameEntity.\nClassIdHasArrayEntity or ArrayNameEntity equals NULL {}", entity);
											return new ArrayList<>();
										});
					});
		}
	}

	protected void setFieldOptions() {
	}
}
