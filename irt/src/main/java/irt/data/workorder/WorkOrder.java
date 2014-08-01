package irt.data.workorder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class WorkOrder {

	private final static Logger logger = LogManager.getLogger();

	private String name;
	private String description;
	private Set<Long> componentsIds = new TreeSet<>();
	private int quantity;
	private Timestamp creationTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name==null || (name=name.trim()).isEmpty() || name.equals("null"))
			this.name = null;
		else
			this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		logger.entry(description);
		if(description==null || (description=description.trim()).isEmpty() || description.equals("null"))
			this.description = null;
		else
			this.description = description;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	private void setCreationTime(String creationTime) {
		if(creationTime==null || (creationTime=creationTime.trim()).isEmpty() || creationTime.equals("null"))
			this.creationTime = null;
		else
			this.creationTime = Timestamp.valueOf(creationTime);
	}

	public void add(Long componentsId) {
		logger.entry(componentsId);
		componentsIds.add(componentsId);
	}

	public void add(Long[] componentsIds) {
		logger.entry((Object[])componentsIds);
		this.componentsIds.addAll(Arrays.asList(componentsIds));
	}

	public Iterator<Long> getIdsIterator(){
		return componentsIds.iterator();
	}

	public Long[] getIdsArray(){
		return componentsIds.toArray(new Long[componentsIds.size()]);
	}


	public void clear() {
		name = null;
		description = null;
		creationTime = null;
		componentsIds.clear();
	}

	public static WorkOrder parseWorkOrder(String workOrderStr) throws Exception {
		logger.entry(workOrderStr);

		WorkOrder workOrder  = null;

		if(workOrderStr!=null && workOrderStr.startsWith(WorkOrder.class.getSimpleName())){
			
			String lookFor = "componentsIds=";
			int indexOf = workOrderStr.indexOf(lookFor);
			int lastIndexOf = workOrderStr.indexOf(", creationTime=", indexOf);
			if (indexOf > 0 && lastIndexOf > 0) {
				//componentsIds
				String str = workOrderStr.substring(indexOf + lookFor.length(), lastIndexOf);
				if (str.length() > 0) {
					workOrder  = new WorkOrder();
					for (String s : str.split(","))
						if (!(s = s.replaceAll("\\D", "")).isEmpty())
							workOrder.add(Long.parseLong(s));

					//Name
					setStringValue("name=", ", description=", workOrderStr, workOrder, WorkOrder.class.getDeclaredMethod("setName", String.class));
					//Description
					setStringValue(", description=", ", componentsIds=", workOrderStr, workOrder, WorkOrder.class.getDeclaredMethod("setDescription", String.class));

					//Creation Time
					lookFor = "creationTime=";
					indexOf = workOrderStr.indexOf(lookFor);
					lastIndexOf = workOrderStr.lastIndexOf(']');
					if(indexOf>0 && lastIndexOf>indexOf)
						workOrder.setCreationTime(workOrderStr.substring(indexOf+lookFor.length(), lastIndexOf));
				}
			}
		}

		return logger.exit(workOrder);

	}

	private static void setStringValue(String lookFor, String enfWith, String workOrderStr, Object object, Method methodToRun) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int indexOf = workOrderStr.indexOf(lookFor);
		int lastIndexOf = workOrderStr.indexOf(enfWith, indexOf);
		if(indexOf>0 && lastIndexOf>0)
			methodToRun.invoke(object, workOrderStr.substring(indexOf+lookFor.length(), lastIndexOf));
		else
			methodToRun.invoke(object, (String)null);
	}

	@Override
	public String toString() {
		return "WorkOrder [name=" + name + ", description=" + description + ", componentsIds=" + componentsIds + ", creationTime=" + creationTime + "]";
	}

	public boolean remove(Long id) {
		return componentsIds.remove(id);
	}
}
