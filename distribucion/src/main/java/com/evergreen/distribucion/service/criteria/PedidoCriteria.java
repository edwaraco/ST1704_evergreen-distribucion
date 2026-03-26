package com.evergreen.distribucion.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evergreen.distribucion.domain.Pedido} entity. This class is used
 * in {@link com.evergreen.distribucion.web.rest.PedidoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /pedidos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PedidoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private LocalDateFilter fechaEntrada;

    private LocalDateFilter fechaSalida;

    private StringFilter estado;

    private LongFilter empaqueId;

    private LongFilter separacionId;

    private LongFilter clienteId;

    private LongFilter productoId;

    private LongFilter canalComercializacionId;

    private LongFilter transporteId;

    private Boolean distinct;

    public PedidoCriteria() {}

    public PedidoCriteria(PedidoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identificador = other.optionalIdentificador().map(StringFilter::copy).orElse(null);
        this.fechaEntrada = other.optionalFechaEntrada().map(LocalDateFilter::copy).orElse(null);
        this.fechaSalida = other.optionalFechaSalida().map(LocalDateFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.empaqueId = other.optionalEmpaqueId().map(LongFilter::copy).orElse(null);
        this.separacionId = other.optionalSeparacionId().map(LongFilter::copy).orElse(null);
        this.clienteId = other.optionalClienteId().map(LongFilter::copy).orElse(null);
        this.productoId = other.optionalProductoId().map(LongFilter::copy).orElse(null);
        this.canalComercializacionId = other.optionalCanalComercializacionId().map(LongFilter::copy).orElse(null);
        this.transporteId = other.optionalTransporteId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PedidoCriteria copy() {
        return new PedidoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getIdentificador() {
        return identificador;
    }

    public Optional<StringFilter> optionalIdentificador() {
        return Optional.ofNullable(identificador);
    }

    public StringFilter identificador() {
        if (identificador == null) {
            setIdentificador(new StringFilter());
        }
        return identificador;
    }

    public void setIdentificador(StringFilter identificador) {
        this.identificador = identificador;
    }

    public LocalDateFilter getFechaEntrada() {
        return fechaEntrada;
    }

    public Optional<LocalDateFilter> optionalFechaEntrada() {
        return Optional.ofNullable(fechaEntrada);
    }

    public LocalDateFilter fechaEntrada() {
        if (fechaEntrada == null) {
            setFechaEntrada(new LocalDateFilter());
        }
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDateFilter fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDateFilter getFechaSalida() {
        return fechaSalida;
    }

    public Optional<LocalDateFilter> optionalFechaSalida() {
        return Optional.ofNullable(fechaSalida);
    }

    public LocalDateFilter fechaSalida() {
        if (fechaSalida == null) {
            setFechaSalida(new LocalDateFilter());
        }
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateFilter fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public StringFilter getEstado() {
        return estado;
    }

    public Optional<StringFilter> optionalEstado() {
        return Optional.ofNullable(estado);
    }

    public StringFilter estado() {
        if (estado == null) {
            setEstado(new StringFilter());
        }
        return estado;
    }

    public void setEstado(StringFilter estado) {
        this.estado = estado;
    }

    public LongFilter getEmpaqueId() {
        return empaqueId;
    }

    public Optional<LongFilter> optionalEmpaqueId() {
        return Optional.ofNullable(empaqueId);
    }

    public LongFilter empaqueId() {
        if (empaqueId == null) {
            setEmpaqueId(new LongFilter());
        }
        return empaqueId;
    }

    public void setEmpaqueId(LongFilter empaqueId) {
        this.empaqueId = empaqueId;
    }

    public LongFilter getSeparacionId() {
        return separacionId;
    }

    public Optional<LongFilter> optionalSeparacionId() {
        return Optional.ofNullable(separacionId);
    }

    public LongFilter separacionId() {
        if (separacionId == null) {
            setSeparacionId(new LongFilter());
        }
        return separacionId;
    }

    public void setSeparacionId(LongFilter separacionId) {
        this.separacionId = separacionId;
    }

    public LongFilter getClienteId() {
        return clienteId;
    }

    public Optional<LongFilter> optionalClienteId() {
        return Optional.ofNullable(clienteId);
    }

    public LongFilter clienteId() {
        if (clienteId == null) {
            setClienteId(new LongFilter());
        }
        return clienteId;
    }

    public void setClienteId(LongFilter clienteId) {
        this.clienteId = clienteId;
    }

    public LongFilter getProductoId() {
        return productoId;
    }

    public Optional<LongFilter> optionalProductoId() {
        return Optional.ofNullable(productoId);
    }

    public LongFilter productoId() {
        if (productoId == null) {
            setProductoId(new LongFilter());
        }
        return productoId;
    }

    public void setProductoId(LongFilter productoId) {
        this.productoId = productoId;
    }

    public LongFilter getCanalComercializacionId() {
        return canalComercializacionId;
    }

    public Optional<LongFilter> optionalCanalComercializacionId() {
        return Optional.ofNullable(canalComercializacionId);
    }

    public LongFilter canalComercializacionId() {
        if (canalComercializacionId == null) {
            setCanalComercializacionId(new LongFilter());
        }
        return canalComercializacionId;
    }

    public void setCanalComercializacionId(LongFilter canalComercializacionId) {
        this.canalComercializacionId = canalComercializacionId;
    }

    public LongFilter getTransporteId() {
        return transporteId;
    }

    public Optional<LongFilter> optionalTransporteId() {
        return Optional.ofNullable(transporteId);
    }

    public LongFilter transporteId() {
        if (transporteId == null) {
            setTransporteId(new LongFilter());
        }
        return transporteId;
    }

    public void setTransporteId(LongFilter transporteId) {
        this.transporteId = transporteId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PedidoCriteria that = (PedidoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(fechaEntrada, that.fechaEntrada) &&
            Objects.equals(fechaSalida, that.fechaSalida) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(empaqueId, that.empaqueId) &&
            Objects.equals(separacionId, that.separacionId) &&
            Objects.equals(clienteId, that.clienteId) &&
            Objects.equals(productoId, that.productoId) &&
            Objects.equals(canalComercializacionId, that.canalComercializacionId) &&
            Objects.equals(transporteId, that.transporteId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            identificador,
            fechaEntrada,
            fechaSalida,
            estado,
            empaqueId,
            separacionId,
            clienteId,
            productoId,
            canalComercializacionId,
            transporteId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PedidoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentificador().map(f -> "identificador=" + f + ", ").orElse("") +
            optionalFechaEntrada().map(f -> "fechaEntrada=" + f + ", ").orElse("") +
            optionalFechaSalida().map(f -> "fechaSalida=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalEmpaqueId().map(f -> "empaqueId=" + f + ", ").orElse("") +
            optionalSeparacionId().map(f -> "separacionId=" + f + ", ").orElse("") +
            optionalClienteId().map(f -> "clienteId=" + f + ", ").orElse("") +
            optionalProductoId().map(f -> "productoId=" + f + ", ").orElse("") +
            optionalCanalComercializacionId().map(f -> "canalComercializacionId=" + f + ", ").orElse("") +
            optionalTransporteId().map(f -> "transporteId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
