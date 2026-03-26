package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Separacion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Separacion entity.
 */
@Repository
public interface SeparacionRepository extends JpaRepository<Separacion, Long> {
    default Optional<Separacion> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Separacion> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Separacion> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select separacion from Separacion separacion left join fetch separacion.pedido",
        countQuery = "select count(separacion) from Separacion separacion"
    )
    Page<Separacion> findAllWithToOneRelationships(Pageable pageable);

    @Query("select separacion from Separacion separacion left join fetch separacion.pedido")
    List<Separacion> findAllWithToOneRelationships();

    @Query("select separacion from Separacion separacion left join fetch separacion.pedido where separacion.id =:id")
    Optional<Separacion> findOneWithToOneRelationships(@Param("id") Long id);
}
