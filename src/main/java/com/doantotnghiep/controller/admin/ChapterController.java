package com.doantotnghiep.controller.admin;

import com.doantotnghiep.constant.Constant;
import com.doantotnghiep.dto.ChapterDTO;
import com.doantotnghiep.service.IChapterService;
import com.doantotnghiep.service.IQuestionBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class ChapterController {
    @Autowired
    private IChapterService chapterService;
    @Autowired
    private IQuestionBankService questionBankService;

    @GetMapping("/chapter")
    public String getListChapter(@RequestParam("questionBankId") long questionBankId, Model model) {
        String status = (String)model.asMap().get("status");
        String message = (String)model.asMap().get("message");
        if(status!=null){
            model.addAttribute("status",status);
            model.addAttribute("message",message);
        }
        model.addAttribute("questionBank", questionBankService.findOneByQuestionBankId(questionBankId));
        model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankId));
        model.addAttribute("chapter", new ChapterDTO());
        return "admin-quiz/pages/question/chapter";
    }

    @PostMapping("/chapter")
    public String postChapter(@ModelAttribute("chapter") ChapterDTO chapterDTO,
                              @RequestParam("questionBankId") long questionBankId,Model model,RedirectAttributes redirectAttributes) {
        chapterDTO.setQuestionBankId(questionBankId);
        chapterService.save(chapterDTO);
        redirectAttributes.addFlashAttribute("status","success");
        redirectAttributes.addFlashAttribute("message","Thao tác thành công !");
        return "redirect:/admin/chapter?questionBankId="+questionBankId;
    }
    @GetMapping("/chapter/{chapterId}")
    public String getQuestionBy(@PathVariable("chapterId") long chapterId,RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("chapterId",chapterId);
        redirectAttributes.addAttribute("level","");
        redirectAttributes.addAttribute("keyword","");
        redirectAttributes.addAttribute("page",1);
        redirectAttributes.addAttribute("limit",Constant.PAGE_QUESTION_LIMIT);
        return "redirect:/admin/question";
    }
    @GetMapping("/update-chapter/{chapterId}")
    public String updateChapter(@PathVariable("chapterId") long chapterId,
                                @RequestParam("questionBankId") long questionBankId,Model model){
        model.addAttribute("chapter",chapterService.findOneById(chapterId));
        model.addAttribute("chapters",chapterService.findByQuestionBankId(questionBankId));
        model.addAttribute("questionBank",questionBankService.findOneByQuestionBankId(questionBankId));
        return "admin-quiz/pages/question/chapter";
    }

    @GetMapping("/delete-chapter/{chapterId}")
    public String deleteChapter(@PathVariable("chapterId")long chapterId,
                                @RequestParam("questionBankId")long questionBankId,RedirectAttributes redirectAttributes){
        if(chapterService.getTotalQuestion(chapterId)==0) {
            redirectAttributes.addFlashAttribute("status","success");
            redirectAttributes.addFlashAttribute("message","Xoá thành công !");
            chapterService.delete(chapterId);
        }else{
            redirectAttributes.addFlashAttribute("status","error");
            redirectAttributes.addFlashAttribute("message","Xoá không thành công !");
        }
        return "redirect:/admin/chapter?questionBankId="+questionBankId;
    }
}
