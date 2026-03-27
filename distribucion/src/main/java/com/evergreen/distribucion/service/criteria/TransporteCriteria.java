package com.evergreen.distribucion.service.criteria;

import com.evergreen.distribucion.domain.enumeration.TipoTransporte;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evergreen.distribucion.domain.Transporte} entity. This class is used
 * in {@link com.evergreen.distribucion.web.rest.TransporteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /transportes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransporteCriteria implements Serializable, Criteria {

    /**
     * Class for filtering TipoTransporte
     */
    public static class TipoTransporteFilter extends Filter<TipoTransporte> {

        public TipoTransporteFilter() {}

        public TipoTransporteFilter(TipoTransporteFilter filter) {
            super(filter);
        }

        @Override
        public TipoTransporteFilter copy() {
            return new TipoTransporteFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private TipoTransporteFilter tipoTransporte;

    private StringFilter matricula;

    private DoubleFilter capacidadKg;

    private DoubleFilter capacidadM3;

    private StringFilter estado;

    private LocalDateFilter fechaAsignacion;

    private Boolean distinct;

    public TransporteCriteria() {}

    public TransporteCriteria(TransporteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identificador = other.optionalIdentificador().map(StringFilter::copy).orElse(null);
        this.tipoTransporte = other.optionalTipoTransporte().map(TipoTransporteFilter::copy).orElse(null);
        this.matricula = other.optionalMatricula().map(StringFilter::copy).orElse(null);
        this.capacidadKg = other.optionalCapacidadKg().map(DoubleFilter::copy).orElse(null);
        this.capacidadM3 = other.optionalCapacidadM3().map(DoubleFilter::copy).orElse(null);
        this.estado = other.optionalEstado().map(StringFilter::copy).orElse(null);
        this.fechaAsignacion = other.optionalFechaAsignacion().map(LocalDateFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TransporteCriteria copy() {
        return new TransporteCriteria(this);
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

    public TipoTransporteFilter getTipoTransporte() {
        return tipoTransporte;
    }

    public Optional<TipoTransporteFilter> optionalTipoTransporte() {
        return Optional.ofNullable(tipoTransporte);
    }

    public TipoTransporteFilter tipoTransporte() {
        if (tipoTransporte == null) {
            setTipoTransporte(new TipoTransporteFilter());
        }
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporteFilter tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public StringFilter getMatricula() {
        return matricula;
    }

    public Optional<StringFilter> optionalMatricula() {
        return Optional.ofNullable(matricula);
    }

    public StringFilter matricula() {
        if (matricula == null) {
            setMatricula(new StringFilter());
        }
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
    }

    public DoubleFilter getCapacidadKg() {
        return capacidadKg;
    }

    public Optional<DoubleFilter> optionalCapacidadKg() {
        return Optional.ofNullable(capacidadKg);
    }

    public DoubleFilter capacidadKg() {
        if (capacidadKg == null) {
            setCapacidadKg(new DoubleFilter());
        }
        return capacidadKg;
    }

    public void setCapacidadKg(DoubleFilter capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public DoubleFilter getCapacidadM3() {
        return capacidadM3;
    }

    public Optional<DoubleFilter> optionalCapacidadM3() {
        return Optional.ofNullable(capacidadM3);
    }

    public DoubleFilter capacidadM3() {
        if (capacidadM3 == null) {
            setCapacidadM3(new DoubleFilter());
        }
        return capacidadM3;
    }

    public void setCapacidadM3(DoubleFilter capacidadM3) {
        this.capacidadM3 = capacidadM3;
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

    public LocalDateFilter getFechaAsignacion() {
        return fechaAsignacion;
    }

    public Optional<LocalDateFilter> optionalFechaAsignacion() {
        return Optional.ofNullable(fechaAsignacion);
    }

    public LocalDateFilter fechaAsignacion() {
        if (fechaAsignacion == null) {
            setFechaAsignacion(new LocalDateFilter());
        }
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateFilter fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
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
        final TransporteCriteria that = (TransporteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(tipoTransporte, that.tipoTransporte) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(capacidadKg, that.capacidadKg) &&
            Objects.equals(capacidadM3, that.capacidadM3) &&
            Objects.equals(estado, that.estado) &&
            Objects.equals(fechaAsignacion, that.fechaAsignacion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificador, tipoTransporte, matricula, capacidadKg, capacidadM3, estado, fechaAsignacion, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransporteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentificador().map(f -> "identificador=" + f + ", ").orElse("") +
            optionalTipoTransporte().map(f -> "tipoTransporte=" + f + ", ").orElse("") +
            optionalMatricula().map(f -> "matricula=" + f + ", ").orElse("") +
            optionalCapacidadKg().map(f -> "capacidadKg=" + f + ", ").orElse("") +
            optionalCapacidadM3().map(f -> "capacidadM3=" + f + ", ").orElse("") +
            optionalEstado().map(f -> "estado=" + f + ", ").orElse("") +
            optionalFechaAsignacion().map(f -> "fechaAsignacion=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
