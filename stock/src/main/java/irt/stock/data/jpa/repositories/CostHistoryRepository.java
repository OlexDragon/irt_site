package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.CostHistory;

public interface CostHistoryRepository extends CrudRepository<CostHistory, Long> {

	List<CostHistory> findByIdComponentsOrderByChangeDateDesc(Long componentId);
}
