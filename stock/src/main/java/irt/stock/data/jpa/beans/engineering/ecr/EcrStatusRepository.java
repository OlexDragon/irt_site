package irt.stock.data.jpa.beans.engineering.ecr;

import java.sql.Timestamp;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface EcrStatusRepository extends PagingAndSortingRepository<EcrStatus, Timestamp> {
}
