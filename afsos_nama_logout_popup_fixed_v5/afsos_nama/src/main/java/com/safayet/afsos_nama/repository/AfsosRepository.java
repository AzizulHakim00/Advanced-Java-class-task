package com.safayet.afsos_nama.repository;

import com.safayet.afsos_nama.model.Afsos;
import com.safayet.afsos_nama.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfsosRepository extends JpaRepository<Afsos, Integer> {

    // Find all Afsos entries created by a specific user.
    // Results are sorted by regretDate from newest to oldest.
    List<Afsos> findByUserOrderByRegretDateDesc(User user);

    // Find anonymous Afsos entries that have been approved for the public feed.
    // Results are sorted by regretDate from newest to oldest.
    List<Afsos> findBySharedAnonymouslyTrueAndApprovedForFeedTrueOrderByRegretDateDesc();

    // Find anonymous Afsos entries that are still waiting for admin approval.
    // Results are sorted by regretDate from newest to oldest.
    List<Afsos> findBySharedAnonymouslyTrueAndApprovedForFeedFalseOrderByRegretDateDesc();
}