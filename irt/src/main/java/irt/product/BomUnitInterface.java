package irt.product;

import java.util.List;

import irt.data.components.Component;

public interface BomUnitInterface {

	public void setFootprint(String footprintStr);

	public  Component getComponent();

	public void addError(String string);

	public String getClassId();

	public String getRefLetters();

	public Integer[] getRefNumberArray();

	public void set(BomUnitInterface bomUnitInterface);

	public boolean isError();

	public List<String> getErrors();

	public String getRefNumbersShort();

	public long getRefId();

	public void setRefId(long l);

	public int getQuantity();

}
