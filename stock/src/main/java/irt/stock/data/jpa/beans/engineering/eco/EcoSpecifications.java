package irt.stock.data.jpa.beans.engineering.eco;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.eco.EcoStatus.Status;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;

public class EcoSpecifications {

	public static Specification<Eco> hasStatus(EcoStatus[] status){

		return (root, query, builder)->{

			Path<EcoStatus> path = root.get("status");

			return path.in((Object[])status);
		};
	}

	public static Specification<Eco> hasNumber(String number){

		return (root, query, builder)->{


			// Temporary 'search' use for ECO Number only
			String n = number.replaceAll("\\D", "");

			if(n.isEmpty())
				return null;

			Path<EcoStatus> path = root.get("number");

			return path.in(n);
		};
	}

	public static Specification<Eco> hasCategories(Category[] categories){

		return (root, query, builder)->{

			if(categories==null)
				return null;

			Path<Category> path = root.join("ecoRelatedCategories").get("idEcoCategories").get("ecoCategory");

			return path.in((Object[])categories);
		};
	}

	public static Specification<Eco> sentTo(User user){

		return (root, query, builder)->{

			if(user==null)
				return null;

			Predicate sentTo = root.get("approvedBy").in(user);
			Predicate satus = root.get("status").in(Status.CREATED);

			return builder.and(sentTo, satus);
		};
	}
}
