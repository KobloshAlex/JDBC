package com.foxminded.sql.controller;

import com.foxminded.sql.DAO.CourseDao;
import com.foxminded.sql.DAO.GroupDao;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.domain.Student;
import com.foxminded.sql.view.ConsoleViewProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
    @InjectMocks
    private Controller controller;
    @Mock
    private ConsoleViewProvider viewProvider;
    @Mock
    private StudentDao studentDao;
    @Mock
    private GroupDao groupDao;
    @Mock
    private CourseDao courseDao;


    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseOne() {
        controller.applicationMenu("1");
        verify(viewProvider).printMessage("Find all groups with less or equals student count");
        verify(viewProvider).printGroups(groupDao.getByStudentCount(viewProvider.getNumber()));
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseTwo() {
        controller.applicationMenu("2");
        verify(viewProvider).printMessage("Find all students related to course with given course name");
        verify(viewProvider).printStudents(studentDao.findByCourseName(viewProvider.readString()));
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseThree() {
        controller.applicationMenu("3");
        verify(viewProvider).printMessage("Add new Student: ");
        verify(viewProvider).getNumber();
        verify(viewProvider, times(2)).readString();
        Student newStudent = Student.builder()
                .withGroupId(0)
                .build();
        List<Student> studentList = new ArrayList<>();
        studentList.add(newStudent);
        verify(studentDao).save(studentList);
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseFour() {
        controller.applicationMenu("4");
        verify(viewProvider).printMessage("Delete student by ID");
        verify(viewProvider).printStudents(studentDao.findAll(1, 1));
        verify(viewProvider).getNumber();
        verify(studentDao).deleteById(0);
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseFive() {
        controller.applicationMenu("5");
        verify(viewProvider).printMessage("Add student to course: ");
        verify(viewProvider).printCourses(courseDao.findAll(1, 1));
        verify(viewProvider, times(2)).getNumber();
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseSix() {
        controller.applicationMenu("6");
        verify(viewProvider).printMessage("Remove student course: ");
        verify(viewProvider, times(2)).getNumber();
        verify(viewProvider).printStudents(studentDao.findAll(1, 1));
    }

    @Test
    void applicationMenuShouldSuccessfullyCallComponentsFromSwitchCaseDefault() {
        controller.applicationMenu("Default");
        verify(viewProvider).printMessage("Please enter only numbers from 1 to 7");
    }

}
