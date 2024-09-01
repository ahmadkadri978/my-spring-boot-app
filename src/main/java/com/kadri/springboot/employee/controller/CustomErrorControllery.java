package com.kadri.springboot.employee.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

public interface CustomErrorControllery extends ErrorController {
    @RequestMapping("/error")
    String handleError();

    String getErrorPath();
}
