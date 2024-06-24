package sba.sms.services;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.List;

public class StudentService {

    public boolean validateStudent(String email, String password) {
        return true;
    }

    public Student getStudentByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            Student student = session.createQuery("FROM Student WHERE email = :email", Student.class)
                    .setParameter("email", email)
                    .getSingleResult();
            transaction.commit();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();
            List<Course> courses = session.createQuery("FROM Course", Course.class)
                    .getResultList();
            transaction.commit();
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void registerStudentToCourse(String email, int courseId) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Student student = getStudentByEmail(email);
            if (student == null) {
                throw new IllegalArgumentException("Student with email " + email + " not found.");
            }

            Course course = session.load(Course.class, courseId);

            student.getCourses().add(course);
            session.update(student);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    public List<Course> getStudentCourses(String email) {
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            Transaction transaction = session.beginTransaction();

            Student student = getStudentByEmail(email);
            if (student == null) {
                throw new IllegalArgumentException("Student with email " + email + " not found.");
            }

            List<Course> courses = (List<Course>) student.getCourses(); // Assuming getCourses() returns a list of courses

            transaction.commit();
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void createStudent(Student student) {

    }
}
