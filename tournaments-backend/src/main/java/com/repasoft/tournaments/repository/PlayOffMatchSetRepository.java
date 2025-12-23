package com.repasoft.tournaments.repository;

import com.repasoft.tournaments.model.PlayOffMatchSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayOffMatchSetRepository extends JpaRepository<PlayOffMatchSet, Long> {
}
