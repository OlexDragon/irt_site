
package irt.controllers.components;

import java.util.Arrays;

public class PartNumberForm {

	private Long id; public Long getId(){ return id; } public PartNumberForm setId(Long id) { this.id = id; return this;}

	private Integer first; public Integer getFirst(){ return first; } public void setFirst(Integer first){ this.first = first; }

	private String second; public String getSecond() { return second; } public void setSecond(String second) { this.second = second; }

	private String[] fields;
	public String[] getFields() {

		if(fields!=null)
			for(int i=0; i<fields.length; i++)
				if(fields[i]!=null)
					fields[i] = fields[i].toUpperCase();

		return fields;
	}
	public void setFields(String[] fields) { this.fields = fields; }
	public String getField(int index){
		String field;
		if(fields!=null && fields.length>index)
			field = fields[index];
		else
			field = null;

		return field;
	}

	private String partNumber; public String getPartNumber() { return partNumber; } public void setPartNumber(String partNumber) { this.partNumber = partNumber; }

	public PartNumberForm set(PartNumberForm partNumbetForm) {
		id = partNumbetForm.id;
		first = partNumbetForm.first;
		second = partNumbetForm.second;
		fields = partNumbetForm.fields;
		partNumber = partNumbetForm.partNumber;
		return this;
	}

	@Override
	public String toString() {
		return "\n\t PartNumbetForm [id=" + id + ", first=" + first + ", second=" + second + ", partNumber=" + partNumber
				+ ", fields=" + Arrays.toString(fields) + "]";
	}
}
