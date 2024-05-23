package com.m1guelsb.springauth.services;

import com.m1guelsb.springauth.entities.Phone;
import com.m1guelsb.springauth.repositories.EmailRepository;
import com.m1guelsb.springauth.repositories.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.m1guelsb.springauth.dtos.SignUpDto;
import com.m1guelsb.springauth.entities.User;
import com.m1guelsb.springauth.entities.Email;
import com.m1guelsb.springauth.exceptions.InvalidJwtException;
import com.m1guelsb.springauth.repositories.UserRepository;

import java.util.ArrayList;


@Slf4j
@Service
public class AuthService implements UserDetailsService {

  @Autowired
  UserRepository repository;
  @Autowired
  EmailRepository emailRepository;
  @Autowired
  PhoneRepository phoneRepository;
  @Override
  public UserDetails loadUserByUsername(String username) {
    var user = repository.findByLogin(username);
    return user;
  }

  public UserDetails signUp(SignUpDto data) throws InvalidJwtException {

    log.info(data.getEmailList().get(0).getEmail());
    log.info(data.getLogin());
    log.info(data.getPhoneList().get(0).getPhone());

    if (repository.findByLogin(data.getLogin()) != null) {
      throw new InvalidJwtException("Username already exists");
    }


    if (emailRepository.findByEmailstr(data.getEmailList().get(0).getEmail()) != null) {
      throw new InvalidJwtException("Email already exists");
    }

    if (phoneRepository.findByPhone(data.getPhoneList().get(0).getPhone()) != null) {
      throw new InvalidJwtException("Phone already exists");
    }

    if (data.getAmount() < 0) {
      throw new InvalidJwtException("Negative amount ");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    User newUser = new User(data.getLogin(), encryptedPassword, new ArrayList<>(), new ArrayList<>(), data.getAmount(), data.getDob(), data.getFio());


    Email em = new Email(newUser, data.getEmailList().get(0).getEmail());
    log.info(data.getEmailList().get(0).getEmail());
    newUser.addEmailToList(em);

    Phone ph = new Phone(newUser, data.getPhoneList().get(0).getPhone());
    newUser.addPhoneToList(ph);

    return repository.saveAndFlush(newUser);

  }
}