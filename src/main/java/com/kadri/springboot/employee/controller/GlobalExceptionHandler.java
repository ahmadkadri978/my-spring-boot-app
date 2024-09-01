package com.kadri.springboot.employee.controller;

import com.kadri.springboot.employee.Exceptions.EmployeeUpdateException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmployeeUpdateException.class)
    public String handleEmployeeUpdateException(EmployeeUpdateException ex, Model model) {
        // Handle the specific exception thrown in updateEmployee method
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorCode", ex.getErrorCode());
        return "error-page"; // Provide the path to your error page
    }

}

