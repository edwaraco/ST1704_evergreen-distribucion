package com.evergreen.distribucion.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.evergreen.distribucion.domain.Cliente} entity. This class is used
 * in {@link com.evergreen.distribucion.web.rest.ClienteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /clientes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClienteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter identificador;

    private StringFilter nombre;

    private StringFilter email;

    private StringFilter telefono;

    private StringFilter direccion;

    private Boolean distinct;

    public ClienteCriteria() {}

    public ClienteCriteria(ClienteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.identificador = other.optionalIdentificador().map(StringFilter::copy).orElse(null);
        this.nombre = other.optionalNombre().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.telefono = other.optionalTelefono().map(StringFilter::copy).orElse(null);
        this.direccion = other.optionalDireccion().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ClienteCriteria copy() {
        return new ClienteCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefono() {
        return telefono;
    }

    public Optional<StringFilter> optionalTelefono() {
        return Optional.ofNullable(telefono);
    }

    public StringFilter telefono() {
        if (telefono == null) {
            setTelefono(new StringFilter());
        }
        return telefono;
    }

    public void setTelefono(StringFilter telefono) {
        this.telefono = telefono;
    }

    public StringFilter getDireccion() {
        return direccion;
    }

    public Optional<StringFilter> optionalDireccion() {
        return Optional.ofNullable(direccion);
    }

    public StringFilter direccion() {
        if (direccion == null) {
            setDireccion(new StringFilter());
        }
        return direccion;
    }

    public void setDireccion(StringFilter direccion) {
        this.direccion = direccion;
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
        final ClienteCriteria that = (ClienteCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(identificador, that.identificador) &&
            Objects.equals(nombre, that.nombre) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefono, that.telefono) &&
            Objects.equals(direccion, that.direccion) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identificador, nombre, email, telefono, direccion, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClienteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIdentificador().map(f -> "identificador=" + f + ", ").orElse("") +
            optionalNombre().map(f -> "nombre=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalTelefono().map(f -> "telefono=" + f + ", ").orElse("") +
            optionalDireccion().map(f -> "direccion=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
