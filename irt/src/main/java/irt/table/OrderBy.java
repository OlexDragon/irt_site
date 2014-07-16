package irt.table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class OrderBy {

    protected final static Logger logger = (Logger) LogManager.getLogger();

    private static final String DESC = "`DESC";
	private static final String ORDER_BY = "ORDER BY`";
	private String orderBy;
	private boolean desc;
	
	public OrderBy(String orderBy){
		this.orderBy = orderBy;
	}

	public void setOrderBy(String orderBy) { 
		if(this.orderBy.equals(orderBy))
			desc = !desc;
		else{
			this.orderBy = orderBy;
			desc = false;
		}
	}

	public boolean isDesc() {
		return desc;
	}

	public void setDesc(boolean desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return (!orderBy.isEmpty()) 
				? ORDER_BY+orderBy
						+ (desc 
								? DESC
								: "`") 
				: "";
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return orderBy!=null ? orderBy.hashCode()+(desc ? 1231 : 1237) : super.hashCode();
	}

	public static OrderBy parseOrderBy(String orderByStr) {
		logger.entry(orderByStr);
		OrderBy orderBy = null;

		boolean desc = false;

		if(orderByStr!=null && orderByStr.startsWith(ORDER_BY)){
			int indexOf = orderByStr.indexOf('`');
			int lastIndexOf = orderByStr.lastIndexOf('`');
			desc = orderByStr.contains(DESC);
			if(indexOf>=0 && lastIndexOf>indexOf)
				orderByStr = orderByStr.substring(++indexOf, lastIndexOf);
			else
				orderByStr = "";
		}else
			orderByStr = "";

		orderBy = new OrderBy(orderByStr);
		orderBy.setDesc(desc);

		return logger.exit(orderBy);
	}
}
