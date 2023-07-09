package com.doantotnghiep.controller.admin;

import com.doantotnghiep.dto.*;
import com.doantotnghiep.entity.Clazz;
import com.doantotnghiep.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller(value = "admin-room")
@RequestMapping("/admin")
public class RoomController {
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IExamService examService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private IRoomDetailService roomDetailService;
    @Autowired
    private ITestService testService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IClazzService clazzService;
    private long examId;

    @GetMapping("/room-exam")
    public String getRoomExam(@RequestParam("examId") long examId, Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        this.examId = examId;
        model.addAttribute("rooms", roomService.findAll(examId));
        model.addAttribute("exams", examService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("room", new RoomDTO());
        return "admin-quiz/pages/room/room";
    }

    @PostMapping("/room-exam")
    public String postRoomExam(@ModelAttribute("room") RoomDTO roomDTO, RedirectAttributes redirectAttributes) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(roomDTO.getTimeStart());
            Instant instant = Timestamp.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).toInstant();
            roomDTO.setStart(Date.from(instant));
            localDateTime = LocalDateTime.parse(roomDTO.getTimeFinish());
            instant = Timestamp.valueOf(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).toInstant();
            roomDTO.setFinish(Date.from(instant));
            roomDTO.setExamId(this.examId);
            roomService.save(roomDTO);
            redirectAttributes.addAttribute("examId", this.examId);
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Thêm phòng thi thành công !");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addAttribute("examId", this.examId);
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Đã có lỗi xảy ra !");
        }
        return "redirect:/admin/room-exam";
    }

    @GetMapping("/room-setting/{roomId}")
    public String getRoomSetting(@PathVariable("roomId") long roomId, Model model) {
        RoomDTO roomDTO = roomService.findOneById(roomId);
        ExamDTO examDTO = examService.findOneById(this.examId);
        SubjectDTO subjectDTO = subjectService.findOneById(roomDTO.getSubjectId());
        List<TestDTO> testDTOs = testService.findBySubjectId(roomDTO.getSubjectId());
        List<CLazzDTO> cLazzDTOs = clazzService.findAll();
        List<RoomDetailDTO> roomDetailDTOs = roomDetailService.findAllByRoomId(roomId);
        long classId = 0;
        List<Long> testIdExits = new ArrayList<>();
        model.addAttribute("classId",classId);
        model.addAttribute("testExits",testIdExits);
        if(roomDetailDTOs.size()>0){
            StudentDTO studentDTO = studentService.findOneById(roomDetailDTOs.get(0).getStudentId());
            classId = studentDTO.getClassId();
            roomDetailDTOs.stream().forEach(roomDetailDTO -> {
                testIdExits.add(roomDetailDTO.getTestId());
            });
            model.addAttribute("active",false);
            model.addAttribute("classId",classId);
            model.addAttribute("testExits",testIdExits);
        }else{
            model.addAttribute("active",true);
        }
        model.addAttribute("room", roomDTO);
        model.addAttribute("exam", examDTO);
        model.addAttribute("subject", subjectDTO);
        model.addAttribute("tests", testDTOs);
        model.addAttribute("clazzs", cLazzDTOs);
        return "admin-quiz/pages/room/room-student-test";
    }

    @Transactional
    @PostMapping("/room-setting")
    public String postRoomSetting(@RequestParam("roomId") long roomId, @RequestParam("testId") List<Long> testIds,
                                  @RequestParam("classId") long classId, RedirectAttributes redirectAttributes) {
        List<StudentDTO> studentDTOs = studentService.findByClass(classId);
        int pos = 0;
        try {
            for (StudentDTO studentDTO : studentDTOs) {
                RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
                roomDetailDTO.setRoomId(roomId);
                roomDetailDTO.setStudentId(studentDTO.getStudentId());
                roomDetailDTO.setTestId(testIds.get(pos));
                roomDetailDTO.setStatus(1);
                roomDetailService.save(roomDetailDTO);
                pos++;
                if (pos == testIds.size()) pos = 0;
            }
            redirectAttributes.addAttribute("examId", this.examId);
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Cập nhật phòng thi thành công !");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addAttribute("examId", this.examId);
            redirectAttributes.addFlashAttribute("status", "error");
            redirectAttributes.addFlashAttribute("message", "Có lỗi xảy ra trong quá trinh cập nhật phòng thi !");
        }
        return "redirect:/admin/room-exam";
    }
}
