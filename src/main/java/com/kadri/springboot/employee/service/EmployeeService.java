package com.kadri.springboot.employee.service;

import com.kadri.springboot.employee.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee findById(int id);
     Employee findByIdwithTasks(int id);

    List<Employee> findAll();

    void save(Employee employee);

    void update(Employee employee);

    void delete(Employee employee);

}
