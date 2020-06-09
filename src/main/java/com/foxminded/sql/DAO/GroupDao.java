package com.foxminded.sql.DAO;

import com.foxminded.sql.domain.Group;

import java.util.List;

public interface GroupDao extends GeneralDao<Group, Integer> {

    List<Group> getByStudentCount(int count);
}
