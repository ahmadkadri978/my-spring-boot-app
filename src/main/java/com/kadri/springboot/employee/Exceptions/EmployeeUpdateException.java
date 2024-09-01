package com.kadri.springboot.employee.Exceptions;

public class EmployeeUpdateException extends RuntimeException {
    private String errorCode;
    public EmployeeUpdateException(String message ,String errorCode) {
        super(message);
        this.errorCode =  errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
