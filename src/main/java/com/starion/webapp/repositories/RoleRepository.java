package com.starion.webapp.repositories;

import com.starion.webapp.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Role getOne(long l);
}

