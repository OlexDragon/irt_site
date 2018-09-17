package irt.stock.rest.helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.Cost;
import irt.stock.data.jpa.beans.Cost.Currency;
import irt.stock.data.jpa.beans.PartNumber;

public class StockReport {

	private final static NumberFormat CURRENCY_INSTANCE = NumberFormat.getCurrencyInstance();

	public static  String[] componentStockReport(Component component){

		Long qty = Optional.ofNullable(component.getQty()).orElse(0L);

		final String partNumber = PartNumber.addDashes(component.getPartNumber());
		final String manufPartNumber = Optional.ofNullable(component.getManufPartNumber()).map(d->d.replaceAll("\"", "\"\"")).map(d->"\"" + d + "\"").orElse("");
		final String description = Optional.ofNullable(component.getDescription()).map(d->d.replaceAll("\"", "\"\"")).map(d->"\"" + d + "\"").orElse("");
		final String qtyStr = qty.toString();
		final String alternatives = component.getAlternatives().parallelStream().map(a->a.getAltMfrPartNumber()).collect(Collectors.joining(" : "));

		final Map<Boolean, List<Cost>> mapCost = component.getCosts().parallelStream().collect(Collectors.partitioningBy(cost->cost.getChangeDate()!=null));

		if(qty.compareTo(0L)<=0 || (mapCost.get(true).isEmpty() && mapCost.get(false).isEmpty()))
			return new String[]{partNumber, manufPartNumber, description, qtyStr, "", "", alternatives };

		// Get component cost with date
		final List<Cost> withDate = mapCost.get(true);
		withDate.sort((a, b)->b.getChangeDate().compareTo(a.getChangeDate()));

		// Collecting line of the price report
		CostCollector costs = new CostCollector();

		BigDecimal cst = null;
		for(Cost cost : withDate){

			BigDecimal gCost = cost.getCost();

			// If the price does not contain a quantity, use the quantity of components
			final Long forQty = Optional.of(cost.getForQty()).filter(fq->fq>0).orElse(qty);

			cst = gCost.stripTrailingZeros();
			Currency currency = cost.getCurrency();

			// The number of purchased components is greater than or equal to the number of components in the stock.
			if(forQty>=qty){

				costs.add(currency, cst, qty);
				qty = 0L;

			}else{

				qty -= forQty;
				costs.add(currency, cst, forQty);
			}

			if(qty.compareTo(0L)<=0)
				break;
		}

		// With out date
		if(qty.compareTo(0L)>0){

			final BigDecimal oCst = Optional.ofNullable(mapCost.get(false)).flatMap(StockReport::round).orElse(cst);

			Long q = qty;
			 Optional.of(oCst)
			 .filter(bd->bd.compareTo(BigDecimal.ZERO)>0)
			 .ifPresent(bd->{
				 
				 costs.add(null, bd, q);
			 });
		}

		return new String[]{partNumber, manufPartNumber, description, qtyStr, "\"" + costs + "\"", "\"" + costs.getSum() + "\"", alternatives };
	}

	public static Optional<BigDecimal> round(final List<Cost> withoutDate) {
		return withoutDate
				.parallelStream()
				.map(Cost::getCost)
				.filter(c->!c.equals(BigDecimal.ZERO))
				.map(bd->new BigDecimal[]{bd, BigDecimal.ONE})
				.reduce((a, b)->new BigDecimal[]{a[0].add(b[0]), a[1].add(b[1])})
				.map(arr->arr[0].divide(arr[1], RoundingMode.HALF_UP).stripTrailingZeros())
				.filter(c->c.compareTo(BigDecimal.ZERO)>0);
	}


	public static class CostCollector{

		private static final String UNKNOWN = "UNKNOWN";
		private final Map<String, Map<BigDecimal, Long>> costs = new HashMap<>();

		public void add(Currency currency, BigDecimal cost, Long qty) {

			if(cost==null || qty<=0 || cost.compareTo(BigDecimal.ZERO)<=0)
				return;

			String key = Optional.ofNullable(currency).map(Currency::name).orElse(UNKNOWN);

			Map<BigDecimal, Long> cs = Optional.ofNullable(costs.get(key))

					.orElseGet(
							()->{
								Map<BigDecimal, Long> map = new TreeMap<>();
								costs.put(key, map);
								return map;
							});

			Optional.ofNullable(cs.get(cost))
			.map(q->q + qty)
			.map(q->cs.put(cost, q))
			.orElseGet(()->cs.put(cost, qty));
		}

		public SumCollector getSum() {
			SumCollector sumCollector = new SumCollector();

			 costs.entrySet().stream()
			 .forEach(
					ks->{
						Currency currency = Optional.of(ks.getKey()).filter(key->!key.contentEquals(UNKNOWN)).map(Currency::valueOf).orElse(null);
						Optional.of(ks.getValue().entrySet())
						.map(Set::stream).orElse(Stream.empty())
						.map(es->es.getKey().multiply(new BigDecimal(es.getValue())))
						.forEach(bd->sumCollector.add(currency, bd));
					});
			return sumCollector;
		}

		@Override
		public String toString() {

			String unknown = toString(UNKNOWN, "");
			String cad = toString(Currency.CAD.name(), unknown);
			return toString(Currency.USD.name(), cad);
		}

		private String toString(String key, String toAdd) {

			Objects.requireNonNull(toAdd);

			String prefix = Optional.of(key).filter(k->k.equals(UNKNOWN)).map(k->"").orElse(key);

			return
					Optional.of(
							Optional.ofNullable(costs.get(key))

							.map(Map::entrySet)
							.map(Set::stream)
							.orElse(Stream.empty())
							.map(entry->entry.getValue() + "x" + prefix + "$" + entry.getKey())
							.collect(Collectors.joining(", ")))

					.filter(str->!str.isEmpty())
					.map(str->str + "; ")
					.orElse("") + toAdd;
		}
	}

	public static class SumCollector{

		private BigDecimal sum = BigDecimal.ZERO;
		private BigDecimal sumUSD = BigDecimal.ZERO;
		private BigDecimal sumCAD = BigDecimal.ZERO;

		public void add(Currency currency, BigDecimal toAdd) {

			if(currency==null) {
				sum = sum.add(toAdd);
				return;
			}

			switch(currency) {
			case USD:
				sumUSD = sumUSD.add(toAdd);
				break;

			case CAD:
				sumCAD = sumCAD.add(toAdd);
				break;
			}
		}

		private String currencyToString(Currency currency, BigDecimal sum, String str) {

			String dumStr = Optional.of(sum)

					.filter(s->s.compareTo(BigDecimal.ZERO)>0)
					.map(CURRENCY_INSTANCE::format)
					.map(
							s->Optional.ofNullable(currency)
							.map(Currency::name)
							.orElse("") + s)
					.orElse("");

			return Optional.ofNullable(str)

					.filter(s->!s.isEmpty())
					.map(s->s + "; " + dumStr)
					.orElse(dumStr);
		}

		@Override
		public String toString() {

			String us = currencyToString(Currency.USD, sumUSD, null);
			String ca = currencyToString(Currency.CAD, sumCAD, us);
			return currencyToString(null, sum, ca);
		}
	}
}
