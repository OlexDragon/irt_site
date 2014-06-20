package irt.data.pcb;

import irt.data.dao.ComponentDAO;
import irt.data.dao.SecondAndThirdDigitsDAO;
import irt.work.TextWorker;

public class BootloaderLoadedPopulatedBoard extends PopulatedBoard {

	@Override
	public void setClassId(){
		setClassId(new SecondAndThirdDigitsDAO().getClassID(TextWorker.BOOTLOADER_LOADED_BOARD));
	}

	@Override
	public void setMenu() {
		if(getClassIdStr()==null || !getClassIdStr().equals(getClassId())){
			setF1Menu(new ComponentDAO().getByClassId("PCA", 10));//TODO
			setClassIdStr(getClassId());
		}
	}

}
