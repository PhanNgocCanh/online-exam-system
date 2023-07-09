package com.doantotnghiep.service.impl;

import com.doantotnghiep.dto.AccountDTO;
import com.doantotnghiep.entity.Account;
import com.doantotnghiep.entity.Role;
import com.doantotnghiep.repository.AccountRepository;
import com.doantotnghiep.repository.RoleRepository;
import com.doantotnghiep.service.IAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public void save(AccountDTO accountDTO,List<String> roleName) {
        accountDTO.setPassword(new BCryptPasswordEncoder().encode(accountDTO.getPassword()));
        accountDTO.setStatus(1);
        Account account = modelMapper.map(accountDTO,Account.class);
        List<Role> roles = new ArrayList<>();
        roleName.forEach((role)->{
            roles.add(roleRepository.findOneByRoleName(role));
        });
        account.setRoles(roles);
        accountRepository.save(account);
    }

    @Override
    public void changePass(AccountDTO accountDTO) {
        Account account = accountRepository.findOneByEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        accountRepository.save(account);
    }

    @Override
    public AccountDTO changeToAccountDTO(String email, String fullName) {
        AccountDTO  accountDTO = new AccountDTO();
        accountDTO.setEmail(email);
        accountDTO.setFullName(fullName);
        accountDTO.setPassword("1");
        return accountDTO;
    }

    @Override
    public AccountDTO findByEmail(String email) {
        Account account = accountRepository.findOneByEmail(email);
        if(account!=null){
            AccountDTO accountDTO = modelMapper.map(account,AccountDTO.class);
            return accountDTO;
        }else{
            return null;
        }
    }

    @Override
    public void delete(String email) {
        accountRepository.deleteById(email);
    }
}
