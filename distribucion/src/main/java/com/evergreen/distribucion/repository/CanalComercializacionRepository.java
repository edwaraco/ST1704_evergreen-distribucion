package com.evergreen.distribucion.repository;

import com.evergreen.distribucion.domain.CanalComercializacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CanalComercializacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CanalComercializacionRepository extends JpaRepository<CanalComercializacion, Long> {}
