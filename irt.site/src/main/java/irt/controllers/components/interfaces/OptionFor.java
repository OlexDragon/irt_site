
package irt.controllers.components.interfaces;

import java.util.List;

import irt.entities.ArrayEntity;

public interface OptionFor {
	ValueText[][] getOptions();
	List<ArrayEntity> getFields();
}
