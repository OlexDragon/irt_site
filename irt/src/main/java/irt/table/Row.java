package irt.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Row {
	public static final boolean HEADER	= true;
	public static final boolean DATA	= false;

	private String className;//use with CSS(HTML)
	private LinkedList<Field> row = new LinkedList<>();;
	private List<Boolean> show ;
	private int rowNumber = -1;

	public Row(String[] titles, String href){
		addTitles(titles, href);
	}

	public Row(ResultSet resultSet) throws SQLException {

		ResultSetMetaData metaData = resultSet.getMetaData();
		String[] fields = new String[metaData.getColumnCount()];
		resetRow();

		for(int i=1; i<=fields.length; i++)
			add(new Field(resultSet.getString(i)));

	}

	public Row() {
	}

	public Row(String[] fields) {
		for(String f:fields)
			add(f);
	}

	private void addTitles(String[] array, String href) {

		if(array!=null)
			for(int i=0; i<array.length; i++)
				add(new Title(array[i], href));
	}

	public void add(String[] array) {
		if(array!=null)
			for(int i=0; i<array.length; i++)
				add(new Field(array[i]));
	}

	public void add(Field field){
		row.add(field);
	}

	public void setRowNumber(int rowNumber	) {
		this.rowNumber = rowNumber;
		if(rowNumber>0)
			if(rowNumber%2==1)
				className = "odd";
			else
				className = "even";
		else
			if(rowNumber==0)
				className = "title";
			else
				className = null;
	}

	private void resetRow() {
		row = new LinkedList<Field>();
	}

	public void add(String fieldStr) { add(new Field(fieldStr));	}
	public int size(){ return (row!=null) ? row.size() : 0;	}

	public List<Field>	getRow() 		{ return row;		}
	public int 			getRowNumber() 	{ return rowNumber;	}
	public String 		getClassName() 	{ return (className!=null) ? className : "";	}
	private Field 		getField(int fieldIndex) { return row!=null ? row.get(fieldIndex) : null;	}

	public void setRow			(LinkedList<Field> row) { this.row = row;	}
	public void setClassName	(String className	) { this.className = className;	}
	public void setFieldClassName	(int fieldIndex, String className) { getField(fieldIndex).setClassName(className);	}

	public void setShow(List<Boolean> show) {
		this.show = show;
	}

	@Override
	public String toString() {
		StringBuilder returnStr = new StringBuilder();
		returnStr.append("<tr ");
		if(!getClassName().isEmpty())
			returnStr.append("class=\""+getClassName()+"\" ");
		returnStr.append(">");

		if (rowNumber>=0) {
			returnStr.append("<td>");
			if(rowNumber != 0)
				returnStr.append(rowNumber);
			returnStr.append("</td>");
		}

		for (int i = 0; i<show.size() || i<row.size(); i++) 
			if(show==null || show.size()<=i || show.get(i))
				if(row!=null && row.size()>i)
					returnStr.append(row.get(i));
				else
					returnStr.append("<td></td>");

		returnStr.append("</tr>");

		return returnStr.toString();
	}
}
