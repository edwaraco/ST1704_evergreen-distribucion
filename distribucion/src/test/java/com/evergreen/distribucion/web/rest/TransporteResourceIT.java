package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.TransporteAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.domain.enumeration.TipoTransporte;
import com.evergreen.distribucion.repository.TransporteRepository;
import com.evergreen.distribucion.service.dto.TransporteDTO;
import com.evergreen.distribucion.service.mapper.TransporteMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TransporteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransporteResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final TipoTransporte DEFAULT_TIPO_TRANSPORTE = TipoTransporte.ACUATICO;
    private static final TipoTransporte UPDATED_TIPO_TRANSPORTE = TipoTransporte.AEREO;

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final Double DEFAULT_CAPACIDAD_KG = 0D;
    private static final Double UPDATED_CAPACIDAD_KG = 1D;
    private static final Double SMALLER_CAPACIDAD_KG = 0D - 1D;

    private static final Double DEFAULT_CAPACIDAD_M_3 = 0D;
    private static final Double UPDATED_CAPACIDAD_M_3 = 1D;
    private static final Double SMALLER_CAPACIDAD_M_3 = 0D - 1D;

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ASIGNACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ASIGNACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ASIGNACION = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/transportes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransporteRepository transporteRepository;

    @Autowired
    private TransporteMapper transporteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransporteMockMvc;

    private Transporte transporte;

    private Transporte insertedTransporte;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporte createEntity() {
        return new Transporte()
            .identificador(DEFAULT_IDENTIFICADOR)
            .tipoTransporte(DEFAULT_TIPO_TRANSPORTE)
            .matricula(DEFAULT_MATRICULA)
            .capacidadKg(DEFAULT_CAPACIDAD_KG)
            .capacidadM3(DEFAULT_CAPACIDAD_M_3)
            .estado(DEFAULT_ESTADO)
            .fechaAsignacion(DEFAULT_FECHA_ASIGNACION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transporte createUpdatedEntity() {
        return new Transporte()
            .identificador(UPDATED_IDENTIFICADOR)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .matricula(UPDATED_MATRICULA)
            .capacidadKg(UPDATED_CAPACIDAD_KG)
            .capacidadM3(UPDATED_CAPACIDAD_M_3)
            .estado(UPDATED_ESTADO)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION);
    }

    @BeforeEach
    void initTest() {
        transporte = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransporte != null) {
            transporteRepository.delete(insertedTransporte);
            insertedTransporte = null;
        }
    }

    @Test
    @Transactional
    void createTransporte() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);
        var returnedTransporteDTO = om.readValue(
            restTransporteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransporteDTO.class
        );

        // Validate the Transporte in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransporte = transporteMapper.toEntity(returnedTransporteDTO);
        assertTransporteUpdatableFieldsEquals(returnedTransporte, getPersistedTransporte(returnedTransporte));

        insertedTransporte = returnedTransporte;
    }

    @Test
    @Transactional
    void createTransporteWithExistingId() throws Exception {
        // Create the Transporte with an existing ID
        transporte.setId(1L);
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transporte.setIdentificador(null);

        // Create the Transporte, which fails.
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        restTransporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoTransporteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transporte.setTipoTransporte(null);

        // Create the Transporte, which fails.
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        restTransporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transporte.setEstado(null);

        // Create the Transporte, which fails.
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        restTransporteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransportes() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transporte.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].tipoTransporte").value(hasItem(DEFAULT_TIPO_TRANSPORTE.toString())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].capacidadKg").value(hasItem(DEFAULT_CAPACIDAD_KG)))
            .andExpect(jsonPath("$.[*].capacidadM3").value(hasItem(DEFAULT_CAPACIDAD_M_3)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaAsignacion").value(hasItem(DEFAULT_FECHA_ASIGNACION.toString())));
    }

    @Test
    @Transactional
    void getTransporte() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get the transporte
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL_ID, transporte.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transporte.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.tipoTransporte").value(DEFAULT_TIPO_TRANSPORTE.toString()))
            .andExpect(jsonPath("$.matricula").value(DEFAULT_MATRICULA))
            .andExpect(jsonPath("$.capacidadKg").value(DEFAULT_CAPACIDAD_KG))
            .andExpect(jsonPath("$.capacidadM3").value(DEFAULT_CAPACIDAD_M_3))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.fechaAsignacion").value(DEFAULT_FECHA_ASIGNACION.toString()));
    }

    @Test
    @Transactional
    void getTransportesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        Long id = transporte.getId();

        defaultTransporteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransporteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransporteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransportesByIdentificadorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where identificador equals to
        defaultTransporteFiltering("identificador.equals=" + DEFAULT_IDENTIFICADOR, "identificador.equals=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllTransportesByIdentificadorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where identificador in
        defaultTransporteFiltering(
            "identificador.in=" + DEFAULT_IDENTIFICADOR + "," + UPDATED_IDENTIFICADOR,
            "identificador.in=" + UPDATED_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllTransportesByIdentificadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where identificador is not null
        defaultTransporteFiltering("identificador.specified=true", "identificador.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByIdentificadorContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where identificador contains
        defaultTransporteFiltering("identificador.contains=" + DEFAULT_IDENTIFICADOR, "identificador.contains=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllTransportesByIdentificadorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where identificador does not contain
        defaultTransporteFiltering(
            "identificador.doesNotContain=" + UPDATED_IDENTIFICADOR,
            "identificador.doesNotContain=" + DEFAULT_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllTransportesByTipoTransporteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where tipoTransporte equals to
        defaultTransporteFiltering("tipoTransporte.equals=" + DEFAULT_TIPO_TRANSPORTE, "tipoTransporte.equals=" + UPDATED_TIPO_TRANSPORTE);
    }

    @Test
    @Transactional
    void getAllTransportesByTipoTransporteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where tipoTransporte in
        defaultTransporteFiltering(
            "tipoTransporte.in=" + DEFAULT_TIPO_TRANSPORTE + "," + UPDATED_TIPO_TRANSPORTE,
            "tipoTransporte.in=" + UPDATED_TIPO_TRANSPORTE
        );
    }

    @Test
    @Transactional
    void getAllTransportesByTipoTransporteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where tipoTransporte is not null
        defaultTransporteFiltering("tipoTransporte.specified=true", "tipoTransporte.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByMatriculaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where matricula equals to
        defaultTransporteFiltering("matricula.equals=" + DEFAULT_MATRICULA, "matricula.equals=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllTransportesByMatriculaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where matricula in
        defaultTransporteFiltering("matricula.in=" + DEFAULT_MATRICULA + "," + UPDATED_MATRICULA, "matricula.in=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllTransportesByMatriculaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where matricula is not null
        defaultTransporteFiltering("matricula.specified=true", "matricula.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByMatriculaContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where matricula contains
        defaultTransporteFiltering("matricula.contains=" + DEFAULT_MATRICULA, "matricula.contains=" + UPDATED_MATRICULA);
    }

    @Test
    @Transactional
    void getAllTransportesByMatriculaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where matricula does not contain
        defaultTransporteFiltering("matricula.doesNotContain=" + UPDATED_MATRICULA, "matricula.doesNotContain=" + DEFAULT_MATRICULA);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg equals to
        defaultTransporteFiltering("capacidadKg.equals=" + DEFAULT_CAPACIDAD_KG, "capacidadKg.equals=" + UPDATED_CAPACIDAD_KG);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg in
        defaultTransporteFiltering(
            "capacidadKg.in=" + DEFAULT_CAPACIDAD_KG + "," + UPDATED_CAPACIDAD_KG,
            "capacidadKg.in=" + UPDATED_CAPACIDAD_KG
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg is not null
        defaultTransporteFiltering("capacidadKg.specified=true", "capacidadKg.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg is greater than or equal to
        defaultTransporteFiltering(
            "capacidadKg.greaterThanOrEqual=" + DEFAULT_CAPACIDAD_KG,
            "capacidadKg.greaterThanOrEqual=" + UPDATED_CAPACIDAD_KG
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg is less than or equal to
        defaultTransporteFiltering(
            "capacidadKg.lessThanOrEqual=" + DEFAULT_CAPACIDAD_KG,
            "capacidadKg.lessThanOrEqual=" + SMALLER_CAPACIDAD_KG
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg is less than
        defaultTransporteFiltering("capacidadKg.lessThan=" + UPDATED_CAPACIDAD_KG, "capacidadKg.lessThan=" + DEFAULT_CAPACIDAD_KG);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadKgIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadKg is greater than
        defaultTransporteFiltering("capacidadKg.greaterThan=" + SMALLER_CAPACIDAD_KG, "capacidadKg.greaterThan=" + DEFAULT_CAPACIDAD_KG);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 equals to
        defaultTransporteFiltering("capacidadM3.equals=" + DEFAULT_CAPACIDAD_M_3, "capacidadM3.equals=" + UPDATED_CAPACIDAD_M_3);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 in
        defaultTransporteFiltering(
            "capacidadM3.in=" + DEFAULT_CAPACIDAD_M_3 + "," + UPDATED_CAPACIDAD_M_3,
            "capacidadM3.in=" + UPDATED_CAPACIDAD_M_3
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 is not null
        defaultTransporteFiltering("capacidadM3.specified=true", "capacidadM3.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 is greater than or equal to
        defaultTransporteFiltering(
            "capacidadM3.greaterThanOrEqual=" + DEFAULT_CAPACIDAD_M_3,
            "capacidadM3.greaterThanOrEqual=" + UPDATED_CAPACIDAD_M_3
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 is less than or equal to
        defaultTransporteFiltering(
            "capacidadM3.lessThanOrEqual=" + DEFAULT_CAPACIDAD_M_3,
            "capacidadM3.lessThanOrEqual=" + SMALLER_CAPACIDAD_M_3
        );
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 is less than
        defaultTransporteFiltering("capacidadM3.lessThan=" + UPDATED_CAPACIDAD_M_3, "capacidadM3.lessThan=" + DEFAULT_CAPACIDAD_M_3);
    }

    @Test
    @Transactional
    void getAllTransportesByCapacidadM3IsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where capacidadM3 is greater than
        defaultTransporteFiltering("capacidadM3.greaterThan=" + SMALLER_CAPACIDAD_M_3, "capacidadM3.greaterThan=" + DEFAULT_CAPACIDAD_M_3);
    }

    @Test
    @Transactional
    void getAllTransportesByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where estado equals to
        defaultTransporteFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTransportesByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where estado in
        defaultTransporteFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTransportesByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where estado is not null
        defaultTransporteFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where estado contains
        defaultTransporteFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllTransportesByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where estado does not contain
        defaultTransporteFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion equals to
        defaultTransporteFiltering(
            "fechaAsignacion.equals=" + DEFAULT_FECHA_ASIGNACION,
            "fechaAsignacion.equals=" + UPDATED_FECHA_ASIGNACION
        );
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion in
        defaultTransporteFiltering(
            "fechaAsignacion.in=" + DEFAULT_FECHA_ASIGNACION + "," + UPDATED_FECHA_ASIGNACION,
            "fechaAsignacion.in=" + UPDATED_FECHA_ASIGNACION
        );
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion is not null
        defaultTransporteFiltering("fechaAsignacion.specified=true", "fechaAsignacion.specified=false");
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion is greater than or equal to
        defaultTransporteFiltering(
            "fechaAsignacion.greaterThanOrEqual=" + DEFAULT_FECHA_ASIGNACION,
            "fechaAsignacion.greaterThanOrEqual=" + UPDATED_FECHA_ASIGNACION
        );
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion is less than or equal to
        defaultTransporteFiltering(
            "fechaAsignacion.lessThanOrEqual=" + DEFAULT_FECHA_ASIGNACION,
            "fechaAsignacion.lessThanOrEqual=" + SMALLER_FECHA_ASIGNACION
        );
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion is less than
        defaultTransporteFiltering(
            "fechaAsignacion.lessThan=" + UPDATED_FECHA_ASIGNACION,
            "fechaAsignacion.lessThan=" + DEFAULT_FECHA_ASIGNACION
        );
    }

    @Test
    @Transactional
    void getAllTransportesByFechaAsignacionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        // Get all the transporteList where fechaAsignacion is greater than
        defaultTransporteFiltering(
            "fechaAsignacion.greaterThan=" + SMALLER_FECHA_ASIGNACION,
            "fechaAsignacion.greaterThan=" + DEFAULT_FECHA_ASIGNACION
        );
    }

    private void defaultTransporteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransporteShouldBeFound(shouldBeFound);
        defaultTransporteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransporteShouldBeFound(String filter) throws Exception {
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transporte.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].tipoTransporte").value(hasItem(DEFAULT_TIPO_TRANSPORTE.toString())))
            .andExpect(jsonPath("$.[*].matricula").value(hasItem(DEFAULT_MATRICULA)))
            .andExpect(jsonPath("$.[*].capacidadKg").value(hasItem(DEFAULT_CAPACIDAD_KG)))
            .andExpect(jsonPath("$.[*].capacidadM3").value(hasItem(DEFAULT_CAPACIDAD_M_3)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].fechaAsignacion").value(hasItem(DEFAULT_FECHA_ASIGNACION.toString())));

        // Check, that the count call also returns 1
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransporteShouldNotBeFound(String filter) throws Exception {
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransporteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransporte() throws Exception {
        // Get the transporte
        restTransporteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransporte() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporte
        Transporte updatedTransporte = transporteRepository.findById(transporte.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransporte are not directly saved in db
        em.detach(updatedTransporte);
        updatedTransporte
            .identificador(UPDATED_IDENTIFICADOR)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .matricula(UPDATED_MATRICULA)
            .capacidadKg(UPDATED_CAPACIDAD_KG)
            .capacidadM3(UPDATED_CAPACIDAD_M_3)
            .estado(UPDATED_ESTADO)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION);
        TransporteDTO transporteDTO = transporteMapper.toDto(updatedTransporte);

        restTransporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transporteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransporteToMatchAllProperties(updatedTransporte);
    }

    @Test
    @Transactional
    void putNonExistingTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transporteDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransporteWithPatch() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporte using partial update
        Transporte partialUpdatedTransporte = new Transporte();
        partialUpdatedTransporte.setId(transporte.getId());

        partialUpdatedTransporte.capacidadKg(UPDATED_CAPACIDAD_KG).estado(UPDATED_ESTADO);

        restTransporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransporte))
            )
            .andExpect(status().isOk());

        // Validate the Transporte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransporteUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransporte, transporte),
            getPersistedTransporte(transporte)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransporteWithPatch() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transporte using partial update
        Transporte partialUpdatedTransporte = new Transporte();
        partialUpdatedTransporte.setId(transporte.getId());

        partialUpdatedTransporte
            .identificador(UPDATED_IDENTIFICADOR)
            .tipoTransporte(UPDATED_TIPO_TRANSPORTE)
            .matricula(UPDATED_MATRICULA)
            .capacidadKg(UPDATED_CAPACIDAD_KG)
            .capacidadM3(UPDATED_CAPACIDAD_M_3)
            .estado(UPDATED_ESTADO)
            .fechaAsignacion(UPDATED_FECHA_ASIGNACION);

        restTransporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransporte.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransporte))
            )
            .andExpect(status().isOk());

        // Validate the Transporte in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransporteUpdatableFieldsEquals(partialUpdatedTransporte, getPersistedTransporte(partialUpdatedTransporte));
    }

    @Test
    @Transactional
    void patchNonExistingTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transporteDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transporteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransporte() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transporte.setId(longCount.incrementAndGet());

        // Create the Transporte
        TransporteDTO transporteDTO = transporteMapper.toDto(transporte);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransporteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transporteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transporte in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransporte() throws Exception {
        // Initialize the database
        insertedTransporte = transporteRepository.saveAndFlush(transporte);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transporte
        restTransporteMockMvc
            .perform(delete(ENTITY_API_URL_ID, transporte.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transporteRepository.count();
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

    protected Transporte getPersistedTransporte(Transporte transporte) {
        return transporteRepository.findById(transporte.getId()).orElseThrow();
    }

    protected void assertPersistedTransporteToMatchAllProperties(Transporte expectedTransporte) {
        assertTransporteAllPropertiesEquals(expectedTransporte, getPersistedTransporte(expectedTransporte));
    }

    protected void assertPersistedTransporteToMatchUpdatableProperties(Transporte expectedTransporte) {
        assertTransporteAllUpdatablePropertiesEquals(expectedTransporte, getPersistedTransporte(expectedTransporte));
    }
}
