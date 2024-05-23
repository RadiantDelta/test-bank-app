package com.m1guelsb.springauth.repositories;

import com.m1guelsb.springauth.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface EmailRepository extends JpaRepository<Email, Long> {
   // Email findByEmail(String email);
    Email findByEmailstr(String emailstr);


    @Transactional
    @Modifying
    @Query("delete from emails e where e.id = :entityId")
    void delete(int entityId);
}
