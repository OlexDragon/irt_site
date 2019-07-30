package irt.entities.builders;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;

import irt.controllers.components.PartNumberForm;
import irt.entities.FirstDigitsRepository;
import irt.entities.IrtComponentEntity;
import irt.entities.ManufactureRepository;

public class EntityBuilder0CB extends EntityBuilder0{

	private static final int SQUENTIAL_NUMBER = 4;
	private static final int LENGTH = 3;
	private static final int CABLE_TYPE = 2;
	private static final int CON_2 = 1;
	private static final int CON_1 = 0;
	@Autowired FirstDigitsRepository firstDigitsRepository;
	@Autowired ManufactureRepository manufactureRepository;

	public EntityBuilder0CB() {
		this("CB", 6, 7, 5, 8);
	}

	protected EntityBuilder0CB(String nextTwoPartnumberChars, int mfrIndex, int mfrPnIndex, int descriptionIndex, int fieldsCount) {
		super(nextTwoPartnumberChars, mfrIndex, mfrPnIndex, descriptionIndex, fieldsCount, 2);
	}

	@Override
	protected void setFormFields(PartNumberForm partNumbetForm, IrtComponentEntity componentEntity) {
		super.setFormFields(partNumbetForm, componentEntity);

		String[] fields = partNumbetForm.getFields();

		String pn = componentEntity.getPartNumber();

		//Connector 1
		String f0 = pn.substring(3, 5);
		fields[CON_1] = f0 ==null || f0.isEmpty() || f0.equals("__") ? null : f0;

		//Connector 2
		String f1 = pn.substring(5, 7);
		fields[CON_2] = f1 ==null || f1.isEmpty() || f1.equals("__") ? null : f1;

		//Cable type
		String f2 = pn.substring(7, 9);
		fields[CABLE_TYPE] =  f2 ==null || f2.isEmpty() || f2.equals("__") ? null : f2;

		//Length
		String f3 = pn.substring(9, 13);
		fields[LENGTH] = toField(f3);

		//SeqN
		String f4 = pn.substring(13);
		fields[SQUENTIAL_NUMBER] = f4 ==null || f4.isEmpty() || f4.equals("___") ? null : f4;

	}

	@Override
	protected String getPartNumber(String[] fields) {
		String pn;

		if(fields!=null){
			pn =  getValue(fields[0], 2, '_', true);
			pn += getValue(fields[1], 2, '_', true);
			pn += getValue(fields[2], 2, '_', true);
			pn += toPartNumber(fields[3]);
			pn += getValue(fields[4]==null ? fields[4]=getSequatialNumber().toString() : fields[4], 3, '0', false);
		}else
			pn = getValue(null, 13, '_', true);

		return pn;
	}

	@Override
	public String toField(String exponent) {

		String result;
		if(exponent!=null && exponent.matches(".*\\d+.*")){
			exponent = exponent.toUpperCase();
			String[] split = exponent.split( "E", 2);

			BigDecimal base = new BigDecimal( toLong(split[0]));
			base.setScale(2, RoundingMode.HALF_EVEN);
			int exp = getExp(split);

			if(exp>=5){
				exp -= 5;
				result = " km";
			}else if(exp>=2){
				exp -= 2;
				result = " m";
			}else
				result = " cm";

			double pow = Math.pow(10, exp)*10;
			long round = Math.round(pow);
			result = base.multiply(new BigDecimal(round)).divide(new BigDecimal(100)) + result;
		}else
			result = null;

		return result;
	}

	@Override
	public int getMultiplier(String length) {

		length = length.replaceAll("[\\d. ]", "");

		return length.isEmpty() || length.charAt(0)=='S' ? 1 :
			length.charAt(0)=='K' ? 100000 :
				length.charAt(0)=='M' ? 100 :
					1;
	}

	@Override
	protected void partNumberToForm(PartNumberForm partNumberForm, String partNumber) {

		String[] fields = partNumberForm.getFields();

		if(partNumber.length()==16){

			fields[SQUENTIAL_NUMBER] = partNumber.substring(13);
			fields[LENGTH] = partNumber.substring(9, 13);
			fields[CABLE_TYPE] = partNumber.substring(7, 9);
			fields[CON_2] = partNumber.substring(5, 7);
			fields[CON_1] = partNumber.substring(3, 5);

		}else if(partNumber.length()>=13){

			fields[LENGTH] = partNumber.substring(9, 13);
			fields[CABLE_TYPE] = partNumber.substring(7, 9);
			fields[CON_2] = partNumber.substring(5, 7);
			fields[CON_1] = partNumber.substring(3, 5);

		}else if(partNumber.length()>=9){

			fields[CABLE_TYPE] = partNumber.substring(7, 9);
			fields[CON_2] = partNumber.substring(5, 7);
			fields[CON_1] = partNumber.substring(3, 5);

		}else if(partNumber.length()>=7){

			fields[CON_2] = partNumber.substring(5, 7);
			fields[CON_1] = partNumber.substring(3, 5);

		}else if(partNumber.length()>=5)

			fields[CON_1] = partNumber.substring(3, 5);
	}
}
