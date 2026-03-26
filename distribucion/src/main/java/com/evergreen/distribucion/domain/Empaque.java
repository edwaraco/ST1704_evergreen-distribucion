package com.evergreen.distribucion.domain;

import com.evergreen.distribucion.domain.enumeration.TamanioEmpaque;
import com.evergreen.distribucion.domain.enumeration.TipoEmpaque;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Empaque.
 */
@Entity
@Table(name = "empaque")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Empaque implements Serializable {

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
    @Column(name = "tipo", nullable = false)
    private TipoEmpaque tipo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tamanio", nullable = false)
    private TamanioEmpaque tamanio;

    @NotNull
    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull
    @Min(value = 0)
    @Column(name = "tiempo_minutos", nullable = false)
    private Integer tiempoMinutos;

    @Column(name = "fecha_realizacion")
    private LocalDate fechaRealizacion;

    @Size(max = 200)
    @Column(name = "responsable", length = 200)
    private String responsable;

    @Size(max = 1000)
    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "empaques", "separacions", "cliente", "productos", "canalComercializacion", "transporte" },
        allowSetters = true
    )
    private Pedido pedido;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Empaque id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return this.identificador;
    }

    public Empaque identificador(String identificador) {
        this.setIdentificador(identificador);
        return this;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public TipoEmpaque getTipo() {
        return this.tipo;
    }

    public Empaque tipo(TipoEmpaque tipo) {
        this.setTipo(tipo);
        return this;
    }

    public void setTipo(TipoEmpaque tipo) {
        this.tipo = tipo;
    }

    public TamanioEmpaque getTamanio() {
        return this.tamanio;
    }

    public Empaque tamanio(TamanioEmpaque tamanio) {
        this.setTamanio(tamanio);
        return this;
    }

    public void setTamanio(TamanioEmpaque tamanio) {
        this.tamanio = tamanio;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Empaque cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getTiempoMinutos() {
        return this.tiempoMinutos;
    }

    public Empaque tiempoMinutos(Integer tiempoMinutos) {
        this.setTiempoMinutos(tiempoMinutos);
        return this;
    }

    public void setTiempoMinutos(Integer tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }

    public LocalDate getFechaRealizacion() {
        return this.fechaRealizacion;
    }

    public Empaque fechaRealizacion(LocalDate fechaRealizacion) {
        this.setFechaRealizacion(fechaRealizacion);
        return this;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getResponsable() {
        return this.responsable;
    }

    public Empaque responsable(String responsable) {
        this.setResponsable(responsable);
        return this;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Empaque observaciones(String observaciones) {
        this.setObservaciones(observaciones);
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Pedido getPedido() {
        return this.pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Empaque pedido(Pedido pedido) {
        this.setPedido(pedido);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empaque)) {
            return false;
        }
        return getId() != null && getId().equals(((Empaque) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Empaque{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", tamanio='" + getTamanio() + "'" +
            ", cantidad=" + getCantidad() +
            ", tiempoMinutos=" + getTiempoMinutos() +
            ", fechaRealizacion='" + getFechaRealizacion() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }
}
