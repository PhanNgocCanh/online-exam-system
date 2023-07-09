package com.doantotnghiep.controller.admin;

import com.doantotnghiep.dto.ExamDTO;
import com.doantotnghiep.service.IExamService;
import com.doantotnghiep.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class ExamController {
    @Autowired
    private IExamService examService;
    @Autowired
    private IRoomService roomService;
    @GetMapping("/exam")
    public String getExam(Model model){
        String status = (String)model.asMap().get("status");
        String message = (String)model.asMap().get("message");
        if(status!=null){
            model.addAttribute("status",status);
            model.addAttribute("message",message);
        }
        model.addAttribute("exams",examService.findAll());
        model.addAttribute("exam",new ExamDTO());
        return "admin-quiz/pages/exam/exam";
    }

    @PostMapping("/exam")
    public String postExam(@ModelAttribute("exam") ExamDTO examDTO, RedirectAttributes redirectAttributes){
        examService.save(examDTO);
        redirectAttributes.addFlashAttribute("status","success");
        redirectAttributes.addFlashAttribute("message","Thao tác thành công !");
        return  "redirect:/admin/exam";
    }
    @GetMapping("/update-exam/{examId}")
    public String updateExam(@PathVariable("examId") long examId, Model model){
        model.addAttribute("exam",examService.findOneById(examId));
        model.addAttribute("exams",examService.findAll());
        return "admin-quiz/pages/exam/exam";
    }

    @GetMapping("/delete-exam/{examId}")
    public String deleteExam(@PathVariable("examId")long examId,RedirectAttributes redirectAttributes){
        if(roomService.findAll(examId).size()>0){
            redirectAttributes.addFlashAttribute("status","warning");
            redirectAttributes.addFlashAttribute("message","Không thể xoá kỳ thi !");
            return "redirect:/admin/exam";
        }
        examService.delete(examId);
        redirectAttributes.addFlashAttribute("status","success");
        redirectAttributes.addFlashAttribute("message","Xoá thành công !");
        return  "redirect:/admin/exam";
    }
}
