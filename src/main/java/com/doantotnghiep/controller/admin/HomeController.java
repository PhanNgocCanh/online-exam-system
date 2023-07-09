package com.doantotnghiep.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "admin-controller")
@RequestMapping("/admin")
public class HomeController {
    @GetMapping("/home")
    public String getHomeQuestion(){

        return "admin-quiz/pages/question/home";
    }
}
