package com.radiantdelta.bankapp.services;

import com.radiantdelta.bankapp.entities.Phone;
import com.radiantdelta.bankapp.repositories.EmailRepository;
import com.radiantdelta.bankapp.repositories.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.radiantdelta.bankapp.dtos.SignUpDto;
import com.radiantdelta.bankapp.entities.User;
import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.exceptions.InvalidJwtException;
import com.radiantdelta.bankapp.repositories.UserRepository;

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