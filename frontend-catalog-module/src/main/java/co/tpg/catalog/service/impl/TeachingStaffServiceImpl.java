package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.TeachingStaffService;
import co.tpg.catalog.domain.TeachingStaff;
import co.tpg.catalog.repository.TeachingStaffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TeachingStaff}.
 */
@Service
@Transactional
public class TeachingStaffServiceImpl implements TeachingStaffService {

    private final Logger log = LoggerFactory.getLogger(TeachingStaffServiceImpl.class);

    private final TeachingStaffRepository teachingStaffRepository;

    public TeachingStaffServiceImpl(TeachingStaffRepository teachingStaffRepository) {
        this.teachingStaffRepository = teachingStaffRepository;
    }

    /**
     * Save a teachingStaff.
     *
     * @param teachingStaff the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TeachingStaff save(TeachingStaff teachingStaff) {
        log.debug("Request to save TeachingStaff : {}", teachingStaff);
        return teachingStaffRepository.save(teachingStaff);
    }

    /**
     * Get all the teachingStaffs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeachingStaff> findAll(Pageable pageable) {
        log.debug("Request to get all TeachingStaffs");
        return teachingStaffRepository.findAll(pageable);
    }


    /**
     * Get one teachingStaff by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TeachingStaff> findOne(Long id) {
        log.debug("Request to get TeachingStaff : {}", id);
        return teachingStaffRepository.findById(id);
    }

    /**
     * Delete the teachingStaff by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeachingStaff : {}", id);
        teachingStaffRepository.deleteById(id);
    }
}
