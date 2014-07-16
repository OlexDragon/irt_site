package irt.data.purchase;

import java.math.BigDecimal;

import irt.work.ComboBoxField;

public class ForPriceService implements ComboBoxField{

	public enum Status{
		HAVE_TO_INSERT,
		HAVE_TO_UPDATE,
		NOT_INITIALIZED,
		SAVED
	}
//	public static int INSERT = 0;
//	public static int UPDATE = 1;
//	public static int SAVED = 3;

	private ForPriceBean forPriceBean;

	public ForPriceBean getForPriceBean() {
		return forPriceBean;
	}

	public ForPriceService setForPriceBean(ForPriceBean forPriceBean) {
		this.forPriceBean = forPriceBean;
		return this;
	}

	public ForPriceService set(BigDecimal price, int forUnits) {
		forPriceBean.setPrice(price);
		forPriceBean.setForUnits(forUnits);
		return this;
	}

	public ForPriceService set(BigDecimal price, int forUnits, Status status) {
		if(status==Status.SAVED)
			forPriceBean.setPrice(price);
		else
			forPriceBean.setNewPrice(price);
		forPriceBean.setForUnits(forUnits);
		return this;
	}

	public static int getForUnits(ForPriceBean forPriceBean) {
		return forPriceBean.getForUnits();
	}

	public int getForUnits() {
		return forPriceBean.getForUnits();
	}

	public static BigDecimal getPrice(ForPriceBean forPriceBean) {
		BigDecimal newPrice = forPriceBean.getNewPrice();
		return newPrice==null ? forPriceBean.getPrice() : newPrice;
	}

	public BigDecimal getPrice() {
		return getPrice(forPriceBean);
	}

	@Override
	public String getValue() {
		return ""+forPriceBean.getForUnits();
	}

	@Override
	public String getText() {
		return ""+forPriceBean.getForUnits();
	}

	public static Status getStatus(ForPriceBean forPriceBean) {
		BigDecimal newPrice = forPriceBean.getNewPrice();
		return forPriceBean.getPrice() == null
				? newPrice == null
					? Status.NOT_INITIALIZED
					: Status.HAVE_TO_INSERT
				: newPrice == null ?
						Status.SAVED
						: Status.HAVE_TO_UPDATE;
	}

	public Status getStatus() {
		return getStatus(forPriceBean);
	}

	public static void setPrice(ForPriceBean forPriceBean, BigDecimal newPrice) {
		if(newPrice!=null && newPrice.compareTo(forPriceBean.getPrice())!=0)
			forPriceBean.setNewPrice(newPrice);
		else
			newPrice = null;
	}

	public void setPrice(BigDecimal newPrice) {
		setPrice(forPriceBean, newPrice);
	}

	public static void setPrice(ForPriceBean forPriceBean) {
		forPriceBean.setPrice(forPriceBean.getNewPrice());
		forPriceBean.setNewPrice(null);
	}

	public void setPrice() {
		setPrice(forPriceBean);
	}

	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}

	@Override
	public int hashCode() {
		return forPriceBean.hashCode();
	}

	public static boolean isSet(ForPriceBean forPriceBean) {
		return (forPriceBean.getNewPrice()!=null || forPriceBean.getPrice()!=null) && forPriceBean.getForUnits() >0;
	}

	public boolean isSet() {
		return isSet(forPriceBean);
	}

	public void setForUnits(int forUnits) {
		forPriceBean.setForUnits(forUnits);
	}

	public static boolean isChanged(ForPriceBean forPriceBean) {
		return forPriceBean.getNewPrice()!=null;
	}

	public boolean isChanged() {
		return isChanged(forPriceBean);
	}

	@Override
	public String toString() {
		return "ForPrice [forPriceBean=" + forPriceBean + "]";
	}
}
