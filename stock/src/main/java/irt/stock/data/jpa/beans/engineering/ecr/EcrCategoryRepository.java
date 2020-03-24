package irt.stock.data.jpa.beans.engineering.ecr;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory;

public interface EcrCategoryRepository extends CrudRepository<EcrCategory, EcrCategoryId> {

}
