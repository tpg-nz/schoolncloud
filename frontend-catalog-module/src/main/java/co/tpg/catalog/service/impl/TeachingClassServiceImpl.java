package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.TeachingClassService;
import co.tpg.catalog.domain.TeachingClass;
import co.tpg.catalog.repository.TeachingClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link TeachingClass}.
 */
@Service
@Transactional
public class TeachingClassServiceImpl implements TeachingClassService {

    private final Logger log = LoggerFactory.getLogger(TeachingClassServiceImpl.class);

    private final TeachingClassRepository teachingClassRepository;

    public TeachingClassServiceImpl(TeachingClassRepository teachingClassRepository) {
        this.teachingClassRepository = teachingClassRepository;
    }

    /**
     * Save a teachingClass.
     *
     * @param teachingClass the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TeachingClass save(TeachingClass teachingClass) {
        log.debug("Request to save TeachingClass : {}", teachingClass);
        return teachingClassRepository.save(teachingClass);
    }

    /**
     * Get all the teachingClasses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TeachingClass> findAll(Pageable pageable) {
        log.debug("Request to get all TeachingClasses");
        return teachingClassRepository.findAll(pageable);
    }


    /**
     * Get one teachingClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TeachingClass> findOne(Long id) {
        log.debug("Request to get TeachingClass : {}", id);
        return teachingClassRepository.findById(id);
    }

    /**
     * Delete the teachingClass by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TeachingClass : {}", id);
        teachingClassRepository.deleteById(id);
    }
}
