package com.foxcode.adex.repository;

import com.foxcode.adex.domain.CompanyMain;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompanyMain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompanyMainRepository extends JpaRepository<CompanyMain, Long> {

}
