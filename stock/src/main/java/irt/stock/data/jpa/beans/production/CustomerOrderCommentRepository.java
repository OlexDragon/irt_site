package irt.stock.data.jpa.beans.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CustomerOrderCommentRepository extends CrudRepository<CustomerOrderComment, Long> {

	Optional<CustomerOrderComment> findByComment(String comment);
}
