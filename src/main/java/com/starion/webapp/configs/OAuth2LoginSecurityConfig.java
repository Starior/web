package com.starion.webapp.configs;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .oauth2Login(oauth2Login ->
            oauth2Login
                .userInfoEndpoint(userInfoEndpoint ->
                    userInfoEndpoint
                        .userAuthoritiesMapper(this.userAuthoritiesMapper())

                    )
            );
  }

  private GrantedAuthoritiesMapper userAuthoritiesMapper() {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      authorities.forEach(
          authority -> {
            if (OidcUserAuthority.class.isInstance(authority)) {
              OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;

              OidcIdToken idToken = oidcUserAuthority.getIdToken();
              OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

              System.out.println(userInfo);

              // Map the claims found in idToken and/or userInfo
              // to one or more GrantedAuthority's and add it to mappedAuthorities

            } else if (OAuth2UserAuthority.class.isInstance(authority)) {
              OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;

              Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

              // Map the attributes found in userAttributes
              // to one or more GrantedAuthority's and add it to mappedAuthorities

            }
          });

      return mappedAuthorities;
    };
  }


}
