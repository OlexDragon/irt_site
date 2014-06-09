package irt.product;

import java.util.List;

import irt.data.components.Component;
import irt.data.components.Data;
import irt.data.dao.ComponentDAO;

public class BomUnitTop implements BomUnitInterface {

	private Data component;
	private String itemNoStr;
	private int qty;
	private long bomTopId = -1;

	private BomUnitTop(String partNumberStr, String itemNoStr, String qtyStr) {
		setComponent(partNumberStr);
		this.itemNoStr = itemNoStr;
		this.qty = Integer.parseInt(qtyStr);
	}

	public static BomUnitInterface getBomTopUnit(String partNumberStr, String itemNoStr,	String qtyStr) {
		BomUnitTop bu = new BomUnitTop(partNumberStr, itemNoStr, qtyStr);
		return bu.component!=null ? bu : null;
	}

	private void setComponent(String partNumber) {
		component = new ComponentDAO().getData(partNumber);
	}

	@Override
	public void setFootprint(String footprintStr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponent() {
		return (Component) component;
	}

	@Override
	public void addError(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClassId() {
		return component.getClassId();
	}

	@Override
	public String getRefLetters() {
		return itemNoStr;
	}

	@Override
	public Integer[] getRefNumberArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(BomUnitInterface bomUnitInterface) {
		ProductStructure.setErrorMessage(component.getPartNumberF()+" apeared more the once.");
	}

	@Override
	public boolean isError() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getErrors() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRefNumbersShort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getRefId() {
		return bomTopId;
	}

	@Override
	public void setRefId(long bomTopId) {
		this.bomTopId = bomTopId;
	}

	@Override
	public int getQuantity() {
		return qty;
	}

}
