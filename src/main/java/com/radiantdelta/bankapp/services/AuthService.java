package com.radiantdelta.bankapp.services;

import com.radiantdelta.bankapp.config.auth.TokenProvider;
import com.radiantdelta.bankapp.entities.Phone;
import com.radiantdelta.bankapp.repositories.EmailRepository;
import com.radiantdelta.bankapp.repositories.PhoneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
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
  @Autowired
  private TokenProvider tokenService;

  @Override
  public UserDetails loadUserByUsername(String username) {
    return repository.findByLogin(username);
  }

  public UserDetails signUp(SignUpDto data) throws InvalidJwtException {



    if (repository.findByLogin(data.getLogin()) != null) {
      log.info("AuthService:  Username already exists");
      throw new InvalidJwtException("Username already exists");
    }


    if (emailRepository.findByEmailstr(data.getEmailList().get(0).getEmail()) != null) {
      log.info("AuthService:  Email already exists");
      throw new InvalidJwtException("Email already exists");
    }

    if (phoneRepository.findByPhone(data.getPhoneList().get(0).getPhone()) != null) {
      log.info("AuthService:  Phone already exists");
      throw new InvalidJwtException("Phone already exists");
    }

    if (data.getAmount() < 0) {
      log.info("AuthService:  balance is negative");
      throw new InvalidJwtException("Negative amount ");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

    User newUser = new User(data.getLogin(), encryptedPassword, new ArrayList<>(), new ArrayList<>(), data.getAmount(), data.getDob(), data.getFio(), data.getAmount());


    Email em = new Email(newUser, data.getEmailList().get(0).getEmail());

    newUser.addEmailToList(em);

    Phone ph = new Phone(newUser, data.getPhoneList().get(0).getPhone());
    newUser.addPhoneToList(ph);

    return repository.saveAndFlush(newUser);

  }

  public HttpHeaders signIn(Authentication authUser) {

    var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " +accessToken);
    return headers;
  }


}