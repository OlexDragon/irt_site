package irt.stock.data.jpa.beans.engineering.eco;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EcoRepository extends PagingAndSortingRepository<Eco, Long>, JpaSpecificationExecutor<Eco> {

	List<Eco> findByStatusIn(Collection<EcoStatus> status, Pageable pageable);
	List<Eco> findByStatusInAndNumber(List<EcoStatus> ecoStatus, String ecoNumber, Pageable pageable);
}
