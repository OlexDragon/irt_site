package irt.table;

public class Title extends Field {
	
	private String href;

	public Title(String valueStr,String href) {
		super(valueStr);
		setHref(href);
	}

	private String getHref() {
		return href!=null && !getOrderBy().isEmpty() ? "href=\"/irt/" + href + "?order_by="+getOrderBy()+"\""
							: "";
	}

	public void setHref(String href) {
		this.href = href;
	}

	@Override
	public String toString() {
		String returnStr = "<th" +(getClassName()!=null && !getClassName().isEmpty() ? " class=\""+getClassName()+"\"" : "")+">";
		
			returnStr += getHref().isEmpty() ? getValue() : "<a "+getHref()+">"+getValue()+"</a>";
		
		return returnStr + "</th>";
	}
}
