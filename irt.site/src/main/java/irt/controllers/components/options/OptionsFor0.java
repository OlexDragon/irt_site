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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import irt.controllers.components.interfaces.OptionFor;
import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.ArrayNameEntity;
import irt.entities.ClassIdHasArrayEntity;
import irt.entities.FirstDigitsHasArrayNamesEntity;
import irt.entities.ManufactureEntity;
import irt.entities.SecondAndThirdDigitEntity;
import irt.entities.SecondAndThirdDigitPK;
import irt.entities.builders.EntityBuilderAbstract;
import irt.entities.repository.ArrayEntityRepository;
import irt.entities.repository.FirstDigitsHasArrayNamesEntityRepository;
import irt.entities.repository.ManufactureRepository;
import irt.entities.repository.SecondAndThirdDigitRepository;

@Component
@Scope("session")
public class OptionsFor0 implements OptionFor{

	protected final Logger logger = LogManager.getLogger(getClass().getName());

	@Autowired @Lazy protected FirstDigitsHasArrayNamesEntityRepository firstDigitsHasArrayNamesEntityRepository;
	@Autowired @Lazy protected SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	@Autowired @Lazy protected ManufactureRepository manufactureRepository;
	@Autowired @Lazy protected ArrayEntityRepository arrayEntityRepository;

	private final Integer FIRST_ID = 1;
	private final String SECOND_ID;
	protected final int LENGTH;
	protected final int MFR;

	protected ValueText[][]		options;			  public ValueText[][] getOptions()		{ return options; }
	protected List<ArrayEntity>	fields; 	@Override public List<ArrayEntity> getFields()	{ return fields; }


	public OptionsFor0(EntityBuilderAbstract builder) {
		this(null, builder.FIELDS_COUNT, builder.MFR_INDEX);
	}
	public OptionsFor0(String secondId, int fieldsCount, int mfr) {

		SECOND_ID = secondId;
		LENGTH = fieldsCount;
		MFR = mfr;
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
			FirstDigitsHasArrayNamesEntity entity = firstDigitsHasArrayNamesEntityRepository.findById(FIRST_ID).get();
			fields = entity.getArrayNameEntity().getArrayEntities();
		}else{
			SecondAndThirdDigitEntity entity = secondAndThirdDigitRepository.findById(new SecondAndThirdDigitPK(SECOND_ID, FIRST_ID)).get();
			fields = entity.getHasArrayEntity().flatMap(ClassIdHasArrayEntity::getArrayNameEntity).map(ArrayNameEntity::getArrayEntities).orElseGet(()->new ArrayList<>());
		}
	}

	protected void setFieldOptions() {

		//Manufactures
		List<ManufactureEntity> entities = manufactureRepository.findAll( Sort.by("name"));
		if(entities!=null)
			options[MFR] = entities.toArray(new ValueText[0]);
	}


	@Override
	public void updateOptions(String partNumber) {
	}
}
