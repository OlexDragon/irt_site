package irt.data.pcb;

import irt.data.components.Component;
import irt.work.InputTitles;
import irt.work.TextWorker;

public class BareBoard extends Board {

	@Override
	public void setClassId(){
		setClassId(Component.CLASS_ID_NAME.get(TextWorker.BARE_BOARD));
	}

	@Override
	public void setTitles(){
		setTitles( new InputTitles(	new String[] 
				{ "Material","Type",	"Revision",	"Description", "SeqN"},
			new String[]
				{ "select",	"select","select",		"text",		"label"}));
	}
}
