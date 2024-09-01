package com.kadri.springboot.employee.service;

import com.kadri.springboot.employee.dao.TaskDAO;
import com.kadri.springboot.employee.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TaskServiceImpl implements TaskService{

    @Autowired
    private TaskDAO taskDAO;

    public TaskServiceImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public Task findById(Long id) {
        return taskDAO.findById(id);
    }

    @Override
    public List<Task> findAll() {
        return taskDAO.findAll();
    }

    @Override
    public void save(Task task) {
        taskDAO.save(task);
    }

    @Override
    public void update(Task task) {
        taskDAO.update(task);
    }

    @Override
    public void delete(Task task) {
        taskDAO.delete(task);
    }

    @Override
    public byte[] getFileContentById(Long id) {
        return taskDAO.getFileContentById(id);
    }
}
