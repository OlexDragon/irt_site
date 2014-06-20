package irt.table;


public class Field {

	private String value;
	private String className;
	private String orderBy;
	

	public Field(String valueStr) {
		setValue(valueStr);
	}

	public String getValue() {
		return (value!=null)
				? value:
					"";
	}

	public void setValue(String value) {
		String[] strs;
		if(value!=null){
			if(value.contains("~")){
				strs = value.split("~");
				this.value = strs[1];
				orderBy = strs[0];
			}else
				this.value = orderBy = value;

			if(value.contains("&") && !value.contains("&#"))
				this.value = value.replace("&", "&amp;");
		}else
			this.value = orderBy = null;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isEmpty(){
		return getValue()==null || getValue().isEmpty();
	}

	@Override
	public String toString() {
		return "<td" +(className!=null && !className.isEmpty() ? " class=\""+className+"\"" : "")+">" + getValue() + "</td>";
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	public int getIntValue() {
		return value!=null ? Integer.parseInt(value) : 0;
	}
}
