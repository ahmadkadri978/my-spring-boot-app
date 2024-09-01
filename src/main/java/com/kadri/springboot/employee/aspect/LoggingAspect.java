package com.kadri.springboot.employee.aspect;

import com.kadri.springboot.employee.entity.Employee;
import com.kadri.springboot.employee.entity.Task;
import com.kadri.springboot.employee.service.EmployeeService;
import com.kadri.springboot.employee.service.TaskService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingAspect {

    private Logger myLogger = Logger.getLogger(getClass().getName());

    @Pointcut("execution(* com.kadri.springboot.employee.controller.*.*(..)) ")
    public void forControllerPackage(){}
    @Pointcut("execution(* com.kadri.springboot.employee.service.*.*(..)) ")
    public void forServicePackage(){}
    @Pointcut("execution(* com.kadri.springboot.employee.dao.*.*(..)) ")
    public void forDaoPackage(){}
    @Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
    private void forAppFlow(){}
    @Before("forAppFlow()")
    public void  before(JoinPoint theJoinPoint) {
        // display method we are calling
        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("====>> in @Befor: calling method: " + theMethod);

        //display the arguments to the method

        Object[] args = theJoinPoint.getArgs();
        for(Object tempArg : args){
            myLogger.info("====>> argument:" + tempArg);
        }

    }
    // add@AfterReturning advice
    @AfterReturning(
            pointcut = "forAppFlow()" ,
            returning = "theResult"
    )
    public void afterReturning(JoinPoint theJoinPoint , Object theResult){

        // display method we are returning from

        String theMethod = theJoinPoint.getSignature().toShortString();
        myLogger.info("====>> in @AfterReturning: from method: " + theMethod);

        // display data returned
        myLogger.info("====>> result:" + theResult);

    }

}
