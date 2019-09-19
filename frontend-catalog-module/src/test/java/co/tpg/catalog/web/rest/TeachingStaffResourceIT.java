package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.TeachingStaff;
import co.tpg.catalog.domain.Paper;
import co.tpg.catalog.repository.TeachingStaffRepository;
import co.tpg.catalog.service.TeachingStaffService;
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

import co.tpg.catalog.domain.enumeration.GraduationType;
/**
 * Integration tests for the {@Link TeachingStaffResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class TeachingStaffResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final GraduationType DEFAULT_GRADUATION_TYPE = GraduationType.POST_GRADUATE;
    private static final GraduationType UPDATED_GRADUATION_TYPE = GraduationType.MASTER_DEGREE;

    @Autowired
    private TeachingStaffRepository teachingStaffRepository;

    @Autowired
    private TeachingStaffService teachingStaffService;

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

    private MockMvc restTeachingStaffMockMvc;

    private TeachingStaff teachingStaff;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TeachingStaffResource teachingStaffResource = new TeachingStaffResource(teachingStaffService);
        this.restTeachingStaffMockMvc = MockMvcBuilders.standaloneSetup(teachingStaffResource)
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
    public static TeachingStaff createEntity(EntityManager em) {
        TeachingStaff teachingStaff = new TeachingStaff()
            .name(DEFAULT_NAME)
            .graduationType(DEFAULT_GRADUATION_TYPE);
        // Add required entity
        Paper paper;
        if (TestUtil.findAll(em, Paper.class).isEmpty()) {
            paper = PaperResourceIT.createEntity(em);
            em.persist(paper);
            em.flush();
        } else {
            paper = TestUtil.findAll(em, Paper.class).get(0);
        }
        teachingStaff.setPaper(paper);
        return teachingStaff;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TeachingStaff createUpdatedEntity(EntityManager em) {
        TeachingStaff teachingStaff = new TeachingStaff()
            .name(UPDATED_NAME)
            .graduationType(UPDATED_GRADUATION_TYPE);
        // Add required entity
        Paper paper;
        if (TestUtil.findAll(em, Paper.class).isEmpty()) {
            paper = PaperResourceIT.createUpdatedEntity(em);
            em.persist(paper);
            em.flush();
        } else {
            paper = TestUtil.findAll(em, Paper.class).get(0);
        }
        teachingStaff.setPaper(paper);
        return teachingStaff;
    }

    @BeforeEach
    public void initTest() {
        teachingStaff = createEntity(em);
    }

    @Test
    @Transactional
    public void createTeachingStaff() throws Exception {
        int databaseSizeBeforeCreate = teachingStaffRepository.findAll().size();

        // Create the TeachingStaff
        restTeachingStaffMockMvc.perform(post("/api/teaching-staffs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingStaff)))
            .andExpect(status().isCreated());

        // Validate the TeachingStaff in the database
        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeCreate + 1);
        TeachingStaff testTeachingStaff = teachingStaffList.get(teachingStaffList.size() - 1);
        assertThat(testTeachingStaff.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTeachingStaff.getGraduationType()).isEqualTo(DEFAULT_GRADUATION_TYPE);
    }

    @Test
    @Transactional
    public void createTeachingStaffWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = teachingStaffRepository.findAll().size();

        // Create the TeachingStaff with an existing ID
        teachingStaff.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTeachingStaffMockMvc.perform(post("/api/teaching-staffs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingStaff)))
            .andExpect(status().isBadRequest());

        // Validate the TeachingStaff in the database
        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = teachingStaffRepository.findAll().size();
        // set the field null
        teachingStaff.setName(null);

        // Create the TeachingStaff, which fails.

        restTeachingStaffMockMvc.perform(post("/api/teaching-staffs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingStaff)))
            .andExpect(status().isBadRequest());

        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTeachingStaffs() throws Exception {
        // Initialize the database
        teachingStaffRepository.saveAndFlush(teachingStaff);

        // Get all the teachingStaffList
        restTeachingStaffMockMvc.perform(get("/api/teaching-staffs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(teachingStaff.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].graduationType").value(hasItem(DEFAULT_GRADUATION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getTeachingStaff() throws Exception {
        // Initialize the database
        teachingStaffRepository.saveAndFlush(teachingStaff);

        // Get the teachingStaff
        restTeachingStaffMockMvc.perform(get("/api/teaching-staffs/{id}", teachingStaff.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(teachingStaff.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.graduationType").value(DEFAULT_GRADUATION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTeachingStaff() throws Exception {
        // Get the teachingStaff
        restTeachingStaffMockMvc.perform(get("/api/teaching-staffs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTeachingStaff() throws Exception {
        // Initialize the database
        teachingStaffService.save(teachingStaff);

        int databaseSizeBeforeUpdate = teachingStaffRepository.findAll().size();

        // Update the teachingStaff
        TeachingStaff updatedTeachingStaff = teachingStaffRepository.findById(teachingStaff.getId()).get();
        // Disconnect from session so that the updates on updatedTeachingStaff are not directly saved in db
        em.detach(updatedTeachingStaff);
        updatedTeachingStaff
            .name(UPDATED_NAME)
            .graduationType(UPDATED_GRADUATION_TYPE);

        restTeachingStaffMockMvc.perform(put("/api/teaching-staffs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTeachingStaff)))
            .andExpect(status().isOk());

        // Validate the TeachingStaff in the database
        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeUpdate);
        TeachingStaff testTeachingStaff = teachingStaffList.get(teachingStaffList.size() - 1);
        assertThat(testTeachingStaff.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTeachingStaff.getGraduationType()).isEqualTo(UPDATED_GRADUATION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingTeachingStaff() throws Exception {
        int databaseSizeBeforeUpdate = teachingStaffRepository.findAll().size();

        // Create the TeachingStaff

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTeachingStaffMockMvc.perform(put("/api/teaching-staffs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(teachingStaff)))
            .andExpect(status().isBadRequest());

        // Validate the TeachingStaff in the database
        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTeachingStaff() throws Exception {
        // Initialize the database
        teachingStaffService.save(teachingStaff);

        int databaseSizeBeforeDelete = teachingStaffRepository.findAll().size();

        // Delete the teachingStaff
        restTeachingStaffMockMvc.perform(delete("/api/teaching-staffs/{id}", teachingStaff.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TeachingStaff> teachingStaffList = teachingStaffRepository.findAll();
        assertThat(teachingStaffList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TeachingStaff.class);
        TeachingStaff teachingStaff1 = new TeachingStaff();
        teachingStaff1.setId(1L);
        TeachingStaff teachingStaff2 = new TeachingStaff();
        teachingStaff2.setId(teachingStaff1.getId());
        assertThat(teachingStaff1).isEqualTo(teachingStaff2);
        teachingStaff2.setId(2L);
        assertThat(teachingStaff1).isNotEqualTo(teachingStaff2);
        teachingStaff1.setId(null);
        assertThat(teachingStaff1).isNotEqualTo(teachingStaff2);
    }
}
