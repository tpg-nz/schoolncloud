package co.tpg.catalog.service.impl;

import co.tpg.catalog.repository.EducationalInstitutionRepository;
import co.tpg.catalog.service.EducationalInstitutionService;
import co.tpg.catalog.domain.EducationalInstitution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EducationalInstitution}.
 */
@Service
@Transactional
public class EducationalInstitutionServiceImpl implements EducationalInstitutionService {

    private final Logger log = LoggerFactory.getLogger(EducationalInstitutionServiceImpl.class);

    private final EducationalInstitutionRepository educationalInstitutionRepository;

    public EducationalInstitutionServiceImpl(EducationalInstitutionRepository educationalInstitutionRepository) {
        this.educationalInstitutionRepository = educationalInstitutionRepository;
    }

    /**
     * Save a educationalInstitution.
     *
     * @param educationalInstitution the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EducationalInstitution save(EducationalInstitution educationalInstitution) {
        log.debug("Request to save EducationalInstitution : {}", educationalInstitution);
        return educationalInstitutionRepository.save(educationalInstitution);
    }

    /**
     * Get all the educationalInstitutions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EducationalInstitution> findAll(Pageable pageable) {
        log.debug("Request to get all EducationalInstitutions");
        return educationalInstitutionRepository.findAll(pageable);
    }


    /**
     * Get one educationalInstitution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EducationalInstitution> findOne(Long id) {
        log.debug("Request to get EducationalInstitution : {}", id);
        return educationalInstitutionRepository.findById(id);
    }

    /**
     * Delete the educationalInstitution by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EducationalInstitution : {}", id);
        educationalInstitutionRepository.deleteById(id);
    }
}
