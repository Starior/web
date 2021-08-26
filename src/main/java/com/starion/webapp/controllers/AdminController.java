package com.starion.webapp.controllers;

import com.starion.webapp.models.Role;
import com.starion.webapp.models.User;
import com.starion.webapp.repositories.RoleRepository;
import com.starion.webapp.repositories.UserRepository;
import com.starion.webapp.security.CustomOAuth2User;
import com.starion.webapp.services.UserService;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;


  @GetMapping("/admin")
  public String userList(Model model) {
    model.addAttribute("title", "admin");
    model.addAttribute("allUsers", userService.allUsers());
    return "admin";
  }

  @PostMapping("/admin/{id}/delete")
  public String sitePostDelete(@PathVariable(value = "id") long id, Model model) {
    userService.deleteUser(id);
    return "redirect:/admin";
  }

  @PostMapping("/admin/{id}/disable")
  public String userDisable(@PathVariable(value = "id") long id, Model model) {
    User user = userRepository.findById(id).orElseThrow();
    user.setEnabled(false);
    userRepository.save(user);
    return "redirect:/admin";
  }

  @PostMapping("/admin/{id}/enable")
  public String userEnable(@PathVariable(value = "id") long id, Model model) {
    User user = userRepository.findById(id).orElseThrow();
    user.setEnabled(true);
    userRepository.save(user);
    /*roleRepository.findById(id)*/
    return "redirect:/admin";
  }

/*  @PostMapping("/admin/{id}/change")
  public String userEmailChange(@PathVariable(value = "id") long id, Model model) {
    User user = userRepository.findById(id).orElseThrow();
    user.setEmail("asdas@qqwe.com");
    userRepository.save(user);
    return "redirect:/admin";
  }*/

  @PostMapping("/admin/{id}/disable_admin")
  public String userDisableAdmin(@PathVariable(value = "id") long id) {
    User user = userRepository.findById(id).orElseThrow();
    Set<Role> roles = new HashSet<>();
    roles.add(roleRepository.getOne(1L));
    user.setRoles(roles);
    userRepository.save(user);

    return "redirect:/admin";
  }

  @PostMapping("/admin/{id}/enable_admin")
  public String userEnableAdmin(@PathVariable(value = "id") long id) {
    User user = userRepository.findById(id).orElseThrow();
    Set<Role> roles = new HashSet<>();
    roles.add(roleRepository.getOne(2L));
    user.setRoles(roles);
    userRepository.save(user);

    return "redirect:/admin";
  }
}
