package com.chainguard.sz.api.repository;

import com.chainguard.sz.api.domain.Parties;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Parties entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PartiesRepository extends JpaRepository<Parties, Long> {}
