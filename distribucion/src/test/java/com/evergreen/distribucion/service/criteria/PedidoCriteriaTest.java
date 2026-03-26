package com.evergreen.distribucion.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PedidoCriteriaTest {

    @Test
    void newPedidoCriteriaHasAllFiltersNullTest() {
        var pedidoCriteria = new PedidoCriteria();
        assertThat(pedidoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pedidoCriteriaFluentMethodsCreatesFiltersTest() {
        var pedidoCriteria = new PedidoCriteria();

        setAllFilters(pedidoCriteria);

        assertThat(pedidoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pedidoCriteriaCopyCreatesNullFilterTest() {
        var pedidoCriteria = new PedidoCriteria();
        var copy = pedidoCriteria.copy();

        assertThat(pedidoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pedidoCriteria)
        );
    }

    @Test
    void pedidoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pedidoCriteria = new PedidoCriteria();
        setAllFilters(pedidoCriteria);

        var copy = pedidoCriteria.copy();

        assertThat(pedidoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pedidoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pedidoCriteria = new PedidoCriteria();

        assertThat(pedidoCriteria).hasToString("PedidoCriteria{}");
    }

    private static void setAllFilters(PedidoCriteria pedidoCriteria) {
        pedidoCriteria.id();
        pedidoCriteria.identificador();
        pedidoCriteria.fechaEntrada();
        pedidoCriteria.fechaSalida();
        pedidoCriteria.estado();
        pedidoCriteria.empaqueId();
        pedidoCriteria.separacionId();
        pedidoCriteria.clienteId();
        pedidoCriteria.productoId();
        pedidoCriteria.canalComercializacionId();
        pedidoCriteria.transporteId();
        pedidoCriteria.distinct();
    }

    private static Condition<PedidoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getIdentificador()) &&
                condition.apply(criteria.getFechaEntrada()) &&
                condition.apply(criteria.getFechaSalida()) &&
                condition.apply(criteria.getEstado()) &&
                condition.apply(criteria.getEmpaqueId()) &&
                condition.apply(criteria.getSeparacionId()) &&
                condition.apply(criteria.getClienteId()) &&
                condition.apply(criteria.getProductoId()) &&
                condition.apply(criteria.getCanalComercializacionId()) &&
                condition.apply(criteria.getTransporteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PedidoCriteria> copyFiltersAre(PedidoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getIdentificador(), copy.getIdentificador()) &&
                condition.apply(criteria.getFechaEntrada(), copy.getFechaEntrada()) &&
                condition.apply(criteria.getFechaSalida(), copy.getFechaSalida()) &&
                condition.apply(criteria.getEstado(), copy.getEstado()) &&
                condition.apply(criteria.getEmpaqueId(), copy.getEmpaqueId()) &&
                condition.apply(criteria.getSeparacionId(), copy.getSeparacionId()) &&
                condition.apply(criteria.getClienteId(), copy.getClienteId()) &&
                condition.apply(criteria.getProductoId(), copy.getProductoId()) &&
                condition.apply(criteria.getCanalComercializacionId(), copy.getCanalComercializacionId()) &&
                condition.apply(criteria.getTransporteId(), copy.getTransporteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
