package com.kadri.springboot.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements CustomErrorControllery {

    @Override
    @RequestMapping("/errori")
    public String handleError() {
        // Your custom error handling logic here
        return "error-page"; // Return the name of your error view/template
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}

