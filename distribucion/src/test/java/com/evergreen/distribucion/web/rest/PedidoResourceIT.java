package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.PedidoAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.CanalComercializacion;
import com.evergreen.distribucion.domain.Cliente;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Producto;
import com.evergreen.distribucion.domain.Transporte;
import com.evergreen.distribucion.repository.PedidoRepository;
import com.evergreen.distribucion.service.PedidoService;
import com.evergreen.distribucion.service.dto.PedidoDTO;
import com.evergreen.distribucion.service.mapper.PedidoMapper;
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
 * Integration tests for the {@link PedidoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PedidoResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ENTRADA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ENTRADA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ENTRADA = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_FECHA_SALIDA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_SALIDA = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_SALIDA = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pedidos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoRepository pedidoRepositoryMock;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Mock
    private PedidoService pedidoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPedidoMockMvc;

    private Pedido pedido;

    private Pedido insertedPedido;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createEntity(EntityManager em) {
        Pedido pedido = new Pedido()
            .identificador(DEFAULT_IDENTIFICADOR)
            .fechaEntrada(DEFAULT_FECHA_ENTRADA)
            .fechaSalida(DEFAULT_FECHA_SALIDA)
            .estado(DEFAULT_ESTADO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        pedido.setCliente(cliente);
        return pedido;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pedido createUpdatedEntity(EntityManager em) {
        Pedido updatedPedido = new Pedido()
            .identificador(UPDATED_IDENTIFICADOR)
            .fechaEntrada(UPDATED_FECHA_ENTRADA)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .estado(UPDATED_ESTADO);
        // Add required entity
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            cliente = ClienteResourceIT.createUpdatedEntity();
            em.persist(cliente);
            em.flush();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        updatedPedido.setCliente(cliente);
        return updatedPedido;
    }

    @BeforeEach
    void initTest() {
        pedido = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPedido != null) {
            pedidoRepository.delete(insertedPedido);
            insertedPedido = null;
        }
    }

    @Test
    @Transactional
    void createPedido() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);
        var returnedPedidoDTO = om.readValue(
            restPedidoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PedidoDTO.class
        );

        // Validate the Pedido in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPedido = pedidoMapper.toEntity(returnedPedidoDTO);
        assertPedidoUpdatableFieldsEquals(returnedPedido, getPersistedPedido(returnedPedido));

        insertedPedido = returnedPedido;
    }

    @Test
    @Transactional
    void createPedidoWithExistingId() throws Exception {
        // Create the Pedido with an existing ID
        pedido.setId(1L);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setIdentificador(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaEntradaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setFechaEntrada(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pedido.setEstado(null);

        // Create the Pedido, which fails.
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        restPedidoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPedidos() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].fechaEntrada").value(hasItem(DEFAULT_FECHA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidosWithEagerRelationshipsIsEnabled() throws Exception {
        when(pedidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPedidoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pedidoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPedidosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pedidoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPedidoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pedidoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get the pedido
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL_ID, pedido.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pedido.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.fechaEntrada").value(DEFAULT_FECHA_ENTRADA.toString()))
            .andExpect(jsonPath("$.fechaSalida").value(DEFAULT_FECHA_SALIDA.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getPedidosByIdFiltering() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        Long id = pedido.getId();

        defaultPedidoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPedidoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPedidoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPedidosByIdentificadorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where identificador equals to
        defaultPedidoFiltering("identificador.equals=" + DEFAULT_IDENTIFICADOR, "identificador.equals=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPedidosByIdentificadorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where identificador in
        defaultPedidoFiltering(
            "identificador.in=" + DEFAULT_IDENTIFICADOR + "," + UPDATED_IDENTIFICADOR,
            "identificador.in=" + UPDATED_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllPedidosByIdentificadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where identificador is not null
        defaultPedidoFiltering("identificador.specified=true", "identificador.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByIdentificadorContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where identificador contains
        defaultPedidoFiltering("identificador.contains=" + DEFAULT_IDENTIFICADOR, "identificador.contains=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllPedidosByIdentificadorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where identificador does not contain
        defaultPedidoFiltering(
            "identificador.doesNotContain=" + UPDATED_IDENTIFICADOR,
            "identificador.doesNotContain=" + DEFAULT_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada equals to
        defaultPedidoFiltering("fechaEntrada.equals=" + DEFAULT_FECHA_ENTRADA, "fechaEntrada.equals=" + UPDATED_FECHA_ENTRADA);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada in
        defaultPedidoFiltering(
            "fechaEntrada.in=" + DEFAULT_FECHA_ENTRADA + "," + UPDATED_FECHA_ENTRADA,
            "fechaEntrada.in=" + UPDATED_FECHA_ENTRADA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada is not null
        defaultPedidoFiltering("fechaEntrada.specified=true", "fechaEntrada.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada is greater than or equal to
        defaultPedidoFiltering(
            "fechaEntrada.greaterThanOrEqual=" + DEFAULT_FECHA_ENTRADA,
            "fechaEntrada.greaterThanOrEqual=" + UPDATED_FECHA_ENTRADA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada is less than or equal to
        defaultPedidoFiltering(
            "fechaEntrada.lessThanOrEqual=" + DEFAULT_FECHA_ENTRADA,
            "fechaEntrada.lessThanOrEqual=" + SMALLER_FECHA_ENTRADA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada is less than
        defaultPedidoFiltering("fechaEntrada.lessThan=" + UPDATED_FECHA_ENTRADA, "fechaEntrada.lessThan=" + DEFAULT_FECHA_ENTRADA);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaEntradaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaEntrada is greater than
        defaultPedidoFiltering("fechaEntrada.greaterThan=" + SMALLER_FECHA_ENTRADA, "fechaEntrada.greaterThan=" + DEFAULT_FECHA_ENTRADA);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida equals to
        defaultPedidoFiltering("fechaSalida.equals=" + DEFAULT_FECHA_SALIDA, "fechaSalida.equals=" + UPDATED_FECHA_SALIDA);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida in
        defaultPedidoFiltering(
            "fechaSalida.in=" + DEFAULT_FECHA_SALIDA + "," + UPDATED_FECHA_SALIDA,
            "fechaSalida.in=" + UPDATED_FECHA_SALIDA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida is not null
        defaultPedidoFiltering("fechaSalida.specified=true", "fechaSalida.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida is greater than or equal to
        defaultPedidoFiltering(
            "fechaSalida.greaterThanOrEqual=" + DEFAULT_FECHA_SALIDA,
            "fechaSalida.greaterThanOrEqual=" + UPDATED_FECHA_SALIDA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida is less than or equal to
        defaultPedidoFiltering(
            "fechaSalida.lessThanOrEqual=" + DEFAULT_FECHA_SALIDA,
            "fechaSalida.lessThanOrEqual=" + SMALLER_FECHA_SALIDA
        );
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida is less than
        defaultPedidoFiltering("fechaSalida.lessThan=" + UPDATED_FECHA_SALIDA, "fechaSalida.lessThan=" + DEFAULT_FECHA_SALIDA);
    }

    @Test
    @Transactional
    void getAllPedidosByFechaSalidaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where fechaSalida is greater than
        defaultPedidoFiltering("fechaSalida.greaterThan=" + SMALLER_FECHA_SALIDA, "fechaSalida.greaterThan=" + DEFAULT_FECHA_SALIDA);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado equals to
        defaultPedidoFiltering("estado.equals=" + DEFAULT_ESTADO, "estado.equals=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado in
        defaultPedidoFiltering("estado.in=" + DEFAULT_ESTADO + "," + UPDATED_ESTADO, "estado.in=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado is not null
        defaultPedidoFiltering("estado.specified=true", "estado.specified=false");
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado contains
        defaultPedidoFiltering("estado.contains=" + DEFAULT_ESTADO, "estado.contains=" + UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByEstadoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        // Get all the pedidoList where estado does not contain
        defaultPedidoFiltering("estado.doesNotContain=" + UPDATED_ESTADO, "estado.doesNotContain=" + DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void getAllPedidosByClienteIsEqualToSomething() throws Exception {
        Cliente cliente;
        if (TestUtil.findAll(em, Cliente.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            cliente = ClienteResourceIT.createEntity();
        } else {
            cliente = TestUtil.findAll(em, Cliente.class).get(0);
        }
        em.persist(cliente);
        em.flush();
        pedido.setCliente(cliente);
        pedidoRepository.saveAndFlush(pedido);
        Long clienteId = cliente.getId();
        // Get all the pedidoList where cliente equals to clienteId
        defaultPedidoShouldBeFound("clienteId.equals=" + clienteId);

        // Get all the pedidoList where cliente equals to (clienteId + 1)
        defaultPedidoShouldNotBeFound("clienteId.equals=" + (clienteId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByProductoIsEqualToSomething() throws Exception {
        Producto producto;
        if (TestUtil.findAll(em, Producto.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            producto = ProductoResourceIT.createEntity();
        } else {
            producto = TestUtil.findAll(em, Producto.class).get(0);
        }
        em.persist(producto);
        em.flush();
        pedido.addProducto(producto);
        pedidoRepository.saveAndFlush(pedido);
        Long productoId = producto.getId();
        // Get all the pedidoList where producto equals to productoId
        defaultPedidoShouldBeFound("productoId.equals=" + productoId);

        // Get all the pedidoList where producto equals to (productoId + 1)
        defaultPedidoShouldNotBeFound("productoId.equals=" + (productoId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByCanalComercializacionIsEqualToSomething() throws Exception {
        CanalComercializacion canalComercializacion;
        if (TestUtil.findAll(em, CanalComercializacion.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            canalComercializacion = CanalComercializacionResourceIT.createEntity();
        } else {
            canalComercializacion = TestUtil.findAll(em, CanalComercializacion.class).get(0);
        }
        em.persist(canalComercializacion);
        em.flush();
        pedido.setCanalComercializacion(canalComercializacion);
        pedidoRepository.saveAndFlush(pedido);
        Long canalComercializacionId = canalComercializacion.getId();
        // Get all the pedidoList where canalComercializacion equals to canalComercializacionId
        defaultPedidoShouldBeFound("canalComercializacionId.equals=" + canalComercializacionId);

        // Get all the pedidoList where canalComercializacion equals to (canalComercializacionId + 1)
        defaultPedidoShouldNotBeFound("canalComercializacionId.equals=" + (canalComercializacionId + 1));
    }

    @Test
    @Transactional
    void getAllPedidosByTransporteIsEqualToSomething() throws Exception {
        Transporte transporte;
        if (TestUtil.findAll(em, Transporte.class).isEmpty()) {
            pedidoRepository.saveAndFlush(pedido);
            transporte = TransporteResourceIT.createEntity();
        } else {
            transporte = TestUtil.findAll(em, Transporte.class).get(0);
        }
        em.persist(transporte);
        em.flush();
        pedido.setTransporte(transporte);
        pedidoRepository.saveAndFlush(pedido);
        Long transporteId = transporte.getId();
        // Get all the pedidoList where transporte equals to transporteId
        defaultPedidoShouldBeFound("transporteId.equals=" + transporteId);

        // Get all the pedidoList where transporte equals to (transporteId + 1)
        defaultPedidoShouldNotBeFound("transporteId.equals=" + (transporteId + 1));
    }

    private void defaultPedidoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPedidoShouldBeFound(shouldBeFound);
        defaultPedidoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPedidoShouldBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pedido.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].fechaEntrada").value(hasItem(DEFAULT_FECHA_ENTRADA.toString())))
            .andExpect(jsonPath("$.[*].fechaSalida").value(hasItem(DEFAULT_FECHA_SALIDA.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));

        // Check, that the count call also returns 1
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPedidoShouldNotBeFound(String filter) throws Exception {
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPedidoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPedido() throws Exception {
        // Get the pedido
        restPedidoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido
        Pedido updatedPedido = pedidoRepository.findById(pedido.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPedido are not directly saved in db
        em.detach(updatedPedido);
        updatedPedido
            .identificador(UPDATED_IDENTIFICADOR)
            .fechaEntrada(UPDATED_FECHA_ENTRADA)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .estado(UPDATED_ESTADO);
        PedidoDTO pedidoDTO = pedidoMapper.toDto(updatedPedido);

        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPedidoToMatchAllProperties(updatedPedido);
    }

    @Test
    @Transactional
    void putNonExistingPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pedidoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido.identificador(UPDATED_IDENTIFICADOR);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPedido, pedido), getPersistedPedido(pedido));
    }

    @Test
    @Transactional
    void fullUpdatePedidoWithPatch() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pedido using partial update
        Pedido partialUpdatedPedido = new Pedido();
        partialUpdatedPedido.setId(pedido.getId());

        partialUpdatedPedido
            .identificador(UPDATED_IDENTIFICADOR)
            .fechaEntrada(UPDATED_FECHA_ENTRADA)
            .fechaSalida(UPDATED_FECHA_SALIDA)
            .estado(UPDATED_ESTADO);

        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPedido.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPedido))
            )
            .andExpect(status().isOk());

        // Validate the Pedido in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPedidoUpdatableFieldsEquals(partialUpdatedPedido, getPersistedPedido(partialUpdatedPedido));
    }

    @Test
    @Transactional
    void patchNonExistingPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pedidoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pedidoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPedido() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pedido.setId(longCount.incrementAndGet());

        // Create the Pedido
        PedidoDTO pedidoDTO = pedidoMapper.toDto(pedido);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPedidoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pedidoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pedido in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePedido() throws Exception {
        // Initialize the database
        insertedPedido = pedidoRepository.saveAndFlush(pedido);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pedido
        restPedidoMockMvc
            .perform(delete(ENTITY_API_URL_ID, pedido.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pedidoRepository.count();
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

    protected Pedido getPersistedPedido(Pedido pedido) {
        return pedidoRepository.findById(pedido.getId()).orElseThrow();
    }

    protected void assertPersistedPedidoToMatchAllProperties(Pedido expectedPedido) {
        assertPedidoAllPropertiesEquals(expectedPedido, getPersistedPedido(expectedPedido));
    }

    protected void assertPersistedPedidoToMatchUpdatableProperties(Pedido expectedPedido) {
        assertPedidoAllUpdatablePropertiesEquals(expectedPedido, getPersistedPedido(expectedPedido));
    }
}
