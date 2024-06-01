package com.radiantdelta.bankapp.repositories;

import com.radiantdelta.bankapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository  extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u WHERE  u.amount > 0 " +
            "and u.amount / u.startAmount < :maxAddVal")
    List<User> findAllForAddInterest(float maxAddVal);

}
