package com.evergreen.distribucion.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TransporteCriteriaTest {

    @Test
    void newTransporteCriteriaHasAllFiltersNullTest() {
        var transporteCriteria = new TransporteCriteria();
        assertThat(transporteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void transporteCriteriaFluentMethodsCreatesFiltersTest() {
        var transporteCriteria = new TransporteCriteria();

        setAllFilters(transporteCriteria);

        assertThat(transporteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void transporteCriteriaCopyCreatesNullFilterTest() {
        var transporteCriteria = new TransporteCriteria();
        var copy = transporteCriteria.copy();

        assertThat(transporteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(transporteCriteria)
        );
    }

    @Test
    void transporteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var transporteCriteria = new TransporteCriteria();
        setAllFilters(transporteCriteria);

        var copy = transporteCriteria.copy();

        assertThat(transporteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(transporteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var transporteCriteria = new TransporteCriteria();

        assertThat(transporteCriteria).hasToString("TransporteCriteria{}");
    }

    private static void setAllFilters(TransporteCriteria transporteCriteria) {
        transporteCriteria.id();
        transporteCriteria.identificador();
        transporteCriteria.tipoTransporte();
        transporteCriteria.matricula();
        transporteCriteria.capacidadKg();
        transporteCriteria.capacidadM3();
        transporteCriteria.estado();
        transporteCriteria.fechaAsignacion();
        transporteCriteria.distinct();
    }

    private static Condition<TransporteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdentificador()) &&
                condition.apply(criteria.getTipoTransporte()) &&
                condition.apply(criteria.getMatricula()) &&
                condition.apply(criteria.getCapacidadKg()) &&
                condition.apply(criteria.getCapacidadM3()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getFechaAsignacion()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TransporteCriteria> copyFiltersAre(TransporteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdentificador(), copy.getIdentificador()) &&
                condition.apply(criteria.getTipoTransporte(), copy.getTipoTransporte()) &&
                condition.apply(criteria.getMatricula(), copy.getMatricula()) &&
                condition.apply(criteria.getCapacidadKg(), copy.getCapacidadKg()) &&
                condition.apply(criteria.getCapacidadM3(), copy.getCapacidadM3()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getFechaAsignacion(), copy.getFechaAsignacion()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
