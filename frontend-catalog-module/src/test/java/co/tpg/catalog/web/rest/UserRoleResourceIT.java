package co.tpg.catalog.web.rest;

import co.tpg.catalog.CatalogApp;
import co.tpg.catalog.domain.UserRole;
import co.tpg.catalog.repository.UserRoleRepository;
import co.tpg.catalog.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static co.tpg.catalog.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link UserRoleResource} REST controller.
 */
@SpringBootTest(classes = CatalogApp.class)
public class UserRoleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserRoleRepository userRoleRepositoryMock;

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

    private MockMvc restUserRoleMockMvc;

    private UserRole userRole;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserRoleResource userRoleResource = new UserRoleResource(userRoleRepository);
        this.restUserRoleMockMvc = MockMvcBuilders.standaloneSetup(userRoleResource)
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
    public static UserRole createEntity(EntityManager em) {
        UserRole userRole = new UserRole()
            .name(DEFAULT_NAME);
        return userRole;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRole createUpdatedEntity(EntityManager em) {
        UserRole userRole = new UserRole()
            .name(UPDATED_NAME);
        return userRole;
    }

    @BeforeEach
    public void initTest() {
        userRole = createEntity(em);
    }

    @Test
    @Transactional
    public void createUserRole() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // Create the UserRole
        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isCreated());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate + 1);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createUserRoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userRoleRepository.findAll().size();

        // Create the UserRole with an existing ID
        userRole.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = userRoleRepository.findAll().size();
        // set the field null
        userRole.setName(null);

        // Create the UserRole, which fails.

        restUserRoleMockMvc.perform(post("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isBadRequest());

        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllUserRoles() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get all the userRoleList
        restUserRoleMockMvc.perform(get("/api/user-roles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRole.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllUserRolesWithEagerRelationshipsIsEnabled() throws Exception {
        UserRoleResource userRoleResource = new UserRoleResource(userRoleRepositoryMock);
        when(userRoleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restUserRoleMockMvc = MockMvcBuilders.standaloneSetup(userRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserRoleMockMvc.perform(get("/api/user-roles?eagerload=true"))
        .andExpect(status().isOk());

        verify(userRoleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllUserRolesWithEagerRelationshipsIsNotEnabled() throws Exception {
        UserRoleResource userRoleResource = new UserRoleResource(userRoleRepositoryMock);
            when(userRoleRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restUserRoleMockMvc = MockMvcBuilders.standaloneSetup(userRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restUserRoleMockMvc.perform(get("/api/user-roles?eagerload=true"))
        .andExpect(status().isOk());

            verify(userRoleRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/user-roles/{id}", userRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userRole.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserRole() throws Exception {
        // Get the userRole
        restUserRoleMockMvc.perform(get("/api/user-roles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Update the userRole
        UserRole updatedUserRole = userRoleRepository.findById(userRole.getId()).get();
        // Disconnect from session so that the updates on updatedUserRole are not directly saved in db
        em.detach(updatedUserRole);
        updatedUserRole
            .name(UPDATED_NAME);

        restUserRoleMockMvc.perform(put("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUserRole)))
            .andExpect(status().isOk());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
        UserRole testUserRole = userRoleList.get(userRoleList.size() - 1);
        assertThat(testUserRole.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingUserRole() throws Exception {
        int databaseSizeBeforeUpdate = userRoleRepository.findAll().size();

        // Create the UserRole

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRoleMockMvc.perform(put("/api/user-roles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userRole)))
            .andExpect(status().isBadRequest());

        // Validate the UserRole in the database
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserRole() throws Exception {
        // Initialize the database
        userRoleRepository.saveAndFlush(userRole);

        int databaseSizeBeforeDelete = userRoleRepository.findAll().size();

        // Delete the userRole
        restUserRoleMockMvc.perform(delete("/api/user-roles/{id}", userRole.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserRole> userRoleList = userRoleRepository.findAll();
        assertThat(userRoleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserRole.class);
        UserRole userRole1 = new UserRole();
        userRole1.setId(1L);
        UserRole userRole2 = new UserRole();
        userRole2.setId(userRole1.getId());
        assertThat(userRole1).isEqualTo(userRole2);
        userRole2.setId(2L);
        assertThat(userRole1).isNotEqualTo(userRole2);
        userRole1.setId(null);
        assertThat(userRole1).isNotEqualTo(userRole2);
    }
}
