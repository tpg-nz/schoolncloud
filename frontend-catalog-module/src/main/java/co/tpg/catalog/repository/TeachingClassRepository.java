package co.tpg.catalog.repository;

import co.tpg.catalog.domain.TeachingClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeachingClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeachingClassRepository extends JpaRepository<TeachingClass, Long> {

}
