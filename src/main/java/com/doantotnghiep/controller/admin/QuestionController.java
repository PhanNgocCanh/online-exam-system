package com.doantotnghiep.controller.admin;

import com.doantotnghiep.constant.Alphabet;
import com.doantotnghiep.constant.Constant;
import com.doantotnghiep.constant.FileTemplate;
import com.doantotnghiep.dto.*;
import com.doantotnghiep.service.*;
import com.doantotnghiep.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class QuestionController {
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IQuestionBankService questionBankService;
    @Autowired
    private IChapterService chapterService;
    @Autowired
    private IAnswerService answerService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ITestDetailService testDetailService;
    private long chapterId;
    private String level = "";
    private int page = 1;
    private int limit = Constant.PAGE_QUESTION_LIMIT;

    @GetMapping("/question")
    public String getQuestion(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "chapterId", required = false) String chapterIdFilter,
                              @RequestParam(value = "level", required = false) String level,
                              @RequestParam("page") int page, @RequestParam("limit") int limit, Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        List<QuestionDTO> questionDTOs = null;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createAt").descending());
        this.page = page;
        this.limit = limit;
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        if (keyword != null && !keyword.equals("") || chapterIdFilter != null && !chapterIdFilter.equals("") ||
                level != null && !level.equals("")) {
            long chapterId = (chapterIdFilter != null && !chapterIdFilter.equals("")) ? Long.parseLong(chapterIdFilter) : 0;
            model.addAttribute("keyword", keyword);
            model.addAttribute("chapterId", chapterIdFilter);
            model.addAttribute("level", level);
            if (this.chapterId != chapterId || !this.level.equals(level)) {
                page = 1;
                model.addAttribute("page", page);
                this.chapterId = chapterId;
                this.level = level;
            }
            pageable = PageRequest.of(page - 1, limit);
            if (level.equals("")) level = null;
            if (keyword.equals("")) keyword = null;
            questionDTOs = questionService.findBySearchAndFilter(chapterId, level, keyword, pageable);
            model.addAttribute("totalPage", (int) (Math.ceil((double) questionService.getTotalItem() / limit)));
        } else {
            questionDTOs = questionService.findAll(pageable);
            model.addAttribute("totalPage", (int) (Math.ceil((double) questionService.getTotalItem() / limit)));
        }
        questionDTOs.stream().forEach(questionDTO -> {
            long chapterId = questionDTO.getChapterId();
            long questionBankId = chapterService.findOneById(chapterId).getQuestionBankId();
            String subjectName = questionBankService.findOneByQuestionBankId(questionBankId).getSubjectName();
            questionDTO.setSubjectName(subjectName);
        });
        List<QuestionBankDTO> questionBankDTOs = questionBankService.findAll();
        List<SubjectChapter> subjectChapters = new ArrayList<>();
        questionBankDTOs.stream().forEach(questionBankDTO -> {
            SubjectChapter subjectChapter = new SubjectChapter();
            subjectChapter.setSubjectId(questionBankDTO.getSubjectId());
            subjectChapter.setSubjectName(questionBankDTO.getSubjectName());
            List<ChapterDTO> chapterDTOs = chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId());
            subjectChapter.setChapters(chapterDTOs);
            subjectChapters.add(subjectChapter);
        });
        model.addAttribute("subjectChapters", subjectChapters);
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("questions", questionDTOs);
        return "admin-quiz/pages/question/home-question";
    }

    @PostMapping("/question")
    public String postWayCreateQuestion(@RequestParam("subjectId") int subjectId,
                                        @RequestParam("wayCreate") String wayCreate,
                                        @RequestParam("page") int page, @RequestParam("limit") int limit, RedirectAttributes redirectAttributes) {
        if (subjectId == 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn phải chọn môn học !");
            redirectAttributes.addAttribute("page", page);
            redirectAttributes.addAttribute("limit", limit);
            return "redirect:/admin/question";
        }
        if (wayCreate.equals("handle")) {
            return "redirect:/admin/question/create-handle?subjectId=" + subjectId;
        } else {
            return "redirect:/admin/question/create-question-from-file?subjectId=" + subjectId;
        }
    }

    @GetMapping("/question/create-handle")
    public String createQuestionHandle(@RequestParam("subjectId") int subjectId,
                                       RedirectAttributes redirectAttributes, Model model) {
        SubjectDTO subjectDTO = subjectService.findOneById(subjectId);
        List<QuestionBankDTO> questionBankDTOs = questionBankService.findBySubjectId(subjectDTO.getSubjectId());
        if (questionBankDTOs.size() == 0) {
            redirectAttributes.addAttribute("chapterId", "");
            redirectAttributes.addAttribute("level", "");
            redirectAttributes.addAttribute("keyword", "");
            redirectAttributes.addAttribute("page", 1);
            redirectAttributes.addAttribute("limit", Constant.PAGE_LIMIT);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Môn học chưa có ngân hàng câu hỏi");
            return "redirect:/admin/question";
        }
        QuestionBankDTO questionBankDTO = questionBankDTOs.get(0);
        model.addAttribute("subject", subjectDTO);
        model.addAttribute("questionBank", questionBankDTO);
        model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId()));
        model.addAttribute("question", new QuestionDTO());
        return "admin-quiz/pages/question/create-question-handle";
    }

    @PostMapping("/question/create-handle")
    public String createQuestionHandle(@Valid @ModelAttribute("question") QuestionDTO questionDTO,
                                       @RequestParam("answer") String answer,
                                       @RequestParam(value = "position", defaultValue = "-1") int position,
                                       @RequestParam("subjectId") int subjectId,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes, Model model) {
        if(questionDTO.getChapterId()==0){
            result.addError(new FieldError("question", "chapterId",
                    "Phải chọn chương cho câu hỏi"));
        }
        if (position == -1) {
            SubjectDTO subjectDTO = subjectService.findOneById(subjectId);
            QuestionBankDTO questionBankDTO = questionBankService.findBySubjectId(subjectDTO.getSubjectId()).get(0);
            model.addAttribute("subject", subjectDTO);
            model.addAttribute("questionBank", questionBankDTO);
            model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId()));
            model.addAttribute("question", questionDTO);
            return "admin-quiz/pages/question/create-question-handle";
        }
        if(result.hasErrors()){
            SubjectDTO subjectDTO = subjectService.findOneById(subjectId);
            QuestionBankDTO questionBankDTO = questionBankService.findBySubjectId(subjectDTO.getSubjectId()).get(0);
            model.addAttribute("subject", subjectDTO);
            model.addAttribute("questionBank", questionBankDTO);
            model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId()));
            model.addAttribute("question", questionDTO);
            return "admin-quiz/pages/question/create-question-handle";
        }
        if (answer != null && !answer.equals("")) {
            answer = answer.substring(0, answer.lastIndexOf("//"));
            String[] answers = answer.split("//,");
            long questionId = questionService.save(questionDTO);
            int pos = 0;
            List<String> ans = Arrays.stream(answers).toList();
            for (String an : ans) {
                AnswerDTO answerDTO = new AnswerDTO();
                answerDTO.setQuestionId(questionId);
                answerDTO.setAnswerContent(an);
                answerDTO.setAnswerSymbol(Alphabet.getPosition(pos));
                long answerId = answerService.save(answerDTO);
                if (pos == position) {
                    questionDTO.setCorrectAnswer(answerId);
                    questionDTO.setQuestionId(questionId);
                    long questionIdRe = questionService.save(questionDTO);
                }
                pos++;
            }
            ;
        }
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm câu hỏi thành công !");
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("limit", limit);
        return "redirect:/admin/question";
    }

    @GetMapping("/question/create-question-from-file")
    public String createQuestionExcel(@RequestParam("subjectId") int subjectId,
                                      RedirectAttributes redirectAttributes, Model model) {
        SubjectDTO subjectDTO = subjectService.findOneById(subjectId);
        List<QuestionBankDTO> questionBankDTOs = questionBankService.findBySubjectId(subjectDTO.getSubjectId());
        if (questionBankDTOs.size() == 0) {
            redirectAttributes.addAttribute("chapterId", "");
            redirectAttributes.addAttribute("level", "");
            redirectAttributes.addAttribute("keyword", "");
            redirectAttributes.addAttribute("page", 1);
            redirectAttributes.addAttribute("limit", Constant.PAGE_LIMIT);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Môn học chưa có ngân hàng câu hỏi");
            return "redirect:/admin/question";
        }
        QuestionBankDTO questionBankDTO = questionBankDTOs.get(0);
        model.addAttribute("subject", subjectDTO);
        model.addAttribute("questionBank", questionBankDTO);
        model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId()));
        QuestionFileDTO questionFileDTO = new QuestionFileDTO();
        questionFileDTO.setSubjectId(subjectId);
        model.addAttribute("questionFile", questionFileDTO);
        return "admin-quiz/pages/question/create-question-from-file";
    }
    @Transactional
    @PostMapping("/question/create-question-from-file")
    public String createQuestionExcel(@Valid @ModelAttribute("questionFile") QuestionFileDTO questionFileDTO,
                                      @RequestParam("file") MultipartFile multipartFile,
                                      BindingResult result, RedirectAttributes redirectAttributes, Model model) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (questionFileDTO.getChapterId() == 0) {
            result.addError(new FieldError("questionFile", "chapterId",
                    "Phải chọn chương cho câu hỏi"));
        }
        if (fileName.equals("") || fileName.lastIndexOf(".xlsx") == -1) {
            result.addError(new FieldError("questionFile", "fileName",
                    "Chưa chọn file hoặc định dạng file không đúng"));
        }
        if (result.hasErrors()) {
            SubjectDTO subjectDTO = subjectService.findOneById(questionFileDTO.getSubjectId());
            QuestionBankDTO questionBankDTO = questionBankService.findBySubjectId(subjectDTO.getSubjectId()).get(0);
            model.addAttribute("subject", subjectDTO);
            model.addAttribute("questionBank", questionBankDTO);
            model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId()));
            return "admin-quiz/pages/question/create-question-from-file";
        }
        String dir = "src/main/resources/static/upload/";
        storageService.saveFileToServer(multipartFile, dir, fileName);
        List<QuestionDTO> success = questionService.readQuestionFromFile(fileName,
                questionFileDTO.getChapterId(), answerService);
        if (success != null) {
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Thêm câu hỏi thành công !");
        } else {
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Đã có lỗi xảy ra !");
        }
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("limit", limit);
        return "redirect:/admin/question";
    }

    @GetMapping("/update-question/{questionId}")
    public String updateQuestion(@PathVariable("questionId") long questionId, Model model) {
        model.addAttribute("question", questionService.findOneById(questionId));
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        long chapterId = questionService.findOneById(questionId).getChapterId();
        long questionBankId = chapterService.findOneById(chapterId).getQuestionBankId();
        model.addAttribute("chapters", chapterService.findByQuestionBankId(questionBankId));
        model.addAttribute("answers", answerService.findByQuestionId(questionId));
        return "admin-quiz/pages/question/edit-question";
    }

    @PostMapping("/update-question")
    public String updateQuestion(@ModelAttribute("question") QuestionDTO questionDTO,
                                 @RequestParam("answerContent") String answerContent,
                                 @RequestParam("answerId") String answerId,
                                 @RequestParam("correctAnswer") long correctAnswer, RedirectAttributes redirectAttributes) {
        answerContent = answerContent.substring(0, answerContent.lastIndexOf("//"));
        questionDTO.setCorrectAnswer(correctAnswer);
        questionService.save(questionDTO);
        List<String> answerContents = Arrays.stream(answerContent.split("//,")).toList();
        List<String> answerIds = Arrays.stream(answerId.split(",")).toList();
        int pos = 0;
        for (String content : answerContents) {
            AnswerDTO answerDTO = new AnswerDTO();
            answerDTO.setAnswerId(Long.parseLong(answerIds.get(pos)));
            answerDTO.setAnswerContent(answerContents.get(pos));
            answerDTO.setQuestionId(questionDTO.getQuestionId());
            answerDTO.setAnswerSymbol(Alphabet.getPosition(pos));
            answerService.save(answerDTO);
            pos++;
        }
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("limit", limit);
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật câu hỏi thành công !");
        return "redirect:/admin/question";
    }

    @GetMapping("/delete-question/{questionId}")
    public String deleteQuestion(@PathVariable("questionId") long questionId, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("limit", limit);
        if (testDetailService.findByQuestionId(questionId).size() > 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Không thể xoá câu hỏi do đang tồn tại trong đề thi !");
            return "redirect:/admin/question";
        }
        List<AnswerDTO> answerDTOs = answerService.findByQuestionId(questionId);
        answerDTOs.stream().forEach((answerDTO -> {
            answerService.delete(answerDTO.getAnswerId());
        }));
        questionService.delete(questionId);
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Xoá câu hỏi thành công !");
        return "redirect:/admin/question";
    }

    @GetMapping("/delete-answer/{answerId}")
    public String deleteAnswer(@PathVariable("answerId") long answerId) {
        AnswerDTO answerDTO = answerService.findOneByAnswerId(answerId);
        answerService.delete(answerId);
        return "redirect:/admin/update-question/" + answerDTO.getQuestionId();
    }

    @RequestMapping("/download-template-question")
    public void downloadTemplateQuestion(HttpServletResponse response) {
        storageService.downloadTemplateFile(response, FileTemplate.FILE_TEMPLATE_QUESTION);
    }
}
