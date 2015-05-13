package irt.web.view;

import irt.web.entities.bom.BomEntity;
import irt.web.entities.bom.repositories.BomRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS)
public class BomView extends ComponentView{

	private static final Logger logger = LogManager.getLogger();

	public enum SortBy{
		DEFAULT,
		REFERENCE,
		PARTNUMBER,
		MFR,
		MFR_PN,
		QTY,
		SQTY,
		DESCRIPTION
	}
	private boolean upDown;
	private SortBy sortBy = SortBy.DEFAULT;

	@Autowired
	private BomRepository bomRepository;

	private Long topComponentId;

	private List<BomEntity> bomEntities;

	public List<BomEntity> getBomEntities() {
		return bomEntities;
	}

	public List<BomEntity> getBOM(Long topComponentId){
		logger.entry(topComponentId);

		if(bomEntities==null || this.topComponentId!=topComponentId){

			bomEntities = bomRepository.findBomComponentsEntities(topComponentId);
			if(bomEntities!=null ){
				this.topComponentId = topComponentId;
				sortBy(sortBy);
			}else
				this.topComponentId = null;
		}

		return logger.exit(bomEntities);
	}

	public void sortBy(final SortBy sortBy) {
		if(bomEntities==null)
			return;

		if(this.sortBy==sortBy){
			upDown = !upDown;
		}else{
			this.sortBy=sortBy;
			upDown = false;
		}

		Collections.sort(bomEntities, new Comparator<BomEntity>(){

			@Override
			public int compare(BomEntity o1, BomEntity o2) {
				int result = 0;

				switch(sortBy){
				case DESCRIPTION:
					break;
				case MFR:
					break;
				case MFR_PN:
					break;
				case PARTNUMBER:
					break;
				case QTY:
					break;
				case REFERENCE:
					break;
				case SQTY:
					break;
				default:
					result = defaultSort(o1, o2);
					break;
				}
				return result;
			}

			private int defaultSort(BomEntity o1, BomEntity o2) {

				int result;
				if(upDown)
					result = o1.getComponentEntity().getSchematicLetter().compareTo(o2.getComponentEntity().getSchematicLetter());
				else
					result = o2.getComponentEntity().getSchematicLetter().compareTo(o1.getComponentEntity().getSchematicLetter());

				if(result==0){
					String str1 = o1.getBomRef().getRef().split("-")[0].split(" ")[0];
					String str2 = o2.getBomRef().getRef().split("-")[0].split(" ")[0];
					int int1 = Integer.parseInt(str1);
					int int2 = Integer.parseInt(str2);
					if(upDown)
						result = Integer.compare(int1, int2);
					else
						result = Integer.compare(int2, int1);
				}
				return result;
			}
			
		});
	}

	@Override
	public boolean setComponent(Object qualifier) {
		boolean set = super.setComponent(qualifier);
		if(set){
			getBOM(getComponentEntity().getId());
		}
		return set;
	}
}
