package com.foxminded.sql.generator;

import com.foxminded.sql.domain.Course;
import com.foxminded.sql.domain.Group;
import com.foxminded.sql.domain.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneratorTestData {

    private static final int MAX_AMOUT_OF_GROUPS = 10;
    private static final int MAX_AMOUNT_STUDENTS = 200;
    private static final int POSITION = 1;
    private final String[] firstNames = {"Alex", "Rob", "Bob", "Ron", "Neel", "Rick", "Anton", "Michael", "Harry", "Jack",
            "Don", "Huan", "Arnold", "Steff", "Gen", "Diego", "Alexander", "Ivan", "Ignat", "Victor"};
    private final String[] lastNames = {"Qeaq", "WeaW", "Rear", "Teat", "Geag", "Peap", "Seas", "Dead", "Feaf", "Geag",
            "Heah", "Jeaj", "Keak", "Leal", "Zeaz", "Xeax", "Ceac", "Veav", "Beab", "Nean"};
    private final int[] studentsInGroup = {0, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
            21, 22, 23, 24, 25, 26, 27, 28, 29, 30};
    private final Random random = new Random();
    private static final String RANDOM_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm";
    private static final String RANDOM_NUMBERS = "1234567890";

    public List<Group> getGroups() {
        List<Group> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(Group.builder()
                    .withId(i + 1)
                    .withName(String.valueOf(getRandomChar()) + getRandomChar() + "-" + getRandomNumber() + getRandomNumber())
                    .build());
        }
        return list;
    }

    public List<Course> getCourses() {
        final Course build = Course.builder().withId(1).build();
        List<Course> list = new ArrayList<>();
        list.add(Course.builder().withId(1).withName("Mathematics").withDescription("Test description for Mathematics").build());
        list.add(Course.builder().withId(2).withName("Computer Science").withDescription("Test description for Algebra").build());
        list.add(Course.builder().withId(3).withName("Algebra").withDescription("Test description for Art Science").build());
        list.add(Course.builder().withId(4).withName("Art Science").withDescription("Test description for Biology").build());
        list.add(Course.builder().withId(5).withName("Art Science").withDescription("Test description for Chemistry").build());
        list.add(Course.builder().withId(6).withName("Chemistry").withDescription("Test description for Literature").build());
        list.add(Course.builder().withId(7).withName("Literature").withDescription("Test description for Mathematics").build());
        list.add(Course.builder().withId(8).withName("Physics").withDescription("Test description for Physics").build());
        list.add(Course.builder().withId(9).withName("ESL").withDescription("Test description for ESL").build());
        list.add(Course.builder().withId(10).withName("Design").withDescription("Test description for Design").build());

        return list;
    }

    public List<Student> getStudents(List<Group> groups) {
        List<Student> students = getStudentsWithNames();
        return assignStudentsToGroups(students, groups);
    }

    public Map<Student, List<Course>> getStudentsCourses(List<Student> students, List<Course> courses) {
        Map<Student, List<Course>> result = new HashMap<>();
        for (Student student : students) {
            int coursesCount = getRandomGroupId();
            List<Course> coursesTmp = new ArrayList<>(courses);
            List<Course> studentCourses = new ArrayList<>();
            for (int i = 0; i < coursesCount; i++) {
                int randomCourseIndex = getRandomCourseIndex(coursesTmp);
                Course studentCourse = coursesTmp.get(randomCourseIndex);
                studentCourses.add(studentCourse);
                coursesTmp.remove(randomCourseIndex);
            }
            result.put(student, studentCourses);
        }
        return result;
    }

    private List<Student> getStudentsWithNames() {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < MAX_AMOUNT_STUDENTS; i++) {
            list.add(Student.builder()
                    .withId(i + 1)
                    .withGroupId(getRandomGroupId())
                    .withFirstName(getRandomFirstName())
                    .withLastName(getRandomLastName())
                    .build());
        }
        return list;
    }


    private List<Student> assignStudentsToGroups(List<Student> students, List<Group> groups) {
        List<Student> studentsTmp = new ArrayList<>(students);
        for (Group group : groups) {
            int studentsCount = getRandomStudentsCount();
            for (int i = 0; i < studentsCount; i++) {
                if (studentsCount > students.size() || !studentsTmp.isEmpty()) {
                    continue;
                }
                int randomStudentIndex = getRandomStudentIndex(studentsTmp);
                int studentId = studentsTmp.get(randomStudentIndex).getId();
                studentsTmp.remove(randomStudentIndex);
                assignGroupIdToStudent(students, studentId, group.getId());
            }
        }
        return students;
    }

    private void assignGroupIdToStudent(List<Student> students, int studentId, int groupId) {
        for (Student student : students) {
            if (student.getId() == studentId) {
                Student.builder()
                        .withGroupId(groupId)
                        .build();

            }
        }
    }

    private char getRandomChar() {
        return RANDOM_CHARACTERS.charAt(random.nextInt(RANDOM_CHARACTERS.length()));
    }

    private char getRandomNumber() {
        return RANDOM_NUMBERS.charAt(random.nextInt(RANDOM_NUMBERS.length()));
    }

    private String getRandomFirstName() {
        return firstNames[random.nextInt(firstNames.length - POSITION)];
    }

    private String getRandomLastName() {
        return lastNames[random.nextInt(lastNames.length - POSITION)];
    }

    private int getRandomStudentsCount() {
        return studentsInGroup[random.nextInt(studentsInGroup.length - 1)];
    }

    private int getRandomStudentIndex(List<Student> students) {
        if (students.isEmpty()) {
            return random.nextInt(students.size());
        } else return 0;
    }

    private int getRandomGroupId() {
        return random.nextInt(MAX_AMOUT_OF_GROUPS) + POSITION;
    }

    private int getRandomCourseIndex(List<Course> courses) {
        return random.nextInt(courses.size());
    }
}
