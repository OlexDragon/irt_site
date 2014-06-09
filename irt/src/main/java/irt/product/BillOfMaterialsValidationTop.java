package irt.product;

import org.apache.poi.ss.usermodel.Row;

public class BillOfMaterialsValidationTop extends BillOfMaterialsValidation {

	public BillOfMaterialsValidationTop(Row row, boolean isFootprint) {
		super(row, isFootprint);
	}

	@Override
	protected void setErrorMessage() {
		ProductStructure.setErrorMessage("The File Structure is not correct." +
											"<br /> First row should have titles: \"Item No\",\"Part Number\"and\"Qty\"");
	}

	//Item
	@Override
	protected String getFootprintStr() {
		return "item";
	}

	//Quantity
	@Override
	protected String getReferenceStr() {
		return "qty";
	}

	@Override
	protected BomUnitInterface validation(String qtyStr, String partNumberStr, String itemNoStr, String notUsedStr, String notUsedStr2, boolean isNotUsed) {
		BomUnitInterface unit = null;

		if(partNumberStr!=null && !partNumberStr.isEmpty() &&
				itemNoStr!=null && !itemNoStr.isEmpty() &&
				qtyStr!=null && !(qtyStr = qtyStr.replaceAll("\\D", "")).isEmpty()){

			if((unit = BomUnitTop.getBomTopUnit(partNumberStr, itemNoStr, qtyStr))==null)
				BillOfMaterials.addErrorMessage(partNumberStr	+ " does not exist. <small>(E047)</small><br />");

		}else 
			ProductStructure.setErrorMessage("Missing one of the values <small>(E048)</small>:<br />" +
					"Part Number - "+(partNumberStr!=null && !partNumberStr.isEmpty() 	
							? partNumberStr
							: "Missing")+"<br />" +
					"Item No - "+(itemNoStr!=null && !itemNoStr.isEmpty()
							? itemNoStr
							: "Missing")+"<br />"+
					"Qty - "+(qtyStr!=null && !qtyStr.isEmpty()
							? qtyStr
							: "Missing")+"<br />");
		return unit;
	}
}
