package co.tpg.catalog.repository;

import co.tpg.catalog.domain.EducationalInstitution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the EducationalInstitution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EducationalInstitutionRepository extends JpaRepository<EducationalInstitution, Long> {

}
