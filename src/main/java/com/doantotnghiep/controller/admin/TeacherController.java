package com.doantotnghiep.controller.admin;

import com.doantotnghiep.dto.AccountDTO;
import com.doantotnghiep.dto.TeacherDTO;
import com.doantotnghiep.service.IAccountService;
import com.doantotnghiep.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IAccountService accountService;
    @GetMapping("/teacher")
    public String getTeacher(Model model){
        String status = (String)model.asMap().get("status");
        String message = (String)model.asMap().get("message");
        if(status!=null){
            model.addAttribute("status",status);
            model.addAttribute("message",message);
        }
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("teacher",new TeacherDTO());
        return "admin-quiz/pages/teacher/teacher";
    }

    @PostMapping("/teacher")
    public String postTeacher(@ModelAttribute("teacher")TeacherDTO teacherDTO, RedirectAttributes redirectAttributes){
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail(teacherDTO.getEmail());
        accountDTO.setPassword("1");
        accountDTO.setFullName(teacherDTO.getFullName());
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_TEACHER");
        if(accountService.findByEmail(accountDTO.getEmail())!=null&&teacherDTO.getTeacherId()==0){
            redirectAttributes.addFlashAttribute("status","warning");
            redirectAttributes.addFlashAttribute("message","Email đã tồn tại trong hệ thống !");
        }else {
            accountService.save(accountDTO,roles);
            redirectAttributes.addFlashAttribute("status","success");
            redirectAttributes.addFlashAttribute("message","Thao tác thành công !");
            teacherService.save(teacherDTO);
        }
        return "redirect:/admin/teacher";
    }

    @GetMapping("/update-teacher/{teacherId}")
    public String updateTeacher(@PathVariable("teacherId")long teacherId,Model model){
        model.addAttribute("teacher",teacherService.findOne(teacherId));
        model.addAttribute("teachers",teacherService.findAll());
        return "admin-quiz/pages/teacher/teacher";
    }


}
