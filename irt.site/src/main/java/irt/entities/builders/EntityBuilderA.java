package irt.entities.builders;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;
import irt.entities.repository.FirstDigitsRepository;
import irt.entities.repository.IrtComponentRepository;
import irt.entities.repository.ManufactureRepository;

public class EntityBuilderA implements EntityBuilder{

	protected final Logger logger = LogManager.getLogger(getClass());

	private static final int SEQUENTIAL	 = 0;
	private static final int OPTION		 = 1;
	private static final int REVISION	 = 2;

	protected @Autowired FirstDigitsRepository firstDigitsRepository;
	protected @Autowired ManufactureRepository manufactureRepository;
	protected @Autowired IrtComponentRepository componentRepository;

	private final String NEXT_TWO_CHARS;

	public 		EntityBuilderA() { this(null); }
	protected 	EntityBuilderA(String secondChars) {
		NEXT_TWO_CHARS = secondChars;
	}

	@Override
	public IrtComponentEntity build(PartNumberForm form) {
		logger.error("build(PartNumberForm): ", form);

		if(form==null)
			return null;

		IrtComponentEntity entity = Optional.ofNullable(form.getId()).flatMap(componentRepository::findById)
					.orElseGet(
							()->{
								IrtComponentEntity e = new IrtComponentEntity();
								e.setPartNumber(form.getPartNumber());
								return e;
							});
		return entity;
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm, IrtComponentEntity componentEntity) {
		logger.error("fillForm(PartNumberForm, IrtComponentEntity):\n{}\n{} ", partNumberForm, componentEntity);
		
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm) {

		final String[] fields = Optional.ofNullable(partNumberForm.getFields())
				.orElseGet(
						()->{
							final String[] strints = new String[4];
							partNumberForm.setFields(strints);
							return strints;
						});

		//Set form part number
		final StringBuilder sb = new StringBuilder(Optional.ofNullable(NEXT_TWO_CHARS).map(second->'A' + second).orElse("A__"))

					.append(Optional.ofNullable(fields[SEQUENTIAL]).orElseGet(()->{ fields[SEQUENTIAL] = getSequentialNumber(); return fields[SEQUENTIAL]; }))
					.append(Optional.ofNullable(fields[OPTION]).filter(option->!option.isEmpty()).orElse("_"))
					.append(Optional.ofNullable(fields[REVISION]).filter(revision->!revision.isEmpty()).orElse("___"))

					.insert(9, '-')
					.insert(3, '-');

			partNumberForm.setPartNumber(sb.toString());
	}

	private String getSequentialNumber() {

		final List<String> sequences = componentRepository.getAssembliesSequences();

		AtomicInteger index = new AtomicInteger();

		sequences.stream()
		.map(seq->seq.replaceAll("\\D", ""))
		.mapToInt(Integer::parseInt)
		.distinct()
		.sorted()
		.filter(
				seq->{

					final int get = index.get();
					if(get<seq)
						return true;

					index.incrementAndGet();
					return false;
				})
		.findAny();

		return String.format("%05d", index.get());
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
