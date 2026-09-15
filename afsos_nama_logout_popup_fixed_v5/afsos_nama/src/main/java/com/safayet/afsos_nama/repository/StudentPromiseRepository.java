package com.safayet.afsos_nama.repository;

import com.safayet.afsos_nama.model.StudentPromise;
import com.safayet.afsos_nama.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPromiseRepository extends JpaRepository<StudentPromise, Integer> {

    List<StudentPromise> findByUserOrderByTargetDateAsc(User user);
}
