package com.example.vmo1.repository;

import com.example.vmo1.model.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findByUsername(String username);

    Optional<Account> findByUsernameOrEmail(String username, String email);

    @Transactional
    @Modifying
    @Query("UPDATE Account a SET a.enable=true WHERE a.email=?1")
    void enableAccount(String email);

    Page<Account> findByRoles_name(String name, Pageable pageable);
}
