package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Pedido;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pedido entity.
 *
 * When extending this class, extend PedidoRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PedidoRepository
    extends PedidoRepositoryWithBagRelationships, JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
    default Optional<Pedido> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Pedido> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Pedido> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select pedido from Pedido pedido left join fetch pedido.cliente left join fetch pedido.canalComercializacion left join fetch pedido.transporte",
        countQuery = "select count(pedido) from Pedido pedido"
    )
    Page<Pedido> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pedido from Pedido pedido left join fetch pedido.cliente left join fetch pedido.canalComercializacion left join fetch pedido.transporte"
    )
    List<Pedido> findAllWithToOneRelationships();

    @Query(
        "select pedido from Pedido pedido left join fetch pedido.cliente left join fetch pedido.canalComercializacion left join fetch pedido.transporte where pedido.id =:id"
    )
    Optional<Pedido> findOneWithToOneRelationships(@Param("id") Long id);
}
