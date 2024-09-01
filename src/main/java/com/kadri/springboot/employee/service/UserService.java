package com.kadri.springboot.employee.service;

import com.kadri.springboot.employee.entity.User;
import com.kadri.springboot.employee.user.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User findByUserName(String userName);

    void save(WebUser webUser);
}
