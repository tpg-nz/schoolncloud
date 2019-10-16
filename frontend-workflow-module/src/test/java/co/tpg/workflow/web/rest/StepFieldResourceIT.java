package co.tpg.workflow.web.rest;

import co.tpg.workflow.WorkflowApp;
import co.tpg.workflow.domain.StepField;
import co.tpg.workflow.domain.Step;
import co.tpg.workflow.repository.StepFieldRepository;
import co.tpg.workflow.service.StepFieldService;
import co.tpg.workflow.web.rest.errors.ExceptionTranslator;

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

import static co.tpg.workflow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.tpg.workflow.domain.enumeration.FieldType;
/**
 * Integration tests for the {@Link StepFieldResource} REST controller.
 */
@SpringBootTest(classes = WorkflowApp.class)
public class StepFieldResourceIT {

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final FieldType DEFAULT_FIELD_TYPE = FieldType.TEXT_FIELD;
    private static final FieldType UPDATED_FIELD_TYPE = FieldType.TEXT_AREA;

    @Autowired
    private StepFieldRepository stepFieldRepository;

    @Autowired
    private StepFieldService stepFieldService;

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

    private MockMvc restStepFieldMockMvc;

    private StepField stepField;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StepFieldResource stepFieldResource = new StepFieldResource(stepFieldService);
        this.restStepFieldMockMvc = MockMvcBuilders.standaloneSetup(stepFieldResource)
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
    public static StepField createEntity(EntityManager em) {
        StepField stepField = new StepField()
            .sequence(DEFAULT_SEQUENCE)
            .label(DEFAULT_LABEL)
            .fieldType(DEFAULT_FIELD_TYPE);
        // Add required entity
        Step step;
        if (TestUtil.findAll(em, Step.class).isEmpty()) {
            step = StepResourceIT.createEntity(em);
            em.persist(step);
            em.flush();
        } else {
            step = TestUtil.findAll(em, Step.class).get(0);
        }
        stepField.setStep(step);
        return stepField;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StepField createUpdatedEntity(EntityManager em) {
        StepField stepField = new StepField()
            .sequence(UPDATED_SEQUENCE)
            .label(UPDATED_LABEL)
            .fieldType(UPDATED_FIELD_TYPE);
        // Add required entity
        Step step;
        if (TestUtil.findAll(em, Step.class).isEmpty()) {
            step = StepResourceIT.createUpdatedEntity(em);
            em.persist(step);
            em.flush();
        } else {
            step = TestUtil.findAll(em, Step.class).get(0);
        }
        stepField.setStep(step);
        return stepField;
    }

    @BeforeEach
    public void initTest() {
        stepField = createEntity(em);
    }

    @Test
    @Transactional
    public void createStepField() throws Exception {
        int databaseSizeBeforeCreate = stepFieldRepository.findAll().size();

        // Create the StepField
        restStepFieldMockMvc.perform(post("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stepField)))
            .andExpect(status().isCreated());

        // Validate the StepField in the database
        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeCreate + 1);
        StepField testStepField = stepFieldList.get(stepFieldList.size() - 1);
        assertThat(testStepField.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testStepField.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testStepField.getFieldType()).isEqualTo(DEFAULT_FIELD_TYPE);
    }

    @Test
    @Transactional
    public void createStepFieldWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stepFieldRepository.findAll().size();

        // Create the StepField with an existing ID
        stepField.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStepFieldMockMvc.perform(post("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stepField)))
            .andExpect(status().isBadRequest());

        // Validate the StepField in the database
        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSequenceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepFieldRepository.findAll().size();
        // set the field null
        stepField.setSequence(null);

        // Create the StepField, which fails.

        restStepFieldMockMvc.perform(post("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stepField)))
            .andExpect(status().isBadRequest());

        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = stepFieldRepository.findAll().size();
        // set the field null
        stepField.setLabel(null);

        // Create the StepField, which fails.

        restStepFieldMockMvc.perform(post("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stepField)))
            .andExpect(status().isBadRequest());

        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStepFields() throws Exception {
        // Initialize the database
        stepFieldRepository.saveAndFlush(stepField);

        // Get all the stepFieldList
        restStepFieldMockMvc.perform(get("/api/step-fields?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stepField.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL.toString())))
            .andExpect(jsonPath("$.[*].fieldType").value(hasItem(DEFAULT_FIELD_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getStepField() throws Exception {
        // Initialize the database
        stepFieldRepository.saveAndFlush(stepField);

        // Get the stepField
        restStepFieldMockMvc.perform(get("/api/step-fields/{id}", stepField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stepField.getId().intValue()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL.toString()))
            .andExpect(jsonPath("$.fieldType").value(DEFAULT_FIELD_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStepField() throws Exception {
        // Get the stepField
        restStepFieldMockMvc.perform(get("/api/step-fields/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStepField() throws Exception {
        // Initialize the database
        stepFieldService.save(stepField);

        int databaseSizeBeforeUpdate = stepFieldRepository.findAll().size();

        // Update the stepField
        StepField updatedStepField = stepFieldRepository.findById(stepField.getId()).get();
        // Disconnect from session so that the updates on updatedStepField are not directly saved in db
        em.detach(updatedStepField);
        updatedStepField
            .sequence(UPDATED_SEQUENCE)
            .label(UPDATED_LABEL)
            .fieldType(UPDATED_FIELD_TYPE);

        restStepFieldMockMvc.perform(put("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStepField)))
            .andExpect(status().isOk());

        // Validate the StepField in the database
        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeUpdate);
        StepField testStepField = stepFieldList.get(stepFieldList.size() - 1);
        assertThat(testStepField.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testStepField.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testStepField.getFieldType()).isEqualTo(UPDATED_FIELD_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingStepField() throws Exception {
        int databaseSizeBeforeUpdate = stepFieldRepository.findAll().size();

        // Create the StepField

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStepFieldMockMvc.perform(put("/api/step-fields")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stepField)))
            .andExpect(status().isBadRequest());

        // Validate the StepField in the database
        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStepField() throws Exception {
        // Initialize the database
        stepFieldService.save(stepField);

        int databaseSizeBeforeDelete = stepFieldRepository.findAll().size();

        // Delete the stepField
        restStepFieldMockMvc.perform(delete("/api/step-fields/{id}", stepField.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StepField> stepFieldList = stepFieldRepository.findAll();
        assertThat(stepFieldList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StepField.class);
        StepField stepField1 = new StepField();
        stepField1.setId(1L);
        StepField stepField2 = new StepField();
        stepField2.setId(stepField1.getId());
        assertThat(stepField1).isEqualTo(stepField2);
        stepField2.setId(2L);
        assertThat(stepField1).isNotEqualTo(stepField2);
        stepField1.setId(null);
        assertThat(stepField1).isNotEqualTo(stepField2);
    }
}
