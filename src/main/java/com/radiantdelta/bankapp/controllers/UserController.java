package com.radiantdelta.bankapp.controllers;

import java.util.*;
import com.radiantdelta.bankapp.dtos.ChangeDTO;
import com.radiantdelta.bankapp.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
  UserService userService;


  @Transactional
  @GetMapping("/transfer")
  @Operation(summary = "Transfer money", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> transfer(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestParam @NotEmpty(message="Input phone cannot be empty") String phone,
                                         @RequestParam @NotNull(message="Input money cannot be null") float money) {
    userService.transfer(bearerToken,phone, money);

    return ResponseEntity.ok(bearerToken);
  }

  @GetMapping("/users")
  @Operation(summary = "Get users", security = @SecurityRequirement(name = "bearerAuth"))
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
      Map<String, Object> response = userService.getAllUsers( phone,  email,  dob,  fio,  sortBy,descTrueFalse, page,  size);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

//http://localhost:8080/swagger-ui/index.html

  @PostMapping("/add-email")
  @Operation(summary = "Add email", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> addEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestBody @NotEmpty(message="Input newEmail cannot be empty") String newEmail) {
    userService.addEmail(bearerToken, newEmail);

    return ResponseEntity.ok(bearerToken);
  }

  @PostMapping("/add-phone")
  @Operation(summary = "Add phone", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> addPhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                         @RequestBody @NotEmpty(message="Input newPhone cannot be empty") String newPhone) {
    userService.addPhone(bearerToken, newPhone);

    return ResponseEntity.ok(bearerToken);
  }


  @Transactional
  @DeleteMapping("/phone/{phone}")
  @Operation(summary = "Delete phone", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> deletePhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @PathVariable @NotEmpty(message="Deleting Phone cannot be empty") String phone) {
    userService.deletePhone(bearerToken, phone);

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @DeleteMapping("/email/{email}")
  @Operation(summary = "Delete email", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> deleteEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @PathVariable @NotEmpty(message="Deleting Email cannot be empty") String email) {
    userService.deleteEmail(bearerToken, email);

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @PutMapping("/change-phone")
  @Operation(summary = "Change phone", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> changePhone(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @RequestBody @Valid ChangeDTO newAndOldPhone) {
    userService.changePhone( bearerToken,  newAndOldPhone);

    return ResponseEntity.ok(bearerToken);
  }

  @Transactional
  @PutMapping("/change-email")
  @Operation(summary = "Change email", security = @SecurityRequirement(name = "bearerAuth"))
  public ResponseEntity<String> changeEmail(@RequestHeader("Authorization") @NotEmpty(message="Bearer token cannot be empty") String bearerToken,
                                            @RequestBody @Valid ChangeDTO newAndOldEmail) {
    userService.changeEmail(bearerToken, newAndOldEmail);

    return ResponseEntity.ok(bearerToken);
  }

}