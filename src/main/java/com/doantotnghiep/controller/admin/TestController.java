package com.doantotnghiep.controller.admin;

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
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller(value = "admin-test")
@RequestMapping("/admin")
public class TestController {
    @Autowired
    private ITestService testService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IChapterService chapterService;
    @Autowired
    private IQuestionBankService questionBankService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IAnswerService answerService;
    @Autowired
    private ITestDetailService testDetailService;
    @Autowired
    private IRoomDetailService roomDetailService;
    @Autowired
    private StorageService storageService;
    private int subjectId;
    private int page = 1;
    private int limit = Constant.PAGE_TEST_LIMIT;

    @GetMapping("/test")
    public String getTest(@RequestParam(value = "subjectId", required = false) String id,
                          @RequestParam("page") int page, @RequestParam("limit") int limit, Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        List<TestDTO> testDTOs = null;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createAt").descending());
        this.page = page;
        this.limit = limit;
        model.addAttribute("page",page);
        model.addAttribute("limit",limit);
        if (id != null && !id.equals("") && !id.equals("0")) {
            int subjectId = Integer.parseInt(id);
            model.addAttribute("subjectId", subjectId);
            if (this.subjectId != subjectId){
                page = 1;
                model.addAttribute("page",page);
                this.subjectId = subjectId;
            }
            pageable = PageRequest.of(page-1,limit,Sort.by("createAt").descending());
            testDTOs = testService.findBySubjectId(subjectId,pageable);
            model.addAttribute("totalPage",(int)(Math.ceil((double)testService.getTotalItem()/limit)));
        } else {
            testDTOs = testService.findAll(pageable);
            model.addAttribute("totalPage",(int)(Math.ceil((double)testService.getTotalItem()/limit)));
        }
        model.addAttribute("tests", testDTOs);
        model.addAttribute("subjects", subjectService.findAll());
        return "admin-quiz/pages/test/list-test";
    }

    @PostMapping("/test")
    public String postWayToCreateTest(@RequestParam("subjectId") int subjectId,
                                      @RequestParam("wayCreate") String wayCreate, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("subjectId", subjectId);
        if (subjectId == 0) {
            redirectAttributes.addAttribute("page",page);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn phải chọn môn học !");
            return "redirect:/admin/test";
        }

        if (wayCreate.equals("handle")) {
            return "redirect:/admin/test/create-handle";
        } else if (wayCreate.equals("excel")) {
            return "redirect:/admin/test/create-from-excel";
        } else {
            return "redirect:/admin/test/create-automatic";
        }
    }

    @GetMapping("/test/create-handle")
    public String createTestHandle(@RequestParam("subjectId") int subjectId,RedirectAttributes redirectAttributes,Model model) {
        model.addAttribute("subject", subjectService.findOneById(subjectId));
        List<QuestionBankDTO> questionBankDTOS = questionBankService.findBySubjectId(subjectId);
        if (questionBankDTOS.size() == 0) {
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn chưa có ngân hàng câu hỏi cho môn này");
            return "redirect:/admin/test";
        }
        TestDTO testDTO = new TestDTO();
        testDTO.setSubjectId(subjectId);
        model.addAttribute("test", testDTO);
        return "admin-quiz/pages/test/create-test-handle";
    }

    @PostMapping("/test/create-handle")
    public String createTestHandle(@ModelAttribute("test") TestDTO testDTO, RedirectAttributes redirectAttributes) {
        testDTO.setCreateAt(new Date());
        long testId = testService.save(testDTO);
        redirectAttributes.addAttribute("page",1);
        redirectAttributes.addAttribute("limit",limit);
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Tạo đề thi thành công !");
        return "redirect:/admin/test";
    }

