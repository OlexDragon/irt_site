package irt.web.entities.part_number.repository;

import java.util.List;

import irt.web.entities.part_number.HtmlOptionsView;
import irt.web.entities.part_number.HtmlOptionsViewPK;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HtmlOptionsViewRepository extends JpaRepository<HtmlOptionsView, HtmlOptionsViewPK>{

	@Query("SELECT h FROM HtmlOptionsView h WHERE h.key.classId = :classId AND h.key.arraySequence = :arraySequence")
	public List<HtmlOptionsView> findByClassIdAndArraySequence(@Param("classId") Integer classId, @Param("arraySequence") Short arraySequence, Sort sort);
}
