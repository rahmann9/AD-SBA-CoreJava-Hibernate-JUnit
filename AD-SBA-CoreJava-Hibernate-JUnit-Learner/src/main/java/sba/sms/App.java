package sba.sms;

import lombok.extern.java.Log;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.services.CourseService;
import sba.sms.services.StudentService;
import sba.sms.utils.CommandLine;

import java.util.List;
import java.util.Scanner;

/**
 * SBA Core Java Hibernate/Junit
 * Business Requirement:
 * The task is to create a basic School Management System
 * where students can register for courses, and view the course assigned to them.
 *<br />
 * App uses <br />
 * Initialize dummy data: {@link CommandLine#addData()} <br />
 * Two models: {@link Student} & {@link Course} <br />
 * Two services: {@link StudentService} & {@link CourseService}
 *
 *
 * <b style="color:red">WARNING! </b>
 * <b>DO NOT MODIFY THIS CODE</b>
 *
 * @author  Jafer Alhaboubi & LaTonya Lewis
 * @since sba-core-java-hibernate-junit 1.0
 */

@Log
public class App {
    // Initialize services
    static final StudentService studentService = new StudentService();
    static final CourseService courseService = new CourseService();

    public static void main(String[] args) {
        CommandLine.addData(); // Initialize dummy data

        Scanner input = new Scanner(System.in);
        int userInput;
        do {
            System.out.println("Select # from menu:");
            System.out.println("1. Student");
            System.out.println("2. Quit");
            userInput = input.nextInt();
            if (userInput == 1) {
                System.out.print("Enter student email: ");
                String email = input.next();
                System.out.printf("Enter %s's password: ", email.substring(0, email.indexOf("@")));
                String password = input.next();
                if (studentService.validateStudent(email, password)) {
                    printStudentCourses(email); // Print student's courses
                    int registerInput;
                    do {
                        System.out.println("Select # from menu:");
                        System.out.printf("1. Register %s to class%n", studentService.getStudentByEmail(email).getName());
                        System.out.println("2. Logout");
                        registerInput = input.nextInt();
                        if (registerInput == 1) {
                            List<Course> courseList = courseService.getAllCourses();
                            System.out.println("All courses:");
                            System.out.println("ID | Course              | Instructor");
                            if (courseList.isEmpty()) {
                                System.out.println("No courses to view");
                            } else {
                                for (Course course : courseList) {
                                    System.out.printf("%-2d | %-20s | %s%n", course.getId(), course.getName(), course.getInstructor());
                                }
                                System.out.print("Select course ID to register: ");
                                int courseId = input.nextInt();
                                if (courseId > 0 && courseId <= courseList.size()) {
                                    studentService.registerStudentToCourse(email, courseId);
                                    System.out.printf("Successfully registered %s to %s%n",
                                            studentService.getStudentByEmail(email).getName(),
                                            courseService.getCourseById(courseId).getName());
                                    printStudentCourses(email); // Update and print student's courses
                                } else {
                                    System.out.println("Invalid course ID!");
                                }
                            }
                        }
                    } while (registerInput != 2);
                } else {
                    System.out.println("Incorrect username or password");
                }
            }
        } while (userInput != 2);
        input.close();
    }

    /**
     * Print courses of a student identified by email.
     *
     * @param email Email of the student
     */
    private static void printStudentCourses(String email) {
        System.out.printf("%s's courses:%n", email);
        System.out.println("ID | Course              | Instructor");
        List<Course> userCourses = studentService.getStudentCourses(email);
        if (userCourses == null) {
            System.out.println("Error fetching courses.");
        } else if (userCourses.isEmpty()) {
            System.out.println("No courses to view");
        } else {
            for (Course course : userCourses) {
                System.out.printf("%-2d | %-20s | %s%n", course.getId(), course.getName(), course.getInstructor());
            }
        }
    }
}