package irt.stock.data.jpa.repositories.production;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.production.SoftwareBuild;

public interface SoftwareBuildRepository extends CrudRepository<SoftwareBuild, Long> {

	Optional<SoftwareBuild> findByBuild(Date build);
}
