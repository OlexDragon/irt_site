package irt.stock.data.jpa.beans.engineering.eco;

public enum EcoStatus {

	CREATED,			//Created by engineer
	APPROVED,			//Approved by top engineer
	FINALLY_APPROVED,	//Approved by O.C.
	RELEASED;			//Released by executor
}
