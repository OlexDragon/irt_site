package irt.stock.data.jpa.beans.engineering.eco;

import java.sql.Timestamp;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EcoStatusRepository extends PagingAndSortingRepository<EcoStatus, Timestamp> {
}
