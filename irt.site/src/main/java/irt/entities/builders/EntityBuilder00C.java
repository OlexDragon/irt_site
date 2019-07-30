
package irt.entities.builders;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import irt.controllers.components.PartNumberForm;
import irt.entities.ArrayEntity;
import irt.entities.IrtComponentEntity;

public class EntityBuilder00C extends EntityBuilder0{

	private final Logger logger = LogManager.getLogger();

	private static final int 	MFR_INDEX			= 6,
								MFR_PN_INDEX		= 7,
								DESCRIPTION_INDEX	= 5,
								FIELDS_COUNT		= 8,
								VALUE_TO_PN_BASE_LENGTH = 2;

	private static final int VALUE		= 0;
	private static final int TYPE		= 1;
	private static final int MOUNTING	= 2;
	private static final int VOLTAGE	= 3;
	private static final int SIZE		= 4;

	private final List<Integer> sizes;

	public EntityBuilder00C(List<ArrayEntity> arrayEntities) {
		super("0C", MFR_INDEX, MFR_PN_INDEX, DESCRIPTION_INDEX, FIELDS_COUNT, VALUE_TO_PN_BASE_LENGTH);

		sizes = new ArrayList<>();
		for(ArrayEntity e:arrayEntities) {

			String id = e.getKey().getId();
			if(Character.isDigit(id.charAt(0)))
				sizes.add(Integer.parseInt(id));
		}
	}

	@Override
	public IrtComponentEntity build(PartNumberForm form) {
		IrtComponentEntity e = super.build(form);
		setValueVoltageToEntity(form, e);

		return e;
	}

	@Override
	public void fillEntity(PartNumberForm form, IrtComponentEntity entity) {
		super.fillEntity(form, entity);
		setValueVoltageToEntity(form, entity);
	}

	private void setValueVoltageToEntity(PartNumberForm form, IrtComponentEntity entity) {
		if(entity!=null){
			entity.setValue(form.getField(VALUE));
			entity.setVoltage(form.getField(VOLTAGE));
		}
	}

	@Override
	protected void setFormFields(PartNumberForm form, IrtComponentEntity entity) {

		super.setFormFields(form, entity);

		partNumberToForm( form, entity.getPartNumber());
		String[] fields = form.getFields();

		String v = entity.getValue();
		if(v!=null && !v.isEmpty())
			fields[VALUE]	= v;

		v = entity.getVoltage();
		if(v!=null && !v.isEmpty())
			fields[VOLTAGE]	= v;

	}

	@Override
	protected void partNumberToForm(PartNumberForm form, String partNumber) {

		String[] fields = form.getFields();

		int length = partNumber.length();
		if(length!=14)
			if(length<7 || length>14) length = 0;

			else if(length<8) length = 7;

			else if(length<9) length = 8;

			else if(length<12) length = 9;

			else length = 12;

		switch(length){
		case 14:
			//SIZE
			fields[SIZE] = getSize(partNumber.substring(12));
		case 12:
			//VOLTAGE
			String f3 = partNumber.substring(9, 12);
			fields[VOLTAGE] = f3==null || (f3 = f3.replaceAll("\\D", "")).isEmpty() ? null :Integer.toString(Integer.parseInt(f3));
		case 9:
			//MOUNTING
			String f2 = partNumber.substring(8, 9);
			fields[MOUNTING] = f2 ==null || f2.isEmpty() || f2.contains("_") ? null : f2;
		case 8:
			//TYPE
			String f1 = partNumber.substring(7,8);
			fields[TYPE] = f1 ==null || f1.isEmpty() || f1.contains("_") ? null : f1;
		case 7:
			//VALUE
			fields[VALUE]	= toField(partNumber.substring(3, 7));
		}
	}

	private String getSize(String f4) {

		if(f4 ==null || !f4.matches("[0-9]*"))
			f4 = null;

		else if(!sizes.contains(Integer.parseInt(f4)))
			f4 = "OT";

		return f4;
	}

	@Override
	public String toField(String value) {

		String result;
		if(value!=null && value.matches(".*\\d+.*")){
			value = value.toUpperCase();
			String[] split = value.split( "E", 2);

			BigDecimal base = new BigDecimal( toLong(split[0]));
			base.setScale(2, RoundingMode.HALF_EVEN);
			int exp = getExp(split);

			if(exp>=6){
				exp -= 6;
				result = " uF";
			}else if(exp>=3){
				exp -= 3;
				result = " nF";
			}else
				result = " pF";

			double pow = Math.pow(10, exp)*10;
			long round = Math.round(pow);
			result = base.multiply(new BigDecimal(round)).divide(new BigDecimal(100)) + result;
		}else
			result = null;

		return result;
	}

	@Override
	protected String getPartNumber(String[] fields) {
		logger.trace("entry: {}", (Object[])fields);
		String pn;

		if(fields!=null){
			pn =  toPartNumber(fields[VALUE]);
			pn += getValue(fields[TYPE]		, 1, '_', false);
			pn += getValue(fields[MOUNTING]	, 1, '_', false);
			String v = fields[VOLTAGE];
			pn += v==null || (v = v.replaceAll("\\D", "")).isEmpty() ? getValue( null, 3, '_', false) : getValue( v, 3, '0', false);
			pn += getSizeOrSequence(FIRST_PARTNUMBER_CHAR + NEXT_TWO_CHARS + pn, fields[SIZE]);
		}else
			pn = getValue(null, 11, '_', true);

		return pn;
	}

	public String getSizeOrSequence(String partNumber, String field) {

		if(field!=null && field.equals("OT")){//OT - Other
			List<IrtComponentEntity> entities = componentRepository.findByPartNumberStartingWith(partNumber);
			logger.trace("{}", entities);
			Integer s = 0;
			if(entities!=null){
				for(IrtComponentEntity e:entities){
					String pn = e.getPartNumber();
					int i = Integer.parseInt(pn.substring(12));
					logger.trace("pn.substring(12): {}", i);
					if(i>=s) s = ++i;
				}
				logger.trace("s: {}\n\t{}", s, sizes);
				while(sizes.contains(s))
					s++;
				logger.trace("s: {}", s);
			}

			field = getValue(s.toString(), 2, '0', false);

		}else
			field = getValue(field, 2, '_', false);

		return field;
	}

	@Override
	public int getMultiplier(String length) {

		length = length.replaceAll("[\\d. ]", "");

		return length.isEmpty() || length.charAt(0)=='S' ? 1 :
			length.charAt(0)=='U' ? 1000000 :
				length.charAt(0)=='N' ? 1000 :
					1;
	}
}
