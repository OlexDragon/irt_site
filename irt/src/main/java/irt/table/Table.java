package irt.table;

import java.util.LinkedList;
import java.util.List;

public class Table {
	private String className;//use with CSS(HTML)
	private HTMLHeader title;
	private LinkedList<Row> rows = new LinkedList<Row>();
	private RowAttribute rowAttribute;
	
	public Table(String[] titels, String href) {
		rowAttribute = new RowAttribute();

		if(titels!=null){
			Row row = new Row(titels, href);
			row.setClassName("title");
			add(row);
		}
	}

	private Row getRow(int index) {
		return index<rows.size() ? rows.get(index) : null;
	}

	public List<Row> getRows() {
		return rows;
	}

	public void setRows(LinkedList<Row> rows) {
		this.rows = rows;
	}

	public void add(Row row){
		if(rows.size()>0)
			rowAttribute.set(row);
		if(rowAttribute.isCount())
			row.setRowNumber(rows.size());
		rows.add(row);
		row.setShow(rowAttribute.getShow());
	}
	
	public void addRows(List<Row> list) {
		for(Row r:list)
			add(r);
	}

	/**
	 * @return Class Name or "" (Used for HTML)
	 */
	public String getClassName() {
		return (className!=null) ? className : "";
	}

	public void setClassName(String name) {
		this.className = name;
	}

	public RowAttribute getRowAttribute() {
		return rowAttribute;
	}

	public void setColumnClassName(int columnIndex, String className) {
		if(!rows.isEmpty())
			for(int i=0; i<rows.size(); i++)
				getRow(i).setFieldClassName(columnIndex, className);
	}

	public HTMLHeader getTitle() {
		return title;
	}

	public void setTitle(HTMLHeader title) {
		this.title = title;
	}

	public int getColumnCount() {
		return rowAttribute.columnCount();
	}

	public void hideColumn(int index) {
		rowAttribute.setShow(index, false);
	}

	public void showColumn(int index) {
		rowAttribute.setShow(index, true);
	}

	public void setRowAttribute(RowAttribute rowAttribute) {
		this.rowAttribute = rowAttribute;
	}

	@Override
	public String toString() {

		StringBuilder returnStr = new StringBuilder();
		if(title!=null)
			returnStr.append(title.toString());

		returnStr.append("<table ");
		if(!getClassName().isEmpty())
			returnStr.append("class=\""+getClassName()+"\"");
		returnStr.append(" >");
		
		for(Row row:rows)
			returnStr.append(row);
		returnStr.append("</table>");
		
		return returnStr.toString();
	}

	public void setRowCount(boolean isCount) {
		if(rowAttribute.isCount()!=isCount){
			rowAttribute.setCount(isCount);
			if(isCount)
				for(int i=0; i<rows.size(); i++)
					rows.get(i).setRowNumber(i);
			else
				for(int i=0; i<rows.size(); i++)
					rows.get(i).setRowNumber(-1);
		}
	}
}
