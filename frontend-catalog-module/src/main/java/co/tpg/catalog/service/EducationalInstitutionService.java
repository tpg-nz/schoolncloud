package co.tpg.catalog.service;

import co.tpg.catalog.domain.EducationalInstitution;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link EducationalInstitution}.
 */
public interface EducationalInstitutionService {

    /**
     * Save a educationalInstitution.
     *
     * @param educationalInstitution the entity to save.
     * @return the persisted entity.
     */
    EducationalInstitution save(EducationalInstitution educationalInstitution);

    /**
     * Get all the educationalInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EducationalInstitution> findAll(Pageable pageable);


    /**
     * Get the "id" educationalInstitution.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EducationalInstitution> findOne(Long id);

    /**
     * Delete the "id" educationalInstitution.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
