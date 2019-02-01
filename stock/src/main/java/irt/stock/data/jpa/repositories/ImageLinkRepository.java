package irt.stock.data.jpa.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.ImageLink;

public interface ImageLinkRepository extends CrudRepository<ImageLink, Long> {

	Optional<ImageLink> findByLinkEndsWith(String imageName);
	
}
