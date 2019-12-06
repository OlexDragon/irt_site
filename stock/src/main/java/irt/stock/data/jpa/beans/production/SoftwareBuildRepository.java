package irt.stock.data.jpa.beans.production;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface SoftwareBuildRepository extends CrudRepository<SoftwareBuild, Long> {

	Optional<SoftwareBuild> findByBuild(Date build);
}
