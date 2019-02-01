package irt.stock.data.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.ComponentImage;

public interface ComponentImageRepository extends CrudRepository<ComponentImage, Long> {
	
}
