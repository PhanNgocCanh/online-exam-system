package com.doantotnghiep.controller.client;

import com.doantotnghiep.dto.*;
import com.doantotnghiep.entity.Exam;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.TestResult;
import com.doantotnghiep.service.*;
import com.doantotnghiep.utils.SessionUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller(value = "client-room-exam")
public class ExamController {
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IRoomDetailService roomDetailService;
    @Autowired
    private IExamService examService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITestService testService;
    @Autowired
    private ITestDetailService testDetailService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IAnswerService answerService;
    @Autowired
    private ITestHistoryService testHistoryService;
    @Autowired
    private ITestResultService testResultService;
    @GetMapping("/exam")
    public String getRoomExam(HttpServletRequest request, Model model) {
        String email = SessionUtils.getInstance().getValue(request,"userEmail").toString();
        StudentDTO studentDTO = studentService.findOneByEmail(email);
        List<RoomDetailDTO> roomDetailDTOs = roomDetailService.findAllByStudentId(studentDTO.getStudentId());
        List<RoomDTO> roomDTOs = new ArrayList<>();
        Date now = new Date();
        roomDetailDTOs.stream().forEach(roomDetailDTO -> {
            RoomDTO roomDTO = roomService.findOneById(roomDetailDTO.getRoomId());
           if(roomDTO.getStart().compareTo(now)<=0&&roomDTO.getFinish().compareTo(now)>=0&&
                   roomDetailDTO.getStatus()==1)
               roomDTOs.add(roomDTO);
        });
        model.addAttribute("rooms",roomDTOs);
        model.addAttribute("teachers",teacherService.findAll());
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("exams",examService.findAll());
        return "client-quiz/pages/room-exam/room-exam";
    }

