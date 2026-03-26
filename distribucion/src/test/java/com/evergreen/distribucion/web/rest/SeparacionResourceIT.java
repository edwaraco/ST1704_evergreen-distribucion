package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.SeparacionAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Separacion;
import com.evergreen.distribucion.repository.SeparacionRepository;
import com.evergreen.distribucion.service.SeparacionService;
import com.evergreen.distribucion.service.dto.SeparacionDTO;
import com.evergreen.distribucion.service.mapper.SeparacionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SeparacionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SeparacionResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final LocalDate DEFAULT_FECHA_REALIZACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REALIZACION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_UBICACION = "AAAAAAAAAA";
    private static final String UPDATED_UBICACION = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/separacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SeparacionRepository separacionRepository;

    @Mock
    private SeparacionRepository separacionRepositoryMock;

    @Autowired
    private SeparacionMapper separacionMapper;

    @Mock
    private SeparacionService separacionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeparacionMockMvc;

    private Separacion separacion;

    private Separacion insertedSeparacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Separacion createEntity(EntityManager em) {
        Separacion separacion = new Separacion()
            .identificador(DEFAULT_IDENTIFICADOR)
            .lote(DEFAULT_LOTE)
            .cantidad(DEFAULT_CANTIDAD)
            .fechaRealizacion(DEFAULT_FECHA_REALIZACION)
            .ubicacion(DEFAULT_UBICACION)
            .responsable(DEFAULT_RESPONSABLE)
            .observaciones(DEFAULT_OBSERVACIONES);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).get(0);
        }
        separacion.setPedido(pedido);
        return separacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Separacion createUpdatedEntity(EntityManager em) {
        Separacion updatedSeparacion = new Separacion()
            .identificador(UPDATED_IDENTIFICADOR)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .ubicacion(UPDATED_UBICACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);
        // Add required entity
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            pedido = PedidoResourceIT.createUpdatedEntity(em);
            em.persist(pedido);
            em.flush();
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).get(0);
        }
        updatedSeparacion.setPedido(pedido);
        return updatedSeparacion;
    }

    @BeforeEach
    void initTest() {
        separacion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSeparacion != null) {
            separacionRepository.delete(insertedSeparacion);
            insertedSeparacion = null;
        }
    }

    @Test
    @Transactional
    void createSeparacion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);
        var returnedSeparacionDTO = om.readValue(
            restSeparacionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SeparacionDTO.class
        );

        // Validate the Separacion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSeparacion = separacionMapper.toEntity(returnedSeparacionDTO);
        assertSeparacionUpdatableFieldsEquals(returnedSeparacion, getPersistedSeparacion(returnedSeparacion));

        insertedSeparacion = returnedSeparacion;
    }

    @Test
    @Transactional
    void createSeparacionWithExistingId() throws Exception {
        // Create the Separacion with an existing ID
        separacion.setId(1L);
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeparacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        separacion.setIdentificador(null);

        // Create the Separacion, which fails.
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        restSeparacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        separacion.setLote(null);

        // Create the Separacion, which fails.
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        restSeparacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        separacion.setCantidad(null);

        // Create the Separacion, which fails.
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        restSeparacionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeparacions() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        // Get all the separacionList
        restSeparacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(separacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].fechaRealizacion").value(hasItem(DEFAULT_FECHA_REALIZACION.toString())))
            .andExpect(jsonPath("$.[*].ubicacion").value(hasItem(DEFAULT_UBICACION)))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSeparacionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(separacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSeparacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(separacionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSeparacionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(separacionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSeparacionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(separacionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSeparacion() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        // Get the separacion
        restSeparacionMockMvc
            .perform(get(ENTITY_API_URL_ID, separacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(separacion.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.lote").value(DEFAULT_LOTE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.fechaRealizacion").value(DEFAULT_FECHA_REALIZACION.toString()))
            .andExpect(jsonPath("$.ubicacion").value(DEFAULT_UBICACION))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingSeparacion() throws Exception {
        // Get the separacion
        restSeparacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeparacion() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the separacion
        Separacion updatedSeparacion = separacionRepository.findById(separacion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeparacion are not directly saved in db
        em.detach(updatedSeparacion);
        updatedSeparacion
            .identificador(UPDATED_IDENTIFICADOR)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .ubicacion(UPDATED_UBICACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);
        SeparacionDTO separacionDTO = separacionMapper.toDto(updatedSeparacion);

        restSeparacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, separacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(separacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSeparacionToMatchAllProperties(updatedSeparacion);
    }

    @Test
    @Transactional
    void putNonExistingSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, separacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(separacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(separacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeparacionWithPatch() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the separacion using partial update
        Separacion partialUpdatedSeparacion = new Separacion();
        partialUpdatedSeparacion.setId(separacion.getId());

        partialUpdatedSeparacion.identificador(UPDATED_IDENTIFICADOR).fechaRealizacion(UPDATED_FECHA_REALIZACION);

        restSeparacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeparacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeparacion))
            )
            .andExpect(status().isOk());

        // Validate the Separacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeparacionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSeparacion, separacion),
            getPersistedSeparacion(separacion)
        );
    }

    @Test
    @Transactional
    void fullUpdateSeparacionWithPatch() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the separacion using partial update
        Separacion partialUpdatedSeparacion = new Separacion();
        partialUpdatedSeparacion.setId(separacion.getId());

        partialUpdatedSeparacion
            .identificador(UPDATED_IDENTIFICADOR)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .ubicacion(UPDATED_UBICACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);

        restSeparacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeparacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSeparacion))
            )
            .andExpect(status().isOk());

        // Validate the Separacion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSeparacionUpdatableFieldsEquals(partialUpdatedSeparacion, getPersistedSeparacion(partialUpdatedSeparacion));
    }

    @Test
    @Transactional
    void patchNonExistingSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, separacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(separacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(separacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeparacion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        separacion.setId(longCount.incrementAndGet());

        // Create the Separacion
        SeparacionDTO separacionDTO = separacionMapper.toDto(separacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeparacionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(separacionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Separacion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeparacion() throws Exception {
        // Initialize the database
        insertedSeparacion = separacionRepository.saveAndFlush(separacion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the separacion
        restSeparacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, separacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return separacionRepository.count();
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

    protected Separacion getPersistedSeparacion(Separacion separacion) {
        return separacionRepository.findById(separacion.getId()).orElseThrow();
    }

    protected void assertPersistedSeparacionToMatchAllProperties(Separacion expectedSeparacion) {
        assertSeparacionAllPropertiesEquals(expectedSeparacion, getPersistedSeparacion(expectedSeparacion));
    }

    protected void assertPersistedSeparacionToMatchUpdatableProperties(Separacion expectedSeparacion) {
        assertSeparacionAllUpdatablePropertiesEquals(expectedSeparacion, getPersistedSeparacion(expectedSeparacion));
    }
}
