package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.Campus;
import co.tpg.catalog.domain.EducationalInstituition;
import co.tpg.catalog.repository.CampusRepository;
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
 * Integration tests for the {@link CampusResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class CampusResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CampusRepository campusRepository;

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

    private MockMvc restCampusMockMvc;

    private Campus campus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CampusResource campusResource = new CampusResource(campusRepository);
        this.restCampusMockMvc = MockMvcBuilders.standaloneSetup(campusResource)
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
    public static Campus createEntity(EntityManager em) {
        Campus campus = new Campus()
            .guid(DEFAULT_GUID)
            .name(DEFAULT_NAME);
        // Add required entity
        EducationalInstituition educationalInstituition;
        if (TestUtil.findAll(em, EducationalInstituition.class).isEmpty()) {
            educationalInstituition = EducationalInstituitionResourceIT.createEntity(em);
            em.persist(educationalInstituition);
            em.flush();
        } else {
            educationalInstituition = TestUtil.findAll(em, EducationalInstituition.class).get(0);
        }
        campus.setEducationalInstitution(educationalInstituition);
        return campus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Campus createUpdatedEntity(EntityManager em) {
        Campus campus = new Campus()
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME);
        // Add required entity
        EducationalInstituition educationalInstituition;
        if (TestUtil.findAll(em, EducationalInstituition.class).isEmpty()) {
            educationalInstituition = EducationalInstituitionResourceIT.createUpdatedEntity(em);
            em.persist(educationalInstituition);
            em.flush();
        } else {
            educationalInstituition = TestUtil.findAll(em, EducationalInstituition.class).get(0);
        }
        campus.setEducationalInstitution(educationalInstituition);
        return campus;
    }

    @BeforeEach
    public void initTest() {
        campus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCampus() throws Exception {
        int databaseSizeBeforeCreate = campusRepository.findAll().size();

        // Create the Campus
        restCampusMockMvc.perform(post("/api/campuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campus)))
            .andExpect(status().isCreated());

        // Validate the Campus in the database
        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeCreate + 1);
        Campus testCampus = campusList.get(campusList.size() - 1);
        assertThat(testCampus.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testCampus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCampusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = campusRepository.findAll().size();

        // Create the Campus with an existing ID
        campus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCampusMockMvc.perform(post("/api/campuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campus)))
            .andExpect(status().isBadRequest());

        // Validate the Campus in the database
        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = campusRepository.findAll().size();
        // set the field null
        campus.setGuid(null);

        // Create the Campus, which fails.

        restCampusMockMvc.perform(post("/api/campuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campus)))
            .andExpect(status().isBadRequest());

        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCampuses() throws Exception {
        // Initialize the database
        campusRepository.saveAndFlush(campus);

        // Get all the campusList
        restCampusMockMvc.perform(get("/api/campuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(campus.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getCampus() throws Exception {
        // Initialize the database
        campusRepository.saveAndFlush(campus);

        // Get the campus
        restCampusMockMvc.perform(get("/api/campuses/{id}", campus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(campus.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCampus() throws Exception {
        // Get the campus
        restCampusMockMvc.perform(get("/api/campuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCampus() throws Exception {
        // Initialize the database
        campusRepository.saveAndFlush(campus);

        int databaseSizeBeforeUpdate = campusRepository.findAll().size();

        // Update the campus
        Campus updatedCampus = campusRepository.findById(campus.getId()).get();
        // Disconnect from session so that the updates on updatedCampus are not directly saved in db
        em.detach(updatedCampus);
        updatedCampus
            .guid(UPDATED_GUID)
            .name(UPDATED_NAME);

        restCampusMockMvc.perform(put("/api/campuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCampus)))
            .andExpect(status().isOk());

        // Validate the Campus in the database
        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeUpdate);
        Campus testCampus = campusList.get(campusList.size() - 1);
        assertThat(testCampus.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testCampus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCampus() throws Exception {
        int databaseSizeBeforeUpdate = campusRepository.findAll().size();

        // Create the Campus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCampusMockMvc.perform(put("/api/campuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(campus)))
            .andExpect(status().isBadRequest());

        // Validate the Campus in the database
        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCampus() throws Exception {
        // Initialize the database
        campusRepository.saveAndFlush(campus);

        int databaseSizeBeforeDelete = campusRepository.findAll().size();

        // Delete the campus
        restCampusMockMvc.perform(delete("/api/campuses/{id}", campus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Campus> campusList = campusRepository.findAll();
        assertThat(campusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Campus.class);
        Campus campus1 = new Campus();
        campus1.setId(1L);
        Campus campus2 = new Campus();
        campus2.setId(campus1.getId());
        assertThat(campus1).isEqualTo(campus2);
        campus2.setId(2L);
        assertThat(campus1).isNotEqualTo(campus2);
        campus1.setId(null);
        assertThat(campus1).isNotEqualTo(campus2);
    }
}
