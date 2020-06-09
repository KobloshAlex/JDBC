package com.foxminded.sql.view;

import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Group;
import com.foxminded.sql.domain.Student;

import java.util.List;
import java.util.Scanner;

public class ConsoleViewProvider {
    private static final String NEW_LINE = "\n";
    private static final String MENU = "1 - Find all groups with less or equals student count" + NEW_LINE +
            "2 - Find all students related to course with given name" + NEW_LINE +
            "3 - Add new student" + NEW_LINE +
            "4 - Delete student by STUDENT_ID" + NEW_LINE +
            "5 - Add a student to the course (from a list)" + NEW_LINE +
            "6 - Remove the student from one of his or her courses" + NEW_LINE +
            "7 - Exit program";
    private static final String SPACE = " ";
    private static final String STUDENT_ID = "Student ID ";
    private static final String COURSE_ID = "Course ID :";
    private static final String STUDENT_NAME = "Student Name: ";
    private static final String GROUP_NAME = "Group name: ";
    private static final String NAME = "Course name: ";
    private static final String PLEASE_ENTER_NUMBER = "Please enter number!";

    private final Scanner scanner;

    public ConsoleViewProvider() {
        this.scanner = new Scanner(System.in);
    }

    public void printMainMenu() {
        System.out.println(MENU);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }

    public String readString() {
        return scanner.next();
    }

    public void printStudents(List<Student> students) {
        students.forEach(student -> System.out.println(STUDENT_ID + student.getId() + NEW_LINE
                + STUDENT_NAME + student.getFirstName() + SPACE + student.getLastName()));
    }

    public void printGroups(List<Group> groups) {
        groups.forEach(group -> System.out.println(GROUP_NAME + group.getName() + NEW_LINE));
    }

    public void printCourses(List<Course> courses) {
        courses.forEach(course -> System.out.println(COURSE_ID + course.getId() + NEW_LINE + NAME + course.getName()));
    }

    public int getNumber() {
        int number = 0;
        while (number == 0) {
            try {
                number = scanner.nextInt();
            } catch (Exception e) {
                System.out.println(PLEASE_ENTER_NUMBER);
            }
        }
        return number;
    }
}
