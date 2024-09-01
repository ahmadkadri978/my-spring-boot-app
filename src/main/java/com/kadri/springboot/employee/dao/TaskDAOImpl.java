package com.kadri.springboot.employee.dao;

import com.kadri.springboot.employee.entity.Task;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
@Repository

public class TaskDAOImpl implements TaskDAO {

    @PersistenceContext
    private EntityManager entityManager;
    private final SessionFactory sessionFactory;
    public TaskDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Task findById(Long id) {
        return entityManager.find(Task.class, id);
    }

    @Override
    public List<Task> findAll() {
        return entityManager.createQuery("SELECT t FROM Task t", Task.class)
                .getResultList();
    }

    @Override
    public void save(Task task) {
        entityManager.persist(task);
    }

    @Override
    public void update(Task task) {
        entityManager.merge(task);
    }

    @Override
    public void delete(Task task) {
        entityManager.remove(task);
    }

    @Override
    public byte[] getFileContentById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Task task = session.get(Task.class, id);
            if (task != null) {
                return task.getReport();
            } else {
                // Handle the case where the task is not found
                return null;
            }
        } catch (Exception e) {
            // Handle any other exceptions that might occur during Hibernate operations
            e.printStackTrace(); // or log the exception
            return null; // Return null or handle the error in an appropriate way
        }
    }

}
