package co.tpg.catalog.service.impl;

import co.tpg.catalog.service.PaperService;
import co.tpg.catalog.domain.Paper;
import co.tpg.catalog.repository.PaperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Paper}.
 */
@Service
@Transactional
public class PaperServiceImpl implements PaperService {

    private final Logger log = LoggerFactory.getLogger(PaperServiceImpl.class);

    private final PaperRepository paperRepository;

    public PaperServiceImpl(PaperRepository paperRepository) {
        this.paperRepository = paperRepository;
    }

    /**
     * Save a paper.
     *
     * @param paper the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Paper save(Paper paper) {
        log.debug("Request to save Paper : {}", paper);
        return paperRepository.save(paper);
    }

    /**
     * Get all the papers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Paper> findAll(Pageable pageable) {
        log.debug("Request to get all Papers");
        return paperRepository.findAll(pageable);
    }


    /**
     * Get one paper by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Paper> findOne(Long id) {
        log.debug("Request to get Paper : {}", id);
        return paperRepository.findById(id);
    }

    /**
     * Delete the paper by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Paper : {}", id);
        paperRepository.deleteById(id);
    }
}
