package com.doantotnghiep.controller.admin;

import com.doantotnghiep.constant.Constant;
import com.doantotnghiep.dto.*;
import com.doantotnghiep.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller(value = "test-result-admin")
@RequestMapping("/admin")
public class TestResultController {
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IRoomDetailService roomDetailService;
    @Autowired
    private IExamService examService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITestResultService testResultService;
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
    private long examId;
    private int page = 1;
    private int limit = Constant.PAGE_LIMIT;

    @GetMapping("/exam-result")
    public String getTestResult(@RequestParam(value = "examId", required = false) String id,
                                @RequestParam("page") int page, @RequestParam("limit") int limit, Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        List<RoomDTO> roomDTOs = null;
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("roomId").descending());
        model.addAttribute("page", page);
        model.addAttribute("limit", limit);
        if (id != null && !id.equals("") && !id.equals("0")) {
            long examId = Long.parseLong(id);
            model.addAttribute("examId", examId);
            if (this.examId != examId) {
                page = 1;
                model.addAttribute("page", page);
                this.examId = examId;
            }
            pageable = PageRequest.of(page - 1, limit, Sort.by("roomId").descending());
            roomDTOs = roomService.findByExam(examId,pageable);
            model.addAttribute("totalPage", (int) (Math.ceil((double) roomService.getTotalItem() / limit)));
        } else {
            roomDTOs = roomService.findAll(pageable);
            model.addAttribute("totalPage", (int) (Math.ceil((double) roomService.getTotalItem() / limit)));
        }
        model.addAttribute("exams", examService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("rooms", roomDTOs);
        return "admin-quiz/pages/exam/exam-history";
    }

    @GetMapping("/room-detail-result/{roomId}")
    public String getRoomDetailResult(@PathVariable("roomId") long roomId, Model model, RedirectAttributes redirectAttributes) {

        List<RoomDetailDTO> roomDetailDTOs = roomDetailService.findAllByRoomId(roomId);
        Date now = new Date();
        if (roomService.findOneById(roomId).getFinish().compareTo(now) >= 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Thời gian thi chưa kết thúc");
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            return "redirect:/admin/exam-result";
        }
        if (roomDetailDTOs.size() == 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Chưa có dữ liệu về phòng thi");
            redirectAttributes.addAttribute("page",1);
            redirectAttributes.addAttribute("limit",limit);
            return "redirect:/admin/exam-result";
        }
        List<Long> existsInRoom = new ArrayList<>();
        List<Long> statusID = new ArrayList<>();
        roomDetailDTOs.stream().forEach(roomDetailDTO -> {
            existsInRoom.add(roomDetailDTO.getStudentId());
            if(roomDetailDTO.getStatus()==1) statusID.add(roomDetailDTO.getStudentId());
        });
        List<StudentDTO> studentDTOs = new ArrayList<>();
        existsInRoom.stream().forEach(id->{
            StudentDTO studentDTO = studentService.findOneById(id);
            studentDTOs.add(studentDTO);
        });
        List<StudentResultDTO> studentResultDTOs = new ArrayList<>();
        studentDTOs.stream().forEach(studentDTO -> {
            StudentResultDTO studentResultDTO = new StudentResultDTO();
            studentResultDTO.setStudentId(studentDTO.getStudentId());
            studentResultDTO.setStudentName(studentDTO.getFullName());
            studentResultDTO.setClassName(studentDTO.getClassName());
            if (existsInRoom.contains(studentDTO.getStudentId())) {
                if(!statusID.contains(studentDTO.getStudentId())){
                    studentResultDTO.setMark(testResultService.
                            findOneByTestResultKey(studentDTO.getStudentId(),
                                    roomDetailService.findOneByRoomAndStudent(roomId, studentDTO.getStudentId())
                                            .getTestId())
                            .getMark());
                }else{
                    RoomDetailDTO roomDetailDTO = roomDetailService.findOneByRoomAndStudent(roomId,studentDTO.getStudentId());
                    roomDetailDTO.setStatus(0);
                    roomDetailService.save(roomDetailDTO);
                    TestResultDTO testResultDTO = new TestResultDTO();
                    testResultDTO.setStudentId(studentDTO.getStudentId());
                    testResultDTO.setTestId(roomDetailService.findOneByRoomAndStudent(roomId,studentDTO.getStudentId()).getTestId());
                    testResultDTO.setMark(0);
                    testResultDTO.setTotalCorrectAnswer(0);
                    testResultService.save(testResultDTO);
                    studentResultDTO.setMark(0);
                }
            } else {
                studentResultDTO.setMark(0);
            }
            studentResultDTOs.add(studentResultDTO);
        });
        List<Integer> markRank = new ArrayList<>();
        for (int i = 0; i <= 10; i++) markRank.add(0);
        studentResultDTOs.stream().forEach(studentResultDTO -> {
            int rank = (int) (Math.ceil(studentResultDTO.getMark()));
            if((int) (Math.ceil(studentResultDTO.getMark()))==0) rank = 1;
            markRank.set(rank, markRank.get(rank) + 1);
        });
        markRank.remove(0);
        model.addAttribute("room", roomService.findOneById(roomId));
        model.addAttribute("results", studentResultDTOs);
        model.addAttribute("dataChart", markRank);
        return "admin-quiz/pages/exam/exam-history-detail";
    }

