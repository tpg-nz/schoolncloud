package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.TeachingClass;
import co.tpg.catalog.repository.TeachingClassRepository;
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
 * Integration tests for the {@Link TeachingClassResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class TeachingClassResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Integer DEFAULT_SEMESTER = 1;
    private static final Integer UPDATED_SEMESTER = 2;

    @Autowired
    private TeachingClassRepository teachingClassRepository;

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

    private MockMvc restTeachingClassMockMvc;

    private TeachingClass teachingClass;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeachingClassResource teachingClassResource = new TeachingClassResource(teachingClassRepository);
        this.restTeachingClassMockMvc = MockMvcBuilders.standaloneSetup(teachingClassResource)
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
    public static TeachingClass createEntity(EntityManager em) {
        TeachingClass teachingClass = new TeachingClass()
            .guid(DEFAULT_GUID)
            .code(DEFAULT_CODE)
            .year(DEFAULT_YEAR)
            .semester(DEFAULT_SEMESTER);
        return teachingClass;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingClass createUpdatedEntity(EntityManager em) {
        TeachingClass teachingClass = new TeachingClass()
            .guid(UPDATED_GUID)
            .code(UPDATED_CODE)
            .year(UPDATED_YEAR)
            .semester(UPDATED_SEMESTER);
        return teachingClass;
    }

    @BeforeEach
    public void initTest() {
        teachingClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeachingClass() throws Exception {
        int databaseSizeBeforeCreate = teachingClassRepository.findAll().size();

        // Create the TeachingClass
        restTeachingClassMockMvc.perform(post("/api/teaching-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingClass)))
            .andExpect(status().isCreated());

        // Validate the TeachingClass in the database
        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeCreate + 1);
        TeachingClass testTeachingClass = teachingClassList.get(teachingClassList.size() - 1);
        assertThat(testTeachingClass.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testTeachingClass.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTeachingClass.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testTeachingClass.getSemester()).isEqualTo(DEFAULT_SEMESTER);
    }

    @Test
    @Transactional
    public void createTeachingClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teachingClassRepository.findAll().size();

        // Create the TeachingClass with an existing ID
        teachingClass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachingClassMockMvc.perform(post("/api/teaching-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingClass)))
            .andExpect(status().isBadRequest());

        // Validate the TeachingClass in the database
        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = teachingClassRepository.findAll().size();
        // set the field null
        teachingClass.setGuid(null);

        // Create the TeachingClass, which fails.

        restTeachingClassMockMvc.perform(post("/api/teaching-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingClass)))
            .andExpect(status().isBadRequest());

        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeachingClasses() throws Exception {
        // Initialize the database
        teachingClassRepository.saveAndFlush(teachingClass);

        // Get all the teachingClassList
        restTeachingClassMockMvc.perform(get("/api/teaching-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teachingClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].semester").value(hasItem(DEFAULT_SEMESTER)));
    }
    
    @Test
    @Transactional
    public void getTeachingClass() throws Exception {
        // Initialize the database
        teachingClassRepository.saveAndFlush(teachingClass);

        // Get the teachingClass
        restTeachingClassMockMvc.perform(get("/api/teaching-classes/{id}", teachingClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teachingClass.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.semester").value(DEFAULT_SEMESTER));
    }

    @Test
    @Transactional
    public void getNonExistingTeachingClass() throws Exception {
        // Get the teachingClass
        restTeachingClassMockMvc.perform(get("/api/teaching-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeachingClass() throws Exception {
        // Initialize the database
        teachingClassRepository.saveAndFlush(teachingClass);

        int databaseSizeBeforeUpdate = teachingClassRepository.findAll().size();

        // Update the teachingClass
        TeachingClass updatedTeachingClass = teachingClassRepository.findById(teachingClass.getId()).get();
        // Disconnect from session so that the updates on updatedTeachingClass are not directly saved in db
        em.detach(updatedTeachingClass);
        updatedTeachingClass
            .guid(UPDATED_GUID)
            .code(UPDATED_CODE)
            .year(UPDATED_YEAR)
            .semester(UPDATED_SEMESTER);

        restTeachingClassMockMvc.perform(put("/api/teaching-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeachingClass)))
            .andExpect(status().isOk());

        // Validate the TeachingClass in the database
        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeUpdate);
        TeachingClass testTeachingClass = teachingClassList.get(teachingClassList.size() - 1);
        assertThat(testTeachingClass.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testTeachingClass.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTeachingClass.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testTeachingClass.getSemester()).isEqualTo(UPDATED_SEMESTER);
    }

    @Test
    @Transactional
    public void updateNonExistingTeachingClass() throws Exception {
        int databaseSizeBeforeUpdate = teachingClassRepository.findAll().size();

        // Create the TeachingClass

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingClassMockMvc.perform(put("/api/teaching-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingClass)))
            .andExpect(status().isBadRequest());

        // Validate the TeachingClass in the database
        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeachingClass() throws Exception {
        // Initialize the database
        teachingClassRepository.saveAndFlush(teachingClass);

        int databaseSizeBeforeDelete = teachingClassRepository.findAll().size();

        // Delete the teachingClass
        restTeachingClassMockMvc.perform(delete("/api/teaching-classes/{id}", teachingClass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeachingClass> teachingClassList = teachingClassRepository.findAll();
        assertThat(teachingClassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingClass.class);
        TeachingClass teachingClass1 = new TeachingClass();
        teachingClass1.setId(1L);
        TeachingClass teachingClass2 = new TeachingClass();
        teachingClass2.setId(teachingClass1.getId());
        assertThat(teachingClass1).isEqualTo(teachingClass2);
        teachingClass2.setId(2L);
        assertThat(teachingClass1).isNotEqualTo(teachingClass2);
        teachingClass1.setId(null);
        assertThat(teachingClass1).isNotEqualTo(teachingClass2);
    }
}
