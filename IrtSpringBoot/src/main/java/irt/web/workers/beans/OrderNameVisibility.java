package irt.web.workers.beans;

import irt.web.workers.beans.interfaces.BOMEntityFieldToString;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderNameVisibility implements Comparable<OrderNameVisibility> {

	private static final Logger logger = LogManager.getLogger();

	private String id;
	private String name;
	private String className;
	private int order;
	private boolean visible = true;
	private BOMEntityFieldToString bomEntityFieldToString;

	public OrderNameVisibility(String id, String name, String className, BOMEntityFieldToString bomEntityFieldToString, int order, boolean visible) {
		this.id = id;
		this.order = order;
		this.name = name;
		this.className=className;
		this.visible = visible;
		this.bomEntityFieldToString = bomEntityFieldToString;
	}

	public String getId() {
		return id;
	}


	public String getClassName() {
		return className;
	}


	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}


	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public BOMEntityFieldToString getBomEntityFieldToString() {
		return logger.exit(bomEntityFieldToString);
	}

	@Override
	public int compareTo(OrderNameVisibility o) {
		return Integer.compare(order, o.order);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof OrderNameVisibility ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public String toString() {
		return "OrderNameVisibility [id=" + id + ", name=" + name
				+ ", className=" + className + ", order=" + order
				+ ", visible=" + visible + ", bomEntityFieldToString="
				+ bomEntityFieldToString + "]";
	}
}
