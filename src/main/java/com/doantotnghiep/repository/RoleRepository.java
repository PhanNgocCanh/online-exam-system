package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findOneByRoleName(String roleName);
}