    @GetMapping("/print-result")
    public String printResult(@RequestParam("roomId") long roomId, Model model) {
        List<RoomDetailDTO> roomDetailDTOs = roomDetailService.findAllByRoomId(roomId);
        List<Long> existsInRoom = new ArrayList<>();
        List<Long> statusID = new ArrayList<>();
        roomDetailDTOs.stream().forEach(roomDetailDTO -> {
            existsInRoom.add(roomDetailDTO.getStudentId());
            if(roomDetailDTO.getStatus()==1) statusID.add(roomDetailDTO.getStudentId());
        });
        List<StudentDTO> studentDTOs = new ArrayList<>();
        existsInRoom.stream().forEach(id->{
            StudentDTO studentDTO = studentService.findOneById(id);
            studentDTOs.add(studentDTO);
        });
        List<StudentResultDTO> studentResultDTOs = new ArrayList<>();
        studentDTOs.stream().forEach(studentDTO -> {
            StudentResultDTO studentResultDTO = new StudentResultDTO();
            studentResultDTO.setStudentId(studentDTO.getStudentId());
            studentResultDTO.setStudentName(studentDTO.getFullName());
            studentResultDTO.setClassName(studentDTO.getClassName());
            if (existsInRoom.contains(studentDTO.getStudentId())) {
                if(!statusID.contains(studentDTO.getStudentId())){
                    studentResultDTO.setMark(testResultService.
                            findOneByTestResultKey(studentDTO.getStudentId(),
                                    roomDetailService.findOneByRoomAndStudent(roomId, studentDTO.getStudentId())
                                            .getTestId())
                            .getMark());
                }else{
                    studentResultDTO.setMark(0);
                }
            } else {
                studentResultDTO.setMark(0);
            }
            studentResultDTOs.add(studentResultDTO);
        });
        model.addAttribute("room", roomService.findOneById(roomId));
        model.addAttribute("results", studentResultDTOs);
        model.addAttribute("exams", examService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        return "admin-quiz/pages/exam/print-result";
    }

    @GetMapping("/view-test-answer")
    public String viewTestAnswer(@RequestParam("roomId") long roomId,@RequestParam("studentId") long studentId,
                                 Model model){
        StudentDTO studentDTO = studentService.findOneById(studentId);
        RoomDetailDTO roomDetailDTO = roomDetailService.findOneByRoomAndStudent(roomId,studentId);
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
        model.addAttribute("answers",listQuestionDTO);
        model.addAttribute("result",testResultDTO);
        model.addAttribute("student",studentDTO);
        model.addAttribute("test",testDTO);
        return "admin-quiz/pages/exam/view-detail-result";
    }
}
