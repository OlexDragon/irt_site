package irt.stock.rest.helpers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import irt.stock.data.jpa.beans.CompanyQty;

public class CoworkerReport {

	private final StringBuffer report = new StringBuffer();
	private final AtomicInteger index = new AtomicInteger(1);

	public synchronized void add(String partNumber, String usd, String cad, String unknown, List<CompanyQty> companyQties) {

		if(companyQties==null || companyQties.isEmpty())
			return;

		final long qty = companyQties.stream().mapToLong(CompanyQty::getQty).sum();
		if(qty==0)
			return;

		final String details = companyQties.stream()

				.filter(cq->cq.getQty()>0)
				.map(cq->cq.getCompany().getCompanyName() + "(" + cq.getQty() + ");")
				.collect(Collectors.joining(" "));

		final int i = index.getAndIncrement();

		report
		.append(partNumber).append(",")			// "A"
		.append(qty).append(",")				// "B"
		.append(usd).append(",")				// "C"
		.append(cad).append(",")				// "D"
		.append(unknown).append(",")			// "E"
		.append(String.format("=B%1$d*C%1$d", i))/*"G"*/.append(",")
		.append(String.format("=B%1$d*D%1$d", i))/*"H"*/.append(",")
		.append(String.format("=B%1$d*E%1$d", i))/*"I"*/.append(",")
		.append(details).append("\n");
	}

	public String getReport() {
		return report.toString();
	}
}
