package com.kadri.springboot.employee.dao;

import com.kadri.springboot.employee.entity.Role;

public interface RoleDao {

    public Role findRoleByName(String theRoleName);
}
