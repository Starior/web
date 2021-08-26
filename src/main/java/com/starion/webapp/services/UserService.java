package com.starion.webapp.services;

import com.starion.webapp.models.AuthenticationProvider;
import com.starion.webapp.models.Role;
import com.starion.webapp.models.User;
import com.starion.webapp.repositories.RoleRepository;
import com.starion.webapp.repositories.UserRepository;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
  @PersistenceContext private EntityManager em;

  @Autowired UserRepository userRepository;

  @Autowired RoleRepository roleRepository;

  @Autowired BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return user;
  }

  public User findUserById(Long userId) {
    Optional<User> userFromDb = userRepository.findById(userId);
    return userFromDb.orElse(new User());
  }

  public List<User> allUsers() {
    return userRepository.findAll();
  }

  /*  public boolean saveUser(User user) {
  User userFromDB = userRepository.findByUsername(user.getUsername());

  if (userFromDB != null) {
    return false;
  }
  */
  /*user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));*/
  /*
    user.setRoles(Set.of(new Role(1L, "ROLE_USER")));
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return true;
  }*/

  public boolean saveUser(User user) {
    User userFromDB = userRepository.findByEmail(user.getEmail());

    if (userFromDB != null) {
      return false;
    }
    /*user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));*/
    /*  user.setRoles(Set.of(new Role(1L, "ROLE_USER")));*/

    Set<Role> roles = new HashSet<>();
    roles.add(roleRepository.getOne(1L));
    user.setRoles(roles);
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return true;
  }

  public boolean deleteUser(Long userId) {
    if (userRepository.findById(userId).isPresent()) {
      userRepository.deleteById(userId);
      return true;
    }
    return false;
  }

  public boolean checkEmail(User user) {
    User userFromDB = userRepository.findByEmail(user.getEmail());

    if (userFromDB != null) {
      return false;
    }
    return true;
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  public void createNewUserAfterOAuthLoginSuccess(
      String email, String name, AuthenticationProvider provider) {
    User user = new User();
    user.setEmail(email);
    user.setUsername(name);
    Set<Role> roles = new HashSet<>();
    roles.add(roleRepository.getOne(1L));
    user.setRoles(roles);
    user.setAuthProvider(provider);
    userRepository.save(user);
  }


  public void updateNewUserAfterOAuthLoginSuccess(User user, String name, AuthenticationProvider provider) {
    user.setUsername(name);
    user.setAuthProvider(provider);
    userRepository.save(user);
  }
}
