package com.kadri.springboot.employee.dao;

import com.kadri.springboot.employee.entity.Employee;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public class EmployeeDAOImpl implements EmployeeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Employee findByIdwithTasks(int id) {
        return entityManager.createQuery(
                        "SELECT e FROM Employee e JOIN FETCH e.tasks WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();

    }
    @Override
    public Employee findById(int id) {
        return entityManager.createQuery(
                        "SELECT e FROM Employee e WHERE e.id = :id", Employee.class)
                .setParameter("id", id)
                .getSingleResult();

    }

    @Override
    public List<Employee> findAll() {
        return entityManager.createQuery("SELECT e FROM Employee e", Employee.class)
                .getResultList();
    }

    @Override
    @Transactional
    public void save(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    @Transactional
    public void update(Employee employee) {


        entityManager.merge(employee);

    }

    @Override
    @Transactional
    public void delete(Employee employee) {
        entityManager.remove(employee);
    }
}
