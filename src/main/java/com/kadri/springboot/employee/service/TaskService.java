package com.kadri.springboot.employee.service;

import com.kadri.springboot.employee.entity.Task;

import java.util.List;

public interface TaskService {

    Task findById(Long id);

    List<Task> findAll();

    void save(Task task);

    void update(Task task);

    void delete(Task task);
    byte[] getFileContentById(Long id);
}
