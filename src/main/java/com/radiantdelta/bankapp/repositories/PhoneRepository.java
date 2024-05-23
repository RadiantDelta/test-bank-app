package com.radiantdelta.bankapp.repositories;

import com.radiantdelta.bankapp.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface PhoneRepository extends JpaRepository<Phone, Long> {
    Phone findByPhone(String phone);
    @Transactional
    @Modifying
    @Query("delete from phones p where p.id = :entityId")
    void delete(int entityId);

}
