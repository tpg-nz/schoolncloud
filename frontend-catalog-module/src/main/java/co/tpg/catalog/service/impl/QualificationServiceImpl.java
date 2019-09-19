package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.QualificationService;
import co.tpg.catalog.domain.Qualification;
import co.tpg.catalog.repository.QualificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Qualification}.
 */
@Service
@Transactional
public class QualificationServiceImpl implements QualificationService {

    private final Logger log = LoggerFactory.getLogger(QualificationServiceImpl.class);

    private final QualificationRepository qualificationRepository;

    public QualificationServiceImpl(QualificationRepository qualificationRepository) {
        this.qualificationRepository = qualificationRepository;
    }

    /**
     * Save a qualification.
     *
     * @param qualification the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Qualification save(Qualification qualification) {
        log.debug("Request to save Qualification : {}", qualification);
        return qualificationRepository.save(qualification);
    }

    /**
     * Get all the qualifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Qualification> findAll(Pageable pageable) {
        log.debug("Request to get all Qualifications");
        return qualificationRepository.findAll(pageable);
    }


    /**
     * Get one qualification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Qualification> findOne(Long id) {
        log.debug("Request to get Qualification : {}", id);
        return qualificationRepository.findById(id);
    }

    /**
     * Delete the qualification by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Qualification : {}", id);
        qualificationRepository.deleteById(id);
    }
}
