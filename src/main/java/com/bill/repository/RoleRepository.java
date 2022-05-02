package com.bill.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bill.models.ERole;
import com.bill.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
