package com.jsb.mysite.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SiteUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(unique = true)
  private String username;

  private String password;

  @Column(unique = true)
  private String email;

  public SiteUser(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }
}
