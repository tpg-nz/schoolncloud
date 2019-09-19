package co.tpg.catalog.service;

import co.tpg.catalog.domain.TeachingStaff;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link TeachingStaff}.
 */
public interface TeachingStaffService {

    /**
     * Save a teachingStaff.
     *
     * @param teachingStaff the entity to save.
     * @return the persisted entity.
     */
    TeachingStaff save(TeachingStaff teachingStaff);

    /**
     * Get all the teachingStaffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TeachingStaff> findAll(Pageable pageable);


    /**
     * Get the "id" teachingStaff.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TeachingStaff> findOne(Long id);

    /**
     * Delete the "id" teachingStaff.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
