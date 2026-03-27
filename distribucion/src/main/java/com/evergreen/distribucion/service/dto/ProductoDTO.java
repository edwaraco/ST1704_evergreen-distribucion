package com.evergreen.distribucion.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.evergreen.distribucion.domain.Producto} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoDTO implements Serializable {

    private Long id;

    @NotNull
    private String identificador;

    @NotNull
    @Size(max = 200)
    private String nombre;

    @Size(max = 1000)
    private String descripcion;

    @NotNull
    private LocalDate fechaElaboracion;

    @Size(max = 50)
    private String lote;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    @Size(max = 20)
    private String unidadMedida;

    private Set<PedidoDTO> pedidos = new HashSet<>();

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(LocalDate fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
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

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Set<PedidoDTO> getPedidos() {
        return pedidos;
    }

    public void setPedidos(Set<PedidoDTO> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductoDTO)) {
            return false;
        }

        ProductoDTO productoDTO = (ProductoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", fechaElaboracion='" + getFechaElaboracion() + "'" +
            ", lote='" + getLote() + "'" +
            ", cantidad=" + getCantidad() +
            ", unidadMedida='" + getUnidadMedida() + "'" +
            ", pedidos=" + getPedidos() +
            "}";
    }
}
