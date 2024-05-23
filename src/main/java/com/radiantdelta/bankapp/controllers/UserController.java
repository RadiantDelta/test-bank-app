package com.radiantdelta.bankapp.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.radiantdelta.bankapp.config.auth.TokenProvider;
import com.radiantdelta.bankapp.dtos.ChangeDTO;
import com.radiantdelta.bankapp.dtos.UserDTO;
import com.radiantdelta.bankapp.entities.Email;
import com.radiantdelta.bankapp.entities.Phone;
import com.radiantdelta.bankapp.entities.User;
import com.radiantdelta.bankapp.exceptions.*;
import com.radiantdelta.bankapp.exceptions.LastDataException;
import com.radiantdelta.bankapp.exceptions.NoExistDataException;
import com.radiantdelta.bankapp.exceptions.NoTargetUserException;
import com.radiantdelta.bankapp.exceptions.NotEnougnAmountException;
import com.radiantdelta.bankapp.exceptions.RepeatedDataException;
import com.radiantdelta.bankapp.repositories.EmailRepository;
import com.radiantdelta.bankapp.repositories.PhoneRepository;
import com.radiantdelta.bankapp.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
  @Autowired
  TokenProvider tokenService;
  @Autowired
  UserRepository userRepository;

  @Autowired
  EmailRepository emailRepository;

  @Autowired
  PhoneRepository phoneRepository;

  @Transactional
  @GetMapping("/transfer")
  @Operation(summary = "Transfer money", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> transfer(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestParam @NotEmpty(message="Input phone cannot be empty") String phone,
                                         @RequestParam @NotNull(message="Input money cannot be null") float money) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);
      if(user.getAmount() < money) {
        log.info("/transfer  " + user.getPhoneList().get(0).getPhone() + " has not enough money on account");
        throw new NotEnougnAmountException(user.getPhoneList().get(0).getPhone() + " has not enough money on account");
      }
      else {
        user.setAmount(user.getAmount()-money);
        User targetUser = userRepository.findByPhoneNotPage(phone);
        if(targetUser == null) {
          log.info("/transfer  target user does not exist");
          throw new NoTargetUserException("target user does not exist");
        }
        targetUser.setAmount(targetUser.getAmount()+money);
        userRepository.saveAndFlush(user);
        userRepository.saveAndFlush(targetUser);
        log.info("/transfer  Money: " +money +" from user with phone " + user.getPhoneList().get(0).getPhone() +
                " transferred to user with phone: " + targetUser.getPhoneList().get(0).getPhone());
      }
    }

    return ResponseEntity.ok(bearerToken);
  }

  @GetMapping("/users")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<Map<String, Object>> getAllUsers(
          @RequestParam(required = false) String phone,
          @RequestParam(required = false) String email,
          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dob,
          @RequestParam(required = false) String fio,
          @RequestParam(required = false) String sortBy,
          @RequestParam(required = false) Boolean descTrueFalse,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "3") int size
  ) {

    try {
      List<User> users = new ArrayList<User>();
      Pageable paging;
      if (sortBy != null) {

        if (descTrueFalse != null) {
          paging = descTrueFalse ? PageRequest.of(page, size, Sort.by(sortBy).descending()) : PageRequest.of(page, size, Sort.by(sortBy));
        }
          else {
          paging = PageRequest.of(page, size, Sort.by(sortBy));
        }
      } else{
         paging = PageRequest.of(page, size);
      }
      Page<User> pageTuts;
      if (phone != null) {
        pageTuts = userRepository.findByPhone(phone, paging);
      }
      else if (email != null) {
        pageTuts = userRepository.findByEmail(email, paging);
      }
      else if (dob != null) {
        pageTuts = userRepository.findByDob(dob, paging);
      }
      else if (fio != null) {
        pageTuts = userRepository.findByFio(fio, paging);
      }
      else{
        pageTuts = userRepository.findAll(paging);
      }

      users = pageTuts.getContent();
      log.info("/users:  users list obtained");
      // HIDING USERS FIELDS
      List<UserDTO> usersDTO = new ArrayList<UserDTO>();
      usersDTO = users.stream().
              map( x -> UserDTO.from(x)).
              collect(Collectors.toList());
      log.info("/users:  users list -> usersDTO list");
      Map<String, Object> response = new HashMap<>();
      response.put("users", usersDTO);
      response.put("currentPage", pageTuts.getNumber());
      response.put("totalItems", pageTuts.getTotalElements());
      response.put("totalPages", pageTuts.getTotalPages());

      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

//http://localhost:8080/swagger-ui/index.html

  @PostMapping("/add-email")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> addEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestBody @NotEmpty(message="Input newEmail cannot be empty") String newEmail) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Email> emailsList = user.getEmailList();
      List<String> strEmailsList =emailsList.stream().map(Email::getEmail).collect(Collectors.toList());

      if( strEmailsList.contains(newEmail) ) {
        log.info("/add-email  " + newEmail + " is email of current user");
       throw new RepeatedDataException(newEmail + " is email of current user");
     }
     else if(emailRepository.findByEmailstr(newEmail) != null) {
       log.info("/add-email  The email: " + newEmail + " belongs to other user");
         throw new RepeatedDataException("The email: " + newEmail + " belongs to other user");
       }
       else{
         Email em = new Email(user, newEmail);

            user.addEmailToList(em);
            userRepository.saveAndFlush(user);
            log.info("/add-email  " + newEmail + " added");
       }
    }

    return ResponseEntity.ok(bearerToken);
  }

  @PostMapping("/add-phone")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> addPhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestBody @NotEmpty(message="Input newPhone cannot be empty") String newPhone) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Phone> phonesList = user.getPhoneList();
      List<String> strPhonesList =phonesList.stream().map(Phone::getPhone).collect(Collectors.toList());

      if( strPhonesList.contains(newPhone) ) {
        log.info("/add-phone  " + newPhone + " is phone of current user");
        throw new RepeatedDataException(newPhone + " is phone of current user");
      }
      else if(phoneRepository.findByPhone(newPhone) != null) {
        log.info("/add-phone  The email: " + newPhone + " belongs to other user");
        throw new RepeatedDataException("The phone: " + newPhone + " belongs to other user");
      }
      else{
        Phone ph = new Phone(user, newPhone);

        user.addPhoneToList(ph);
        userRepository.saveAndFlush(user);
        log.info("/add-phone  " + newPhone + " added");
      }
    }

    return ResponseEntity.ok(bearerToken);
  }


  @Transactional
  @DeleteMapping("/phone/{phone}")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> deletePhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @PathVariable @NotEmpty(message="Deleting Phone cannot be empty") String phone) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Phone> phonesList = user.getPhoneList();
      List<String> strPhonesList =phonesList.stream().map(Phone::getPhone).collect(Collectors.toList());
      if(strPhonesList.size() == 1) {
        log.info("DELETE-phone  The phone: " + phone + " is the only phone of current user");
        throw new LastDataException("The phone: " + phone + " is the only phone of current user");
      }
      else if( strPhonesList.contains(phone) ) {
        Phone oph = phoneRepository.findByPhone(phone);

        phoneRepository.delete(oph.getId());
        phoneRepository.flush();
        log.info("DELETE-phone " + phone + " is deleted");
      }
      else  {
        log.info("DELETE-phone  The phone: " + phone + " does not belong to current user");
        throw new NoExistDataException("The phone: " + phone + " does not belong to current user");
      }

    }

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @DeleteMapping("/email/{email}")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> deleteEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @PathVariable @NotEmpty(message="Deleting Email cannot be empty") String email) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Email> emailsList = user.getEmailList();
      List<String> strEmailsList =emailsList.stream().map(Email::getEmail).collect(Collectors.toList());
      if(strEmailsList.size() == 1) {
        log.info("DELETE-email  The email: " + email + " is the only email of current user");
        throw new LastDataException("The email: " + email + " is the only email of current user");
      }
      else if( strEmailsList.contains(email) ) {
        Email oem = emailRepository.findByEmailstr(email);

        emailRepository.delete(oem.getId());
        emailRepository.flush();
        log.info("DELETE-email " + email + " is deleted");
      }
      else  {
        log.info("DELETE-email The email: " + email + " does not belong to current user");
        throw new NoExistDataException("The email: " + email + " does not belong to current user");
      }

    }

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @PutMapping("/change-phone")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> changePhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @RequestBody @Valid ChangeDTO newAndOldPhone) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Phone> phonesList = user.getPhoneList();
      List<String> strPhonesList =phonesList.stream().map(Phone::getPhone).collect(Collectors.toList());

      if( strPhonesList.contains(newAndOldPhone.getNewVal()) ) {
        log.info("CHANGE-phone " + newAndOldPhone.getNewVal() + " is phone of current user");
        throw new RepeatedDataException(newAndOldPhone.getNewVal() + " is phone of current user");
      }
      else if(phoneRepository.findByPhone(newAndOldPhone.getNewVal()) != null) {
        log.info("CHANGE-phone  The phone: " + newAndOldPhone.getNewVal() + " belongs to other user");
        throw new RepeatedDataException("The phone: " + newAndOldPhone.getNewVal() + " belongs to other user");
      }
      else{
        if(strPhonesList.contains(newAndOldPhone.getOldVal())){

          Phone ph =new Phone(user, newAndOldPhone.getNewVal());

          Phone oph = phoneRepository.findByPhone(newAndOldPhone.getOldVal());
          phoneRepository.delete(oph.getId());
          phoneRepository.flush();
          phoneRepository.saveAndFlush(ph);
          log.info("CHANGE-phone  phone changed");
        }
        else{
          log.info("CHANGE-phone  The phone: " + newAndOldPhone.getOldVal() + " does not belong to current user");
          throw new NoExistDataException("The phone: " + newAndOldPhone.getOldVal() + " does not belong to current user");
        }
      }
    }

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @PutMapping("/change-email")
  @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> changeEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @RequestBody @Valid ChangeDTO newAndOldEmail) {
    if (bearerToken != null) {
      var login = tokenService.validateToken(bearerToken.replace("Bearer ", ""));
      var user = (User) userRepository.findByLogin(login);

      List<Email> emailsList = user.getEmailList();
      List<String> strEmailsList =emailsList.stream().map(Email::getEmail).collect(Collectors.toList());

      if( strEmailsList.contains(newAndOldEmail.getNewVal()) ) {
        log.info("CHANGE-email " + newAndOldEmail.getNewVal() + " is email of current user");
        throw new RepeatedDataException(newAndOldEmail.getNewVal() + " is email of current user");
      }
      else if(emailRepository.findByEmailstr(newAndOldEmail.getNewVal()) != null) {
        log.info("CHANGE-email  The email: " + newAndOldEmail.getNewVal() + " belongs to other user");
        throw new RepeatedDataException("The email: " + newAndOldEmail.getNewVal() + " belongs to other user");
      }
      else{
        if(strEmailsList.contains(newAndOldEmail.getOldVal())){

          Email em =new Email(user, newAndOldEmail.getNewVal());

          Email oem = emailRepository.findByEmailstr(newAndOldEmail.getOldVal());

          emailRepository.delete(oem.getId());
          emailRepository.flush();
          emailRepository.saveAndFlush(em);
          log.info("CHANGE-email email changed");
        }
        else{
          log.info("CHANGE-email The email: " + newAndOldEmail.getOldVal() + " does not belong to current user");
          throw new NoExistDataException("The email: " + newAndOldEmail.getOldVal() + " does not belong to current user");
        }
      }
    }

    return ResponseEntity.ok(bearerToken);
  }

}