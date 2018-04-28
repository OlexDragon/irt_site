package irt.stock.data.jpa.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.Component;

public interface ComponentRepository extends CrudRepository<Component, Long> {
	List<Component> findByPartNumberContaining(String desiredPN);
}
