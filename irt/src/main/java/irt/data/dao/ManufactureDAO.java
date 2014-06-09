package irt.data.dao;

import irt.data.manufacture.Manufacture;
import irt.table.OrderBy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


public class ManufactureDAO extends DataAccessObject {

//	private Logger logger = Logger.getLogger(this.getClass());

	public Manufacture[] getAll(OrderBy orderBy) {
		Connection conecsion = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Manufacture[] manufactures = null;
		String query = "SELECT*FROM`irt`.`manufacture`"+(orderBy!=null ? orderBy : "");

		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.prepareStatement(query);
			resultSet = statement.executeQuery();

			if (resultSet.last()){
				int index = resultSet.getRow();
				if(index>0){
					manufactures = new Manufacture[index];
					do
						manufactures[--index] = new Manufacture(resultSet);
					while(resultSet.previous());
				}
			}

		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ManufactureDAO.getAll");
			throw new RuntimeException(e);
		} finally {
			close(resultSet, statement, conecsion);
		}

		return manufactures;
	}

	public String getId(String name){

		String query = "select `id` from `irt`.`manufacture` where `name`=\""+name+"\"";
		return (String) getSQLObject(query);
	}

	public String getMfrName(String mfrId) {

		String query = "SELECT`name`FROM`irt`.`manufacture`WHERE`id`=\""+mfrId+"\"";
		return (String) getSQLObject(query);
	}

	public void insert(int userId, Manufacture manufacture) {

		Connection conecsion = null;
		Statement statement = null;
		ResultSet resultSet = null;

		if (manufacture != null && manufacture.isSet())
		try {
			conecsion = getDataSource().getConnection();
			statement = conecsion.createStatement();

				String query = "INSERT INTO `irt`.`manufacture` (`id`, `name`, `link`)"
					+ "			 VALUES ('"
					+ manufacture.getId()
					+ "', '"
					+ manufacture.getName()
					+ "', '"
					+ manufacture.getLink()
					+ "');";
				if(statement.executeUpdate(query)>0)
					new HistoryDAO().insert(statement, userId, "`irt`.`manufacture`", manufacture.getId());
		} catch (SQLException e) {
			new ErrorDAO().saveError(e, "ManufactureDAO.insert");
			throw new RuntimeException(e);
		} finally { close(resultSet, statement, conecsion); }
	}

	public boolean isExist(Manufacture manufacture) {
		boolean isExist = false;

		if (manufacture != null && manufacture.isSet()) {
			String query = "select*from`irt`.`manufacture`where`id`='"+manufacture.getId()+"'or`name`='"+manufacture.getName()+"'";
			isExist = isResult(query);
		}
		return isExist;
	}

	public PdfPTable getPdfTable(Image logo) {
		Manufacture[] manufactures = getAll(null);
		int middle = (int) Math.round(manufactures.length /3.0);
		Manufacture[] manufactures2 = Arrays.copyOfRange(manufactures, middle, middle*2);
		Manufacture[] manufactures3 = Arrays.copyOfRange(manufactures, middle*2, manufactures.length);
		manufactures = Arrays.copyOf(manufactures, middle);
		Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
		com.itextpdf.text.Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);

		PdfPTable pdfPTable = new PdfPTable(11);
		try {
			pdfPTable.setWidths(new float[]{0.5f,0.5f,3f,0.2f,0.5f,0.5f,3f,0.2f,0.5f,0.5f,3f});
		} catch (DocumentException e) {
			new ErrorDAO().saveError(e, "ManufactureDAO.getPdfTable");
			throw new RuntimeException(e);
		}

		PdfPCell cell;
//Header 1
		cell = new PdfPCell(logo);
		cell.setBorder(0);
		cell.setColspan(2);
		pdfPTable.addCell(cell);	//1,2
		cell = new PdfPCell();
		cell.setBorder(0);
		pdfPTable.addCell(cell);	//3
		cell = new PdfPCell(new Phrase("IRT Tecnologies. Manufactures.",fontTitle));
		cell.setBorder(0);
		cell.setColspan(9);
		pdfPTable.addCell(cell);	//4,5,6,7,8,9,10,11

//Header 2
		pdfPTable.addCell(new Phrase());
		pdfPTable.addCell(new Phrase("ID",fontTitle));
		pdfPTable.addCell(new Phrase("Manufacture",fontTitle));
		cell = new PdfPCell(new Phrase());
		cell.setBorder(0);
		pdfPTable.addCell(cell);
		pdfPTable.addCell(new Phrase());
		pdfPTable.addCell(new Phrase("ID",fontTitle));
		pdfPTable.addCell(new Phrase("Manufacture",fontTitle));
		cell.setBorder(0);
		pdfPTable.addCell(cell);
		pdfPTable.addCell(new Phrase());
		pdfPTable.addCell(new Phrase("ID",fontTitle));
		pdfPTable.addCell(new Phrase("Manufacture",fontTitle));

		int size = manufactures.length;
		int size2 = manufactures2.length;
		int size3 = manufactures3.length;
		for(int i=0; i<size; i++){
			pdfPTable.addCell(new Phrase(""+(i+1),font));
			addMnfToPdfTable(pdfPTable, manufactures[i], font);
			cell = new PdfPCell(new Phrase());
			cell.setBorder(0);
			pdfPTable.addCell(cell);
			pdfPTable.addCell(new Phrase(i<size2 ? ""+(size+i+1) : null,font));
			addMnfToPdfTable(pdfPTable, i<size2 ? manufactures2[i] : null, font);
			cell.setBorder(0);
			pdfPTable.addCell(cell);
			pdfPTable.addCell(new Phrase(i<size3 ? ""+(size+size2+i+1) : null,font));
			addMnfToPdfTable(pdfPTable, i<size3 ? manufactures3[i] : null, font);
		}

		return pdfPTable;
	}

	private void addMnfToPdfTable(PdfPTable pdfPTable, Manufacture manufacture,	com.itextpdf.text.Font font) {
		pdfPTable.addCell(new Phrase(manufacture!=null ? manufacture.getId() : null, font));
		pdfPTable.addCell(new Phrase(manufacture!=null ? manufacture.getName() : null, font));
	}
}
