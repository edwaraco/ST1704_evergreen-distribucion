package com.evergreen.distribucion.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evergreen.distribucion.domain.Producto} entity. This class is used
 * in {@link com.evergreen.distribucion.web.rest.ProductoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /productos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private StringFilter nombre;

    private StringFilter descripcion;

    private LocalDateFilter fechaElaboracion;

    private StringFilter lote;

    private IntegerFilter cantidad;

    private StringFilter unidadMedida;

    private Boolean distinct;

    public ProductoCriteria() {}

    public ProductoCriteria(ProductoCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identificador = other.optionalIdentificador().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.descripcion = other.optionalDescripcion().map(StringFilter::copy).orElse(null);
        this.fechaElaboracion = other.optionalFechaElaboracion().map(LocalDateFilter::copy).orElse(null);
        this.lote = other.optionalLote().map(StringFilter::copy).orElse(null);
        this.cantidad = other.optionalCantidad().map(IntegerFilter::copy).orElse(null);
        this.unidadMedida = other.optionalUnidadMedida().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductoCriteria copy() {
        return new ProductoCriteria(this);
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

    public StringFilter getNombre() {
        return nombre;
    }

    public Optional<StringFilter> optionalNombre() {
        return Optional.ofNullable(nombre);
    }

    public StringFilter nombre() {
        if (nombre == null) {
            setNombre(new StringFilter());
        }
        return nombre;
    }

    public void setNombre(StringFilter nombre) {
        this.nombre = nombre;
    }

    public StringFilter getDescripcion() {
        return descripcion;
    }

    public Optional<StringFilter> optionalDescripcion() {
        return Optional.ofNullable(descripcion);
    }

    public StringFilter descripcion() {
        if (descripcion == null) {
            setDescripcion(new StringFilter());
        }
        return descripcion;
    }

    public void setDescripcion(StringFilter descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateFilter getFechaElaboracion() {
        return fechaElaboracion;
    }

    public Optional<LocalDateFilter> optionalFechaElaboracion() {
        return Optional.ofNullable(fechaElaboracion);
    }

    public LocalDateFilter fechaElaboracion() {
        if (fechaElaboracion == null) {
            setFechaElaboracion(new LocalDateFilter());
        }
        return fechaElaboracion;
    }

    public void setFechaElaboracion(LocalDateFilter fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public StringFilter getLote() {
        return lote;
    }

    public Optional<StringFilter> optionalLote() {
        return Optional.ofNullable(lote);
    }

    public StringFilter lote() {
        if (lote == null) {
            setLote(new StringFilter());
        }
        return lote;
    }

    public void setLote(StringFilter lote) {
        this.lote = lote;
    }

    public IntegerFilter getCantidad() {
        return cantidad;
    }

    public Optional<IntegerFilter> optionalCantidad() {
        return Optional.ofNullable(cantidad);
    }

    public IntegerFilter cantidad() {
        if (cantidad == null) {
            setCantidad(new IntegerFilter());
        }
        return cantidad;
    }

    public void setCantidad(IntegerFilter cantidad) {
        this.cantidad = cantidad;
    }

    public StringFilter getUnidadMedida() {
        return unidadMedida;
    }

    public Optional<StringFilter> optionalUnidadMedida() {
        return Optional.ofNullable(unidadMedida);
    }

    public StringFilter unidadMedida() {
        if (unidadMedida == null) {
            setUnidadMedida(new StringFilter());
        }
        return unidadMedida;
    }

    public void setUnidadMedida(StringFilter unidadMedida) {
        this.unidadMedida = unidadMedida;
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
        final ProductoCriteria that = (ProductoCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(descripcion, that.descripcion) &&
            Objects.equals(fechaElaboracion, that.fechaElaboracion) &&
            Objects.equals(lote, that.lote) &&
            Objects.equals(cantidad, that.cantidad) &&
            Objects.equals(unidadMedida, that.unidadMedida) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificador, nombre, descripcion, fechaElaboracion, lote, cantidad, unidadMedida, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductoCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentificador().map(f -> "identificador=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalDescripcion().map(f -> "descripcion=" + f + ", ").orElse("") +
            optionalFechaElaboracion().map(f -> "fechaElaboracion=" + f + ", ").orElse("") +
            optionalLote().map(f -> "lote=" + f + ", ").orElse("") +
            optionalCantidad().map(f -> "cantidad=" + f + ", ").orElse("") +
            optionalUnidadMedida().map(f -> "unidadMedida=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
