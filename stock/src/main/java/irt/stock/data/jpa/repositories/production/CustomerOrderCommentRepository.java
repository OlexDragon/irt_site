package irt.stock.data.jpa.repositories.production;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import irt.stock.data.jpa.beans.production.CustomerOrderComment;

public interface CustomerOrderCommentRepository extends CrudRepository<CustomerOrderComment, Long> {

	Optional<CustomerOrderComment> findByComment(String comment);
}
