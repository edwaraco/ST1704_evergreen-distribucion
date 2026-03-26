package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.EmpaqueAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.Empaque;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.enumeration.TamanioEmpaque;
import com.evergreen.distribucion.domain.enumeration.TipoEmpaque;
import com.evergreen.distribucion.repository.EmpaqueRepository;
import com.evergreen.distribucion.service.EmpaqueService;
import com.evergreen.distribucion.service.dto.EmpaqueDTO;
import com.evergreen.distribucion.service.mapper.EmpaqueMapper;
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
 * Integration tests for the {@link EmpaqueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EmpaqueResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final TipoEmpaque DEFAULT_TIPO = TipoEmpaque.CAJA;
    private static final TipoEmpaque UPDATED_TIPO = TipoEmpaque.BOLSA;

    private static final TamanioEmpaque DEFAULT_TAMANIO = TamanioEmpaque.PEQUENO;
    private static final TamanioEmpaque UPDATED_TAMANIO = TamanioEmpaque.MEDIANO;

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;

    private static final Integer DEFAULT_TIEMPO_MINUTOS = 0;
    private static final Integer UPDATED_TIEMPO_MINUTOS = 1;

    private static final LocalDate DEFAULT_FECHA_REALIZACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_REALIZACION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_OBSERVACIONES = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACIONES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/empaques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmpaqueRepository empaqueRepository;

    @Mock
    private EmpaqueRepository empaqueRepositoryMock;

    @Autowired
    private EmpaqueMapper empaqueMapper;

    @Mock
    private EmpaqueService empaqueServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmpaqueMockMvc;

    private Empaque empaque;

    private Empaque insertedEmpaque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empaque createEntity(EntityManager em) {
        Empaque empaque = new Empaque()
            .identificador(DEFAULT_IDENTIFICADOR)
            .tipo(DEFAULT_TIPO)
            .tamanio(DEFAULT_TAMANIO)
            .cantidad(DEFAULT_CANTIDAD)
            .tiempoMinutos(DEFAULT_TIEMPO_MINUTOS)
            .fechaRealizacion(DEFAULT_FECHA_REALIZACION)
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
        empaque.setPedido(pedido);
        return empaque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Empaque createUpdatedEntity(EntityManager em) {
        Empaque updatedEmpaque = new Empaque()
            .identificador(UPDATED_IDENTIFICADOR)
            .tipo(UPDATED_TIPO)
            .tamanio(UPDATED_TAMANIO)
            .cantidad(UPDATED_CANTIDAD)
            .tiempoMinutos(UPDATED_TIEMPO_MINUTOS)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
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
        updatedEmpaque.setPedido(pedido);
        return updatedEmpaque;
    }

    @BeforeEach
    void initTest() {
        empaque = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEmpaque != null) {
            empaqueRepository.delete(insertedEmpaque);
            insertedEmpaque = null;
        }
    }

    @Test
    @Transactional
    void createEmpaque() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);
        var returnedEmpaqueDTO = om.readValue(
            restEmpaqueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmpaqueDTO.class
        );

        // Validate the Empaque in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmpaque = empaqueMapper.toEntity(returnedEmpaqueDTO);
        assertEmpaqueUpdatableFieldsEquals(returnedEmpaque, getPersistedEmpaque(returnedEmpaque));

        insertedEmpaque = returnedEmpaque;
    }

    @Test
    @Transactional
    void createEmpaqueWithExistingId() throws Exception {
        // Create the Empaque with an existing ID
        empaque.setId(1L);
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empaque.setIdentificador(null);

        // Create the Empaque, which fails.
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empaque.setTipo(null);

        // Create the Empaque, which fails.
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTamanioIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empaque.setTamanio(null);

        // Create the Empaque, which fails.
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empaque.setCantidad(null);

        // Create the Empaque, which fails.
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTiempoMinutosIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        empaque.setTiempoMinutos(null);

        // Create the Empaque, which fails.
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        restEmpaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmpaques() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        // Get all the empaqueList
        restEmpaqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(empaque.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].tamanio").value(hasItem(DEFAULT_TAMANIO.toString())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].tiempoMinutos").value(hasItem(DEFAULT_TIEMPO_MINUTOS)))
            .andExpect(jsonPath("$.[*].fechaRealizacion").value(hasItem(DEFAULT_FECHA_REALIZACION.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].observaciones").value(hasItem(DEFAULT_OBSERVACIONES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpaquesWithEagerRelationshipsIsEnabled() throws Exception {
        when(empaqueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpaqueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(empaqueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEmpaquesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(empaqueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEmpaqueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(empaqueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEmpaque() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        // Get the empaque
        restEmpaqueMockMvc
            .perform(get(ENTITY_API_URL_ID, empaque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(empaque.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.tamanio").value(DEFAULT_TAMANIO.toString()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.tiempoMinutos").value(DEFAULT_TIEMPO_MINUTOS))
            .andExpect(jsonPath("$.fechaRealizacion").value(DEFAULT_FECHA_REALIZACION.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE))
            .andExpect(jsonPath("$.observaciones").value(DEFAULT_OBSERVACIONES));
    }

    @Test
    @Transactional
    void getNonExistingEmpaque() throws Exception {
        // Get the empaque
        restEmpaqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmpaque() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empaque
        Empaque updatedEmpaque = empaqueRepository.findById(empaque.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmpaque are not directly saved in db
        em.detach(updatedEmpaque);
        updatedEmpaque
            .identificador(UPDATED_IDENTIFICADOR)
            .tipo(UPDATED_TIPO)
            .tamanio(UPDATED_TAMANIO)
            .cantidad(UPDATED_CANTIDAD)
            .tiempoMinutos(UPDATED_TIEMPO_MINUTOS)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(updatedEmpaque);

        restEmpaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empaqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmpaqueToMatchAllProperties(updatedEmpaque);
    }

    @Test
    @Transactional
    void putNonExistingEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, empaqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(empaqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmpaqueWithPatch() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empaque using partial update
        Empaque partialUpdatedEmpaque = new Empaque();
        partialUpdatedEmpaque.setId(empaque.getId());

        partialUpdatedEmpaque
            .identificador(UPDATED_IDENTIFICADOR)
            .tipo(UPDATED_TIPO)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);

        restEmpaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpaque))
            )
            .andExpect(status().isOk());

        // Validate the Empaque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpaqueUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmpaque, empaque), getPersistedEmpaque(empaque));
    }

    @Test
    @Transactional
    void fullUpdateEmpaqueWithPatch() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the empaque using partial update
        Empaque partialUpdatedEmpaque = new Empaque();
        partialUpdatedEmpaque.setId(empaque.getId());

        partialUpdatedEmpaque
            .identificador(UPDATED_IDENTIFICADOR)
            .tipo(UPDATED_TIPO)
            .tamanio(UPDATED_TAMANIO)
            .cantidad(UPDATED_CANTIDAD)
            .tiempoMinutos(UPDATED_TIEMPO_MINUTOS)
            .fechaRealizacion(UPDATED_FECHA_REALIZACION)
            .responsable(UPDATED_RESPONSABLE)
            .observaciones(UPDATED_OBSERVACIONES);

        restEmpaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmpaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmpaque))
            )
            .andExpect(status().isOk());

        // Validate the Empaque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmpaqueUpdatableFieldsEquals(partialUpdatedEmpaque, getPersistedEmpaque(partialUpdatedEmpaque));
    }

    @Test
    @Transactional
    void patchNonExistingEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, empaqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empaqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(empaqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmpaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        empaque.setId(longCount.incrementAndGet());

        // Create the Empaque
        EmpaqueDTO empaqueDTO = empaqueMapper.toDto(empaque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmpaqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(empaqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Empaque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmpaque() throws Exception {
        // Initialize the database
        insertedEmpaque = empaqueRepository.saveAndFlush(empaque);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the empaque
        restEmpaqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, empaque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return empaqueRepository.count();
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

    protected Empaque getPersistedEmpaque(Empaque empaque) {
        return empaqueRepository.findById(empaque.getId()).orElseThrow();
    }

    protected void assertPersistedEmpaqueToMatchAllProperties(Empaque expectedEmpaque) {
        assertEmpaqueAllPropertiesEquals(expectedEmpaque, getPersistedEmpaque(expectedEmpaque));
    }

    protected void assertPersistedEmpaqueToMatchUpdatableProperties(Empaque expectedEmpaque) {
        assertEmpaqueAllUpdatablePropertiesEquals(expectedEmpaque, getPersistedEmpaque(expectedEmpaque));
    }
}
