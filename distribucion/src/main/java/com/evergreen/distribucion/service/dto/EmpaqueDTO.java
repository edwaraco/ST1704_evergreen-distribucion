package com.evergreen.distribucion.service.dto;

import com.evergreen.distribucion.domain.enumeration.TamanioEmpaque;
import com.evergreen.distribucion.domain.enumeration.TipoEmpaque;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.evergreen.distribucion.domain.Empaque} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EmpaqueDTO implements Serializable {

    private Long id;

    @NotNull
    private String identificador;

    @NotNull
    private TipoEmpaque tipo;

    @NotNull
    private TamanioEmpaque tamanio;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @NotNull
    @Min(value = 0)
    private Integer tiempoMinutos;

    private LocalDate fechaRealizacion;

    @Size(max = 200)
    private String responsable;

    @Size(max = 1000)
    private String observaciones;

    @NotNull
    private PedidoDTO pedido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public TipoEmpaque getTipo() {
        return tipo;
    }

    public void setTipo(TipoEmpaque tipo) {
        this.tipo = tipo;
    }

    public TamanioEmpaque getTamanio() {
        return tamanio;
    }

    public void setTamanio(TamanioEmpaque tamanio) {
        this.tamanio = tamanio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getTiempoMinutos() {
        return tiempoMinutos;
    }

    public void setTiempoMinutos(Integer tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public PedidoDTO getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDTO pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmpaqueDTO)) {
            return false;
        }

        EmpaqueDTO empaqueDTO = (EmpaqueDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, empaqueDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EmpaqueDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", tamanio='" + getTamanio() + "'" +
            ", cantidad=" + getCantidad() +
            ", tiempoMinutos=" + getTiempoMinutos() +
            ", fechaRealizacion='" + getFechaRealizacion() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", pedido=" + getPedido() +
            "}";
    }
}
