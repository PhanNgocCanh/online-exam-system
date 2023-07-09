package com.doantotnghiep.controller.login;

import com.doantotnghiep.dto.AccountDTO;
import com.doantotnghiep.entity.Account;
import com.doantotnghiep.repository.AccountRepository;
import com.doantotnghiep.service.IAccountService;
import com.doantotnghiep.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
    @Autowired
    private IAccountService accountService;
    @GetMapping("/login")
    public String getLogin(){

        return "login/login";
    }

    @GetMapping("/change-password")
    public String changePass(HttpServletRequest request, @RequestParam("oldPass") String oldPass,
                             @RequestParam("newPass") String newPass, RedirectAttributes redirectAttributes){
        String email = (String) SessionUtils.getInstance().getValue(request,"userEmail");
        AccountDTO accountDTO = accountService.findByEmail(email);
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        if(bc.matches(oldPass,accountDTO.getPassword())&&!newPass.equals("")){
            accountDTO.setPassword(bc.encode(newPass));
            accountService.changePass(accountDTO);
            redirectAttributes.addFlashAttribute("status","success");
            redirectAttributes.addFlashAttribute("message","Thay đổi mật khẩu thành công !");
        }else{
            redirectAttributes.addFlashAttribute("status","error");
            redirectAttributes.addFlashAttribute("message","Mật khẩu không trùng khớp");
        }
        return "redirect:/";
    }

}
