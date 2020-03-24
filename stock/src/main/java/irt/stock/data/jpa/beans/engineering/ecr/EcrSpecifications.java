package irt.stock.data.jpa.beans.engineering.ecr;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

import org.springframework.data.jpa.domain.Specification;

import irt.stock.data.jpa.beans.engineering.ecr.EcrStatus.Status;

public class EcrSpecifications {

	public static Specification<Ecr> hasStatus(Status[] status){

		return (root, query, builder)->{

			if(status==null)
				return null;

			Join<Object, Object> join = root.join("status");
			Path<Status> statusPath = join.get("status");
			Order order = builder.desc(join.get("date"));
			query.orderBy(order);
			query.distinct(true);

			return statusPath.in((Object[])status);
		};
	}
}
