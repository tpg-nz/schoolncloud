package co.tpg.catalog.repository;

import co.tpg.catalog.domain.EducationalInstituition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EducationalInstituition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationalInstituitionRepository extends JpaRepository<EducationalInstituition, Long> {

}
