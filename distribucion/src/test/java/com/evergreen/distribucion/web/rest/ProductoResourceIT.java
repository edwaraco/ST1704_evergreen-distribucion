package com.evergreen.distribucion.web.rest;

import static com.evergreen.distribucion.domain.ProductoAsserts.*;
import static com.evergreen.distribucion.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evergreen.distribucion.IntegrationTest;
import com.evergreen.distribucion.domain.Pedido;
import com.evergreen.distribucion.domain.Producto;
import com.evergreen.distribucion.repository.ProductoRepository;
import com.evergreen.distribucion.service.dto.ProductoDTO;
import com.evergreen.distribucion.service.mapper.ProductoMapper;
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
 * Integration tests for the {@link ProductoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductoResourceIT {

    private static final String DEFAULT_IDENTIFICADOR = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR = "BBBBBBBBBB";

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_FECHA_ELABORACION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FECHA_ELABORACION = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_FECHA_ELABORACION = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final Integer DEFAULT_CANTIDAD = 1;
    private static final Integer UPDATED_CANTIDAD = 2;
    private static final Integer SMALLER_CANTIDAD = 1 - 1;

    private static final String DEFAULT_UNIDAD_MEDIDA = "AAAAAAAAAA";
    private static final String UPDATED_UNIDAD_MEDIDA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/productos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoMapper productoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductoMockMvc;

    private Producto producto;

    private Producto insertedProducto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createEntity() {
        return new Producto()
            .identificador(DEFAULT_IDENTIFICADOR)
            .nombre(DEFAULT_NOMBRE)
            .descripcion(DEFAULT_DESCRIPCION)
            .fechaElaboracion(DEFAULT_FECHA_ELABORACION)
            .lote(DEFAULT_LOTE)
            .cantidad(DEFAULT_CANTIDAD)
            .unidadMedida(DEFAULT_UNIDAD_MEDIDA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producto createUpdatedEntity() {
        return new Producto()
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaElaboracion(UPDATED_FECHA_ELABORACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);
    }

    @BeforeEach
    void initTest() {
        producto = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProducto != null) {
            productoRepository.delete(insertedProducto);
            insertedProducto = null;
        }
    }

    @Test
    @Transactional
    void createProducto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);
        var returnedProductoDTO = om.readValue(
            restProductoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductoDTO.class
        );

        // Validate the Producto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProducto = productoMapper.toEntity(returnedProductoDTO);
        assertProductoUpdatableFieldsEquals(returnedProducto, getPersistedProducto(returnedProducto));

        insertedProducto = returnedProducto;
    }

    @Test
    @Transactional
    void createProductoWithExistingId() throws Exception {
        // Create the Producto with an existing ID
        producto.setId(1L);
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdentificadorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setIdentificador(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNombreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setNombre(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFechaElaboracionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setFechaElaboracion(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCantidadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        producto.setCantidad(null);

        // Create the Producto, which fails.
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        restProductoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductos() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaElaboracion").value(hasItem(DEFAULT_FECHA_ELABORACION.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].unidadMedida").value(hasItem(DEFAULT_UNIDAD_MEDIDA)));
    }

    @Test
    @Transactional
    void getProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get the producto
        restProductoMockMvc
            .perform(get(ENTITY_API_URL_ID, producto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producto.getId().intValue()))
            .andExpect(jsonPath("$.identificador").value(DEFAULT_IDENTIFICADOR))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fechaElaboracion").value(DEFAULT_FECHA_ELABORACION.toString()))
            .andExpect(jsonPath("$.lote").value(DEFAULT_LOTE))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD))
            .andExpect(jsonPath("$.unidadMedida").value(DEFAULT_UNIDAD_MEDIDA));
    }

    @Test
    @Transactional
    void getProductosByIdFiltering() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        Long id = producto.getId();

        defaultProductoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultProductoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultProductoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductosByIdentificadorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where identificador equals to
        defaultProductoFiltering("identificador.equals=" + DEFAULT_IDENTIFICADOR, "identificador.equals=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllProductosByIdentificadorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where identificador in
        defaultProductoFiltering(
            "identificador.in=" + DEFAULT_IDENTIFICADOR + "," + UPDATED_IDENTIFICADOR,
            "identificador.in=" + UPDATED_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllProductosByIdentificadorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where identificador is not null
        defaultProductoFiltering("identificador.specified=true", "identificador.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByIdentificadorContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where identificador contains
        defaultProductoFiltering("identificador.contains=" + DEFAULT_IDENTIFICADOR, "identificador.contains=" + UPDATED_IDENTIFICADOR);
    }

    @Test
    @Transactional
    void getAllProductosByIdentificadorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where identificador does not contain
        defaultProductoFiltering(
            "identificador.doesNotContain=" + UPDATED_IDENTIFICADOR,
            "identificador.doesNotContain=" + DEFAULT_IDENTIFICADOR
        );
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre equals to
        defaultProductoFiltering("nombre.equals=" + DEFAULT_NOMBRE, "nombre.equals=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre in
        defaultProductoFiltering("nombre.in=" + DEFAULT_NOMBRE + "," + UPDATED_NOMBRE, "nombre.in=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre is not null
        defaultProductoFiltering("nombre.specified=true", "nombre.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByNombreContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre contains
        defaultProductoFiltering("nombre.contains=" + DEFAULT_NOMBRE, "nombre.contains=" + UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByNombreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where nombre does not contain
        defaultProductoFiltering("nombre.doesNotContain=" + UPDATED_NOMBRE, "nombre.doesNotContain=" + DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion equals to
        defaultProductoFiltering("descripcion.equals=" + DEFAULT_DESCRIPCION, "descripcion.equals=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion in
        defaultProductoFiltering(
            "descripcion.in=" + DEFAULT_DESCRIPCION + "," + UPDATED_DESCRIPCION,
            "descripcion.in=" + UPDATED_DESCRIPCION
        );
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion is not null
        defaultProductoFiltering("descripcion.specified=true", "descripcion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion contains
        defaultProductoFiltering("descripcion.contains=" + DEFAULT_DESCRIPCION, "descripcion.contains=" + UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByDescripcionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where descripcion does not contain
        defaultProductoFiltering("descripcion.doesNotContain=" + UPDATED_DESCRIPCION, "descripcion.doesNotContain=" + DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion equals to
        defaultProductoFiltering(
            "fechaElaboracion.equals=" + DEFAULT_FECHA_ELABORACION,
            "fechaElaboracion.equals=" + UPDATED_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion in
        defaultProductoFiltering(
            "fechaElaboracion.in=" + DEFAULT_FECHA_ELABORACION + "," + UPDATED_FECHA_ELABORACION,
            "fechaElaboracion.in=" + UPDATED_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion is not null
        defaultProductoFiltering("fechaElaboracion.specified=true", "fechaElaboracion.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion is greater than or equal to
        defaultProductoFiltering(
            "fechaElaboracion.greaterThanOrEqual=" + DEFAULT_FECHA_ELABORACION,
            "fechaElaboracion.greaterThanOrEqual=" + UPDATED_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion is less than or equal to
        defaultProductoFiltering(
            "fechaElaboracion.lessThanOrEqual=" + DEFAULT_FECHA_ELABORACION,
            "fechaElaboracion.lessThanOrEqual=" + SMALLER_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion is less than
        defaultProductoFiltering(
            "fechaElaboracion.lessThan=" + UPDATED_FECHA_ELABORACION,
            "fechaElaboracion.lessThan=" + DEFAULT_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByFechaElaboracionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where fechaElaboracion is greater than
        defaultProductoFiltering(
            "fechaElaboracion.greaterThan=" + SMALLER_FECHA_ELABORACION,
            "fechaElaboracion.greaterThan=" + DEFAULT_FECHA_ELABORACION
        );
    }

    @Test
    @Transactional
    void getAllProductosByLoteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lote equals to
        defaultProductoFiltering("lote.equals=" + DEFAULT_LOTE, "lote.equals=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllProductosByLoteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lote in
        defaultProductoFiltering("lote.in=" + DEFAULT_LOTE + "," + UPDATED_LOTE, "lote.in=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllProductosByLoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lote is not null
        defaultProductoFiltering("lote.specified=true", "lote.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByLoteContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lote contains
        defaultProductoFiltering("lote.contains=" + DEFAULT_LOTE, "lote.contains=" + UPDATED_LOTE);
    }

    @Test
    @Transactional
    void getAllProductosByLoteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where lote does not contain
        defaultProductoFiltering("lote.doesNotContain=" + UPDATED_LOTE, "lote.doesNotContain=" + DEFAULT_LOTE);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad equals to
        defaultProductoFiltering("cantidad.equals=" + DEFAULT_CANTIDAD, "cantidad.equals=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad in
        defaultProductoFiltering("cantidad.in=" + DEFAULT_CANTIDAD + "," + UPDATED_CANTIDAD, "cantidad.in=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is not null
        defaultProductoFiltering("cantidad.specified=true", "cantidad.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is greater than or equal to
        defaultProductoFiltering("cantidad.greaterThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.greaterThanOrEqual=" + UPDATED_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is less than or equal to
        defaultProductoFiltering("cantidad.lessThanOrEqual=" + DEFAULT_CANTIDAD, "cantidad.lessThanOrEqual=" + SMALLER_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is less than
        defaultProductoFiltering("cantidad.lessThan=" + UPDATED_CANTIDAD, "cantidad.lessThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByCantidadIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where cantidad is greater than
        defaultProductoFiltering("cantidad.greaterThan=" + SMALLER_CANTIDAD, "cantidad.greaterThan=" + DEFAULT_CANTIDAD);
    }

    @Test
    @Transactional
    void getAllProductosByUnidadMedidaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where unidadMedida equals to
        defaultProductoFiltering("unidadMedida.equals=" + DEFAULT_UNIDAD_MEDIDA, "unidadMedida.equals=" + UPDATED_UNIDAD_MEDIDA);
    }

    @Test
    @Transactional
    void getAllProductosByUnidadMedidaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where unidadMedida in
        defaultProductoFiltering(
            "unidadMedida.in=" + DEFAULT_UNIDAD_MEDIDA + "," + UPDATED_UNIDAD_MEDIDA,
            "unidadMedida.in=" + UPDATED_UNIDAD_MEDIDA
        );
    }

    @Test
    @Transactional
    void getAllProductosByUnidadMedidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where unidadMedida is not null
        defaultProductoFiltering("unidadMedida.specified=true", "unidadMedida.specified=false");
    }

    @Test
    @Transactional
    void getAllProductosByUnidadMedidaContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where unidadMedida contains
        defaultProductoFiltering("unidadMedida.contains=" + DEFAULT_UNIDAD_MEDIDA, "unidadMedida.contains=" + UPDATED_UNIDAD_MEDIDA);
    }

    @Test
    @Transactional
    void getAllProductosByUnidadMedidaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        // Get all the productoList where unidadMedida does not contain
        defaultProductoFiltering(
            "unidadMedida.doesNotContain=" + UPDATED_UNIDAD_MEDIDA,
            "unidadMedida.doesNotContain=" + DEFAULT_UNIDAD_MEDIDA
        );
    }

    @Test
    @Transactional
    void getAllProductosByPedidoIsEqualToSomething() throws Exception {
        Pedido pedido;
        if (TestUtil.findAll(em, Pedido.class).isEmpty()) {
            productoRepository.saveAndFlush(producto);
            pedido = PedidoResourceIT.createEntity(em);
        } else {
            pedido = TestUtil.findAll(em, Pedido.class).get(0);
        }
        em.persist(pedido);
        em.flush();
        producto.addPedido(pedido);
        productoRepository.saveAndFlush(producto);
        Long pedidoId = pedido.getId();
        // Get all the productoList where pedido equals to pedidoId
        defaultProductoShouldBeFound("pedidoId.equals=" + pedidoId);

        // Get all the productoList where pedido equals to (pedidoId + 1)
        defaultProductoShouldNotBeFound("pedidoId.equals=" + (pedidoId + 1));
    }

    private void defaultProductoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultProductoShouldBeFound(shouldBeFound);
        defaultProductoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductoShouldBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producto.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificador").value(hasItem(DEFAULT_IDENTIFICADOR)))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fechaElaboracion").value(hasItem(DEFAULT_FECHA_ELABORACION.toString())))
            .andExpect(jsonPath("$.[*].lote").value(hasItem(DEFAULT_LOTE)))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD)))
            .andExpect(jsonPath("$.[*].unidadMedida").value(hasItem(DEFAULT_UNIDAD_MEDIDA)));

        // Check, that the count call also returns 1
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductoShouldNotBeFound(String filter) throws Exception {
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducto() throws Exception {
        // Get the producto
        restProductoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto
        Producto updatedProducto = productoRepository.findById(producto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProducto are not directly saved in db
        em.detach(updatedProducto);
        updatedProducto
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaElaboracion(UPDATED_FECHA_ELABORACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);
        ProductoDTO productoDTO = productoMapper.toDto(updatedProducto);

        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductoToMatchAllProperties(updatedProducto);
    }

    @Test
    @Transactional
    void putNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto.identificador(UPDATED_IDENTIFICADOR).cantidad(UPDATED_CANTIDAD);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProducto, producto), getPersistedProducto(producto));
    }

    @Test
    @Transactional
    void fullUpdateProductoWithPatch() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the producto using partial update
        Producto partialUpdatedProducto = new Producto();
        partialUpdatedProducto.setId(producto.getId());

        partialUpdatedProducto
            .identificador(UPDATED_IDENTIFICADOR)
            .nombre(UPDATED_NOMBRE)
            .descripcion(UPDATED_DESCRIPCION)
            .fechaElaboracion(UPDATED_FECHA_ELABORACION)
            .lote(UPDATED_LOTE)
            .cantidad(UPDATED_CANTIDAD)
            .unidadMedida(UPDATED_UNIDAD_MEDIDA);

        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProducto))
            )
            .andExpect(status().isOk());

        // Validate the Producto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductoUpdatableFieldsEquals(partialUpdatedProducto, getPersistedProducto(partialUpdatedProducto));
    }

    @Test
    @Transactional
    void patchNonExistingProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        producto.setId(longCount.incrementAndGet());

        // Create the Producto
        ProductoDTO productoDTO = productoMapper.toDto(producto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducto() throws Exception {
        // Initialize the database
        insertedProducto = productoRepository.saveAndFlush(producto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the producto
        restProductoMockMvc
            .perform(delete(ENTITY_API_URL_ID, producto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return productoRepository.count();
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

    protected Producto getPersistedProducto(Producto producto) {
        return productoRepository.findById(producto.getId()).orElseThrow();
    }

    protected void assertPersistedProductoToMatchAllProperties(Producto expectedProducto) {
        assertProductoAllPropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }

    protected void assertPersistedProductoToMatchUpdatableProperties(Producto expectedProducto) {
        assertProductoAllUpdatablePropertiesEquals(expectedProducto, getPersistedProducto(expectedProducto));
    }
}
