package irt.stock.data.jpa.beans.engineering.ecr;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EcrRepository extends PagingAndSortingRepository<Ecr, Long>, JpaSpecificationExecutor<Ecr> {
}
