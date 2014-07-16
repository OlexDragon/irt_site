package irt.data.purchase;

import irt.data.companies.Company;
import irt.data.dao.CompanyDAO;
import irt.data.dao.MenuDAO;
import irt.data.dao.PurchaseDAO;
import irt.data.manufacture.ManufacturePartNumber;
import irt.data.partnumber.PartNumber;
import irt.data.user.UserBean;
import irt.files.Pdf;
import irt.table.Row;
import irt.table.Table;
import irt.web.Init;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class PurchaseOrder {

	private static final String OTHER_COMMENTS_OR_SPECIAL_INSTRUCTIONS = "Other Comments or Special Instructions";
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final int ALL 		= -1;
	public static final int OPEN 		= 0;
	public static final int ACTIVE 		= 1;
	public static final int CLOSE 		= 2;
	public static final int COMPLETE 	= 3;

	private int id;
	private String		poNumber;
	private UserBean	user;
	private UserBean	newUser;
	private Company		company;
	private Company		newCompany;
	private String		comments;
	private String		newComments;
	private String		invoiceLink;
	private Calendar	dateTime;
	private Calendar	newDateTime;
	private int 		status;
	private List<PurchaseOrderUnit> purchaseOrderUnits;
	private List<Tax> taxes = new ArrayList<>();
	private List<Tax> extra = new ArrayList<>();

	private int statusToShow = ALL;
	private boolean isShippingInfo;
	private boolean isEdit;
	private boolean isGST;
	private boolean isQST;
	private boolean isVendor;
	private boolean isNewVendor;

	public PurchaseOrder(List<PurchaseOrderUnit> purchaseOrderUnits) {
		this.purchaseOrderUnits = purchaseOrderUnits;
	}

	public void checkHtmlFields(HttpServletRequest request) {

		String tmpStr = request.getParameter("cbSeller");
		if(!(tmpStr=tmpStr.replaceAll("\\D", "")).isEmpty())
			setCompany(new CompanyDAO().getCompany(Integer.parseInt(tmpStr)));

		Enumeration<String> names = request.getParameterNames();
		String fieldName;
		int tmpInt;
		int index;
		while(names.hasMoreElements()){
			fieldName = names.nextElement();
			if(fieldName.startsWith("qty")){
				if((index = purchaseOrderUnits.indexOf(new PurchaseOrderUnit(Integer.parseInt(fieldName.replaceAll("\\D", "")))))>=0){
					tmpStr = request.getParameter(fieldName).replaceAll("\\D", "");
					if(!tmpStr.isEmpty()){
						tmpInt = Integer.parseInt(tmpStr);
						if(tmpInt>0)
							purchaseOrderUnits.get(index).setOrderQuantity(tmpInt);
						else
							purchaseOrderUnits.remove(index);
					}
				}
			}else if(fieldName.startsWith("mfrPN")){
				index = purchaseOrderUnits.indexOf(new PurchaseOrderUnit(Integer.parseInt(fieldName.replaceAll("\\D", ""))));
				if(index>=0)
					purchaseOrderUnits.get(index).setMfrPNIndex(Integer.parseInt(request.getParameter(fieldName)));
			}else if(fieldName.startsWith("price")){
				if((index = purchaseOrderUnits.indexOf(new PurchaseOrderUnit(Integer.parseInt(fieldName.replaceAll("\\D", "")))))>=0)
						purchaseOrderUnits.get(index).setPrice(request.getParameter(fieldName));
			}
		}
	}

	public Table getTable(){
		Table table = new Table(new String[]{"Part Number","Description","Mfr PN","Mfr","Price","Qty","Total"}, null);
		DecimalFormat decimalFormat = new DecimalFormat("#.00");
		BigDecimal totalSum = null;
		for(PurchaseOrderUnit pou:purchaseOrderUnits){
			String mfrPN;
			if(pou.size()>1 && isEdit){
				mfrPN = "<select id=\"mfrPN"+pou.getComponentId()+"\" name=\"mfrPN"+pou.getComponentId()+"\" onchange=\"oneClick('submit');\">";
				for(ManufacturePartNumber mpn:pou.getMfrPNs())
					mfrPN += "<option value=\""+mpn.getId()+"\""+(mpn.getId()==pou.getMfrPNIndex() ? " selected=\"selected\"":"")+" >"+(mpn.getMfrPN()!=null ? mpn.getMfrPN() : "")+"</option>";
				mfrPN +=	"</select>";
			}else
				mfrPN = pou.getMfrPN().getMfrPN();

			BigDecimal total = pou.getPrice()!=null ? new BigDecimal(pou.getPrice()).multiply(new BigDecimal(pou.getOrderQuantity())) : null;
			if(total!=null)
				if(totalSum==null)
					totalSum = total;
				else
					totalSum = totalSum.add(total);

			table.add(new Row(new String[]{pou.getPartNumberLink(),
											pou.getDescription(),
											mfrPN,
											pou.getMfrPN().getMfr(),
											isEdit ?
													"<input class=\"c4emRight\" type=\"text\" id=\"price"+pou.getComponentId()+"\" name=\"price"+pou.getComponentId()+"\" value=\""+(pou.getPrice()!=null ? pou.getPrice() : "")+"\" "+(PartNumber.getBrowserId()==Init.CHROME ? "onclick" : "onfocus")+"=\"this.select();\" onkeypress=\"return oneKeyPress(event,'submit');\" />" :
														pou.getPrice()!=null ? pou.getPrice() : "",
											isEdit ? "<input class=\"c3em\" type=\"text\" id=\"qty"+pou.getComponentId()+"\" name=\"qty"+pou.getComponentId()+"\" value=\""+pou.getOrderQuantity()+"\" "+(PartNumber.getBrowserId()==Init.CHROME ? "onclick" : "onfocus")+"=\"this.select();\" onkeypress=\"return oneKeyPress(event,'submit');\" />" :
														""+pou.getOrderQuantity(),
											total!=null ? decimalFormat.format(total): ""}));
		}
		if(totalSum!=null){
			Row row = new Row(new String[]{"","","",isGST || isQST ? "Subtotal":"Total","","",decimalFormat.format(totalSum)});
			table.add(row);
			row.setClassName("cBgYellow");

			BigDecimal gst = null;
			if(isGST)
				gst = tableAddTax(table, "GST", totalSum);
			BigDecimal qst = null;
			if(isQST)
				qst = tableAddTax(table, "QST", totalSum);

			if(gst!=null)
				totalSum = totalSum.add(gst);
			if(qst!=null)
				totalSum = totalSum.add(qst);
			if(isGST || isQST){
				row = new Row(new String[]{"","","","Total","","",decimalFormat.format(totalSum)});
				table.add(row);
				row.setClassName("cBgYellow");
			}
		}

		return table;
	}

	protected BigDecimal tableAddTax(Table table, String taxName, BigDecimal totalSum) {
		DecimalFormat decimalFormat = new DecimalFormat("0.00####");
		BigDecimal xst;
		Tax tax = new Tax(taxName, null);
		if (taxes.contains(tax)) {
			xst = taxes.get(taxes.indexOf(tax)).getPursent();
		} else {
			String gstStr = new MenuDAO().getDescription("tax", taxName);
			xst = new BigDecimal(gstStr);
			addTax(taxName, xst);
		}

		Row row = new Row(new String[] { "", "", "", "", taxName, xst + "%",	decimalFormat.format(xst = totalSum.multiply(xst)) });
		table.add(row);
		row.setClassName("cBgYellow");
		return xst;
	}

	private void addTax(String name, BigDecimal bigDecimal) {
		Tax tax = new Tax(name, bigDecimal);
		if(!taxes.contains(tax))
			addTax(tax);
	}

	public String getPONumber() {
		return poNumber;
	}
	public Company getCompany() {
		return newCompany!=null ? newCompany : company;
	}

	public String getDate() {
		return new SimpleDateFormat("MMM dd, yyyy").format(newDateTime!=null ? newDateTime.getTime() : dateTime.getTime());
	}
	public String getDateTime() {
		return new SimpleDateFormat(DATE_FORMAT).format(newDateTime!=null ? newDateTime.getTime() : dateTime.getTime());
	}
	public int getStatus() {
		return status;
	}
	public List<PurchaseOrderUnit> getPurchaseOrderUnits() {
		return purchaseOrderUnits;
	}

	public String setPONumber(int index) {
		resetDate();
		DateFormat dateFormat = new SimpleDateFormat("yyMM");
		return poNumber = dateFormat.format(dateTime.getTime())+String.format("%3s", index).replace(' ', '0');
	}

	public void resetDate() {
		newDateTime = Calendar.getInstance();
	}
	public void setPONumber(String number) {
		this.poNumber = number;
	}
	public void setDateTime() {
		dateTime = newDateTime;
	}
	public void setDateTime(Timestamp timestamp) {
		if(timestamp!=null) {
			dateTime = new GregorianCalendar();
			dateTime.setTime(timestamp);
		}
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public void setPurchaseOrderUnits(List<PurchaseOrderUnit> purchaseOrderUnits) {
		this.purchaseOrderUnits = purchaseOrderUnits;
	}
	@Override
	public String toString() {
		return "PurchaseOrder [number=" + poNumber + ", seller="
				+ company + ", dateTime=" + dateTime + ", status=" + status
				+ ", purchaseOrderUnits=" + purchaseOrderUnits + "]";
	}

	public void createPO() {
		if(isSet())
			new PurchaseDAO();
	}

	public boolean isEdit() {
		return isEdit;
	}

	public boolean isSet(){
		boolean isSet = getCompany()!=null && getCompany().getId()>0;
	
		for(PurchaseOrderUnit pou:purchaseOrderUnits)
			if(!pou.isSet()){
				isSet =false;
				break;
			}
	
		return isSet;
	}

	public boolean isGST() {
		return isGST;
	}

	public boolean isQST() {
		return isQST;
	}

	public void setEdit(boolean isEdit) {
		if(!(this.isEdit = isEdit))
			newComments = null;
	}

	public void setGST(boolean isGST) {
		this.isGST = isGST;
	}

	public void setQST(boolean isQST) {
		this.isQST = isQST;
	}

	public String getTaxes() {
		DecimalFormat decimalFormat = new DecimalFormat("#.00####");
		String taxesStr = null;
		if(!taxes.isEmpty())
			for(Tax t:taxes)
				if(taxesStr==null)
					taxesStr = t.getName()+":"+decimalFormat.format(t.getPursent());
				else
					taxesStr += ","+t.getName()+":"+decimalFormat.format(t.getPursent());
		return taxesStr;
	}

	public String getExtra() {
		DecimalFormat decimalFormat = new DecimalFormat("#.00####");
		String extraStr = null;
		if(!extra.isEmpty())
			for(Tax t:extra)
				if(extraStr==null)
					extraStr = t.getName()+":"+decimalFormat.format(t.getPursent());
				else
					extraStr += ","+t.getName()+":"+decimalFormat.format(t.getPursent());
		return extraStr;
	}

	public void setTaxes(String taxesStr) {
		if (taxesStr != null) {
			String[] taxStrs = taxesStr.split(",");
			for (String s : taxStrs) {
				String[] tax = s.split(":");
				addTax(new Tax(tax[0], new BigDecimal(tax[1])));
				if (tax[0].equals("GST"))
					isGST = true;
				else if (tax[0].equals("QST"))
					isQST = true;
			}
		}
	}

	private void addTax(Tax tax) {
		taxes.add(tax);
		Collections.sort(taxes);
	}

	public void setExtra(String extraStr) {
		if (extraStr != null) {
			String[] extraStrs = extraStr.split(",");
			for (String s : extraStrs) {
				String[] extras = s.split(":");
				addExtra(new Tax(extras[0], new BigDecimal(extras[1])));
			}
		}
	}

	private void addExtra(Tax tax) {
		extra.add(tax);
		Collections.sort(extra);
	}

	public UserBean getUser() {
		return newUser!=null ? newUser : user;
	}

	public void setUser() {
			user = newUser;
	}
	
	public void setUser(UserBean user) {
		if(user!=null && !user.equals(this.user))
			newUser = user;
		else
			newUser = null;
	}

	public boolean isNewUser() {
		return newUser!=null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void upLoadPDF(HttpServletResponse response, String pathLogo) throws IOException, BadElementException, DocumentException {

		ByteArrayOutputStream baos = getPdf(pathLogo);

		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",	"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		response.setContentType("application/pdf");
		// the content length
		response.setContentLength(baos.size());
		// write ByteArrayOutputStream to the ServletOutputStream
		ServletOutputStream os = response.getOutputStream();
		baos.writeTo(os);
		os.flush();
		os.close();
	}

	private ByteArrayOutputStream getPdf(String pathLogo) throws BadElementException, MalformedURLException, DocumentException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Document document = Pdf.openDocument( baos, PageSize.A4);
		document.add(getPdfTable(Image.getInstance(pathLogo)));
		document.close();

		return baos;
	}

	private PdfPTable getPdfTable(Image logo) throws DocumentException {
		DecimalFormat decimalFormat = new DecimalFormat("#.00####");
		int numColumns = 8;
		PdfPTable pdfPTable = new PdfPTable(numColumns);
		pdfPTable.setWidthPercentage(100);
		pdfPTable.setWidths(new float[]{.25f,.5f,1f,1f,.5f,.4f,.45f,.6f});
		PdfPCell cell;
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD);
		font.setColor(BaseColor.GRAY);
		Font fontWhite = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
		fontWhite.setColor(BaseColor.WHITE);
		Font fontBolt = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
//Header 1; PURCHASE ORDER
		cell = new PdfPCell(logo);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		pdfPTable.addCell(cell);	//1,2
		cell = new PdfPCell(new Phrase("PURCHASE ORDER", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(6);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);	//3-8
//Header 2
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		pdfPTable.addCell(cell);	//1-8
//Header 3 IRT TECHNOLOGIES
		font = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
		cell = new PdfPCell(new Phrase("IRT Technologies",font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);	//1-6
		cell = new PdfPCell(new Phrase("P.O. #",font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(cell);	//7
		cell = new PdfPCell(new Phrase(poNumber, font));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		pdfPTable.addCell(cell);	//8
//Header 4 ADDRESS DATE
		font = FontFactory.getFont(FontFactory.HELVETICA, 9);
		String address = "800, Chemin Saint Jose, La Prairie\nQC, Canada, J5R 6W9\nPhone: +1-450-444-1227\nFax:     +1-450-444-1335";
		Paragraph phrase = new Paragraph(address,fontBolt);
		cell = new PdfPCell(phrase);
		cell.setLeading(0,1.1f);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);	//1-6
		cell = new PdfPCell(new Phrase("Date:",font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pdfPTable.addCell(cell);	//7
		cell = new PdfPCell(new Phrase(getDate(),font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);	//8
// Header 5
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		pdfPTable.addCell(cell); // 1-8
// Header 6	VENDOR, SHIP TO
		cell = new PdfPCell(new Phrase("VENDOR", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		pdfPTable.addCell(cell);	//1-3
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 4
		cell = new PdfPCell(new Phrase("SHIP TO", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);	//5-8
// Header 7	COMPANY NAME
		Phrase companyAddress = new Phrase(company.getCompanyName(), font);
		if(!company.getAddress().isEmpty())
			companyAddress.add("\n"+company.getAddress());
		if(!company.getTelephone().isEmpty())
			companyAddress.add("\nPhone: "+company.getTelephone());
		if(!company.getFax().isEmpty())
			companyAddress.add("\nFax "+company.getFax());
		cell = new PdfPCell(companyAddress);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		pdfPTable.addCell(cell); // 1-3
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 4
		phrase = new Paragraph("IRT Technologies\n"+address, font);
		cell = new PdfPCell(phrase);
		cell.setLeading(0,1.1f);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 5-8

		if(isShippingInfo){
			cell = new PdfPCell();
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(4);
			pdfPTable.addCell(cell); // 1-4
			cell = new PdfPCell(new Phrase("Shipping Info:\n"+new MenuDAO().getDescription("shipping", "info"), font));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setColspan(4);
			pdfPTable.addCell(cell); // 4-8
		}
//Header 8
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		pdfPTable.addCell(cell); // 1-8
//Table Header
		cell = new PdfPCell(new Phrase("#", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell); // 1
		cell = new PdfPCell(new Phrase("Mfr Part Number / Description", fontWhite));
		cell.setColspan(4);
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell); // 2-5
		cell = new PdfPCell(new Phrase("Qty", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell); // 6
		cell = new PdfPCell(new Phrase("U/P", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell); // 7
		cell = new PdfPCell(new Phrase("Total", fontWhite));
		cell.setBackgroundColor(BaseColor.BLUE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell); // 8
//Table
		Price totalPrice = new Price(0);
		for(int i=0; i<purchaseOrderUnits.size(); i++){
			cell = new PdfPCell(new Phrase(""+(i+1), font));
			pdfPTable.addCell(cell); // 1

			PurchaseOrderUnit purchaseOrderUnit = purchaseOrderUnits.get(i);
			String mfrPN = purchaseOrderUnit.getMfrPN().getMfrPN();
			if(mfrPN==null)
				mfrPN = purchaseOrderUnit.getPartNumberStr();
			Phrase phraseMfrPN = new Phrase(mfrPN, fontBolt);
			Paragraph paragraph = new Paragraph();
			paragraph.add(phraseMfrPN);
			String description = purchaseOrderUnit.getDescription();
			if(description.length()+mfrPN.length()>55)
				description = (" / "+description).substring(0,57-mfrPN.length())+"...";
			else
				description = " / "+description;
			paragraph.add(new Phrase(description, font));
			cell = new PdfPCell(paragraph);
			cell.setColspan(4);
			pdfPTable.addCell(cell); // 2-5

			cell = new PdfPCell(new Phrase(""+purchaseOrderUnit.getOrderQuantity(), font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pdfPTable.addCell(cell); // 6
			cell = new PdfPCell(new Phrase("$"+purchaseOrderUnit.getPrice(), font));
			pdfPTable.addCell(cell); // 7

			Price totalUnitsPrice = purchaseOrderUnit.getTotalPrice();
			totalPrice.addValue(totalUnitsPrice.getValueLong());
			cell = new PdfPCell(new Phrase("$"+totalUnitsPrice.getValue(2, 2), font));
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			pdfPTable.addCell(cell); // 8
		}
		if(purchaseOrderUnits.size()<10)
			for(int i=purchaseOrderUnits.size(); i<10; i++){
				cell = new PdfPCell(new Phrase(" ", font));
				pdfPTable.addCell(cell); // 1
				cell = new PdfPCell(new Phrase(" ", font));
				cell.setColspan(4);
				pdfPTable.addCell(cell); // 2-5
				cell = new PdfPCell(new Phrase(" ", font));
				cell = new PdfPCell(new Phrase(" ", font));
				pdfPTable.addCell(cell); // 6
				cell = new PdfPCell(new Phrase(" ", font));
				pdfPTable.addCell(cell); // 7
				cell = new PdfPCell(new Phrase(" ", font));
				pdfPTable.addCell(cell); // 8
			}
//sub total
		cell = new PdfPCell();
		cell.setColspan(5);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 1-5
		cell = new PdfPCell(new Phrase(isGST || isQST ? "SUBTOTAL" : "TOTAL", font));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 6,7
		cell = new PdfPCell(new Phrase("$"+totalPrice.getValue(2, 2), fontBolt));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 8
//0 - Other Comments or Special Instructions
		cell = new PdfPCell(new Phrase(OTHER_COMMENTS_OR_SPECIAL_INSTRUCTIONS, font));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 5

		Tax tax1 = null;
		Price tax1Price = null;
		if(taxes.size()>0){
			tax1 = taxes.get(0);
			tax1Price = new Price(totalPrice.getValueLong(), decimalFormat.format(tax1.getPursent()));
			totalPrice.addValue(tax1Price.getValueLong());
		}
		cell = new PdfPCell(new Phrase(tax1!=null ? tax1.getName()+" "+decimalFormat.format(tax1.getPursent())+"%" : "", font));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);//6,7
		if(tax1Price!=null){
			cell = new PdfPCell(new Phrase("$"+tax1Price.getValue(2, 2), font));
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}else
			cell = new PdfPCell();
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 8
//1 - Other Comments or Special Instructions
		String[] split = comments!=null ? comments.split("\n") : null;
		cell = new PdfPCell(new Phrase(split!=null && split.length>0 ? split[0] : "", font));
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 5

		Price tax2Price = null;
		Tax tax2 = null;
		if(taxes.size()>1){
			tax2 = taxes.get(1);
			tax2Price = new Price(totalPrice.getValueLong(), decimalFormat.format(tax2.getPursent()));
			totalPrice.addValue(tax2Price.getValueLong());
		}
		cell = new PdfPCell(new Phrase(tax2!=null ? tax2.getName() + " " + decimalFormat.format(tax2.getPursent()) + "%" : "", font));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 6,7

		if(tax2Price!=null){
			cell = new PdfPCell(new Phrase("$" + tax2Price.getValue(2, 2), font));
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}else
			cell = new PdfPCell();
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 8

//2 - Other Comments or Special Instructions
		cell = new PdfPCell(new Phrase(split!=null && split.length>1 ? split[1] : "", font));
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 5

		cell = new PdfPCell(new Phrase(tax1Price!=null || tax2Price!=null ? "Total" : "", fontBolt));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 6,7
		if(tax1Price!=null || tax2Price!=null){
			cell = new PdfPCell(new Phrase("$" + totalPrice.getValue(2, 2), fontBolt));
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}else
			cell = new PdfPCell();
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell); // 8
//3 - Other Comments or Special Instructions
		cell = new PdfPCell(new Phrase(split!=null && split.length>2 ? split[2] : "", font));
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);// 5-8
//4 - Other Comments or Special Instructions
		cell = new PdfPCell(new Phrase(split!=null && split.length>3 ? split[3] : "", font));
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);// 5-8
//5 - Other Comments or Special Instructions
		cell = new PdfPCell(new Phrase(split!=null && split.length>4 ? split[4] : "", font));
		cell.setBorderColor(BaseColor.LIGHT_GRAY);
		cell.setBorder(Rectangle.LEFT | Rectangle.RIGHT | Rectangle.BOTTOM);
		cell.setColspan(4);
		pdfPTable.addCell(cell); // 1-4
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(4);
		pdfPTable.addCell(cell);// 5-8
//User
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		pdfPTable.addCell(cell);// 1-3
		cell = new PdfPCell(new Phrase(getUser().getFullName(), fontBolt));
		cell.setColspan(3);
		cell.setBorder(Rectangle.BOTTOM);
		pdfPTable.addCell(cell);// 3-5
		cell = new PdfPCell(new Phrase(getDate(),fontBolt));
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);	//8
//Authorized by
		cell = new PdfPCell();
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(3);
		pdfPTable.addCell(cell);// 1-3
		cell = new PdfPCell(new Phrase("Authorized by", font));
		cell.setColspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		pdfPTable.addCell(cell);// 3-5
		cell = new PdfPCell(new Phrase("Date",font));
		cell.setColspan(2);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);	//8
//
		cell = new PdfPCell(new Phrase(" "));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		pdfPTable.addCell(cell);// 1-8
//If question
		cell = new PdfPCell(new Phrase("If you have any questions about this purchase order, please contact", font));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);// 1-8
//E-mail
		cell = new PdfPCell(new Phrase(getUser().getFullName()+"  "+getUser().geteMail(), fontBolt));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(8);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		pdfPTable.addCell(cell);// 1-8

		return pdfPTable;
	}

	public Company setCompany() {
		company = newCompany;
		setVendor();
		return company;
	}
 
	public void setCompany(Company company) {
		if(company!=null){
			newCompany = company;
			setVendor(company.getType() == Company.TYPE_VENDOR);
		}
	}

	public boolean isNewCompany(){
		return newCompany!=null;
	}

	public boolean isShippingInfo() {
		return isShippingInfo;
	}

	public boolean isNewComments() {
		return newComments!=null;
	}

	public void setShippingInfo(boolean isShippingInfo) {
		this.isShippingInfo = isShippingInfo;
	}
	public int getSellerId(){
		return company!=null ? company.getId() : -0;
		
	}

	public String getComments() {
		return newComments==null ? comments : newComments;
	}

	public void setComments(String comments) {
		if(this.comments==null || !this.comments.equals(comments) && !this.comments.equals(OTHER_COMMENTS_OR_SPECIAL_INSTRUCTIONS)){
			newComments = comments;
			if(newComments!=null)
				newComments = newComments.replace("\n\n", "\n");
		}
	}

	public String setComments() {
		comments = newComments;
		newComments = null;
		return comments;
	}

	public void setVendor() {
		isVendor = isNewVendor = company.getType()==Company.TYPE_VENDOR;
	}

	public void setVendor(boolean isVendor) {
		isNewVendor = isVendor;
	}

	public void setVendor(String parameter) {
		if(parameter!=null)
			setVendor(parameter.equals("vendor"));
	}

	public boolean isVendor() {
		return isNewVendor;
	}

	public boolean isCM() {
		return !isNewVendor;
	}

	public String getInvoiceLink() {
		return invoiceLink;
	}

	public void setInvoiceLink(String invoiceLink) {
		this.invoiceLink = invoiceLink;
	}

	public boolean hasComponent(int componentId) {
		return purchaseOrderUnits!=null ? purchaseOrderUnits.contains(new PurchaseOrderUnit(componentId)) : false;
	}

	public int getStatusToShow() {
		return statusToShow;
	}

	public void setStatusToShow(int statusToShow) {
		this.statusToShow = statusToShow;
	}

	public void cancel() {
		newComments = null;
		newCompany = null;
		newUser = null;
		isNewVendor = isVendor;
		for(PurchaseOrderUnit pou:purchaseOrderUnits)
			pou.cancel();
	}
}
