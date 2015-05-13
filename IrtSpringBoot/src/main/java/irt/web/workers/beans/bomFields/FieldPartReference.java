package irt.web.workers.beans.bomFields;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import irt.web.entities.bom.BomEntity;
import irt.web.entities.component.ComponentEntity;
import irt.web.entities.component.ComponentsOldSchematicLetterEntity;
import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

public class FieldPartReference implements BOMEntityFieldToString {

	private static final Logger logger = LogManager.getLogger();

	@Override
	public Class<?> returnType() {
		return String.class;
	}

	@Override
	public String toString(BomEntity bomEntity) {
		String refLetter = getSchemeticLetter(bomEntity);

		if(refLetter!=null){
			refLetter = refLetter+bomEntity.getBomRef().getRef().replaceAll(" ", " "+refLetter);
		}

		return logger.exit(refLetter);
	}

	private String getSchemeticLetter(BomEntity bomEntity) {
		logger.entry(bomEntity);

		ComponentEntity ce = bomEntity.getComponentEntity();
		List<ComponentsOldSchematicLetterEntity> osles = ce.getOldSchematicLetterEntities();
		String refLeter;

		if(osles==null){
			refLeter = ce.getSchematicLetter();
		}else{
			String dateStr = bomEntity.getTopComponentEntity().getManufPartNumber().substring(8);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
			try {
				Date date = sdf.parse(dateStr);
				ComponentsOldSchematicLetterEntity cl = null;
				for(ComponentsOldSchematicLetterEntity cosl:osles){
					Date lastDate = cosl.getComponentsOldSchematicLetterEntityPK().getLastDate();
					if(date.before(lastDate))
						cl = cosl;
					else
						break;
				}
				refLeter = cl!=null ? cl.getSchematicLetter() : ce.getSchematicLetter();

			} catch (ParseException e) {
				logger.catching(e);
				refLeter = null;
			}
		}
		return logger.exit(refLeter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String value(BomEntity bomEntity) {
		return toString(bomEntity);
	}

	@Override
	public String getLink(BomEntity bomEntity) {
		return null;
	}

}
