package co.tpg.workflow.repository;

import co.tpg.workflow.domain.StepField;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the StepField entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepFieldRepository extends JpaRepository<StepField, Long> {

}
