package com.doantotnghiep.service;

import com.doantotnghiep.dto.AccountDTO;

import java.util.List;

public interface IAccountService {
    void save(AccountDTO accountDTO, List<String> roleName);
    void changePass(AccountDTO accountDTO);
    AccountDTO changeToAccountDTO(String email,String fullName);
    AccountDTO findByEmail(String email);
    void delete(String email);
}
