package com.doantotnghiep.controller.admin;

import com.doantotnghiep.dto.QuestionBankDTO;
import com.doantotnghiep.dto.QuestionDTO;
import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.service.IQuestionBankService;
import com.doantotnghiep.service.ISubjectService;
import com.doantotnghiep.service.impl.QuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class QuestionBankController {
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IQuestionBankService questionBankService;

    @GetMapping("/question-bank")
    public String getFormAddQuestionBank(Model model) {
        model.addAttribute("questionBank",new QuestionBankDTO());
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("questionBanks",questionBankService.findAll());
        return "admin-quiz/pages/question/question-bank";
    }

    @PostMapping("/question-bank")
    public String postFormAddQuestionBank(@ModelAttribute("questionBank") QuestionBankDTO questionBankDTO){
        questionBankService.save(questionBankDTO);
        return "redirect:/admin/question-bank";
    }


}
