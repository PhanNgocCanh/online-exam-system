package com.doantotnghiep.controller.admin;

import com.doantotnghiep.dto.SubjectDTO;
import com.doantotnghiep.service.IQuestionBankService;
import com.doantotnghiep.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class SubjectController {
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IQuestionBankService questionBankService;
    @GetMapping("/subject")
    public String getSubject(Model model){
        String status = (String)model.asMap().get("status");
        String message = (String)model.asMap().get("message");
        if(status!=null){
            model.addAttribute("status",status);
            model.addAttribute("message",message);
        }
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("subject",new SubjectDTO());
        return "admin-quiz/pages/subject/subject";
    }

    @PostMapping("/subject")
    public String postSubject(@ModelAttribute("subject") SubjectDTO subjectDTO, RedirectAttributes redirectAttributes){
        subjectService.save(subjectDTO);
        redirectAttributes.addFlashAttribute("status","success");
        redirectAttributes.addFlashAttribute("message","Thao tác thành công !");
        return  "redirect:/admin/subject";
    }
    @GetMapping("/update-subject/{subjectId}")
    public String updateExam(@PathVariable("subjectId") int subjectId, Model model){
        model.addAttribute("subject",subjectService.findOneById(subjectId));
        model.addAttribute("subjects",subjectService.findAll());
        return "admin-quiz/pages/subject/subject";
    }

    @GetMapping("/delete-subject/{subjectId}")
    public String deleteExam(@PathVariable("subjectId")int subjectId,RedirectAttributes redirectAttributes){
        if(questionBankService.findBySubjectId(subjectId).size()==0){
            subjectService.delete(subjectId);
            redirectAttributes.addFlashAttribute("status","success");
            redirectAttributes.addFlashAttribute("message","Xoá thành công !");
        }
       else{
            redirectAttributes.addFlashAttribute("status","error");
            redirectAttributes.addFlashAttribute("message","Xoá không thành công !");
        }
        return  "redirect:/admin/subject";
    }

}
