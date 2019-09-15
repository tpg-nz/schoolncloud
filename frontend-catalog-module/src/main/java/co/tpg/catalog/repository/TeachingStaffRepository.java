package co.tpg.catalog.repository;

import co.tpg.catalog.domain.TeachingStaff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the TeachingStaff entity.
 */
@Repository
public interface TeachingStaffRepository extends JpaRepository<TeachingStaff, Long> {

    @Query(value = "select distinct teachingStaff from TeachingStaff teachingStaff left join fetch teachingStaff.papers",
        countQuery = "select count(distinct teachingStaff) from TeachingStaff teachingStaff")
    Page<TeachingStaff> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct teachingStaff from TeachingStaff teachingStaff left join fetch teachingStaff.papers")
    List<TeachingStaff> findAllWithEagerRelationships();

    @Query("select teachingStaff from TeachingStaff teachingStaff left join fetch teachingStaff.papers where teachingStaff.id =:id")
    Optional<TeachingStaff> findOneWithEagerRelationships(@Param("id") Long id);

}
