package irt.web.entities.part_number.repository;

import irt.web.entities.part_number.PartNumberDetailsPK;
import irt.web.entities.part_number.PartNumberDetailsView;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartNumberDetailsViewRepository  extends JpaRepository<PartNumberDetailsView, PartNumberDetailsPK>{

	PartNumberDetailsView findFirstByKeyFirstThreeCharsAndKeySequence(String substring, Integer sequence);
}
