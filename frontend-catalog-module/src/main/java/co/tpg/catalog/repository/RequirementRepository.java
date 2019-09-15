package co.tpg.catalog.repository;

import co.tpg.catalog.domain.Requirement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Requirement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

}
