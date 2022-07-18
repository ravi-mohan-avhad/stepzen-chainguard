package com.chainguard.sz.api.repository;

import com.chainguard.sz.api.domain.UserRecords;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserRecords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRecordsRepository extends JpaRepository<UserRecords, Long> {}
