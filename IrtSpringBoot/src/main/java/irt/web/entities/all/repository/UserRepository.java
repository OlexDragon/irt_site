package irt.web.entities.all.repository;

import irt.web.entities.all.UsersEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<UsersEntity, Long>{

	UsersEntity findOneByUsername(String username);
}
