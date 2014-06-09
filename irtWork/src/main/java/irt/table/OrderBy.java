package irt.table;

import irt.data.CookiesWorker;

import javax.servlet.http.HttpServletRequest;

public class OrderBy {
	private static final String DESC = "DESC";
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
				? ORDER_BY+orderBy +"`"
						+ ((isDesc()) 
								? DESC
								: "") 
				: "";
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return orderBy!=null ? orderBy.hashCode() : super.hashCode();
	}

	public static OrderBy parseOrderBy(String orderByStr) {
		OrderBy orderBy = null;

		if(orderByStr!=null && orderByStr.startsWith(ORDER_BY)){
			boolean desc = orderByStr.contains(DESC);
			orderByStr = orderByStr.substring(ORDER_BY.length(), orderByStr.indexOf('`'));
			orderBy = new OrderBy(orderByStr);
			orderBy.setDesc(desc);
		}

		return orderBy;
	}

	public static OrderBy getOrderBy(HttpServletRequest request) {

		String orderByStr = CookiesWorker.getCookieValue(request, OrderBy.class.getSimpleName());
		return OrderBy.parseOrderBy(orderByStr);
	}
}
