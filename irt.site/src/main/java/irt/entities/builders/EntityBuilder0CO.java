
package irt.entities.builders;

import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;

public class EntityBuilder0CO extends EntityBuilder0{

	public static final int		MFR_PN_INDEX		= 0,
								NUMB_OF_PIN_INDEX	= 1,
								MALE_FEMALE_INDEX	= 2,
								TYPE_INDEX			= 3,
								SEQUENTIAL_NUMBER	= 4,
								DESCRIPTION_INDEX	= 5,
								MFR_INDEX			= 6,

								FIELDS_COUNT		= 7;

	public EntityBuilder0CO() {
		this("CO", MFR_INDEX, MFR_PN_INDEX, DESCRIPTION_INDEX, FIELDS_COUNT);
	}

	protected EntityBuilder0CO(String nextTwoPartnumberChars, int mfrIndex, int mfrPnIndex, int descriptionIndex, int fieldsCount) {
		super(nextTwoPartnumberChars, mfrIndex, mfrPnIndex, descriptionIndex, fieldsCount, 0);
	}

	@Override
	protected void setFormFields(PartNumberForm form, IrtComponentEntity entity) {

		super.setFormFields(form, entity);
		partNumberToForm( form, entity.getPartNumber());
	}

	@Override
	public String toField(String str) {
		return str==null || (str = str.replaceAll("\\D", "")).isEmpty() ? null :Integer.toString(Integer.parseInt(str));
	}

	@Override
	protected String getPartNumber(String[] fields) {

		String pn;

		if(fields!=null){
			pn =  toPartNumber(fields[NUMB_OF_PIN_INDEX]);
			pn += getValue(fields[MALE_FEMALE_INDEX], 1, '_', true);
			pn += getValue(fields[TYPE_INDEX], 2, '_', true);
			pn += getValue(fields[SEQUENTIAL_NUMBER]==null ? fields[SEQUENTIAL_NUMBER]=getSequatialNumber().toString() : fields[SEQUENTIAL_NUMBER], 4, '0', false);
		}else
			pn = getValue(null, 10, '_', true);

		return pn;
	}

	@Override
	protected void fillEntity(PartNumberForm form, IrtComponentEntity entity) {
		form.getFields()[SEQUENTIAL_NUMBER] = getSequentiolNumber(form.getPartNumber());
		super.fillEntity(form, entity);
	}

	private String getSequentiolNumber(String partNumber) {
		String seqN;

		if(partNumber!=null && partNumber.length()==13)
			seqN = partNumber.substring(9);
		else
			seqN = null;

		return seqN;
	}

	@Override
	public String toPartNumber(String value) {

		if(value==null || (value = value.replaceAll("\\D", "")).isEmpty())
			value = getValue(null, 3, '_', true);
		else
			value = getValue(Integer.toString(Integer.parseInt(value)), 3, '0', false);

		return value;
	}

	@Override
	protected void partNumberToForm(PartNumberForm partNumberForm, String partNumber) {

		String[] fields = partNumberForm.getFields();

		int length = partNumber.length();
		if(length!=13)
			if(length<7 || length>13) length = 0;

			else if(length<7) length = TYPE_INDEX;

			else if(length<9) length = MALE_FEMALE_INDEX;

			else length = NUMB_OF_PIN_INDEX;
		else
			length = SEQUENTIAL_NUMBER;

		switch(length){

		case SEQUENTIAL_NUMBER:
			fields[SEQUENTIAL_NUMBER] = toField(partNumber.substring(9));

		case TYPE_INDEX:
			String f2 = partNumber.substring(7, 9);
			fields[TYPE_INDEX] = f2 ==null || f2.isEmpty() || f2.contains("_") ? null : f2;

		case MALE_FEMALE_INDEX:
			String f3 = partNumber.substring(6, 7);
			fields[MALE_FEMALE_INDEX] = f3 ==null || f3.isEmpty() || f3.contains("_") ? null : f3;

		case NUMB_OF_PIN_INDEX:
			fields[NUMB_OF_PIN_INDEX] = toField(partNumber.substring(3,6));
		}
	}
}
