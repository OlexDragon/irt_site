package irt.files;

import irt.data.dao.BomDAO;
import irt.data.dao.ManufactureDAO;
import irt.data.partnumber.PartNumber;
import irt.table.OrderByService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BadPdfFormatException;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf {

	private static final Logger logger = LogManager.getLogger();

	public static ByteArrayOutputStream getPdf(String partNumberStr, String pathLogo, OrderByService orderBy) throws DocumentException, BadElementException, MalformedURLException, IOException, BadPdfFormatException {
		logger.entry(partNumberStr, pathLogo, orderBy);

		partNumberStr = partNumberStr.trim().replace("-", "");

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		Document document = openDocument( baos, PartNumber.isTop(partNumberStr) ? PageSize.A4 : PageSize.A4.rotate());

		Image logo = Image.getInstance(pathLogo);
		if(partNumberStr!=null){
			document.add( PartNumber.isTop(partNumberStr) ? new BomDAO().getTopPdfTable(partNumberStr,logo) : new BomDAO().getPdfTable(partNumberStr,logo, orderBy));
			document.newPage();
		}
		document.add(new ManufactureDAO().getPdfTable(logo));
		document.close();

//Page count
		baos = addPageCount(baos);

		return baos;
	}

	protected static ByteArrayOutputStream addPageCount( ByteArrayOutputStream baos) throws DocumentException, IOException, BadPdfFormatException {

		byte[] baosCopy = baos.toByteArray();
		baos = new ByteArrayOutputStream();

		Document document = new Document(PageSize.A4.rotate());
		PdfCopy copy  = new PdfCopy(document,baos);
		document.open();
		PdfReader reader = new PdfReader(baosCopy);
		int numberOfPages = reader.getNumberOfPages();
		PdfImportedPage page;
		PdfCopy.PageStamp stamp;

		int i;
		for (i = 1; i <= numberOfPages; i++) {
		    page = copy.getImportedPage(reader, i);
		    stamp = copy.createPageStamp(page);
		    // add page numbers
		    ColumnText.showTextAligned(
		            stamp.getUnderContent(), Element.ALIGN_RIGHT,
		            new Phrase(String.format("page %d of %d", i, numberOfPages),FontFactory.getFont(FontFactory.HELVETICA, 8)),
		            800, 10, 0);
		    stamp.alterContents();
		    copy.addPage(page);
		}

		logger.trace("{} pages", ++i);
		document.close();

		return baos;
	}

	public static Document openDocument(ByteArrayOutputStream baos, Rectangle pageSize) throws DocumentException {

		Document document = new Document(pageSize);
		PdfWriter.getInstance(document, baos);
		document.open();

		return document;
	}


}
