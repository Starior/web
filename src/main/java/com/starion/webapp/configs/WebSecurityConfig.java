package com.starion.webapp.configs;

import com.starion.webapp.security.CustomOAuth2UserService;
import com.starion.webapp.security.OAuth2LoginSuccessHandler;
import com.starion.webapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired UserService userService;

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/register", "/", "/process_register", "/login").not().fullyAuthenticated()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/site/add").hasAnyRole("USER", "ADMIN")
        .antMatchers("/", "/about", "/site","/oauth2/**").permitAll()
        .anyRequest().authenticated()
        .and()
        // Login settings
        .formLogin()
          .loginPage("/login")
          .usernameParameter("email")
          .permitAll()
          .defaultSuccessUrl("/")
        .and()
        .oauth2Login()
          .loginPage("/login")
          .userInfoEndpoint().userService(oAuth2UserService)
          .and()
          .successHandler(oAuth2LoginSuccessHandler)
        .and()
        .logout()
        .permitAll()
        .logoutSuccessUrl("/");
  }


  @Autowired
  protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder());
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private CustomOAuth2UserService oAuth2UserService;

  @Autowired
  private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

}
