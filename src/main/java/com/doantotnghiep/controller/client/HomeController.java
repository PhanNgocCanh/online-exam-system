package com.doantotnghiep.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller(value = "home-client")
public class HomeController {

    @GetMapping("/")
    public String getHomeClient(Model model){
        String status = (String)model.asMap().get("status");
        String message = (String)model.asMap().get("message");
        if(status!=null){
            model.addAttribute("status",status);
            model.addAttribute("message",message);
        }
        return "client-quiz/pages/home/home";
    }
}
