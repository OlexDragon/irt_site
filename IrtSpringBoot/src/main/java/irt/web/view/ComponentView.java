package irt.web.view;

import irt.web.entities.component.ComponentEntity;
import irt.web.entities.component.repositories.ComponentsRepository;
import irt.web.entities.component.repositories.MovementDetailRepository;
import irt.web.view.workers.component.PartNumbers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ComponentView {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private ComponentsRepository componentsRepository;
	@Autowired
	private MovementDetailRepository movementDetailRepository;

	private ComponentEntity componentEntity;

	public ComponentView() {
		logger.entry();
	}

	public boolean setComponent(Object qualifier){
		logger.entry(qualifier);

		if(qualifier instanceof Long)
			componentEntity = componentsRepository.findOne((Long) qualifier);

		else if(qualifier instanceof Integer)
			componentEntity = componentsRepository.findOne(new Long((Integer)qualifier));

		else if(qualifier instanceof String)
			componentEntity = componentsRepository.findOneByPartNumber(PartNumbers.dbFormat((String)qualifier));

		else if(qualifier instanceof ComponentEntity)
			componentEntity = (ComponentEntity) qualifier;
		else
			componentEntity=null;

//		if(componentEntity!=null && componentEntity.getMovementDetailsEntities()==null){
//			componentEntity.setMovementDetailsEntities(movementDetailRepository.findByIdComponents(componentEntity.getId()));
//		}

		return logger.exit(componentEntity!=null);
	}

	public ComponentEntity getComponentEntity() {
		return logger.exit(componentEntity);
	}

	public String getPartNumber(){
		return logger.exit(componentEntity!=null ? PartNumbers.format(componentEntity.getPartNumber()) : null);
	}

	public String getDescription(){
		return componentEntity!=null ? componentEntity.getDescription() : null;
	}

	@Override
	public String toString() {
		return "\n\tComponentView [\n\t\tComponentEntity=" + componentEntity + "]";
	}

	public boolean hasBOM() {
		return componentEntity!=null && componentEntity.getManufPartNumber()!=null && componentEntity.getManufPartNumber().startsWith("IRT BOM");
	}
}
