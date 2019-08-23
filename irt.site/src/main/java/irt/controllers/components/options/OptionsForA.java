package irt.controllers.components.options;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import irt.beans.ValueTextImpl;
import irt.beans.assemblies.AssembliesRevisionBean;
import irt.controllers.components.interfaces.OptionFor;
import irt.controllers.components.interfaces.ValueText;
import irt.entities.ArrayEntity;
import irt.entities.repository.ArrayEntityRepository;
import irt.entities.repository.FirstDigitsHasArrayNamesEntityRepository;
import irt.entities.repository.IrtComponentRepository;
import irt.entities.repository.SecondAndThirdDigitRepository;

@Component
@Scope("session")
public class OptionsForA implements OptionFor{

	protected final Logger logger = LogManager.getLogger(getClass().getName());

	@Autowired @Lazy protected FirstDigitsHasArrayNamesEntityRepository firstDigitsHasArrayNamesEntityRepository;
	@Autowired @Lazy protected SecondAndThirdDigitRepository secondAndThirdDigitRepository;
	@Autowired @Lazy protected ArrayEntityRepository arrayEntityRepository;
	@Autowired @Lazy protected IrtComponentRepository irtComponentRepository;

	private final Integer FIRST_ID = 2;
	private final int LENGTH	= 3;
	private final int OPTION	= 1;
	private final int REVISION	= 2;

	protected ValueText[][]		options;			  public ValueText[][] getOptions()		{ return options; }
	protected List<ArrayEntity>	fields; 	@Override public List<ArrayEntity> getFields()	{ return fields; }

	@PostConstruct
	private void fillProperties(){

		setFields();
	}

	@Transactional
	protected void setFields() {

		options = new ValueText[LENGTH][0];

		firstDigitsHasArrayNamesEntityRepository.findById(FIRST_ID).ifPresent(entity->fields = entity.getArrayNameEntity().getArrayEntities());
	}

	@Override
	public void updateOptions(String partNumber) {
		Optional.ofNullable(partNumber).map(pn->pn.replaceAll("[-_]", "")).filter(pn->!pn.isEmpty())
		.ifPresent(
				pn->{

					int count = 0;
					//Remove revision
					if(pn.length()>9) {
						pn = pn.substring(0, 9);
						count = irtComponentRepository.findByPartNumberStartingWith(pn).size();
					}

					options[REVISION] = IntStream.rangeClosed(1, ++count).mapToObj(AssembliesRevisionBean::new).toArray(ValueText[]::new);

					
					//Remove revision
					if(pn.length()>8) {
						pn = pn.substring(0, 8);
						count = irtComponentRepository.findByPartNumberStartingWith(pn).size();
					}else
						count = 0;

					if(count>8)	//only one digit is used
						count = 8;

					options[OPTION] = IntStream.rangeClosed(1, ++count).mapToObj(ValueTextImpl::new).toArray(ValueText[]::new);
				});
	}
}
