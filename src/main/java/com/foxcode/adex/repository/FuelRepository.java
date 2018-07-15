package com.foxcode.adex.repository;

import com.foxcode.adex.domain.Fuel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Fuel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FuelRepository extends JpaRepository<Fuel, Long> {

}