    @GetMapping("/test/create-from-excel")
    public String createTestFromExcel(@RequestParam("subjectId") int subjectId, Model model, RedirectAttributes redirectAttributes) {
        model.addAttribute("subject", subjectService.findOneById(subjectId));
        List<QuestionBankDTO> questionBankDTOS = questionBankService.findBySubjectId(subjectId);
        if (questionBankDTOS.size() == 0) {
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn chưa có ngân hàng câu hỏi cho môn này");
            return "redirect:/admin/test";
        }
        QuestionBankDTO questionBankDTO = questionBankDTOS.get(0);
        List<ChapterDTO> chapterDTOs = chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId());
        model.addAttribute("chapters", chapterDTOs);
        TestExcelDTO testExcelDTO = new TestExcelDTO();
        testExcelDTO.setSubjectId(subjectId);
        model.addAttribute("test", testExcelDTO);
        return "admin-quiz/pages/test/create-test-from-excel";
    }

    @PostMapping("/test/create-from-excel")
    public String createTestFromExcel(@Valid @ModelAttribute("test") TestExcelDTO testExcelDTO,
                                      @RequestParam("file") MultipartFile multipartFile, BindingResult result,
                                      RedirectAttributes redirectAttributes, Model model) throws IOException {
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.equals("") || fileName.lastIndexOf(".xlsx") == -1) {
            result.addError(new FieldError("test", "fileName",
                    "Chưa chọn file hoặc định dạng file không đúng"));
        }
        if (testExcelDTO.getChapterId() == 0) {
            result.addError(new FieldError("test", "chapterId", "Phải chọn chương cho đề thi"));
        }
        if (result.hasErrors()) {
            List<QuestionBankDTO> questionBankDTOS = questionBankService.findBySubjectId(testExcelDTO.getSubjectId());
            QuestionBankDTO questionBankDTO = questionBankDTOS.get(0);
            List<ChapterDTO> chapterDTOs = chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId());
            model.addAttribute("subject", subjectService.findOneById(testExcelDTO.getSubjectId()));
            model.addAttribute("chapters", chapterDTOs);
            model.addAttribute("test", testExcelDTO);
            return "admin-quiz/pages/test/create-test-from-excel";
        }
        String dir = "src/main/resources/static/upload/";
        storageService.saveFileToServer(multipartFile, dir, fileName);
        List<QuestionDTO> questionDTOs = questionService.readQuestionFromFile(fileName, testExcelDTO.getChapterId(), answerService);
        redirectAttributes.addAttribute("page",1);
        redirectAttributes.addAttribute("limit",limit);
        if (questionDTOs == null) {
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Quá trình tải câu hỏi lỗi !");
            return "redirect:/admin/test";
        }
        TestDTO testDTO = new TestDTO();
        testDTO.setTestName(testExcelDTO.getTestName());
        testDTO.setSubjectId(testExcelDTO.getSubjectId());
        testDTO.setMaxScore(testExcelDTO.getMaxScore());
        testDTO.setMaxTime(testExcelDTO.getMaxTime());
        testDTO.setTotalSentence(questionDTOs.size());
        testDTO.setCreateAt(new Date());
        long testId = testService.save(testDTO);
        questionDTOs.stream().forEach(questionDTO -> {
            TestDetailDTO testDetailDTO = new TestDetailDTO();
            testDetailDTO.setTestId(testId);
            testDetailDTO.setQuestionId(questionDTO.getQuestionId());
            testDetailService.save(testDetailDTO);
        });
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Tải đề thi thành công !");
        return "redirect:/admin/test";
    }

    @GetMapping("/test/create-automatic")
    public String createTestAutomatic(@RequestParam("subjectId") int subjectId, RedirectAttributes redirectAttributes, Model model) {
        SubjectDTO subjectDTO = subjectService.findOneById(subjectId);
        model.addAttribute("subject", subjectDTO);
        List<QuestionBankDTO> questionBankDTOS = questionBankService.findBySubjectId(subjectId);
        if (questionBankDTOS.size() == 0) {
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn chưa có ngân hàng câu hỏi cho môn này");
            return "redirect:/admin/test";
        }
        QuestionBankDTO questionBankDTO = questionBankDTOS.get(0);
        questionBankDTO.setSubjectId(subjectDTO.getSubjectId());
        questionBankDTO.setSubjectName(subjectDTO.getSubjectName());
        List<ChapterDTO> chapterDTOs = chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId());
        TestAutomaticDTO testAutomaticDTO = new TestAutomaticDTO();
        List<RowTestAutomatic> rows = new ArrayList<>();
        chapterDTOs.stream().forEach(chapterDTO -> {
            RowTestAutomatic rowTestAutomatic = new RowTestAutomatic();
            rowTestAutomatic.setChapterId(chapterDTO.getChapterId());
            rowTestAutomatic.setChapterName(chapterDTO.getChapterName());
            rowTestAutomatic.setTotalNb(questionService.findByChapterIdAndLevel(chapterDTO.getChapterId(), "NB").size());
            rowTestAutomatic.setTotalTh(questionService.findByChapterIdAndLevel(chapterDTO.getChapterId(), "TH").size());
            rowTestAutomatic.setTotalVd(questionService.findByChapterIdAndLevel(chapterDTO.getChapterId(), "VD").size());
            rowTestAutomatic.setTotalVdc(questionService.findByChapterIdAndLevel(chapterDTO.getChapterId(), "VDC").size());
            rows.add(rowTestAutomatic);
        });
        testAutomaticDTO.setRowTestAutomatic(rows);
        model.addAttribute("test", testAutomaticDTO);
        model.addAttribute("chapters", chapterDTOs);
        model.addAttribute("subject", subjectService.findOneById(subjectId));

        return "admin-quiz/pages/test/create-test-automatic";
    }

    @Transactional
    @PostMapping("/test/create-automatic")
    public String createTestAutomatic(@ModelAttribute("test") TestAutomaticDTO testAutomaticDTO,
                                      @RequestParam("subjectId") int subjectId, RedirectAttributes redirectAttributes) {
        List<RowTestAutomatic> rowsTest = testAutomaticDTO.getRowTestAutomatic();
        try {
            for (int i = 0; i < testAutomaticDTO.getNumOfTest(); i++) {
                List<QuestionDTO> questionDTOs = new ArrayList<>();
                rowsTest.stream().forEach(row -> {
                    if (row.getNbCount() > 0) {
                        questionDTOs.addAll(questionService.getQuestionAutomaticForTest(row.getChapterId(), "NB", row.getNbCount()));
                    }
                    if (row.getThCount() > 0) {
                        questionDTOs.addAll(questionService.getQuestionAutomaticForTest(row.getChapterId(), "TH", row.getThCount()));
                    }
                    if (row.getVdCount() > 0) {
                        questionDTOs.addAll(questionService.getQuestionAutomaticForTest(row.getChapterId(), "VD", row.getVdCount()));
                    }
                    if (row.getVdcCount() > 0) {
                        questionDTOs.addAll(questionService.getQuestionAutomaticForTest(row.getChapterId(), "VDC", row.getVdcCount()));
                    }
                });
                TestDTO testDTO = new TestDTO();
                testDTO.setTestName(testAutomaticDTO.getTestName() + " ver_" + (i + 1));
                testDTO.setSubjectId(subjectId);
                testDTO.setMaxScore(testAutomaticDTO.getMaxScore());
                testDTO.setMaxTime(testAutomaticDTO.getMaxTime());
                testDTO.setTotalSentence(questionDTOs.size());
                testDTO.setCreateAt(new Date());
                long testId = testService.save(testDTO);
                Collections.shuffle(questionDTOs);
                questionDTOs.stream().forEach(questionDTO -> {
                    TestDetailDTO testDetailDTO = new TestDetailDTO();
                    testDetailDTO.setTestId(testId);
                    testDetailDTO.setQuestionId(questionDTO.getQuestionId());
                    testDetailService.save(testDetailDTO);
                });
            }
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Tạo " + testAutomaticDTO.getNumOfTest() + " đề thành công!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Đã sảy ra lỗi trong quá trình tạo đề !");
        }
        return "redirect:/admin/test";
    }

    @GetMapping("/test/{testId}")
    public String getQuestionInTest(@PathVariable("testId") long testId, Model model) {
        TestDTO testDTO = testService.findOneById(testId);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testId);
        testDetailDTOs.stream().forEach(testDetailDTO -> {
            questionDTOs.add(questionService.findOneById(testDetailDTO.getQuestionId()));
        });
        model.addAttribute("test", testDTO);
        model.addAttribute("totalQuestion", questionDTOs.size());
        model.addAttribute("questions", questionDTOs);
        model.addAttribute("chapters", chapterService.findAll());
        return "admin-quiz/pages/test/test-question";
    }

    @GetMapping("/test/add-question/{testId}")
    public String addQuestionToTest(@PathVariable("testId") long testId,
                                    RedirectAttributes redirectAttributes, Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        TestDTO testDTO = testService.findOneById(testId);
        List<QuestionBankDTO> questionBankDTOs = questionBankService.findBySubjectId(testDTO.getSubjectId());
        if (questionBankDTOs.size() == 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn chưa có ngân hàng câu hỏi cho môn này");
            return "redirect:/admin/test";
        }
        QuestionBankDTO questionBankDTO = questionBankDTOs.get(0);
        List<ChapterDTO> chapterDTOs = chapterService.findByQuestionBankId(questionBankDTO.getQuestionBankId());
        List<Long> chapterIds = new ArrayList<>();
        chapterDTOs.stream().forEach(chapterDTO -> {
            chapterIds.add(chapterDTO.getChapterId());
        });
        if (chapterDTOs.size() == 0) {
            redirectAttributes.addAttribute("subjectId",subjectId);
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Bạn chưa có chương câu hỏi cho môn này");
            return "redirect:/admin/test";
        }
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testId);
        List<QuestionDTO> questionDTOAll = questionService.findAll();
        List<Long> questionIds = new ArrayList<>();
        testDetailDTOs.stream().forEach(questionDTO -> {
            questionIds.add(questionDTO.getQuestionId());
        });
        questionDTOAll.stream().forEach(questionDTO -> {
            if (chapterIds.contains(questionDTO.getChapterId()) && !questionIds.contains(questionDTO.getQuestionId()))
                questionDTOs.add(questionDTO);
        });
        model.addAttribute("questions", questionDTOs);
        model.addAttribute("chapters", chapterDTOs);
        model.addAttribute("subject", subjectService.findOneById(testDTO.getSubjectId()));
        model.addAttribute("test", testDTO);
        return "admin-quiz/pages/test/add-question-to-test";
    }

    @PostMapping("/test/add-question")
    public String addQuestionToTest(@RequestParam("testId") long testId, @RequestParam("questionIds") List<Long> questionIds,
                                    RedirectAttributes redirectAttributes) {
        questionIds.stream().forEach(questionId -> {
            TestDetailDTO testDetailDTO = new TestDetailDTO();
            testDetailDTO.setTestId(testId);
            testDetailDTO.setQuestionId(questionId);
            testDetailService.save(testDetailDTO);
        });
        TestDTO testDTO = testService.findOneById(testId);
        testDTO.setTotalSentence(testDTO.getTotalSentence() + questionIds.size());
        testService.save(testDTO);
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm vào đề thi thành công !");
        return "redirect:/admin/test/" + testId;
    }

    @GetMapping("/print-test/{testId}")
    public String printTest(@PathVariable("testId") long testId,Model model){
        TestDTO testDTO = testService.findOneById(testId);
        List<QuestionAnswerDTO> questionAnswerDTOs = new ArrayList<>();
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testId);
        testDetailDTOs.stream().forEach(testDetailDTO -> {
            QuestionAnswerDTO qa = new QuestionAnswerDTO();
            qa.setQuestionId(testDetailDTO.getQuestionId());
            QuestionDTO questionDTO = questionService.findOneById(testDetailDTO.getQuestionId());
            qa.setQuestionContent(questionDTO.getQuestionContent());
            List<AnswerDTO> answerDTOs = answerService.findByQuestionId(questionDTO.getQuestionId());
            answerDTOs.stream().forEach(answerDTO -> {
                qa.getAnswerDTOs().add(answerDTO);
            });
            qa.setCorrectAnswer(questionDTO.getCorrectAnswer());
            questionAnswerDTOs.add(qa);
        });
        model.addAttribute("test",testDTO);
        model.addAttribute("questions",questionAnswerDTOs);
        return "admin-quiz/pages/test/print-test";
    }
    @GetMapping("/print-answer/{testId}")
    public String getAnswerOfTest(@PathVariable("testId") long testId,Model model){
        TestDTO testDTO = testService.findOneById(testId);
        List<String> questionAnswerSym = new ArrayList<>();
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testId);
        testDetailDTOs.stream().forEach(testDetailDTO -> {
            QuestionDTO questionDTO = questionService.findOneById(testDetailDTO.getQuestionId());
            List<AnswerDTO> answerDTOs = answerService.findByQuestionId(questionDTO.getQuestionId());
            answerDTOs.stream().forEach(answerDTO -> {
                if(answerDTO.getAnswerId()==questionDTO.getCorrectAnswer()) questionAnswerSym.add(answerDTO.getAnswerSymbol());
            });
        });
        model.addAttribute("test",testDTO);
        model.addAttribute("answers",questionAnswerSym);
        return "admin-quiz/pages/test/print-answer";
    }
    @GetMapping("/test/delete-question-from-test/{testId}")
    public String deleteQuestionFromTest(@PathVariable("testId") long testId, @RequestParam("questionId") long questionId,
                                         RedirectAttributes redirectAttributes) {
        TestDetailDTO testDetailDTO = testDetailService.findByTestIdAndQuestionId(testId, questionId);
        testDetailService.delete(testDetailDTO.getId());
        TestDTO testDTO = testService.findOneById(testDetailDTO.getTestId());
        testDTO.setTotalSentence(testDTO.getTotalSentence() - 1);
        testService.save(testDTO);
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Đã xoá câu hỏi khỏi đề thi");
        return "redirect:/admin/test/" + testDetailDTO.getTestId();
    }
    @Transactional
    @GetMapping("/delete-test/{testId}")
    public String deleteTest(@PathVariable("testId")long testId,RedirectAttributes redirectAttributes){
        if(roomDetailService.testExitInRoom(testId)){
            redirectAttributes.addAttribute("page",page);
            redirectAttributes.addAttribute("limit",limit);
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Không thể xoá đề thi do đã được sử dụng !");
            return "redirect:/admin/test";
        }
        try {
            List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testId);
            testDetailDTOs.stream().forEach(testDetailDTO -> {
                testDetailService.delete(testDetailDTO.getId());
            });
            testService.delete(testId);
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Xoá đề thi thành công !");
        }catch (Exception e){
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Lỗi trong quá trình xoá đề !");
        }
        redirectAttributes.addAttribute("page", 1);
        redirectAttributes.addAttribute("limit", limit);
        return "redirect:/admin/test";
    }
    @RequestMapping("/download-template-test")
    public void downloadTemplateTest(HttpServletResponse response) {
        storageService.downloadTemplateFile(response, FileTemplate.FILE_TEMPLATE_TEST);
    }
}
