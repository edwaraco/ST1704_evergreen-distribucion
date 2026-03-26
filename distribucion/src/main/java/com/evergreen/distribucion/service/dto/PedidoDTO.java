package com.evergreen.distribucion.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.evergreen.distribucion.domain.Pedido} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoDTO implements Serializable {

    private Long id;

    @NotNull
    private String identificador;

    @NotNull
    private LocalDate fechaEntrada;

    private LocalDate fechaSalida;

    @NotNull
    @Size(max = 50)
    private String estado;

    @NotNull
    private ClienteDTO cliente;

    private Set<ProductoDTO> productos = new HashSet<>();

    private CanalComercializacionDTO canalComercializacion;

    private TransporteDTO transporte;

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

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public Set<ProductoDTO> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoDTO> productos) {
        this.productos = productos;
    }

    public CanalComercializacionDTO getCanalComercializacion() {
        return canalComercializacion;
    }

    public void setCanalComercializacion(CanalComercializacionDTO canalComercializacion) {
        this.canalComercializacion = canalComercializacion;
    }

    public TransporteDTO getTransporte() {
        return transporte;
    }

    public void setTransporte(TransporteDTO transporte) {
        this.transporte = transporte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PedidoDTO)) {
            return false;
        }

        PedidoDTO pedidoDTO = (PedidoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pedidoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoDTO{" +
            "id=" + getId() +
            ", identificador='" + getIdentificador() + "'" +
            ", fechaEntrada='" + getFechaEntrada() + "'" +
            ", fechaSalida='" + getFechaSalida() + "'" +
            ", estado='" + getEstado() + "'" +
            ", cliente=" + getCliente() +
            ", productos=" + getProductos() +
            ", canalComercializacion=" + getCanalComercializacion() +
            ", transporte=" + getTransporte() +
            "}";
    }
}
