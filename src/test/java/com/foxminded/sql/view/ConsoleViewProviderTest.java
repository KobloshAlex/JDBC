package com.foxminded.sql.view;

import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Group;
import com.foxminded.sql.domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConsoleViewProviderTest {
    private static final String NEW_LINE = "\n";
    private static final String MENU = "1 - Find all groups with less or equals student count" + NEW_LINE +
            "2 - Find all students related to course with given name" + NEW_LINE +
            "3 - Add new student" + NEW_LINE +
            "4 - Delete student by STUDENT_ID" + NEW_LINE +
            "5 - Add a student to the course (from a list)" + NEW_LINE +
            "6 - Remove the student from one of his or her courses" + NEW_LINE +
            "7 - Exit program";

    private final ConsoleViewProvider viewProvider = new ConsoleViewProvider();
    @Mock
    private PrintStream out;

    @BeforeEach
    void setOut() {
        System.setOut(out);
    }

    @Test
    void printMainMenu() {
        viewProvider.printMainMenu();
        verify(out).println(MENU);
    }

    @Test
    void printMessage() {
        viewProvider.printMessage("Test");
        verify(out).println("Test");
    }

    @Test
    void printStudents() {
        List<Student> students = new ArrayList<>();
        students.add(Student.builder().withId(1).withFirstName("FirstName").withLastName("LastName").build());
        viewProvider.printStudents(students);
        verify(out).println("Student ID 1" + NEW_LINE + "Student Name: FirstName LastName");
    }

    @Test
    void printGroups() {
        List<Group> groups = new ArrayList<>();
        groups.add(Group.builder().withName("Test").build());
        viewProvider.printGroups(groups);
        verify(out).println("Group name: Test" + NEW_LINE);

    }

    @Test
    void printCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(Course.builder().withId(1).withName("TestName").build());
        viewProvider.printCourses(courses);
        verify(out).println("Course ID :1" + NEW_LINE + "Course name: TestName");
    }
}
