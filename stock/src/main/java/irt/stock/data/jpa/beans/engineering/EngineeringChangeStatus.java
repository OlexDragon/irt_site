package irt.stock.data.jpa.beans.engineering;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.ecr.EcrForwardTo;

public interface EngineeringChangeStatus {

	StatusOf getStatus();
	User getChangedBy();
	EcrForwardTo getForwardTo();
}
