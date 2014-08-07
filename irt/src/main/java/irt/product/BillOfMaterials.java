package irt.product;

import irt.data.components.ComponentIds;
import irt.data.dao.BomDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.ErrorDAO;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class BillOfMaterials {

	private final Logger logger = LogManager.getLogger();

	private ComponentIds topComponentIds;
	private List<BomUnitInterface> units = new ArrayList<>();
	private static String errorMessage = "";
	

	public BillOfMaterials(String partNumber, String sourceFile, boolean isFootprint) {

		if(partNumber!=null &&sourceFile!=null && !sourceFile.isEmpty()){
			setTopComponentIds(partNumber);

			Workbook workbook = null;

			try {

				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				workbook = WorkbookFactory.create(fileInputStream);

			} catch (InvalidFormatException | IOException e) {
				new ErrorDAO().saveError(e, "BillOfMaterials.BillOfMaterials");
				throw new RuntimeException(e);
			}

			if (workbook != null) {
				Sheet sheet = workbook.getSheetAt(0);
				int rowNumber = sheet.getLastRowNum();
				BillOfMaterialsValidation val = getValidationObj(sheet, isFootprint);//check titles

				if (!val.isError())	//BOM has 'reference','partnumber'and'footprint' titles
					for (int i = 1; i <= rowNumber; i++) {
						addUnit(val.parseUnit(sheet.getRow(i), isFootprint));
					}
			}
		}
	}

//is used in BillOfMAterialsTop
	protected BillOfMaterialsValidation getValidationObj(Sheet sheet, boolean isFootprint) {
		return new BillOfMaterialsValidation(sheet.getRow(0), isFootprint);
	}

	public BillOfMaterials(ComponentIds topComponentIds) {
		logger.entry(topComponentIds);

		if(topComponentIds != null){
			this.topComponentIds = topComponentIds;
			units = new BomDAO().getBomUnits(topComponentIds.getId());
		}
	}


	private void addUnit(BomUnitInterface bomUnitInterface) {
		if (bomUnitInterface != null){
			if (this.units.contains(bomUnitInterface))
				this.units.get(this.units.indexOf(bomUnitInterface)).set(bomUnitInterface);
			else
				this.units.add(bomUnitInterface);
		}
	}


	public int size() {
		return units!=null ? units.size() : 0;
	}


	public List<BomUnitInterface> getUnits() {
		return units;
	}


	public String getErrorMessage() {
		String tmpStr = errorMessage;
		errorMessage = "";
		return tmpStr;
	}


	public boolean isError() {
		boolean isError = false;

		for (BomUnitInterface u : units)
			if (u.isError()) {
				isError = true;
				break;
			}

		return !errorMessage.isEmpty() || isError;
	}

	public static void addErrorMessage(String error){
		errorMessage += error;
	}

	public ComponentIds getTopComponentIds() {
		return topComponentIds;
	}


	public void setTopComponentIds(ComponentIds topComponentIds) {
		logger.entry(topComponentIds);
		this.topComponentIds = topComponentIds;
	}


	public void setTopComponentIds(int setTopComponentId) {
		setTopComponentIds(new ComponentDAO().getComponentIds(setTopComponentId));
	}

	public void setTopComponentIds(String partNumber) {
		logger.entry(partNumber);
		setTopComponentIds(new ComponentDAO().getComponentIds(partNumber));
	}

	public boolean hasBom() {
		return topComponentIds!=null ? topComponentIds.hasBom() : false;
	}


	public int getTopId() {
		return topComponentIds!=null ? topComponentIds.getId() : -1;
	}


	public void setBomCreationDate(String bomDate) {
		if(topComponentIds.getMfrPartNumber()==null || topComponentIds.getMfrPartNumber().isEmpty()){
			topComponentIds.setMfrPartNumber(bomDate);
			new ComponentDAO().updateMfrPatrNumber(topComponentIds);
		}
	}


	@Override
	public String toString() {
		String str = "Top Component: "+topComponentIds.getId()+":"+topComponentIds.getPartNumber()+":"+topComponentIds.getMfrPartNumber()+":"+topComponentIds.getQuantity();
		for(BomUnitInterface bu:units)
			str += "<br />"+bu;
		return str;
	}


	public boolean isTop() {
		String partNumber;
		return topComponentIds!=null && (partNumber =topComponentIds.getPartNumber())!=null && !partNumber.isEmpty() ? partNumber.charAt(0)=='T' : false;
	}
}
