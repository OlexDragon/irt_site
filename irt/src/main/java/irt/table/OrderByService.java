package irt.table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderByService {

	protected final static Logger logger = LogManager.getLogger();

    public static class OrderByBean {
		private String orderBy;
		private boolean desc;

		public OrderByBean() {
		}

		public String getOrderBy() {
			return orderBy;
		}

		public void setOrderBy(String orderBy) {
			this.orderBy = orderBy;
		}

		public boolean isDesc() {
			return desc;
		}

		public void setDesc(boolean desc) {
			this.desc = desc;
		}
	}

    private static final String DESC = "`DESC";
	private static final String ORDER_BY = "ORDER BY`";
	private OrderByBean orderByBean = new OrderByBean();

	public OrderByService(String orderBy){
		this.orderByBean.setOrderBy(orderBy);
	}

	public OrderByService() {
	}

	public void setOrderBy(String orderBy) { 
		if(this.orderByBean.getOrderBy().equals(orderBy))
			orderByBean.setDesc(!orderByBean.isDesc());
		else{
			this.orderByBean.setOrderBy(orderBy);
			orderByBean.setDesc(false);
		}
	}

	public boolean isDesc() {
		return orderByBean.isDesc();
	}

	public void setDesc(boolean desc) {
		this.orderByBean.setDesc(desc);
	}

	@Override
	public String toString() {
		return (!orderByBean.getOrderBy().isEmpty()) 
				? ORDER_BY+orderByBean.getOrderBy()
						+ (orderByBean.isDesc() 
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
		return orderByBean.getOrderBy()!=null ? orderByBean.getOrderBy().hashCode()+(orderByBean.isDesc() ? 1231 : 1237) : super.hashCode();
	}

	public static OrderByService parseOrderBy(String orderByStr) {
		logger.entry(orderByStr);
		OrderByService orderBy = null;

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

		orderBy = new OrderByService(orderByStr);
		orderBy.setDesc(desc);

		return logger.exit(orderBy);
	}

	public OrderByBean getOrderByBean() {
		return orderByBean;
	}

	public OrderByService setOrderByBean(OrderByBean orderByBean) {
		this.orderByBean = orderByBean;
		return this;
	}
}
