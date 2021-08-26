package com.starion.webapp.security;

import com.starion.webapp.models.AuthenticationProvider;
import com.starion.webapp.models.User;
import com.starion.webapp.services.UserService;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired private UserService userService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

  DefaultOidcUser oAuth2User = (DefaultOidcUser) authentication.getPrincipal();


    String email = oAuth2User.getEmail();
    String name = oAuth2User.getFullName();

    User user = userService.getUserByEmail(email);
    Collection<? extends GrantedAuthority> role = oAuth2User.getAuthorities();

    if (user == null) {
      // register
      userService.createNewUserAfterOAuthLoginSuccess(email, name, AuthenticationProvider.GOOGLE);
    } else {
      // update
      userService.updateNewUserAfterOAuthLoginSuccess(user, name, AuthenticationProvider.GOOGLE);
    }

    System.out.println("Customer's role: " + role);
    System.out.println("Customer's oAuth2User: " + oAuth2User);

    System.out.println(authentication);

    super.onAuthenticationSuccess(request, response, authentication);
  }
}
