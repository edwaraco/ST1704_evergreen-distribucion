package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.CanalComercializacionAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.CanalComercializacion;
import com.evergreen.distribucion.repository.CanalComercializacionRepository;
import com.evergreen.distribucion.service.dto.CanalComercializacionDTO;
import com.evergreen.distribucion.service.mapper.CanalComercializacionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CanalComercializacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CanalComercializacionResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVO = false;
    private static final Boolean UPDATED_ACTIVO = true;

    private static final String ENTITY_API_URL = "/api/canal-comercializacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CanalComercializacionRepository canalComercializacionRepository;

    @Autowired
    private CanalComercializacionMapper canalComercializacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCanalComercializacionMockMvc;

    private CanalComercializacion canalComercializacion;

    private CanalComercializacion insertedCanalComercializacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanalComercializacion createEntity() {
        return new CanalComercializacion()
            .identificador(DEFAULT_IDENTIFICADOR)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .activo(DEFAULT_ACTIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CanalComercializacion createUpdatedEntity() {
        return new CanalComercializacion()
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO);
    }

    @BeforeEach
    void initTest() {
        canalComercializacion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCanalComercializacion != null) {
            canalComercializacionRepository.delete(insertedCanalComercializacion);
            insertedCanalComercializacion = null;
        }
    }

    @Test
    @Transactional
    void createCanalComercializacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);
        var returnedCanalComercializacionDTO = om.readValue(
            restCanalComercializacionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CanalComercializacionDTO.class
        );

        // Validate the CanalComercializacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCanalComercializacion = canalComercializacionMapper.toEntity(returnedCanalComercializacionDTO);
        assertCanalComercializacionUpdatableFieldsEquals(
            returnedCanalComercializacion,
            getPersistedCanalComercializacion(returnedCanalComercializacion)
        );

        insertedCanalComercializacion = returnedCanalComercializacion;
    }

    @Test
    @Transactional
    void createCanalComercializacionWithExistingId() throws Exception {
        // Create the CanalComercializacion with an existing ID
        canalComercializacion.setId(1L);
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCanalComercializacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalComercializacion.setIdentificador(null);

        // Create the CanalComercializacion, which fails.
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        restCanalComercializacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalComercializacion.setNombre(null);

        // Create the CanalComercializacion, which fails.
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        restCanalComercializacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        canalComercializacion.setActivo(null);

        // Create the CanalComercializacion, which fails.
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        restCanalComercializacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCanalComercializacions() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        // Get all the canalComercializacionList
        restCanalComercializacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(canalComercializacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].activo").value(hasItem(DEFAULT_ACTIVO)));
    }

    @Test
    @Transactional
    void getCanalComercializacion() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        // Get the canalComercializacion
        restCanalComercializacionMockMvc
            .perform(get(ENTITY_API_URL_ID, canalComercializacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(canalComercializacion.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO));
    }

    @Test
    @Transactional
    void getNonExistingCanalComercializacion() throws Exception {
        // Get the canalComercializacion
        restCanalComercializacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCanalComercializacion() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalComercializacion
        CanalComercializacion updatedCanalComercializacion = canalComercializacionRepository
            .findById(canalComercializacion.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCanalComercializacion are not directly saved in db
        em.detach(updatedCanalComercializacion);
        updatedCanalComercializacion
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO);
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(updatedCanalComercializacion);

        restCanalComercializacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canalComercializacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCanalComercializacionToMatchAllProperties(updatedCanalComercializacion);
    }

    @Test
    @Transactional
    void putNonExistingCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, canalComercializacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(canalComercializacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCanalComercializacionWithPatch() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalComercializacion using partial update
        CanalComercializacion partialUpdatedCanalComercializacion = new CanalComercializacion();
        partialUpdatedCanalComercializacion.setId(canalComercializacion.getId());

        restCanalComercializacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanalComercializacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanalComercializacion))
            )
            .andExpect(status().isOk());

        // Validate the CanalComercializacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanalComercializacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCanalComercializacion, canalComercializacion),
            getPersistedCanalComercializacion(canalComercializacion)
        );
    }

    @Test
    @Transactional
    void fullUpdateCanalComercializacionWithPatch() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the canalComercializacion using partial update
        CanalComercializacion partialUpdatedCanalComercializacion = new CanalComercializacion();
        partialUpdatedCanalComercializacion.setId(canalComercializacion.getId());

        partialUpdatedCanalComercializacion
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .activo(UPDATED_ACTIVO);

        restCanalComercializacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCanalComercializacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCanalComercializacion))
            )
            .andExpect(status().isOk());

        // Validate the CanalComercializacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCanalComercializacionUpdatableFieldsEquals(
            partialUpdatedCanalComercializacion,
            getPersistedCanalComercializacion(partialUpdatedCanalComercializacion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, canalComercializacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCanalComercializacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        canalComercializacion.setId(longCount.incrementAndGet());

        // Create the CanalComercializacion
        CanalComercializacionDTO canalComercializacionDTO = canalComercializacionMapper.toDto(canalComercializacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCanalComercializacionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(canalComercializacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CanalComercializacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCanalComercializacion() throws Exception {
        // Initialize the database
        insertedCanalComercializacion = canalComercializacionRepository.saveAndFlush(canalComercializacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the canalComercializacion
        restCanalComercializacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, canalComercializacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return canalComercializacionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CanalComercializacion getPersistedCanalComercializacion(CanalComercializacion canalComercializacion) {
        return canalComercializacionRepository.findById(canalComercializacion.getId()).orElseThrow();
    }

    protected void assertPersistedCanalComercializacionToMatchAllProperties(CanalComercializacion expectedCanalComercializacion) {
        assertCanalComercializacionAllPropertiesEquals(
            expectedCanalComercializacion,
            getPersistedCanalComercializacion(expectedCanalComercializacion)
        );
    }

    protected void assertPersistedCanalComercializacionToMatchUpdatableProperties(CanalComercializacion expectedCanalComercializacion) {
        assertCanalComercializacionAllUpdatablePropertiesEquals(
            expectedCanalComercializacion,
            getPersistedCanalComercializacion(expectedCanalComercializacion)
        );
    }
}
