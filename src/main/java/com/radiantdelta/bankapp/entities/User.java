package com.radiantdelta.bankapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.radiantdelta.bankapp.enums.UserRole;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table()
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

  @Id
  @Column(name = "user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name="login")
  private String login;
  @Column(name="password")
  private String password;


//  @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  //@JoinColumn(name = "phone_id")
  private List<Phone> phoneList;


 // @JsonManagedReference
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
  //@JoinColumn(name = "email_id")
  private List<Email> emailList;

  @Min(0)
  @Column(name="amount")
  private int amount;


  @Column(name="dob")
  private Date dob;

  @Column(name="fio")
  private String fio;

  @Column(name="role")
  @Enumerated(EnumType.STRING)
  private UserRole role;

public void addEmailToList(Email email) {this.emailList.add(email);}

  public List<Email> getEmailList() { return emailList; }
  public void setEmailList(List<Email> emailList) { this.emailList = emailList; }

  public void addPhoneToList(Phone phone) {this.phoneList.add(phone);}


  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public List<Phone> getPhoneList() { return phoneList; }
  public void setPhoneList(List<Phone> phoneList) { this.phoneList = phoneList; }

  public User(String login, String password, List<Phone> phoneList, List<Email> emailList, int amount, Date dob, String fio/*, UserRole role*/) {
    this.login = login;
    this.password = password;
    this.emailList = emailList;
    this.phoneList = phoneList;
    this.amount = amount;
    this.dob =dob;
    this.fio = fio;
//    this.role = role;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (this.role == UserRole.ADMIN) {
      return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
    }
    return List.of(new SimpleGrantedAuthority("ROLE_USER"));
  }

  @Override
  public String getUsername() {
    return login;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}