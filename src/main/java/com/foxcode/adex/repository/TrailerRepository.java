package com.foxcode.adex.repository;

import com.foxcode.adex.domain.Trailer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Trailer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrailerRepository extends JpaRepository<Trailer, Long> {

}
