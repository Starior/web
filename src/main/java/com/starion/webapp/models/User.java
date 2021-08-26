package com.starion.webapp.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "t_users")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 45)
  private String username;

  @Column(nullable = false, unique = true, length = 45)
  private String email;

  @Column
  private String password;

  @Column(name = "enabled")
  private boolean enabled = true;

  @Enumerated(EnumType.STRING)
  @Column(name = "auth_provider")
  private AuthenticationProvider authProvider;

  @ManyToMany(fetch = FetchType.EAGER)
  public Set<Role> roles;

  public User() {}

public Role getRole(){
/*    return roles.stream().filter(data -> Object.equals(data, this.id)).findFirst().get();*/
    return this.roles.stream().findFirst().get();
}

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
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
    return this.enabled;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles();
  }

/*  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<Role> roles = this.getRoles();
    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    for (Role role : roles) {
      authorities.add(new SimpleGrantedAuthority(role.getName()));
    }

    return authorities;
  }*/


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public AuthenticationProvider getAuthProvider() {
    return authProvider;
  }

  public void setAuthProvider(AuthenticationProvider authProvider) {
    this.authProvider = authProvider;
  }

  public String getFullName(){
    return username;
  }
}
