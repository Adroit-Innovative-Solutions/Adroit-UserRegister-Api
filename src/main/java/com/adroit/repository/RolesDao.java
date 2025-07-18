package com.adroit.repository;

import com.adroit.model.Roles;
import com.adroit.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RolesDao extends JpaRepository<Roles,Integer> {

   Optional<Roles> findByRole(UserType role);
}
