package co.tpg.workflow.repository;

import co.tpg.workflow.domain.Step;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Step entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StepRepository extends JpaRepository<Step, Long> {

}
