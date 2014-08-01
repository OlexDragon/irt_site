package irt.product;

import irt.data.dao.BomDAO;
import irt.data.dao.ComponentDAO;
import irt.data.dao.MenuDAO;
import irt.table.OrderBy;
import irt.table.Table;

import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPTable;

public class ProductStructure {

	public boolean isFootprint	= true;
	public boolean isQty		= false;
	private boolean isSymbol	= false;

	private String partNumber;

	private BillOfMaterials billOfMaterials;
	private Table table;
	private OrderBy orderBy;
	private boolean isOrderByReference = true;
	private boolean isBom;

	private static String errorMessage = "";

	/**
	 * @param partNumber without dash
	 * @param isBom
	 * @return 
	 */
	public boolean setPartNumber(String partNumber, boolean isBom) {

		if(partNumber!=null && !partNumber.isEmpty()){
			this.isBom=isBom;
			partNumber = validationBom(partNumber, isBom);
			if(partNumber==null || !partNumber.equals(this.partNumber)){
				this.partNumber = partNumber;
				table = null;
			}
		}
		return table!=null;
	}

	public void setOrderBy(String orderBy) {

		if(orderBy==null)
			this.orderBy = null;
		else if(this.orderBy!=null)
			this.orderBy.setOrderBy(orderBy);
		else 
			this.orderBy = new OrderBy(orderBy);

		table = null;
		isOrderByReference = false;
	}

	public String validationBom(String partNumber, boolean isBom) {

		if(partNumber!=null && !(partNumber=partNumber.trim().replace("-", "")).isEmpty() && (!isBom || new MenuDAO().isBomValid(partNumber))){
			if(!new ComponentDAO().isExists(partNumber)){
				partNumber = null;
				setErrorMessage(partNumber + " does not exist <small>(E051)</small>.");
			}
		}else{
			partNumber = null;
			setErrorMessage("The data entered is not valid. <small>(E051)</small>");
		}
		return partNumber;
	}

	public String getPartNumber() {
		return partNumber!=null ? partNumber : "";
	}

	public String htmlFile(){
		String html  = isBomValid() && !hasBom() || isSymbol
				? "<input type=\"file\" name=\"file\" id=\"file\" />"	//to upload the file
						: "";
		return html;
	}

	public Table getTable() {

		if(table==null)
			if(this.partNumber!=null){
				if(this.isBom) {
					table = new BomDAO().getBomTable(this.partNumber, this.orderBy);
				} else
					table = new BomDAO().getTableWhereUsed(getPartNumber());
			}else
				table = null;

		return table;
	}

	public boolean isBomValid(){
		return new MenuDAO().isBomValid(partNumber);
	}

	public boolean hasBom(){
		return new ComponentDAO().hasBom(partNumber);
	}

	public boolean isErrorMessage(){
		return (billOfMaterials!=null && billOfMaterials.isError()) || !errorMessage.isEmpty();
	}

	public String getErrorMessage(){
		String tmpStr = "";

		if (billOfMaterials != null){
			for (BomUnitInterface u : billOfMaterials.getUnits())
				if (u.isError())
					for(String s:u.getErrors())
						tmpStr += s+"<br />";

			if(billOfMaterials.isError())
				tmpStr += billOfMaterials.getErrorMessage()+"<br />";
		}

		if(!errorMessage.isEmpty()){
			tmpStr += errorMessage;
			errorMessage = "";
		}

		return tmpStr;
	}

	public void set(BillOfMaterials billOfMaterials) {
		this.billOfMaterials = billOfMaterials;
		if(billOfMaterials!=null)
			billOfMaterials.setTopComponentIds(getPartNumber());
	}

	public static void setErrorMessage(String errorMessage) {
			ProductStructure.errorMessage += (errorMessage!=null) ? errorMessage+"<br />" : "";
	}

	public PdfPTable getPdfTable(Image logo) {
		return isTop() ? new BomDAO().getTopPdfTable(getPartNumber(),logo) : new BomDAO().getPdfTable(getPartNumber(),logo, orderBy);
	}

	public void setSymbol(boolean isSymbol) {
		this.isSymbol = isSymbol;
	}

	public boolean isSymbol() {
		return isSymbol;
	}

	public boolean isTop() {
		return partNumber!=null && !partNumber.isEmpty() ? partNumber.charAt(0)=='T' : false;
	}

	public void setOrderByReference(boolean isOrderByReference) {
		if(this.isOrderByReference != isOrderByReference){
			if(isOrderByReference)
				orderBy = null;
			else
				orderBy = new OrderBy(BomDAO.PART_REFERENCE);

			this.isOrderByReference = isOrderByReference;

			table = null;
		}
	}

	public boolean isOrderByReference() {
		return isOrderByReference;
	}

	public boolean isBom() {
		return isBom;
	}

	public OrderBy getOrderBy() {
		return orderBy;
	}
}