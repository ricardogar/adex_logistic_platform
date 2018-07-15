package com.foxcode.adex.repository;

import com.foxcode.adex.domain.CompanyFactory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanyFactory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyFactoryRepository extends JpaRepository<CompanyFactory, Long> {

}
