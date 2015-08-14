package irt.data.purchase;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonProperty;

public class ForPriceBean {

	private final Logger logger = LogManager.getLogger();
	@JsonProperty("fu")
	private int forUnits;
	@JsonProperty("p")
	private BigDecimal price;
	@JsonProperty("np")
	private BigDecimal newPrice;

	public int getForUnits() {
		return logger.exit(forUnits);
	}
	public ForPriceBean setForUnits(int forUnits) {
		logger.entry(forUnits);
		this.forUnits = forUnits;
		return this;
	}
	public BigDecimal getPrice() {
		return logger.exit(price);
	}
	public ForPriceBean setPrice(BigDecimal price) {
		logger.entry(price);
		this.price = price;
		return this;
	}
	public BigDecimal getNewPrice() {
		return logger.exit(newPrice);
	}
	public ForPriceBean setNewPrice(BigDecimal newPrice) {
		logger.entry(newPrice);
		this.newPrice = newPrice;
		return this;
	}
	@Override
	public boolean equals(Object obj) {
		return obj!=null ? obj.hashCode()==hashCode() : false;
	}
	@Override
	public int hashCode() {
		return forUnits;
	}
	@Override
	public String toString() {
		return "ForPriceBean [forUnits=" + forUnits + ", price=" + price + ", newPrice=" + newPrice + "]";
	}
}
