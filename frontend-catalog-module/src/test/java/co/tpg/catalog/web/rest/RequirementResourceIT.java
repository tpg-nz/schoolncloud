package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.Requirement;
import co.tpg.catalog.repository.RequirementRepository;
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
 * Integration tests for the {@Link RequirementResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class RequirementResourceIT {

    private static final String DEFAULT_GUID = "AAAAAAAAAA";
    private static final String UPDATED_GUID = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    @Autowired
    private RequirementRepository requirementRepository;

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

    private MockMvc restRequirementMockMvc;

    private Requirement requirement;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RequirementResource requirementResource = new RequirementResource(requirementRepository);
        this.restRequirementMockMvc = MockMvcBuilders.standaloneSetup(requirementResource)
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
    public static Requirement createEntity(EntityManager em) {
        Requirement requirement = new Requirement()
            .guid(DEFAULT_GUID)
            .level(DEFAULT_LEVEL);
        return requirement;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requirement createUpdatedEntity(EntityManager em) {
        Requirement requirement = new Requirement()
            .guid(UPDATED_GUID)
            .level(UPDATED_LEVEL);
        return requirement;
    }

    @BeforeEach
    public void initTest() {
        requirement = createEntity(em);
    }

    @Test
    @Transactional
    public void createRequirement() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement
        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isCreated());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeCreate + 1);
        Requirement testRequirement = requirementList.get(requirementList.size() - 1);
        assertThat(testRequirement.getGuid()).isEqualTo(DEFAULT_GUID);
        assertThat(testRequirement.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createRequirementWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = requirementRepository.findAll().size();

        // Create the Requirement with an existing ID
        requirement.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkGuidIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementRepository.findAll().size();
        // set the field null
        requirement.setGuid(null);

        // Create the Requirement, which fails.

        restRequirementMockMvc.perform(post("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRequirements() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get all the requirementList
        restRequirementMockMvc.perform(get("/api/requirements?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirement.getId().intValue())))
            .andExpect(jsonPath("$.[*].guid").value(hasItem(DEFAULT_GUID.toString())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", requirement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(requirement.getId().intValue()))
            .andExpect(jsonPath("$.guid").value(DEFAULT_GUID.toString()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    public void getNonExistingRequirement() throws Exception {
        // Get the requirement
        restRequirementMockMvc.perform(get("/api/requirements/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Update the requirement
        Requirement updatedRequirement = requirementRepository.findById(requirement.getId()).get();
        // Disconnect from session so that the updates on updatedRequirement are not directly saved in db
        em.detach(updatedRequirement);
        updatedRequirement
            .guid(UPDATED_GUID)
            .level(UPDATED_LEVEL);

        restRequirementMockMvc.perform(put("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRequirement)))
            .andExpect(status().isOk());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeUpdate);
        Requirement testRequirement = requirementList.get(requirementList.size() - 1);
        assertThat(testRequirement.getGuid()).isEqualTo(UPDATED_GUID);
        assertThat(testRequirement.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingRequirement() throws Exception {
        int databaseSizeBeforeUpdate = requirementRepository.findAll().size();

        // Create the Requirement

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequirementMockMvc.perform(put("/api/requirements")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(requirement)))
            .andExpect(status().isBadRequest());

        // Validate the Requirement in the database
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRequirement() throws Exception {
        // Initialize the database
        requirementRepository.saveAndFlush(requirement);

        int databaseSizeBeforeDelete = requirementRepository.findAll().size();

        // Delete the requirement
        restRequirementMockMvc.perform(delete("/api/requirements/{id}", requirement.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requirement> requirementList = requirementRepository.findAll();
        assertThat(requirementList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Requirement.class);
        Requirement requirement1 = new Requirement();
        requirement1.setId(1L);
        Requirement requirement2 = new Requirement();
        requirement2.setId(requirement1.getId());
        assertThat(requirement1).isEqualTo(requirement2);
        requirement2.setId(2L);
        assertThat(requirement1).isNotEqualTo(requirement2);
        requirement1.setId(null);
        assertThat(requirement1).isNotEqualTo(requirement2);
    }
}
