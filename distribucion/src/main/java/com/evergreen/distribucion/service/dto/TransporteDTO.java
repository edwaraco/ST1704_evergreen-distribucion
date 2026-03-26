package com.evergreen.distribucion.service.dto;

import com.evergreen.distribucion.domain.enumeration.TipoTransporte;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.evergreen.distribucion.domain.Transporte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransporteDTO implements Serializable {

    private Long id;

    @NotNull
    private String identificador;

    @NotNull
    private TipoTransporte tipoTransporte;

    @Size(max = 50)
    private String matricula;

    @DecimalMin(value = "0")
    private Double capacidadKg;

    @DecimalMin(value = "0")
    private Double capacidadM3;

    @NotNull
    @Size(max = 50)
    private String estado;

    private LocalDate fechaAsignacion;

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

    public TipoTransporte getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporte tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Double getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(Double capacidadKg) {
        this.capacidadKg = capacidadKg;
    }

    public Double getCapacidadM3() {
        return capacidadM3;
    }

    public void setCapacidadM3(Double capacidadM3) {
        this.capacidadM3 = capacidadM3;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDate fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransporteDTO)) {
            return false;
        }

        TransporteDTO transporteDTO = (TransporteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transporteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransporteDTO{" +
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
