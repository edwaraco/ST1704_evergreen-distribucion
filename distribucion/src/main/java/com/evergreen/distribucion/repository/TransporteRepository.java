package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.Transporte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transporte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Long>, JpaSpecificationExecutor<Transporte> {}
