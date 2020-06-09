package com.foxminded.sql.DAO;

import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Student;

import java.util.List;
import java.util.Map;

public interface StudentDao extends GeneralDao<Student, Integer> {

    List<Student> findByCourseName(String courseName);

    void fillCoursesWithStudents(Map<Student, List<Course>> map);

    void assignToCourse(int studentId, int courseId);

    void deleteFromCourse(int studentId, int courseId);
}
