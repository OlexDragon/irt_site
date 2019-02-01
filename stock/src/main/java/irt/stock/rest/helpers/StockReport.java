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

		final List<Cost> cs = component.getCosts();
		final Optional<Cost> lastPurchase = cs.parallelStream().collect(Collectors.maxBy(
				(a,b)->{

					return Optional
							.ofNullable(a.getChangeDate())
							.map(
									aDate->
									Optional.ofNullable(b.getChangeDate())
									.map(
											bDate->
											aDate.after(bDate) ? 1 : aDate.before(bDate) ? -1 : 0)
									.orElse(1))
							.orElse(-1);
				}));

		final String[] result = new String[11];
		result[0] = partNumber;
		result[1] = manufPartNumber;
		result[2] = description;
		result[3] = qtyStr;
		result[7] = "=D%1$d*E%1$d";
		result[8] = "=D%1$d*F%1$d";
		result[9] = "=D%1$d*G%1$d";
		result[10] = alternatives;

		if(lastPurchase.isPresent()) {

			final Cost cost = lastPurchase.get();
			final Currency currency = cost.getCurrency();

			BigDecimal us = BigDecimal.ZERO;
			BigDecimal ca = BigDecimal.ZERO;
			BigDecimal un = BigDecimal.ZERO;

			if(currency==null)
				un = cost.getCost();
			else
				switch(currency) {
				case CAD:
					ca = cost.getCost();
					break;
				case USD:
					us = cost.getCost();
					break;
				default:
					un = cost.getCost();
				}
			result[4] = us.toString();
			result[5] = ca.toString();
			result[6] = un.toString();

		}else {
			result[4] = "0";
			result[5] = "0";
			result[6] = "0";
		}
		return result;
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

		public String sumOf(Currency currency) {

			BigDecimal result = Optional

					.ofNullable(currency)
					.map(c->{
						switch(currency) {
						case CAD:
							return sumCAD;
						case USD:
							return sumUSD;
						default:
							return sum;
						}
					})
					.orElse(sum);

			return Optional.of(result)

					.map(CURRENCY_INSTANCE::format)
					.orElse("");
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
