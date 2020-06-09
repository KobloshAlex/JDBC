package com.foxminded.sql.controller;

import com.foxminded.sql.DAO.CourseDao;
import com.foxminded.sql.DAO.GroupDao;
import com.foxminded.sql.DAO.StudentDao;
import com.foxminded.sql.domain.Student;
import com.foxminded.sql.view.ConsoleViewProvider;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    private static final int COURSE_OFFSET = 0;
    private static final int COURSE_LIMIT = 10;

    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final ConsoleViewProvider viewProvider;

    public Controller(CourseDao courseDao, GroupDao groupDao, StudentDao studentDao, ConsoleViewProvider viewProvider) {
        this.courseDao = courseDao;
        this.groupDao = groupDao;
        this.studentDao = studentDao;
        this.viewProvider = viewProvider;
    }

    public void callMenu() {
        boolean exit = false;
        while (!exit) {
            viewProvider.printMainMenu();
            String input = viewProvider.readString();
            if (!applicationMenu(input)) {
                exit = true;
            }
            applicationMenu(input);
        }
    }

    public boolean applicationMenu(String input) {
        boolean isProcessed=true;
        switch (input) {
            case "1":
                findGroups();
                break;
            case "2":
                findStudentsByCourseName();
                break;
            case "3":
                addNewStudent();
                break;
            case "4":
                deleteStudentById();
                break;
            case "5":
                addStudentToCourse();
                break;
            case "6":
                removeStudentCourse();
                break;
            case "7":
                isProcessed = false;
                break;
            default:
                printDefault();
                break;
        }
        return isProcessed;
    }

    private void printDefault() {
        viewProvider.printMessage("Please enter only numbers from 1 to 7");
    }

    private void findGroups() {
        viewProvider.printMessage("Find all groups with less or equals student count");
        viewProvider.printMessage("Enter number of students: ");
        viewProvider.printGroups(groupDao.getByStudentCount(viewProvider.getNumber()));
    }

    private void findStudentsByCourseName() {
        viewProvider.printMessage("Find all students related to course with given course name");
        viewProvider.printMessage("Enter course name: ");
        viewProvider.printStudents(studentDao.findByCourseName(viewProvider.readString()));
    }

    private void addNewStudent() {
        viewProvider.printMessage("Add new Student: ");
        viewProvider.printMessage("Enter Group ID (from 1 to 10) ");
        int groupId = viewProvider.getNumber();

        viewProvider.printMessage("Enter First Name ");
        String firstName = viewProvider.readString();

        viewProvider.printMessage("Enter Last Name ");
        String lastName = viewProvider.readString();

        Student newStudent = Student.builder()
                .withGroupId(groupId)
                .withFirstName(firstName)
                .withLastName(lastName)
                .build();
        List<Student> studentList = new ArrayList<>();
        studentList.add(newStudent);
        studentDao.save(studentList);
        viewProvider.printMessage("Student " + newStudent.getFirstName() + " " + newStudent.getLastName() + " was inserted");
    }

    private void deleteStudentById() {
        viewProvider.printMessage("Delete student by ID");
        viewProvider.printMessage("Show list of Students start from ID: ");
        int offset = viewProvider.getNumber();
        viewProvider.printMessage("Amount of students to show: ");
        int limit = viewProvider.getNumber();
        viewProvider.printStudents(studentDao.findAll(offset - 1, limit));
        viewProvider.printMessage("Enter Student ID: ");
        int studentId = viewProvider.getNumber();
        studentDao.deleteById(studentId);
        viewProvider.printMessage("Student with ID: " + studentId + " was deleted");
    }

    private void addStudentToCourse() {
        viewProvider.printMessage("Add student to course: ");
        viewProvider.printMessage("Enter student ID: ");
        int studentID = viewProvider.getNumber();
        viewProvider.printMessage("List of courses: " + studentID);
        viewProvider.printCourses(courseDao.findAll(COURSE_OFFSET, COURSE_LIMIT));

        int courseId = viewProvider.getNumber();
        if (studentID > 0 && courseId > 0) {
            studentDao.assignToCourse(studentID, courseId);
            viewProvider.printMessage("Course with ID: " + courseId + " added");
        } else {
            viewProvider.printMessage("Wrong IDs entered");
        }
    }

    private void removeStudentCourse() {
        viewProvider.printMessage("Remove student course: ");
        viewProvider.printMessage("Show list of Students start from ID: ");
        int offset = viewProvider.getNumber();
        viewProvider.printMessage("Amount of students to show: ");
        int limit = viewProvider.getNumber();
        viewProvider.printStudents(studentDao.findAll(offset - 1, limit));

        viewProvider.printMessage("Enter student ID ");
        int studentId = viewProvider.getNumber();

        viewProvider.printMessage("List of student courses :");
        viewProvider.printCourses(courseDao.findById(studentId));

        viewProvider.printMessage("Enter course id: ");
        int courseId = viewProvider.getNumber();


        if (studentId > 0 && courseId > 0) {
            studentDao.deleteFromCourse(studentId, courseId);
            viewProvider.printMessage("Student with id" + studentId + " was deleted from following course");
        } else {
            viewProvider.printMessage("Error, wrong IDs entered");
        }
    }
}
