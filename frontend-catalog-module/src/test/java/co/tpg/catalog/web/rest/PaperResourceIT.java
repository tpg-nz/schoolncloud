package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.Paper;
import co.tpg.catalog.repository.PaperRepository;
import co.tpg.catalog.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static co.tpg.catalog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PaperResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class PaperResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;
    private static final Integer SMALLER_YEAR = 1 - 1;

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;
    private static final Integer SMALLER_POINTS = 1 - 1;

    private static final String DEFAULT_TEACHING_PERIOD = "AAAAAAAAAA";
    private static final String UPDATED_TEACHING_PERIOD = "BBBBBBBBBB";

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restPaperMockMvc;

    private Paper paper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PaperResource paperResource = new PaperResource(paperRepository);
        this.restPaperMockMvc = MockMvcBuilders.standaloneSetup(paperResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paper createEntity(EntityManager em) {
        Paper paper = new Paper()
            .code(DEFAULT_CODE)
            .year(DEFAULT_YEAR)
            .points(DEFAULT_POINTS)
            .teachingPeriod(DEFAULT_TEACHING_PERIOD);
        return paper;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Paper createUpdatedEntity(EntityManager em) {
        Paper paper = new Paper()
            .code(UPDATED_CODE)
            .year(UPDATED_YEAR)
            .points(UPDATED_POINTS)
            .teachingPeriod(UPDATED_TEACHING_PERIOD);
        return paper;
    }

    @BeforeEach
    public void initTest() {
        paper = createEntity(em);
    }

    @Test
    @Transactional
    public void createPaper() throws Exception {
        int databaseSizeBeforeCreate = paperRepository.findAll().size();

        // Create the Paper
        restPaperMockMvc.perform(post("/api/papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paper)))
            .andExpect(status().isCreated());

        // Validate the Paper in the database
        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeCreate + 1);
        Paper testPaper = paperList.get(paperList.size() - 1);
        assertThat(testPaper.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPaper.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testPaper.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testPaper.getTeachingPeriod()).isEqualTo(DEFAULT_TEACHING_PERIOD);
    }

    @Test
    @Transactional
    public void createPaperWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = paperRepository.findAll().size();

        // Create the Paper with an existing ID
        paper.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaperMockMvc.perform(post("/api/papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paper)))
            .andExpect(status().isBadRequest());

        // Validate the Paper in the database
        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paperRepository.findAll().size();
        // set the field null
        paper.setCode(null);

        // Create the Paper, which fails.

        restPaperMockMvc.perform(post("/api/papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paper)))
            .andExpect(status().isBadRequest());

        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPapers() throws Exception {
        // Initialize the database
        paperRepository.saveAndFlush(paper);

        // Get all the paperList
        restPaperMockMvc.perform(get("/api/papers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paper.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].teachingPeriod").value(hasItem(DEFAULT_TEACHING_PERIOD.toString())));
    }
    
    @Test
    @Transactional
    public void getPaper() throws Exception {
        // Initialize the database
        paperRepository.saveAndFlush(paper);

        // Get the paper
        restPaperMockMvc.perform(get("/api/papers/{id}", paper.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(paper.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.teachingPeriod").value(DEFAULT_TEACHING_PERIOD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaper() throws Exception {
        // Get the paper
        restPaperMockMvc.perform(get("/api/papers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaper() throws Exception {
        // Initialize the database
        paperRepository.saveAndFlush(paper);

        int databaseSizeBeforeUpdate = paperRepository.findAll().size();

        // Update the paper
        Paper updatedPaper = paperRepository.findById(paper.getId()).get();
        // Disconnect from session so that the updates on updatedPaper are not directly saved in db
        em.detach(updatedPaper);
        updatedPaper
            .code(UPDATED_CODE)
            .year(UPDATED_YEAR)
            .points(UPDATED_POINTS)
            .teachingPeriod(UPDATED_TEACHING_PERIOD);

        restPaperMockMvc.perform(put("/api/papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPaper)))
            .andExpect(status().isOk());

        // Validate the Paper in the database
        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeUpdate);
        Paper testPaper = paperList.get(paperList.size() - 1);
        assertThat(testPaper.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPaper.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testPaper.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPaper.getTeachingPeriod()).isEqualTo(UPDATED_TEACHING_PERIOD);
    }

    @Test
    @Transactional
    public void updateNonExistingPaper() throws Exception {
        int databaseSizeBeforeUpdate = paperRepository.findAll().size();

        // Create the Paper

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaperMockMvc.perform(put("/api/papers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(paper)))
            .andExpect(status().isBadRequest());

        // Validate the Paper in the database
        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePaper() throws Exception {
        // Initialize the database
        paperRepository.saveAndFlush(paper);

        int databaseSizeBeforeDelete = paperRepository.findAll().size();

        // Delete the paper
        restPaperMockMvc.perform(delete("/api/papers/{id}", paper.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Paper> paperList = paperRepository.findAll();
        assertThat(paperList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Paper.class);
        Paper paper1 = new Paper();
        paper1.setId(1L);
        Paper paper2 = new Paper();
        paper2.setId(paper1.getId());
        assertThat(paper1).isEqualTo(paper2);
        paper2.setId(2L);
        assertThat(paper1).isNotEqualTo(paper2);
        paper1.setId(null);
        assertThat(paper1).isNotEqualTo(paper2);
    }
}
