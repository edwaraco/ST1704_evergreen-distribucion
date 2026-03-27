package com.evergreen.distribucion.domain;

import com.evergreen.distribucion.domain.enumeration.TipoTransporte;
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
 * A Transporte.
 */
@Entity
@Table(name = "transporte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transporte implements Serializable {

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
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transporte", nullable = false)
    private TipoTransporte tipoTransporte;

    @Size(max = 50)
    @Column(name = "matricula", length = 50)
    private String matricula;

    @DecimalMin(value = "0")
    @Column(name = "capacidad_kg")
    private Double capacidadKg;

    @DecimalMin(value = "0")
    @Column(name = "capacidad_m_3")
    private Double capacidadM3;

    @NotNull
    @Size(max = 50)
    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "transporte")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "cliente", "productos", "canalComercializacion", "transporte" }, allowSetters = true)
    private Set<Pedido> pedidos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transporte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public Transporte identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public TipoTransporte getTipoTransporte() {
        return this.tipoTransporte;
    }

    public Transporte tipoTransporte(TipoTransporte tipoTransporte) {
        this.setTipoTransporte(tipoTransporte);
        return this;
    }

    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public Transporte matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getCapacidadKg() {
        return this.capacidadKg;
    }

    public Transporte capacidadKg(Double capacidadKg) {
        this.setCapacidadKg(capacidadKg);
        return this;
    }

    public void setCapacidadKg(Double capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public Double getCapacidadM3() {
        return this.capacidadM3;
    }

    public Transporte capacidadM3(Double capacidadM3) {
        this.setCapacidadM3(capacidadM3);
        return this;
    }

    public void setCapacidadM3(Double capacidadM3) {
        this.capacidadM3 = capacidadM3;
    }

    public String getEstado() {
        return this.estado;
    }

    public Transporte estado(String estado) {
        this.setEstado(estado);
        return this;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAsignacion() {
        return this.fechaAsignacion;
    }

    public Transporte fechaAsignacion(LocalDate fechaAsignacion) {
        this.setFechaAsignacion(fechaAsignacion);
        return this;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Set<Pedido> getPedidos() {
        return this.pedidos;
    }

    public void setPedidos(Set<Pedido> pedidos) {
        if (this.pedidos != null) {
            this.pedidos.forEach(i -> i.setTransporte(null));
        }
        if (pedidos != null) {
            pedidos.forEach(i -> i.setTransporte(this));
        }
        this.pedidos = pedidos;
    }

    public Transporte pedidos(Set<Pedido> pedidos) {
        this.setPedidos(pedidos);
        return this;
    }

    public Transporte addPedido(Pedido pedido) {
        this.pedidos.add(pedido);
        pedido.setTransporte(this);
        return this;
    }

    public Transporte removePedido(Pedido pedido) {
        this.pedidos.remove(pedido);
        pedido.setTransporte(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transporte)) {
            return false;
        }
        return getId() != null && getId().equals(((Transporte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transporte{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", tipoTransporte='" + getTipoTransporte() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", capacidadKg=" + getCapacidadKg() +
            ", capacidadM3=" + getCapacidadM3() +
            ", estado='" + getEstado() + "'" +
            ", fechaAsignacion='" + getFechaAsignacion() + "'" +
            "}";
    }
}
