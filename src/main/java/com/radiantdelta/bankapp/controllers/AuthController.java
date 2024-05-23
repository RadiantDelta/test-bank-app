package com.radiantdelta.bankapp.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.radiantdelta.bankapp.config.auth.TokenProvider;
import com.radiantdelta.bankapp.dtos.SignInDto;
import com.radiantdelta.bankapp.dtos.SignUpDto;
import com.radiantdelta.bankapp.entities.User;
import com.radiantdelta.bankapp.services.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private AuthService service;
  @Autowired
  private TokenProvider tokenService;

  @PostMapping("/signup")
  public ResponseEntity<?> signUp(@RequestBody /*@Valid*/ SignUpDto data) {
    service.signUp(data);
    return ResponseEntity.status(HttpStatus.CREATED).build();

  }

  @PostMapping("/signin")
 // public ResponseEntity<JwtDto> signIn(@RequestBody @Valid SignInDto data) {
  public ResponseEntity<?> signIn(@RequestBody @Valid SignInDto data) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

    var authUser = authenticationManager.authenticate(usernamePassword);

    var accessToken = tokenService.generateAccessToken((User) authUser.getPrincipal());
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " +accessToken);

   // return ResponseEntity.ok(new JwtDto(accessToken));
    return ResponseEntity.ok().headers(headers).build();
  }

}