    @GetMapping("/rule-exam/{roomId}")
    public String getRuleExam(HttpServletRequest request,@PathVariable("roomId") long roomId,Model model){
        SessionUtils.getInstance().putValue(request,"roomId",roomId);
        RoomDTO roomDTO = roomService.findOneById(roomId);
        ExamDTO examDTO = examService.findOneById(roomDTO.getExamId());
        SubjectDTO subjectDTO = subjectService.findOneById(roomDTO.getSubjectId());
        model.addAttribute("room",roomDTO);
        model.addAttribute("exam",examDTO);
        model.addAttribute("subject",subjectDTO);
        return "client-quiz/pages/exam/exam-rule";
    }
    @GetMapping("/test")
    public String getTest(HttpServletRequest request,Model model){
        String email = SessionUtils.getInstance().getValue(request,"userEmail").toString();
        StudentDTO studentDTO = studentService.findOneByEmail(email);
        long roomId =(long) SessionUtils.getInstance().getValue(request,"roomId");
        RoomDTO roomDTO = roomService.findOneById(roomId);
        SubjectDTO subjectDTO = subjectService.findOneById(roomDTO.getSubjectId());
        ExamDTO examDTO = examService.findOneById(roomDTO.getExamId());
        RoomDetailDTO roomDetailDTO = roomDetailService.findOneByRoomAndStudent(roomId,studentDTO.getStudentId());
        TestDTO testDTO = testService.findOneById(roomDetailDTO.getTestId());
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testDTO.getTestId());
        ListQuestionDTO listQuestionDTO = new ListQuestionDTO();
        testDetailDTOs.stream().forEach(testDetailDTO -> {
            QuestionDTO questionDTO = questionService.findOneById(testDetailDTO.getQuestionId());
            List<AnswerDTO> answerDTOs = answerService.findByQuestionId(questionDTO.getQuestionId());
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setQuestionId(questionDTO.getQuestionId());
            questionAnswerDTO.setQuestionContent(questionDTO.getQuestionContent());
            questionAnswerDTO.setAnswerDTOs(answerDTOs);
            listQuestionDTO.add(questionAnswerDTO);
        });
        model.addAttribute("questions",listQuestionDTO);
        model.addAttribute("test",testDTO);
        model.addAttribute("student",studentDTO);
        model.addAttribute("subject",subjectDTO);
        model.addAttribute("exam",examDTO);
        return "client-quiz/pages/exam/test";
    }

    @PostMapping("/test")
    public String submitTest(@ModelAttribute("questions") ListQuestionDTO listQuestionDTO, HttpServletRequest request,
                             @RequestParam("testId") long testId, RedirectAttributes redirectAttributes){
        List<QuestionAnswerDTO> questionAnswerDTOs = listQuestionDTO.getQuestionAnswerDTOs();
        String email = SessionUtils.getInstance().getValue(request,"userEmail").toString();
        long roomId =(long) SessionUtils.getInstance().getValue(request,"roomId");
        StudentDTO studentDTO = studentService.findOneByEmail(email);
        TestDTO testDTO = testService.findOneById(testId);
        int correctSentence = 0;
        for(QuestionAnswerDTO qa : questionAnswerDTOs){
            QuestionDTO questionDTO = questionService.findOneById(qa.getQuestionId());
            if(qa.getAnswerSelected()==questionDTO.getCorrectAnswer()) correctSentence++;
           TestHistoryDTO testHistoryDTO = new TestHistoryDTO();
           testHistoryDTO.setStudentId(studentDTO.getStudentId());
           testHistoryDTO.setTestId(testId);
           testHistoryDTO.setQuestionId(qa.getQuestionId());
           testHistoryDTO.setAnswer(qa.getAnswerSelected());
           testHistoryService.save(testHistoryDTO);
        }
        TestResultDTO testResultDTO = new TestResultDTO();
        testResultDTO.setStudentId(studentDTO.getStudentId());
        testResultDTO.setTestId(testId);
        double mark = ((double)(testDTO.getMaxScore()/(double)testDTO.getTotalSentence()))*correctSentence;
        testResultDTO.setMark((double)Math.round(mark*100)/100);
        testResultDTO.setTotalCorrectAnswer(correctSentence);
        testResultService.save(testResultDTO);
        RoomDetailDTO roomDetailDTO = roomDetailService.findOneByRoomAndStudent(roomId,studentDTO.getStudentId());
        roomDetailDTO.setStatus(0);
        roomDetailService.save(roomDetailDTO);
        redirectAttributes.addAttribute("studentId",studentDTO.getStudentId());
        redirectAttributes.addAttribute("testId",testId);
        return "redirect:/test-score";
    }

    @GetMapping("/test-score")
    public String getTestScore(@RequestParam("studentId") long studentId,@RequestParam("testId") long testId,
                               HttpServletRequest request,Model model){
        long roomId =(long) SessionUtils.getInstance().getValue(request,"roomId");
        RoomDTO roomDTO = roomService.findOneById(roomId);
        boolean acceptViewAnswer = false;
        if(roomDTO.getAnswerView()==1) acceptViewAnswer = true;
        TestResultDTO testResultDTO = testResultService.findOneByTestResultKey(studentId,testId);
        TestDTO testDTO = testService.findOneById(testId);
        double percent = (double)Math.round((((testResultDTO.getMark()/(double)testDTO.getMaxScore()))*100)*100)/100;
        model.addAttribute("result",testResultDTO);
        model.addAttribute("test",testDTO);
        model.addAttribute("percent",percent);
        model.addAttribute("acceptViewAnswer",acceptViewAnswer);
        return "client-quiz/pages/exam/test-score";
    }

    @GetMapping("/view-answer")
    public String getAnswerOfTest(HttpServletRequest request,Model model){
        String email = SessionUtils.getInstance().getValue(request,"userEmail").toString();
        StudentDTO studentDTO = studentService.findOneByEmail(email);
        long roomId =(long) SessionUtils.getInstance().getValue(request,"roomId");
        RoomDTO roomDTO = roomService.findOneById(roomId);
        SubjectDTO subjectDTO = subjectService.findOneById(roomDTO.getSubjectId());
        ExamDTO examDTO = examService.findOneById(roomDTO.getExamId());
        RoomDetailDTO roomDetailDTO = roomDetailService.findOneByRoomAndStudent(roomId,studentDTO.getStudentId());
        TestDTO testDTO = testService.findOneById(roomDetailDTO.getTestId());
        List<TestDetailDTO> testDetailDTOs = testDetailService.findByTestId(testDTO.getTestId());
        ListQuestionDTO listQuestionDTO = new ListQuestionDTO();
        testDetailDTOs.stream().forEach(testDetailDTO -> {
            QuestionDTO questionDTO = questionService.findOneById(testDetailDTO.getQuestionId());
            List<AnswerDTO> answerDTOs = answerService.findByQuestionId(questionDTO.getQuestionId());
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setQuestionId(questionDTO.getQuestionId());
            questionAnswerDTO.setQuestionContent(questionDTO.getQuestionContent());
            questionAnswerDTO.setAnswerDTOs(answerDTOs);
            questionAnswerDTO.setCorrectAnswer(questionDTO.getCorrectAnswer());
            questionAnswerDTO.setAnswerSelected(testHistoryService.findByTestHistoryKey(studentDTO.getStudentId(),
                    testDTO.getTestId(),questionDTO.getQuestionId())
                    .getAnswer());
            questionAnswerDTO.setAnswerExplain(questionDTO.getExplain());
            listQuestionDTO.add(questionAnswerDTO);
        });
        TestResultDTO testResultDTO = testResultService.findOneByTestResultKey(studentDTO.getStudentId(),testDTO.getTestId());
        model.addAttribute("questions",listQuestionDTO);
        model.addAttribute("test",testDTO);
        model.addAttribute("student",studentDTO);
        model.addAttribute("subject",subjectDTO);
        model.addAttribute("exam",examDTO);
        model.addAttribute("testResult",testResultDTO);
        return "client-quiz/pages/exam/test-result";
    }

    @GetMapping("/view-history-exam")
    public String getHistoryExam(HttpServletRequest request,
                                 @RequestParam(value = "examId",required = false)String id,Model model){
        String email = SessionUtils.getInstance().getValue(request,"userEmail").toString();
        StudentDTO studentDTO = studentService.findOneByEmail(email);
        List<RoomDetailDTO> roomDetailDTOs = roomDetailService.findAllByStudentId(studentDTO.getStudentId());
        if(id!=null&&!id.equals("")&&!id.equals("0")){
            List<RoomDetailDTO> roomDTS = new ArrayList<>();
            long examId = Long.parseLong(id);
            roomDetailDTOs.stream().forEach(roomDetailDTO -> {
                RoomDTO roomDTO = roomService.findOneById(roomDetailDTO.getRoomId());
                if(roomDTO.getExamId()==examId) roomDTS.add(roomDetailDTO);
            });
            roomDetailDTOs = roomDTS;
        }
        roomDetailDTOs.stream().forEach(roomDetailDTO -> {
            if(roomDetailDTO.getStatus()==0){
                TestDTO testDTO = testService.findOneById(roomDetailDTO.getTestId());
                TestResultDTO testResultDTO = testResultService.findOneByTestResultKey(studentDTO.getStudentId(),testDTO.getTestId());
                roomDetailDTO.setMark(testResultDTO.getMark());
                roomDetailDTO.setMaxScore(testDTO.getMaxScore());
            }
        });

        List<RoomDTO> roomDTOs = new ArrayList<>();
        roomDetailDTOs.stream().forEach(roomDetailDTO -> {
            if(roomDetailDTO.getStatus()==0) {
                RoomDTO roomDTO = roomService.findOneById(roomDetailDTO.getRoomId());
                roomDTOs.add(roomDTO);
            }
        });
        model.addAttribute("rooms",roomDTOs);
        model.addAttribute("subjects",subjectService.findAll());
        model.addAttribute("exams",examService.findAll());
        model.addAttribute("roomDetail",roomDetailDTOs);
        return "client-quiz/pages/exam/exam-history";
    }
}
