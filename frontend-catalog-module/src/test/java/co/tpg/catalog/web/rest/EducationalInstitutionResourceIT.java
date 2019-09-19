package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.EducationalInstitution;
import co.tpg.catalog.repository.EducationalInstitutionRepository;
import co.tpg.catalog.service.EducationalInstitutionService;
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
 * Integration tests for the {@Link EducationalInstitutionResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class EducationalInstitutionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EducationalInstitutionRepository educationalInstitutionRepository;

    @Autowired
    private EducationalInstitutionService educationalInstitutionService;

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

    private MockMvc restEducationalInstitutionMockMvc;

    private EducationalInstitution educationalInstitution;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EducationalInstitutionResource educationalInstitutionResource = new EducationalInstitutionResource(educationalInstitutionService);
        this.restEducationalInstitutionMockMvc = MockMvcBuilders.standaloneSetup(educationalInstitutionResource)
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
    public static EducationalInstitution createEntity(EntityManager em) {
        EducationalInstitution educationalInstitution = new EducationalInstitution()
            .name(DEFAULT_NAME);
        return educationalInstitution;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationalInstitution createUpdatedEntity(EntityManager em) {
        EducationalInstitution educationalInstitution = new EducationalInstitution()
            .name(UPDATED_NAME);
        return educationalInstitution;
    }

    @BeforeEach
    public void initTest() {
        educationalInstitution = createEntity(em);
    }

    @Test
    @Transactional
    public void createEducationalInstitution() throws Exception {
        int databaseSizeBeforeCreate = educationalInstitutionRepository.findAll().size();

        // Create the EducationalInstitution
        restEducationalInstitutionMockMvc.perform(post("/api/educational-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstitution)))
            .andExpect(status().isCreated());

        // Validate the EducationalInstitution in the database
        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeCreate + 1);
        EducationalInstitution testEducationalInstitution = educationalInstitutionList.get(educationalInstitutionList.size() - 1);
        assertThat(testEducationalInstitution.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEducationalInstitutionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = educationalInstitutionRepository.findAll().size();

        // Create the EducationalInstitution with an existing ID
        educationalInstitution.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationalInstitutionMockMvc.perform(post("/api/educational-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstitution)))
            .andExpect(status().isBadRequest());

        // Validate the EducationalInstitution in the database
        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationalInstitutionRepository.findAll().size();
        // set the field null
        educationalInstitution.setName(null);

        // Create the EducationalInstitution, which fails.

        restEducationalInstitutionMockMvc.perform(post("/api/educational-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstitution)))
            .andExpect(status().isBadRequest());

        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEducationalInstitutions() throws Exception {
        // Initialize the database
        educationalInstitutionRepository.saveAndFlush(educationalInstitution);

        // Get all the educationalInstitutionList
        restEducationalInstitutionMockMvc.perform(get("/api/educational-institutions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationalInstitution.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getEducationalInstitution() throws Exception {
        // Initialize the database
        educationalInstitutionRepository.saveAndFlush(educationalInstitution);

        // Get the educationalInstitution
        restEducationalInstitutionMockMvc.perform(get("/api/educational-institutions/{id}", educationalInstitution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(educationalInstitution.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEducationalInstitution() throws Exception {
        // Get the educationalInstitution
        restEducationalInstitutionMockMvc.perform(get("/api/educational-institutions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEducationalInstitution() throws Exception {
        // Initialize the database
        educationalInstitutionService.save(educationalInstitution);

        int databaseSizeBeforeUpdate = educationalInstitutionRepository.findAll().size();

        // Update the educationalInstitution
        EducationalInstitution updatedEducationalInstitution = educationalInstitutionRepository.findById(educationalInstitution.getId()).get();
        // Disconnect from session so that the updates on updatedEducationalInstitution are not directly saved in db
        em.detach(updatedEducationalInstitution);
        updatedEducationalInstitution
            .name(UPDATED_NAME);

        restEducationalInstitutionMockMvc.perform(put("/api/educational-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEducationalInstitution)))
            .andExpect(status().isOk());

        // Validate the EducationalInstitution in the database
        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeUpdate);
        EducationalInstitution testEducationalInstitution = educationalInstitutionList.get(educationalInstitutionList.size() - 1);
        assertThat(testEducationalInstitution.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEducationalInstitution() throws Exception {
        int databaseSizeBeforeUpdate = educationalInstitutionRepository.findAll().size();

        // Create the EducationalInstitution

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationalInstitutionMockMvc.perform(put("/api/educational-institutions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstitution)))
            .andExpect(status().isBadRequest());

        // Validate the EducationalInstitution in the database
        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEducationalInstitution() throws Exception {
        // Initialize the database
        educationalInstitutionService.save(educationalInstitution);

        int databaseSizeBeforeDelete = educationalInstitutionRepository.findAll().size();

        // Delete the educationalInstitution
        restEducationalInstitutionMockMvc.perform(delete("/api/educational-institutions/{id}", educationalInstitution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EducationalInstitution> educationalInstitutionList = educationalInstitutionRepository.findAll();
        assertThat(educationalInstitutionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationalInstitution.class);
        EducationalInstitution educationalInstitution1 = new EducationalInstitution();
        educationalInstitution1.setId(1L);
        EducationalInstitution educationalInstitution2 = new EducationalInstitution();
        educationalInstitution2.setId(educationalInstitution1.getId());
        assertThat(educationalInstitution1).isEqualTo(educationalInstitution2);
        educationalInstitution2.setId(2L);
        assertThat(educationalInstitution1).isNotEqualTo(educationalInstitution2);
        educationalInstitution1.setId(null);
        assertThat(educationalInstitution1).isNotEqualTo(educationalInstitution2);
    }
}
