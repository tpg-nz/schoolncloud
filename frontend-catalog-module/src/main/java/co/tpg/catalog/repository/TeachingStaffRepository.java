package co.tpg.catalog.repository;

import co.tpg.catalog.domain.TeachingStaff;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TeachingStaff entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TeachingStaffRepository extends JpaRepository<TeachingStaff, Long> {

}
