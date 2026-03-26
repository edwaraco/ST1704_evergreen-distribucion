package com.evergreen.distribucion.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.evergreen.distribucion.domain.Separacion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SeparacionDTO implements Serializable {

    private Long id;

    @NotNull
    private String identificador;

    @NotNull
    @Size(max = 50)
    private String lote;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    private LocalDate fechaRealizacion;

    @Size(max = 200)
    private String ubicacion;

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

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaRealizacion() {
        return fechaRealizacion;
    }

    public void setFechaRealizacion(LocalDate fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
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
        if (!(o instanceof SeparacionDTO)) {
            return false;
        }

        SeparacionDTO separacionDTO = (SeparacionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, separacionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeparacionDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", lote='" + getLote() + "'" +
            ", cantidad=" + getCantidad() +
            ", fechaRealizacion='" + getFechaRealizacion() + "'" +
            ", ubicacion='" + getUbicacion() + "'" +
            ", responsable='" + getResponsable() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", pedido=" + getPedido() +
            "}";
    }
}
