package sba.sms.models;

import jakarta.persistence.*;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Student is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'student' in the database. A Student object contains fields that represent student
 * login credentials and a join table containing a registered student's email and course(s)
 * data. The Student class can be viewed as the owner of the bi-directional relationship.
 * Implement Lombok annotations to eliminate boilerplate code.
 */


@Setter
@Entity
@Table (name = "Student")
public class Student {
    @Id
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.REMOVE, CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id", referencedColumnName = "id"))
    private Set<Course> courses = new HashSet<>();

    public Student() {
    }

    public Student(String email, String name, String password) {
        this(email, name, password, new HashSet<>());
    }

    public Student(String email, String name, String password, Set<Course> courses) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.courses = courses;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(email, student.email) && Objects.equals(name, student.name) && Objects.equals(password, student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }



    @Override
    public String toString() {
        return "Student{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void addCourse(Course course) {
        this.courses.add(course);
        course.getStudents().add(this);
    }

    public void removeCourse(Course course) {
        this.courses.remove(course);
        course.getStudents().remove(this);
    }
}




