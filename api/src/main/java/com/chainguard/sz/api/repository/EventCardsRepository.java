package com.chainguard.sz.api.repository;

import com.chainguard.sz.api.domain.EventCards;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EventCards entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventCardsRepository extends JpaRepository<EventCards, Long> {}
