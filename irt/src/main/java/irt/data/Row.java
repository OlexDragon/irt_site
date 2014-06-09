package irt.data;

import irt.data.components.Data;
import irt.data.partnumber.PartNumberDetails;
import irt.table.Field;

public class Row extends irt.table.Row {

	public Row(Data data) {

		Data dataTmp = new PartNumberDetails(null).getComponent(data.getClassId());
		if (dataTmp != null) {
			dataTmp.setValues(data);

			//"Part Numb.","Mfr P/N","Description","MID","FP","Qty","Location","Link"

			add(new Field("<input type=\"checkbox\" name=\"checked"+dataTmp.getId()+"\" id=\"checked"+dataTmp.getId()+"\" />"));
			add(new Field("<a href=\"/irt/part-numbers?pn="+dataTmp.getPartNumberF()+"\">"+dataTmp.getPartNumberF()+"</a>"));
			add(new Field(dataTmp.getManufPartNumber()));
			add(new Field(dataTmp.getDescription()));
			add(new Field(dataTmp.getManufId()));
			add(new Field(dataTmp.getFootprint()));			//"FP"
			add(new Field(dataTmp.getQuantityStr()));		//"Qty"
			add(new Field(dataTmp.getLocation()));
			add(new Field(dataTmp.getLink().getHTML()));

		}
	}
}
