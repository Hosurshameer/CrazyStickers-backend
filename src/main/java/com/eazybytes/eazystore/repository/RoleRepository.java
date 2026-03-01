package com.eazybytes.eazystore.repository;

import com.eazybytes.eazystore.entity.Role;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Cacheable("roles")
    Optional<Role> findByName(String name);
}
