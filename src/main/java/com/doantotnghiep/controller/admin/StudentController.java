package com.doantotnghiep.controller.admin;

import com.doantotnghiep.constant.Constant;
import com.doantotnghiep.constant.FileTemplate;
import com.doantotnghiep.dto.AccountDTO;
import com.doantotnghiep.dto.StudentDTO;
import com.doantotnghiep.dto.StudentFileDTO;
import com.doantotnghiep.service.IAccountService;
import com.doantotnghiep.service.IClazzService;
import com.doantotnghiep.service.IMailService;
import com.doantotnghiep.service.IStudentService;
import com.doantotnghiep.service.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class StudentController {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private IClazzService clazzService;
    @Autowired
    private IAccountService accountService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private IMailService mailService;
    private long classId;
    private int page = 1;
    private int limit = Constant.PAGE_STUDENT_LIMIT;
    @GetMapping("/student")
    public String getStudent(@RequestParam(value = "classId",required = false) String id,
                             @RequestParam("page") int page,@RequestParam("limit")int limit,Model model) {
        String status = (String) model.asMap().get("status");
        String message = (String) model.asMap().get("message");
        if (status != null) {
            model.addAttribute("status", status);
            model.addAttribute("message", message);
        }
        List<StudentDTO> studentDTOs = null;
        Pageable pageable = PageRequest.of(page-1,limit);
        model.addAttribute("page",page);
        model.addAttribute("limit",limit);
        if(id!=null&&!id.equals("")&&!id.equals("0")){
            long classId = Long.parseLong(id);
            model.addAttribute("classId",classId);
            if(this.classId!=classId){
                page = 1;
                this.classId = classId;
                model.addAttribute("page",page);
            }
            pageable = PageRequest.of(page-1,limit);
            studentDTOs = studentService.findByClass(classId,pageable);
            model.addAttribute("totalPage",(int)(Math.ceil((double)studentService.getTotalItem()/limit)));
        }else{
            studentDTOs = studentService.findAll(pageable);
            model.addAttribute("totalPage",(int)(Math.ceil((double)studentService.getTotalItem()/limit)));
        }
        model.addAttribute("students", studentDTOs);
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("clazzs", clazzService.findAll());
        return "admin-quiz/pages/student/student";
    }

    @PostMapping("/student")
    public String postStudent(@ModelAttribute("student") StudentDTO studentDTO, RedirectAttributes redirectAttributes) {
        String email = studentDTO.getEmail();
        String fullName = studentDTO.getFullName();
        AccountDTO accountDTO = accountService.changeToAccountDTO(email, fullName);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_STUDENT");
        if (accountService.findByEmail(accountDTO.getEmail()) != null && studentDTO.getStudentId() == 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Email đã tồn tại !");
        } else {
            StudentDTO studentDTOExits = studentService.findOneById(studentDTO.getStudentId());
            if (studentDTOExits!=null){
                String emailExist = studentDTOExits.getEmail();
                accountService.delete(emailExist);
            }
            studentService.save(studentDTO);
            accountService.save(accountDTO, roles);
            mailService.sendSimpleMail(studentDTO.getEmail());
            redirectAttributes.addFlashAttribute("status", "success");
            redirectAttributes.addFlashAttribute("message", "Thao tác thành công !");
        }
        redirectAttributes.addAttribute("page",page);
        redirectAttributes.addAttribute("limit",limit);
        return "redirect:/admin/student";
    }

    @GetMapping("/update-student/{studentId}")
    public String updateStudent(@PathVariable("studentId") long studentId, Model model) {
        model.addAttribute("page",1);
        model.addAttribute("limit",limit);
        model.addAttribute("students", studentService.findAll(PageRequest.of(0,limit)));
        model.addAttribute("totalPage",(int)(Math.ceil((double)studentService.getTotalItem()/limit)));
        model.addAttribute("student", studentService.findOneById(studentId));
        model.addAttribute("clazzs", clazzService.findAll());
        return "admin-quiz/pages/student/student";
    }

    @GetMapping("/student-excel")
    public String addStudentFromExcel(Model model) {
        model.addAttribute("clazzs", clazzService.findAll());
        model.addAttribute("studentFile", new StudentFileDTO());
        return "admin-quiz/pages/student/create-student-from-excel";
    }

    @PostMapping("/student-excel")
    @Transactional
    public String postStudentFromFile(@Valid @ModelAttribute("studentFile") StudentFileDTO studentFileDTO,
                                      @RequestParam("file") MultipartFile multipartFile,
                                      RedirectAttributes redirectAttributes, BindingResult result, Model model) throws IOException {
        if (studentFileDTO.getClassId() == 0) {
            result.addError(new FieldError("studentFile", "classId",
                    "Phải chọn lớp học"));
            model.addAttribute("clazzs", clazzService.findAll());
            return "admin-quiz/pages/student/create-student-from-excel";
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (fileName == null || fileName.lastIndexOf(".xlsx") == -1) {
            result.addError(new FieldError("studentFile", "fileName",
                    "Chưa chọn file hoặc file không đúng định dạng"));
            model.addAttribute("clazzs", clazzService.findAll());
            return "admin-quiz/pages/student/create-student-from-excel";
        }
        String dir = "src/main/resources/static/upload/";
        storageService.saveFileToServer(multipartFile, dir, fileName);
        List<StudentDTO> studentDTOs = studentService.readStudentFromFile("/static/upload/" + fileName);
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_STUDENT");
        List<StudentDTO> exitsEmail = studentDTOs.stream().filter(studentDTO ->
                accountService.findByEmail(studentDTO.getEmail()) != null).collect(Collectors.toList());
        if (exitsEmail.size() > 0) {
            redirectAttributes.addFlashAttribute("status", "warning");
            redirectAttributes.addFlashAttribute("message", "Email đã tồn tại trong hệ thống !");
            return "redirect:/admin/student";
        }
        studentDTOs.stream().forEach((studentDTO) -> {
            studentDTO.setClassId(studentFileDTO.getClassId());
            String email = studentDTO.getEmail();
            String fullName = studentDTO.getFullName();
            studentService.save(studentDTO);
            accountService.save(accountService.changeToAccountDTO(email, fullName), roles);
            mailService.sendSimpleMail(studentDTO.getEmail());
        });
        redirectAttributes.addFlashAttribute("status", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm học sinh thành công !");
        redirectAttributes.addAttribute("page",1);
        redirectAttributes.addAttribute("limit",limit);
        return "redirect:/admin/student";
    }

    @RequestMapping("/download-template-student")
    public void DownloadFileStudent(HttpServletResponse response) {
        storageService.downloadTemplateFile(response, FileTemplate.FILE_TEMPLATE_STUDENT);
    }
}
