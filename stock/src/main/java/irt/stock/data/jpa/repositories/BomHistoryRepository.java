package irt.stock.data.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.BomHistory;

public interface BomHistoryRepository extends CrudRepository<BomHistory, Long> {
}
