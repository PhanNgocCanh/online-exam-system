package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,String> {
    Account findOneByEmail(String email);
}
