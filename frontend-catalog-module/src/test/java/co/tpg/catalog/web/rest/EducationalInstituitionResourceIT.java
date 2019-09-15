package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.EducationalInstituition;
import co.tpg.catalog.repository.EducationalInstituitionRepository;
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
 * Integration tests for the {@link EducationalInstituitionResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class EducationalInstituitionResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private EducationalInstituitionRepository educationalInstituitionRepository;

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

    private MockMvc restEducationalInstituitionMockMvc;

    private EducationalInstituition educationalInstituition;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EducationalInstituitionResource educationalInstituitionResource = new EducationalInstituitionResource(educationalInstituitionRepository);
        this.restEducationalInstituitionMockMvc = MockMvcBuilders.standaloneSetup(educationalInstituitionResource)
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
    public static EducationalInstituition createEntity(EntityManager em) {
        EducationalInstituition educationalInstituition = new EducationalInstituition()
            .guid(DEFAULT_GUID)
            .name(DEFAULT_NAME);
        return educationalInstituition;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationalInstituition createUpdatedEntity(EntityManager em) {
        EducationalInstituition educationalInstituition = new EducationalInstituition()
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME);
        return educationalInstituition;
    }

    @BeforeEach
    public void initTest() {
        educationalInstituition = createEntity(em);
    }

    @Test
    @Transactional
    public void createEducationalInstituition() throws Exception {
        int databaseSizeBeforeCreate = educationalInstituitionRepository.findAll().size();

        // Create the EducationalInstituition
        restEducationalInstituitionMockMvc.perform(post("/api/educational-instituitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstituition)))
            .andExpect(status().isCreated());

        // Validate the EducationalInstituition in the database
        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeCreate + 1);
        EducationalInstituition testEducationalInstituition = educationalInstituitionList.get(educationalInstituitionList.size() - 1);
        assertThat(testEducationalInstituition.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testEducationalInstituition.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createEducationalInstituitionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = educationalInstituitionRepository.findAll().size();

        // Create the EducationalInstituition with an existing ID
        educationalInstituition.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationalInstituitionMockMvc.perform(post("/api/educational-instituitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstituition)))
            .andExpect(status().isBadRequest());

        // Validate the EducationalInstituition in the database
        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationalInstituitionRepository.findAll().size();
        // set the field null
        educationalInstituition.setGuid(null);

        // Create the EducationalInstituition, which fails.

        restEducationalInstituitionMockMvc.perform(post("/api/educational-instituitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstituition)))
            .andExpect(status().isBadRequest());

        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEducationalInstituitions() throws Exception {
        // Initialize the database
        educationalInstituitionRepository.saveAndFlush(educationalInstituition);

        // Get all the educationalInstituitionList
        restEducationalInstituitionMockMvc.perform(get("/api/educational-instituitions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationalInstituition.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getEducationalInstituition() throws Exception {
        // Initialize the database
        educationalInstituitionRepository.saveAndFlush(educationalInstituition);

        // Get the educationalInstituition
        restEducationalInstituitionMockMvc.perform(get("/api/educational-instituitions/{id}", educationalInstituition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(educationalInstituition.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEducationalInstituition() throws Exception {
        // Get the educationalInstituition
        restEducationalInstituitionMockMvc.perform(get("/api/educational-instituitions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEducationalInstituition() throws Exception {
        // Initialize the database
        educationalInstituitionRepository.saveAndFlush(educationalInstituition);

        int databaseSizeBeforeUpdate = educationalInstituitionRepository.findAll().size();

        // Update the educationalInstituition
        EducationalInstituition updatedEducationalInstituition = educationalInstituitionRepository.findById(educationalInstituition.getId()).get();
        // Disconnect from session so that the updates on updatedEducationalInstituition are not directly saved in db
        em.detach(updatedEducationalInstituition);
        updatedEducationalInstituition
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME);

        restEducationalInstituitionMockMvc.perform(put("/api/educational-instituitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEducationalInstituition)))
            .andExpect(status().isOk());

        // Validate the EducationalInstituition in the database
        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeUpdate);
        EducationalInstituition testEducationalInstituition = educationalInstituitionList.get(educationalInstituitionList.size() - 1);
        assertThat(testEducationalInstituition.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testEducationalInstituition.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEducationalInstituition() throws Exception {
        int databaseSizeBeforeUpdate = educationalInstituitionRepository.findAll().size();

        // Create the EducationalInstituition

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationalInstituitionMockMvc.perform(put("/api/educational-instituitions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(educationalInstituition)))
            .andExpect(status().isBadRequest());

        // Validate the EducationalInstituition in the database
        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEducationalInstituition() throws Exception {
        // Initialize the database
        educationalInstituitionRepository.saveAndFlush(educationalInstituition);

        int databaseSizeBeforeDelete = educationalInstituitionRepository.findAll().size();

        // Delete the educationalInstituition
        restEducationalInstituitionMockMvc.perform(delete("/api/educational-instituitions/{id}", educationalInstituition.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EducationalInstituition> educationalInstituitionList = educationalInstituitionRepository.findAll();
        assertThat(educationalInstituitionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EducationalInstituition.class);
        EducationalInstituition educationalInstituition1 = new EducationalInstituition();
        educationalInstituition1.setId(1L);
        EducationalInstituition educationalInstituition2 = new EducationalInstituition();
        educationalInstituition2.setId(educationalInstituition1.getId());
        assertThat(educationalInstituition1).isEqualTo(educationalInstituition2);
        educationalInstituition2.setId(2L);
        assertThat(educationalInstituition1).isNotEqualTo(educationalInstituition2);
        educationalInstituition1.setId(null);
        assertThat(educationalInstituition1).isNotEqualTo(educationalInstituition2);
    }
}
