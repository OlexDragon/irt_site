package irt.entities.builders;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import irt.controllers.components.PartNumberForm;
import irt.entities.FirstDigitsRepository;
import irt.entities.IrtComponentEntity;
import irt.entities.IrtComponentRepository;
import irt.entities.ManufactureEntity;
import irt.entities.ManufactureRepository;

public class EntityBuilder0 extends EntityBuilderAbstract{

	protected @Autowired FirstDigitsRepository firstDigitsRepository;
	protected @Autowired ManufactureRepository manufactureRepository;
	protected @Autowired IrtComponentRepository componentRepository;

	public EntityBuilder0() {
		this( null, 0, 1, 2, 3, 0);
	}

	protected EntityBuilder0(String nextTwoPartnumberChars, int mfrIndex, int mfrPnIndex, int descriptionIndex, int fieldsCount, int valueToPnBaseLength) {
		super(1, '0', nextTwoPartnumberChars, mfrIndex, mfrPnIndex, descriptionIndex, fieldsCount, valueToPnBaseLength);
	}

	@Override
	public IrtComponentEntity build(PartNumberForm form) {

		IrtComponentEntity entity;
		if(form!=null){
			

			entity = new IrtComponentEntity();
			entity.setId(form.getId());
			fillEntity(form, entity);
		}else
			entity = null;

		return entity;
	}

	/**
	 * Set Description, Manufacture and part number
	 * @param form - PartNumberForm
	 * @param entity - IrtComponentEntity
	 */
	protected void fillEntity(PartNumberForm form, IrtComponentEntity entity) {

		String[] fields = form.getFields();
		if(fields!=null){

			entity.setDescription(fields[DESCRIPTION_INDEX]==null || (fields[DESCRIPTION_INDEX] = fields[DESCRIPTION_INDEX].trim()).isEmpty() ? null : fields[DESCRIPTION_INDEX] );

			ManufactureEntity mfr = fields[MFR_INDEX]!=null && !(fields[MFR_INDEX] = fields[MFR_INDEX].trim()).isEmpty() && fields[MFR_INDEX].length()==2 ? manufactureRepository.findById(fields[MFR_INDEX]).get() : null;
			entity.setManufacture(mfr);

			entity.setManufPartNumber(fields[MFR_PN_INDEX]==null || (fields[MFR_PN_INDEX] = fields[MFR_PN_INDEX].trim()).isEmpty() ? null : fields[MFR_PN_INDEX]);
		}

		entity.setPartNumber(FIRST_PARTNUMBER_CHAR + (NEXT_TWO_CHARS!=null ? NEXT_TWO_CHARS : "__") + getPartNumber(form.getFields()));

	}

	protected String getPartNumber(String[] fields) {
		return "";
	}

	@Override
	public void fillForm(PartNumberForm form, IrtComponentEntity componentEntity) {

		form.setId(componentEntity.getId());

		form.setFirst(FIRST_CHAR_ID);
		form.setSecond(NEXT_TWO_CHARS);

		setFormFields(form, componentEntity);
		setFormPartNumber(form, componentEntity.getPartNumber());

	}

	protected void setFormFields(PartNumberForm partNumbetForm, IrtComponentEntity componentEntity) {

		String[] fields = new String[FIELDS_COUNT];
		partNumbetForm.setFields(fields);

		fields[DESCRIPTION_INDEX] = componentEntity.getDescription();
		fields[MFR_INDEX] = componentEntity.getManufacture()!=null ? componentEntity.getManufacture().getId() : null;
		fields[MFR_PN_INDEX] = componentEntity.getManufPartNumber();
	}

	protected void setFormPartNumber(PartNumberForm partNumbetForm, String pn) {
		partNumbetForm.setPartNumber(pn!=null && pn.length()>3 ? componentRepository.partNumberWithDashes(pn) : pn);
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm) {
		if(partNumberForm!=null){

			partNumberForm.setFirst(FIRST_CHAR_ID);
			partNumberForm.setSecond(NEXT_TWO_CHARS);

			if(partNumberForm.getFields()==null)
				partNumberForm.setFields(new String[FIELDS_COUNT]);

			else if(partNumberForm.getFields().length<FIELDS_COUNT)
				partNumberForm.setFields(Arrays.copyOf(partNumberForm.getFields(), FIELDS_COUNT));

			partNumberForm.setPartNumber(componentRepository.partNumberWithDashes(FIRST_PARTNUMBER_CHAR + (NEXT_TWO_CHARS!=null ? NEXT_TWO_CHARS : "__") + getPartNumber(partNumberForm.getFields())));
		}
	}

	@Override
	public void fillForm(PartNumberForm partNumberForm, String partNumber) {

		if(partNumberForm!=null){

			//Set the proper length
			String[] fields = partNumberForm.getFields();
			if(fields.length<FIELDS_COUNT)
				partNumberForm.setFields(Arrays.copyOf(fields, FIELDS_COUNT));

			partNumberForm.setFirst(FIRST_CHAR_ID);
			partNumberForm.setSecond(NEXT_TWO_CHARS);
			partNumberToForm(partNumberForm, partNumber);
		}
	}

	protected void partNumberToForm(PartNumberForm partNumberForm, String partNumber) { }

	@Override
	public String toField(String exponent) {
		return null;
	}

	@Override
	public int getExp(String[] split) {
		int exp;
		if(split.length>1){
			if(split[1]==null || split[1].isEmpty())
				exp = 0;
			else if(split[1].charAt(0)=='A')
				exp = -1;
			else{
				split[1] = split[1].replaceAll("\\D", "");
				if(split[1].isEmpty())
					exp = 0;
				else
					exp = (int)toLong(split[1]);
			}
		}else
			exp = 0;
		return exp;
	}

	@Override
	public IrtComponentEntity updateEntity(PartNumberForm form) {

		IrtComponentEntity e;
		if(form.getId()!=null)
			e = componentRepository.findById(form.getId()).get();

		else {
			String mfrPN = getMfrPN(form);
			if(mfrPN!=null)
				e = componentRepository.findOneByManufPartNumber(getMfrPN(form));

			else
				e = null;

			String pn = form.getPartNumber();
			if(e==null && pn!=null && !pn.contains("_"))
				e = componentRepository.findOneByPartNumber(pn.replaceAll("-", ""));		
		}

		fillEntity(form, e);

		return e;
	}

	@Override
	protected int getMultiplier(String value) {
		return 0;
	}

}
