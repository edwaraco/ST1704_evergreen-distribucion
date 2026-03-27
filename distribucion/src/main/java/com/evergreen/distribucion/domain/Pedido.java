package com.evergreen.distribucion.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Historia de Usuario 2: Gestión de Pedidos
 *
 * Como operador del sistema de distribución
 * Quiero gestionar pedidos asociando clientes, productos, canales y transportes
 * Para coordinar el flujo de distribución de productos
 *
 * Entidades nuevas:
 * - Pedido: Pedidos de clientes con sus productos asignados
 *
 * Dependencias: HU-1 (Catálogos base)
 *
 * NOTA: Este JDL solo contiene la entidad Pedido y sus relaciones.
 * Las entidades de HU-1 ya deben existir en el proyecto.
 */
@Entity
@Table(name = "pedido")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "identificador", nullable = false, unique = true)
    private String identificador;

    @NotNull
    @Column(name = "fecha_entrada", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @NotNull
    @Size(max = 50)
    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @ManyToOne(optional = false)
    @NotNull
    private Cliente cliente;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_pedido__producto",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pedidos" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pedidos" }, allowSetters = true)
    private CanalComercializacion canalComercializacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pedidos" }, allowSetters = true)
    private Transporte transporte;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pedido id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public Pedido identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public LocalDate getFechaEntrada() {
        return this.fechaEntrada;
    }

    public Pedido fechaEntrada(LocalDate fechaEntrada) {
        this.setFechaEntrada(fechaEntrada);
        return this;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return this.fechaSalida;
    }

    public Pedido fechaSalida(LocalDate fechaSalida) {
        this.setFechaSalida(fechaSalida);
        return this;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return this.estado;
    }

    public Pedido estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Pedido cliente(Cliente cliente) {
        this.setCliente(cliente);
        return this;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Pedido productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Pedido addProducto(Producto producto) {
        this.productos.add(producto);
        return this;
    }

    public Pedido removeProducto(Producto producto) {
        this.productos.remove(producto);
        return this;
    }

    public CanalComercializacion getCanalComercializacion() {
        return this.canalComercializacion;
    }

    public void setCanalComercializacion(CanalComercializacion canalComercializacion) {
        this.canalComercializacion = canalComercializacion;
    }

    public Pedido canalComercializacion(CanalComercializacion canalComercializacion) {
        this.setCanalComercializacion(canalComercializacion);
        return this;
    }

    public Transporte getTransporte() {
        return this.transporte;
    }

    public void setTransporte(Transporte transporte) {
        this.transporte = transporte;
    }

    public Pedido transporte(Transporte transporte) {
        this.setTransporte(transporte);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pedido)) {
            return false;
        }
        return getId() != null && getId().equals(((Pedido) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pedido{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", fechaSalida='" + getFechaSalida() + "'" +
            ", estado='" + getEstado() + "'" +
            "}";
    }
}
