package com.kadri.springboot.employee.dao;

import com.kadri.springboot.employee.entity.User;

public interface UserDao {

    User findByUserName(String userName);

    void save(User theUser);
}
