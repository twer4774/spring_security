package com.walter.fastcampus.user.repository;

import com.walter.fastcampus.user.domain.SpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpUserRepository extends JpaRepository<SpUser, Long> {

    Optional<SpUser> findSpUserByEmail(String email);
}
