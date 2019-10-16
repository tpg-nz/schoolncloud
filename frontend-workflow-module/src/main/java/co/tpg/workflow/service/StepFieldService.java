package co.tpg.workflow.service;

import co.tpg.workflow.domain.StepField;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link StepField}.
 */
public interface StepFieldService {

    /**
     * Save a stepField.
     *
     * @param stepField the entity to save.
     * @return the persisted entity.
     */
    StepField save(StepField stepField);

    /**
     * Get all the stepFields.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StepField> findAll(Pageable pageable);


    /**
     * Get the "id" stepField.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StepField> findOne(Long id);

    /**
     * Delete the "id" stepField.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
