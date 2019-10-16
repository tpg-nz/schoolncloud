package co.tpg.workflow.service;

import co.tpg.workflow.domain.Step;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Step}.
 */
public interface StepService {

    /**
     * Save a step.
     *
     * @param step the entity to save.
     * @return the persisted entity.
     */
    Step save(Step step);

    /**
     * Get all the steps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Step> findAll(Pageable pageable);


    /**
     * Get the "id" step.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Step> findOne(Long id);

    /**
     * Delete the "id" step.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
