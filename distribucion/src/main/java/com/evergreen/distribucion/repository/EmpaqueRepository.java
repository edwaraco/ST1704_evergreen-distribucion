package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Empaque;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Empaque entity.
 */
@Repository
public interface EmpaqueRepository extends JpaRepository<Empaque, Long> {
    default Optional<Empaque> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Empaque> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Empaque> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select empaque from Empaque empaque left join fetch empaque.pedido",
        countQuery = "select count(empaque) from Empaque empaque"
    )
    Page<Empaque> findAllWithToOneRelationships(Pageable pageable);

    @Query("select empaque from Empaque empaque left join fetch empaque.pedido")
    List<Empaque> findAllWithToOneRelationships();

    @Query("select empaque from Empaque empaque left join fetch empaque.pedido where empaque.id =:id")
    Optional<Empaque> findOneWithToOneRelationships(@Param("id") Long id);
}
