package com.starion.webapp.security;

import com.starion.webapp.models.User;
import com.starion.webapp.repositories.UserRepository;
import java.util.Collection;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

  private OAuth2User oAuth2User;

  @Autowired
  UserRepository userRepository;

  public CustomOAuth2User(OAuth2User oAuth2User) {
    this.oAuth2User = oAuth2User;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return oAuth2User.getAttributes();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
   /* return oAuth2User.getAuthorities();*/
    User user = userRepository.findByEmail(this.getEmail());
    return user.getRoles();
  }


  @Override
  public String getName() {
    return oAuth2User.getAttribute("name");
  }

  public String getFullName() {
    return oAuth2User.getAttribute("name");
  }

  public String getEmail(){
    return oAuth2User.getAttribute("email");
  }

}
