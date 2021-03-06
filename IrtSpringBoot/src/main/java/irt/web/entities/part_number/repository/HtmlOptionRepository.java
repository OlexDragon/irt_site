package irt.web.entities.part_number.repository;

import irt.web.entities.part_number.HtmOptionEntityPK;
import irt.web.entities.part_number.HtmlOptionEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HtmlOptionRepository extends JpaRepository<HtmlOptionEntity, HtmOptionEntityPK>{

	HtmlOptionEntity findFirstByArrayName(String title);

}
