package com.starion.webapp.repositories;

import com.starion.webapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u WHERE u.email = ?1")
  User findByEmail(String email);
  User findByUsername(String username);



}