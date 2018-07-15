package com.foxcode.adex.repository;

import com.foxcode.adex.domain.Distance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Distance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DistanceRepository extends JpaRepository<Distance, Long> {

}
