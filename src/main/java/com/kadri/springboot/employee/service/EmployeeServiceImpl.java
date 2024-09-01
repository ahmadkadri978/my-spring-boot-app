package com.kadri.springboot.employee.service;

import com.kadri.springboot.employee.dao.EmployeeDAO;
import com.kadri.springboot.employee.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll();
    }


    @Override
    public Employee findById(int theId) {

        Optional<Employee> result = Optional.ofNullable(employeeDAO.findById(theId));

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theEmployee;
    }

    @Override
    public Employee findByIdwithTasks(int id) {
        Optional<Employee> result = Optional.ofNullable(employeeDAO.findByIdwithTasks(id));

        Employee theEmployee = null;

        if (result.isPresent()) {
            theEmployee = result.get();
        }
        else {
            // we didn't find the employee
            throw new RuntimeException("Did not find employee id - " + id);
        }

        return theEmployee;
    }

    @Override
    @Transactional
    public void update(Employee employee) {
        employeeDAO.update(employee);
    }

    @Override
    @Transactional
    public void  save(Employee theEmployee) {
         employeeDAO.save(theEmployee);
    }

    @Override
    @Transactional
    public void delete(Employee theemployee) {
        employeeDAO.delete(theemployee);
    }
}






