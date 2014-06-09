package irt.product;

import org.apache.poi.ss.usermodel.Sheet;

public class BillOfMaterialsTop extends BillOfMaterials{

	public BillOfMaterialsTop(String sourceFile) {
		super(sourceFile, true);
	}

	@Override
	protected BillOfMaterialsValidation getValidationObj(Sheet sheet,	boolean isFootprint) {
		return new BillOfMaterialsValidationTop(sheet.getRow(0), isFootprint);//check titles
	}

}
