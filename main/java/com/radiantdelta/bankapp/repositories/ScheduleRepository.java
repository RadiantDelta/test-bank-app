package com.radiantdelta.bankapp.repositories;

import com.radiantdelta.bankapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository  extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u WHERE  u.amount > 0 " +
            "and u.amount / u.startAmount < :maxAddVal")
    List<User> findAllForAddInterest(float maxAddVal);

}
