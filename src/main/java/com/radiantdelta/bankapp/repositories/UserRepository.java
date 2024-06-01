package com.radiantdelta.bankapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import com.radiantdelta.bankapp.domain.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Transactional(readOnly = true)
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
  Page<User> findAll(Pageable pageable);

  @Query("SELECT u FROM users u WHERE u.dob > :dob")
  Page<User> findByDob(@Param("dob") Date dob, Pageable pageable);

  @Query("SELECT u FROM users u WHERE u IN (SELECT p.user FROM phones p WHERE p.phone = :phone)")
  Page<User> findByPhone(@Param("phone") String phone, Pageable pageable);

  @Query("SELECT u FROM users u WHERE u IN (SELECT p.user FROM phones p WHERE p.phone = :phone)")
  User findByPhoneNotPage(@Param("phone") String phone);
  @Query("SELECT u FROM users u WHERE u.fio LIKE :fio%")
  Page<User> findByFio(@Param("fio") String fio, Pageable pageable);

  @Query("SELECT u FROM users u WHERE u.fio LIKE :fio%")
  User findByFioNotPage(@Param("fio") String fio);
  @Query("SELECT u FROM users u WHERE u IN (SELECT e.user FROM emails e WHERE e.emailstr = :email)")
  Page<User> findByEmail(@Param("email") String email, Pageable pageable);
  UserDetails findByLogin(String login);
  @Transactional
  User saveAndFlush(User user);
}
