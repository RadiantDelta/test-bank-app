package com.radiantdelta.bankapp.repositories;

import com.radiantdelta.bankapp.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Email findByEmailstr(String emailstr);


    @Transactional
    @Modifying
    @Query("delete from emails e where e.id = :entityId")
    void delete(int entityId);
}
