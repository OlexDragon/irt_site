package irt.product;

import irt.data.components.Capacitor;
import irt.data.components.Component;
import irt.data.components.Value;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;

public class BillOfMaterialsValidation {
	
	private List<String > titles = new ArrayList<>();
	private int valueIndex			= -1;

	protected int getPartReferenceIndex() {
		return partReferenceIndex;
	}

	protected int getPartNumberIndex() {
		return partNumberIndex;
	}

	protected int getFootprintIndex() {
		return footprintIndex;
	}

	protected void setPartReferenceIndex(int partReferenceIndex) {
		this.partReferenceIndex = partReferenceIndex;
	}

	protected void setPartNumberIndex(int partNumberIndex) {
		this.partNumberIndex = partNumberIndex;
	}

	protected void setFootprintIndex(int footprintIndex) {
		this.footprintIndex = footprintIndex;
	}

	private int partReferenceIndex	= -1;
	private int partNumberIndex		= -1;
	private int voltageIndex		= -1;
	private int footprintIndex		= -1;
	private boolean isError = false;

	public BillOfMaterialsValidation(Row row, boolean isFootprint) {
		if (row != null) {
			int columnIndex = row.getLastCellNum();
			for (int i = 0; i < columnIndex; i++)
				if (row.getCell(i).getCellType() == Cell.CELL_TYPE_STRING) {
					String title = row.getCell(i).getRichStringCellValue()
							.toString().toLowerCase();
					titles.add(title);
					if (title.contains(getReferenceStr()))
						partReferenceIndex = i;
					else if (title.equals("part number"))
						partNumberIndex = i;
					else if (title.equals("value"))
						valueIndex = i;
					else if (title.equals("voltage"))
						voltageIndex = i;
					else if (title.contains(getFootprintStr()) && isFootprint)
						footprintIndex = i;
				}
		}
		if (partNumberIndex == -1 || partReferenceIndex == -1
				|| (isFootprint && footprintIndex == -1)) {
			setErrorMessage();
			isError = true;
		}
	}

	protected void setErrorMessage() {
		ProductStructure.setErrorMessage("The File Structure is not correct." +
		"<br /> First row should have titles: \"Part Reference\",\"Part Number\"and\"Footprint\"" +
		"<br />\"Value\"and\"Voltage\" are optional.");
	}

	protected String getFootprintStr() {
		return "footprint";
	}

	protected String getReferenceStr() {
		return "reference";
	}

	public List<String> getTitles() {
		return titles;
	}

	@Override
	public String toString() {
		return "BillOfMaterialsValidation [titles=" + titles + ", units="
				+ ", valueIndex=" + valueIndex
				+ ", partReferenceIndex=" + partReferenceIndex
				+ ", partNumberIndex=" + partNumberIndex + "]";
	}

	public BomUnitInterface parseUnit(Row row, boolean isFootprint) {

		RichTextString richText;
		Cell cell = null;

		String valueStr = null;
		if(valueIndex>=0){
			cell = row.getCell(valueIndex);
			richText = cell!=null ? cell.getRichStringCellValue() : null;
			valueStr = richText!=null ? richText.getString() : null;
		}

		String refStr = null;
		if(partReferenceIndex>=0){
			cell = row.getCell(partReferenceIndex);
			if (cell != null)
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					richText = cell.getRichStringCellValue();
					refStr = richText != null ? richText.getString() : null;
					break;
				case Cell.CELL_TYPE_NUMERIC:
					refStr = ""+(int)cell.getNumericCellValue();
				}
		}

		String partNumberStr = null;
		if(partNumberIndex>=0){
			cell = row.getCell(partNumberIndex);
			richText = cell!=null ? cell.getRichStringCellValue() : null;
			partNumberStr = richText!=null ? richText.getString().trim().replace("-", "") : null;
		}

		String voltageStr = null;
		if(voltageIndex>=0){
			cell = row.getCell(voltageIndex);
			richText = cell!=null ? cell.getRichStringCellValue() : null;
			voltageStr = richText!=null ? richText.getString() : null;
		}

		String footprintStr = null;
		if(footprintIndex>=0){
			cell = row.getCell(footprintIndex);
			if (cell != null)
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					richText = cell.getRichStringCellValue();
					footprintStr = richText != null ? richText.getString() : null;
					break;
				case Cell.CELL_TYPE_NUMERIC:
					footprintStr = ""+(int)cell.getNumericCellValue();
				}
		}

		return validation(refStr, partNumberStr, footprintStr, valueStr, voltageStr, isFootprint);
	}

	protected BomUnitInterface validation(String refStr, String partNumberStr, String footprintStr, String valueStr, String voltageStr, boolean isFootprint) {
		BomUnitInterface unit = null;

//Validation
		if (partNumberStr != null && !partNumberStr.isEmpty() && refStr != null && !refStr.isEmpty() && (footprintStr!=null && !footprintStr.isEmpty() || !isFootprint)){

			unit = BomUnit.getBomUnit(partNumberStr, refStr);
			if (unit!=null){
				unit.setFootprint(footprintStr);

				int classId = new SecondAndThirdDigitsDAO().getClassID(unit.getClassId());
				Component compUnit = unit.getComponent();

				if(classId==TextWorker.RESISTOR || classId==TextWorker.CAPACITOR || classId==TextWorker.INDUCTOR){

					if(valueStr==null || valueStr.isEmpty() || compUnit.getValue().equals(new Value(valueStr, classId).toString())){
						if(voltageStr!=null && !compUnit.getValue(Capacitor.VOLTAGE).equals(new Value(voltageStr, TextWorker.VOLTAGE).toValueString()))
							unit.addError("Voltage - "+voltageStr+" do not match with part number "+compUnit.getPartNumberF());
					}else//match
						unit.addError("Value - "+valueStr+" not match with "+compUnit.getPartNumberF());
				}
			}else{
				BillOfMaterials.addErrorMessage(partNumberStr	+ " does not exist. <small>(E045)</small><br />");
			}
		}else if (!valueStr.equals("FEEDTHROUGH") && !valueStr.equals("SMA") && !valueStr.equals("TP_SQ")&& !partNumberStr.equals("N/A"))
			ProductStructure.setErrorMessage("Missing one of the values <small>(E046)</small>:<br />" +
												"Part Number - "+(partNumberStr!=null && !partNumberStr.isEmpty() 	
														? partNumberStr
														: "Missing")+"<br />" +
												"Reference - "+(refStr!=null && !refStr.isEmpty()
														? refStr
														: "Missing")+"<br />"+
												"Footprint - "+(footprintStr!=null && !footprintStr.isEmpty()
														? footprintStr
														: "Missing")+"<br />");

		return unit;
	}

	public boolean isError() {
		return isError;
	}
}
