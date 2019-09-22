package co.tpg.catalog.repository;

import co.tpg.catalog.domain.Paper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Paper entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {

}
