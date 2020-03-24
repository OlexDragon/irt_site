package irt.stock.data.jpa.beans.engineering;

import java.util.List;

public interface EngineeringChange {

	Long 							getNumber();
	String 							getReason();
	EngineeringChangeStatus 		getLastStatus();
	List<EngineeringChangeStatus> 	getStatus();
}
