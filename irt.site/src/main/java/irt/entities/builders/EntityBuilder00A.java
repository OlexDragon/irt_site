
package irt.entities.builders;

import irt.controllers.components.PartNumberForm;
import irt.entities.IrtComponentEntity;

public class EntityBuilder00A extends EntityBuilder0{

	private static final int SQUENTIAL_NUMBER = 5;
	private static final int LEADS_NUMBER_INDEX = 3;
	private static final int PACKAGE_INDEX = 2;

	public EntityBuilder00A() {
		this("0A", 1, 0, 4, 6);
	}

	protected EntityBuilder00A(String nextTwoPartnumberChars, int mfrIndex, int mfrPnIndex, int descriptionIndex, int fieldsCount) {
		super(nextTwoPartnumberChars, mfrIndex, mfrPnIndex, descriptionIndex, fieldsCount, 0);
	}

	@Override
	protected void setFormFields(PartNumberForm partNumbetForm, IrtComponentEntity componentEntity) {

		super.setFormFields(partNumbetForm, componentEntity);

		String[] fields = partNumbetForm.getFields();

		String pn = componentEntity.getPartNumber();

		//Package
		String f2 = pn.substring(9, 11);
		fields[PACKAGE_INDEX] = f2 ==null || f2.isEmpty() || f2.equals("__") ? null : f2;

		//Leads Number
		String f3 = pn.substring(11);
		fields[LEADS_NUMBER_INDEX] = toField(f3);

		//SeqN
		String f5 = pn.substring(5, 9);
		fields[SQUENTIAL_NUMBER] = f5 ==null || f5.isEmpty() || f5.equals("___") ? null : f5;
	}

	@Override
	public String toField(String str) {

		if(str!=null && !(str = str.replaceAll("\\D", "")).isEmpty())
			str = Integer.toString(Integer.parseInt(str));
		else
			str = null;

		return str;
	}

	@Override
	protected String getPartNumber(String[] fields) {
		String pn;

		if(fields!=null){
			pn =  getValue(fields[MFR_INDEX], 2, '_', true);
			pn += getValue(fields[5]==null ? fields[5]=getSequatialNumber().toString() : fields[5], 4, '0', false);
			pn += getValue(fields[2], 2, '_', true);
			pn += toPartNumber(fields[3]);
		}else
			pn = getValue(null, 11, '_', true);

		return pn;
	}

	@Override
	public String toPartNumber(String value) {

		if(value==null || (value = value.replaceAll("\\D", "")).isEmpty())
			value = getValue(null, 3, '_', true);
		else{
			value = getValue(Integer.toString(Integer.parseInt(value)), 3, '0', false);
		}
		return value;
	}

	@Override
	protected void partNumberToForm(PartNumberForm partNumberForm, String partNumber) {

		String[] fields = partNumberForm.getFields();

		if(partNumber.length()==14){

			fields[LEADS_NUMBER_INDEX] = partNumber.substring(11);
			fields[SQUENTIAL_NUMBER] = partNumber.substring(5, 9);
			fields[PACKAGE_INDEX] = partNumber.substring(9, 11);
			fields[MFR_INDEX] = partNumber.substring(3,5);

		}else if(partNumber.length()>=9){

			fields[SQUENTIAL_NUMBER] = partNumber.substring(5, 9);
			fields[PACKAGE_INDEX] = partNumber.substring(9, 11);
			fields[MFR_INDEX] = partNumber.substring(3,5);

		}else if(partNumber.length()>=11){

			fields[PACKAGE_INDEX] = partNumber.substring(9, 11);
			fields[MFR_INDEX] = partNumber.substring(3,5);

		}else if(partNumber.length()>=5)

			fields[MFR_INDEX] = partNumber.substring(3,5);
	}
}
