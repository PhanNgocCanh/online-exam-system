package com.doantotnghiep.service.impl;

import com.doantotnghiep.entity.Account;
import com.doantotnghiep.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findOneByEmail(email);
        if(account==null){
            throw new UsernameNotFoundException("USER NOT FOUND !");
        }
        return new ShowUserDetail(account);
    }
}
